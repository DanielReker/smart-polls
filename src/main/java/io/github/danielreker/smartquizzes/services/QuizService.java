package io.github.danielreker.smartquizzes.services;

import io.github.danielreker.smartquizzes.dao.entities.QuizEntity;
import io.github.danielreker.smartquizzes.dao.entities.QuizStatus;
import io.github.danielreker.smartquizzes.dao.entities.SubmissionEntity;
import io.github.danielreker.smartquizzes.dao.entities.answers.AnswerEntity;
import io.github.danielreker.smartquizzes.dao.repositories.QuizRepository;
import io.github.danielreker.smartquizzes.dao.repositories.SubmissionRepository;
import io.github.danielreker.smartquizzes.mappers.QuestionMapper;
import io.github.danielreker.smartquizzes.mappers.QuizMapper;
import io.github.danielreker.smartquizzes.mappers.SubmissionMapper;
import io.github.danielreker.smartquizzes.model.auth.AuthenticatedUser;
import io.github.danielreker.smartquizzes.model.exceptions.InvalidQuizStatusException;
import io.github.danielreker.smartquizzes.services.auth.UserService;
import io.github.danielreker.smartquizzes.services.question.AiQuestionGeneratorService;
import io.github.danielreker.smartquizzes.web.dtos.*;
import io.github.danielreker.smartquizzes.web.dtos.answers.AnswerDto;
import io.github.danielreker.smartquizzes.web.dtos.questions.QuestionDto;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Transactional
@RequiredArgsConstructor
@Service
public class QuizService {

    private final QuizRepository quizRepository;

    private final SubmissionRepository submissionRepository;

    private final QuizMapper quizMapper;

    private final SubmissionMapper submissionMapper;

    private final QuestionManagerService questionManagerService;

    private final UserService userService;

    private final QuizSecurityService quizSecurityService;

    private final QuestionMapper questionMapper;

    private final AiQuestionGeneratorService aiQuestionGeneratorService;


    public QuizResponse createQuiz(
            QuizCreateRequest request,
            AuthenticatedUser user
    ) {
        QuizEntity quizEntity = quizMapper.createRequestToEntity(request);
        quizEntity.setStatus(QuizStatus.DRAFT);
        quizEntity.setOwner(userService.getUserReference(user));

        quizEntity = quizRepository.save(quizEntity);

        return quizMapper.toResponse(
                quizEntity,
                quizSecurityService.countUserSubmissionsToQuiz(user.getId(), quizEntity.getId())
        );
    }

    public QuizResponse getQuiz(
            UUID quizId,
            AuthenticatedUser user
    ) {
        final QuizEntity quizEntity = findQuizEntityById(quizId);

        return quizMapper.toResponse(
                quizEntity,
                quizSecurityService.countUserSubmissionsToQuiz(user.getId(), quizEntity.getId())
        );
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or @quizSecurityService.isUserQuizOwner(authentication.principal.id, #quizId)")
    public QuizResponse upsertQuestions(
            UUID quizId,
            QuizQuestionsUpsertRequest request,
            AuthenticatedUser user
    ) {
        final QuizEntity quizEntity = findQuizEntityById(quizId);

        if (quizEntity.getStatus() != QuizStatus.DRAFT) {
            throw new InvalidQuizStatusException("Can't update questions in non-draft quiz, " +
                    "its current status: " + quizEntity.getStatus());
        }

        quizEntity.getQuestions().clear();
        quizEntity
                .getQuestions()
                .addAll(request
                        .questions()
                        .stream()
                        .map(questionManagerService::buildQuestion)
                        .peek(questionEntity -> questionEntity.setQuiz(quizEntity))
                        .toList());

        return quizMapper.toResponse(
                quizRepository.save(quizEntity),
                quizSecurityService.countUserSubmissionsToQuiz(user.getId(), quizEntity.getId())
        );
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or @quizSecurityService.isUserQuizOwner(authentication.principal.id, #quizId)")
    public QuizResponse startQuiz(
            UUID quizId,
            AuthenticatedUser user
    ) {
        final QuizEntity quizEntity = findQuizEntityById(quizId);

        if (quizEntity.getStatus() != QuizStatus.DRAFT) {
            throw new InvalidQuizStatusException("Can't start quiz, its current status: " + quizEntity.getStatus());
        }

        quizEntity.setStatus(QuizStatus.ACTIVE);

        return quizMapper.toResponse(
                quizRepository.save(quizEntity),
                quizSecurityService.countUserSubmissionsToQuiz(user.getId(), quizEntity.getId())
        );
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or @quizSecurityService.isUserQuizOwner(authentication.principal.id, #quizId)")
    public QuizResponse finishQuiz(
            UUID quizId,
            AuthenticatedUser user
    ) {
        final QuizEntity quizEntity = findQuizEntityById(quizId);

        if (quizEntity.getStatus() != QuizStatus.ACTIVE) {
            throw new InvalidQuizStatusException("Can't finish quiz, its current status: " + quizEntity.getStatus());
        }

        quizEntity.setStatus(QuizStatus.FINISHED);

        return quizMapper.toResponse(
                quizRepository.save(quizEntity),
                quizSecurityService.countUserSubmissionsToQuiz(user.getId(), quizEntity.getId())
        );
    }

    @PreAuthorize("""
        hasRole('ROLE_ADMIN') or
        @quizSecurityService.countUserSubmissionsToQuiz(authentication.principal.id, #quizId) < 1
        """)
    public SubmissionResponse createSubmission(
            UUID quizId,
            SubmissionCreateRequest request,
            AuthenticatedUser user
    ) {
        final QuizEntity quizEntity = findQuizEntityById(quizId);

        if (quizEntity.getStatus() != QuizStatus.ACTIVE) {
            throw new InvalidQuizStatusException("Can't create submission for non-active quiz, " +
                    "its current status: " + quizEntity.getStatus());
        }

        final SubmissionEntity submissionEntity = new SubmissionEntity();

        final Map<UUID, AnswerDto> questionIdToAnswerDto = request
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

        final List<AnswerEntity> answers = quizEntity
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

        submissionEntity.setQuiz(quizEntity);
        submissionEntity.setAnswers(answers);
        submissionEntity.setOwner(userService.getUserReference(user));

        final SubmissionEntity savedSubmissionEntity =
                submissionRepository.save(submissionEntity);

        return submissionMapper.toResponse(savedSubmissionEntity);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or @quizSecurityService.isUserQuizOwner(authentication.principal.id, #quizId)")
    public StatsResponse getStats(UUID quizId) {
        final QuizEntity quizEntity = findQuizEntityById(quizId);

        final Long submissionsCount = submissionRepository
                .countByQuizId(quizId);

        return StatsResponse
                .builder()
                .submissionsCount(submissionsCount)
                .stats(quizEntity
                        .getQuestions()
                        .stream()
                        .map(questionManagerService::getQuestionStats)
                        .toList())
                .build();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or @quizSecurityService.isUserQuizOwner(authentication.principal.id, #quizId)")
    public QuizResponse aiGenerateQuestions(
            UUID quizId,
            AiGenerateQuestionsRequest request,
            AuthenticatedUser user
    ) {
        final QuizEntity quizEntity = findQuizEntityById(quizId);

        final List<QuestionDto> currentQuestions = quizEntity
                .getQuestions()
                .stream()
                .map(questionMapper::toDto)
                .toList();

        final List<QuestionDto> newQuestions = aiQuestionGeneratorService
                .transformQuestions(currentQuestions, request.getPrompt());

        final QuizQuestionsUpsertRequest quizQuestionsUpsertRequest = QuizQuestionsUpsertRequest
                .builder()
                .questions(newQuestions)
                .build();

        return upsertQuestions(quizId, quizQuestionsUpsertRequest, user);
    }

    private QuizEntity findQuizEntityById(UUID quizId) {
        return quizRepository
                .findById(quizId)
                .orElseThrow(() -> new EntityNotFoundException("Quiz with id " + quizId + " not found"));
    }

    public QuizzesResponse getUserQuizzes(AuthenticatedUser user) {
        return QuizzesResponse
                .builder()
                .quizzes(quizRepository
                        .findByOwnerId(user.getId())
                        .stream()
                        .sorted(Comparator.comparing(QuizEntity::getCreatedDate).reversed())
                        .map(quizMapper::toShortResponse)
                        .toList())
                .build();
    }
}
