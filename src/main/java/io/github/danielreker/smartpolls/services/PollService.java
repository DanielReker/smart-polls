package io.github.danielreker.smartpolls.services;

import io.github.danielreker.smartpolls.dao.entities.PollEntity;
import io.github.danielreker.smartpolls.dao.entities.PollStatus;
import io.github.danielreker.smartpolls.dao.entities.SubmissionEntity;
import io.github.danielreker.smartpolls.dao.entities.answers.AnswerEntity;
import io.github.danielreker.smartpolls.dao.entities.questions.TextQuestionEntity;
import io.github.danielreker.smartpolls.dao.repositories.PollRepository;
import io.github.danielreker.smartpolls.dao.repositories.SubmissionRepository;
import io.github.danielreker.smartpolls.dao.repositories.questions.TextQuestionRepository;
import io.github.danielreker.smartpolls.mappers.PollMapper;
import io.github.danielreker.smartpolls.mappers.QuestionMapper;
import io.github.danielreker.smartpolls.mappers.SubmissionMapper;
import io.github.danielreker.smartpolls.model.auth.AuthenticatedUser;
import io.github.danielreker.smartpolls.model.exceptions.InvalidPollStatusException;
import io.github.danielreker.smartpolls.services.auth.UserService;
import io.github.danielreker.smartpolls.services.question.AiQuestionGeneratorService;
import io.github.danielreker.smartpolls.web.dtos.*;
import io.github.danielreker.smartpolls.web.dtos.answers.AnswerDto;
import io.github.danielreker.smartpolls.web.dtos.questions.QuestionDto;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Transactional
@RequiredArgsConstructor
@Service
public class PollService {

    private final PollRepository pollRepository;

    private final SubmissionRepository submissionRepository;

    private final PollMapper pollMapper;

    private final SubmissionMapper submissionMapper;

    private final QuestionManagerService questionManagerService;

    private final UserService userService;

    private final PollSecurityService pollSecurityService;

    private final QuestionMapper questionMapper;

    private final AiQuestionGeneratorService aiQuestionGeneratorService;

    private final TextQuestionRepository textQuestionRepository;

    private final AiTextQuestionSummarizerService aiTextQuestionSummarizerService;


    public PollResponse createPoll(
            PollCreateRequest request,
            AuthenticatedUser user
    ) {
        PollEntity pollEntity = pollMapper.createRequestToEntity(request);
        pollEntity.setStatus(PollStatus.DRAFT);
        pollEntity.setOwner(userService.getUserReference(user));

        pollEntity = pollRepository.save(pollEntity);

        return pollMapper.toResponse(
                pollEntity,
                pollSecurityService.countUserSubmissionsToPoll(user.getId(), pollEntity.getId())
        );
    }

    public PollResponse getPoll(
            Long pollId,
            AuthenticatedUser user
    ) {
        final PollEntity pollEntity = findPollEntityById(pollId);

        return pollMapper.toResponse(
                pollEntity,
                pollSecurityService.countUserSubmissionsToPoll(user.getId(), pollEntity.getId())
        );
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or @pollSecurityService.isUserPollOwner(authentication.principal.id, #pollId)")
    public PollResponse upsertQuestions(
            Long pollId,
            PollQuestionsUpsertRequest request,
            AuthenticatedUser user
    ) {
        final PollEntity pollEntity = findPollEntityById(pollId);

        if (pollEntity.getStatus() != PollStatus.DRAFT) {
            throw new InvalidPollStatusException("Can't update questions in non-draft poll, " +
                    "its current status: " + pollEntity.getStatus());
        }

        pollEntity.getQuestions().clear();
        pollEntity
                .getQuestions()
                .addAll(request
                        .questions()
                        .stream()
                        .map(questionManagerService::buildQuestion)
                        .peek(questionEntity -> questionEntity.setPoll(pollEntity))
                        .toList());

        return pollMapper.toResponse(
                pollRepository.save(pollEntity),
                pollSecurityService.countUserSubmissionsToPoll(user.getId(), pollEntity.getId())
        );
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or @pollSecurityService.isUserPollOwner(authentication.principal.id, #pollId)")
    public PollResponse startPoll(
            Long pollId,
            AuthenticatedUser user
    ) {
        final PollEntity pollEntity = findPollEntityById(pollId);

        if (pollEntity.getStatus() != PollStatus.DRAFT) {
            throw new InvalidPollStatusException("Can't start poll, its current status: " + pollEntity.getStatus());
        }

        pollEntity.setStatus(PollStatus.ACTIVE);

        return pollMapper.toResponse(
                pollRepository.save(pollEntity),
                pollSecurityService.countUserSubmissionsToPoll(user.getId(), pollEntity.getId())
        );
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or @pollSecurityService.isUserPollOwner(authentication.principal.id, #pollId)")
    public PollResponse finishPoll(
            Long pollId,
            AuthenticatedUser user
    ) {
        final PollEntity pollEntity = findPollEntityById(pollId);

        if (pollEntity.getStatus() != PollStatus.ACTIVE) {
            throw new InvalidPollStatusException("Can't finish poll, its current status: " + pollEntity.getStatus());
        }

        pollEntity.setStatus(PollStatus.FINISHED);

        return pollMapper.toResponse(
                pollRepository.save(pollEntity),
                pollSecurityService.countUserSubmissionsToPoll(user.getId(), pollEntity.getId())
        );
    }

    @PreAuthorize("""
        hasRole('ROLE_ADMIN') or
        @pollSecurityService.countUserSubmissionsToPoll(authentication.principal.id, #pollId) < 1
        """)
    public SubmissionResponse createSubmission(
            Long pollId,
            SubmissionCreateRequest request,
            AuthenticatedUser user
    ) {
        final PollEntity pollEntity = findPollEntityById(pollId);

        if (pollEntity.getStatus() != PollStatus.ACTIVE) {
            throw new InvalidPollStatusException("Can't create submission for non-active poll, " +
                    "its current status: " + pollEntity.getStatus());
        }

        final SubmissionEntity submissionEntity = new SubmissionEntity();

        final Map<Long, AnswerDto> questionIdToAnswerDto = request
                .getAnswers()
                .stream()
                .collect(Collectors.toMap(
                        AnswerDto::getQuestionId,
                        Function.identity(),
                        (answerDto, _) -> {
                            throw new IllegalArgumentException("Duplicated answer found " +
                                    "for question id " + answerDto.getQuestionId());
                        }
                ));

        final List<AnswerEntity> answers = pollEntity
                .getQuestions()
                .stream()
                .map(question -> {
                    final Optional<AnswerEntity> answerEntity = Optional
                            .ofNullable(questionIdToAnswerDto.get(question.getId()))
                            .map(answerDto -> questionManagerService
                                    .buildAnswer(question, answerDto));

                    if (answerEntity.isEmpty() && question.getIsRequired()) {
                        throw new IllegalArgumentException("Missing answer for " +
                                "required question with id " + question.getId());
                    }

                    return answerEntity;

                })
                .flatMap(Optional::stream)
                .peek(answerEntity -> answerEntity.setSubmission(submissionEntity))
                .toList();

        submissionEntity.setPoll(pollEntity);
        submissionEntity.setAnswers(answers);
        submissionEntity.setOwner(userService.getUserReference(user));

        final SubmissionEntity savedSubmissionEntity =
                submissionRepository.save(submissionEntity);

        return submissionMapper.toResponse(savedSubmissionEntity);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or @pollSecurityService.isUserPollOwner(authentication.principal.id, #pollId)")
    public StatsResponse getStats(Long pollId) {
        final PollEntity pollEntity = findPollEntityById(pollId);

        final Long submissionsCount = submissionRepository
                .countByPollId(pollId);

        return StatsResponse
                .builder()
                .submissionsCount(submissionsCount)
                .stats(pollEntity
                        .getQuestions()
                        .stream()
                        .map(questionManagerService::getQuestionStats)
                        .toList())
                .build();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or @pollSecurityService.isUserPollOwner(authentication.principal.id, #pollId)")
    public PollResponse aiGenerateQuestions(
            Long pollId,
            AiGenerateQuestionsRequest request,
            AuthenticatedUser user
    ) {
        final PollEntity pollEntity = findPollEntityById(pollId);

        final List<QuestionDto> currentQuestions = pollEntity
                .getQuestions()
                .stream()
                .map(questionMapper::toDto)
                .toList();

        final List<QuestionDto> newQuestions = aiQuestionGeneratorService
                .transformQuestions(currentQuestions, request.getPrompt());

        final PollQuestionsUpsertRequest pollQuestionsUpsertRequest = PollQuestionsUpsertRequest
                .builder()
                .questions(newQuestions)
                .build();

        return upsertQuestions(pollId, pollQuestionsUpsertRequest, user);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or @pollSecurityService.isUserPollOwner(authentication.principal.id, #pollId)")
    public void summarizeTextQuestion(Long pollId, Long textQuestionId) {
        textQuestionRepository
                .findById(textQuestionId)
                .filter(textQuestionEntity ->
                        Objects.equals(textQuestionEntity.getPoll().getId(), pollId))
                .filter(TextQuestionEntity::getNeedAiSummary)
                .orElseThrow();

        aiTextQuestionSummarizerService.summarizeTextQuestion(textQuestionId);

    }

    private PollEntity findPollEntityById(Long pollId) {
        return pollRepository
                .findById(pollId)
                .orElseThrow(() -> new EntityNotFoundException("Poll with id " + pollId + " not found"));
    }

    public PollsResponse getUserPolls(AuthenticatedUser user) {
        return PollsResponse
                .builder()
                .polls(pollRepository
                        .findByOwnerId(user.getId())
                        .stream()
                        .map(pollMapper::toShortResponse)
                        .toList())
                .build();
    }
}
