package io.github.danielreker.smartpolls.services;

import io.github.danielreker.smartpolls.dao.entities.PollEntity;
import io.github.danielreker.smartpolls.dao.entities.PollStatus;
import io.github.danielreker.smartpolls.dao.entities.SubmissionEntity;
import io.github.danielreker.smartpolls.dao.entities.answers.AnswerEntity;
import io.github.danielreker.smartpolls.dao.repositories.PollRepository;
import io.github.danielreker.smartpolls.dao.repositories.SubmissionRepository;
import io.github.danielreker.smartpolls.mappers.PollMapper;
import io.github.danielreker.smartpolls.mappers.SubmissionMapper;
import io.github.danielreker.smartpolls.model.exceptions.InvalidPollStatusException;
import io.github.danielreker.smartpolls.web.dtos.*;
import io.github.danielreker.smartpolls.web.dtos.answers.AnswerDto;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
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

    private final QuestionBuilderManagerService questionBuilderManagerService;




    public PollResponse createPoll(PollCreateRequest request) {
        PollEntity pollEntity = pollMapper.createRequestToEntity(request);
        pollEntity.setStatus(PollStatus.DRAFT);

        pollEntity = pollRepository.save(pollEntity);

        return pollMapper.toResponse(pollEntity);
    }

    public PollResponse getPoll(Long pollId) {
        final PollEntity pollEntity = findPollEntityById(pollId);

        return pollMapper.toResponse(pollEntity);
    }

    public PollResponse upsertQuestions(Long pollId, PollQuestionsUpsertRequest request) {
        final PollEntity pollEntity = findPollEntityById(pollId);

        if (pollEntity.getStatus() != PollStatus.DRAFT) {
            throw new InvalidPollStatusException("Can't update questions in non-draft poll," +
                    "its current status: " + pollEntity.getStatus());
        }

        pollEntity.getQuestions().clear();
        pollEntity
                .getQuestions()
                .addAll(request
                        .questions()
                        .stream()
                        .map(questionBuilderManagerService::buildQuestion)
                        .peek(questionEntity -> questionEntity.setPoll(pollEntity))
                        .toList());

        return pollMapper.toResponse(pollRepository.save(pollEntity));
    }

    public PollResponse startPoll(Long pollId) {
        final PollEntity pollEntity = findPollEntityById(pollId);

        if (pollEntity.getStatus() != PollStatus.DRAFT) {
            throw new InvalidPollStatusException("Can't start poll, its current status: " + pollEntity.getStatus());
        }

        pollEntity.setStatus(PollStatus.ACTIVE);

        return pollMapper.toResponse(pollRepository.save(pollEntity));
    }

    public PollResponse finishPoll(Long pollId) {
        final PollEntity pollEntity = findPollEntityById(pollId);

        if (pollEntity.getStatus() != PollStatus.ACTIVE) {
            throw new InvalidPollStatusException("Can't finish poll, its current status: " + pollEntity.getStatus());
        }

        pollEntity.setStatus(PollStatus.FINISHED);

        return pollMapper.toResponse(pollRepository.save(pollEntity));
    }

    public SubmissionResponse createSubmission(Long pollId, SubmissionCreateRequest request) {
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
                            .map(answerDto -> questionBuilderManagerService
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

        final SubmissionEntity savedSubmissionEntity =
                submissionRepository.save(submissionEntity);

        return submissionMapper.toResponse(savedSubmissionEntity);
    }


    private PollEntity findPollEntityById(Long pollId) {
        return pollRepository
                .findById(pollId)
                .orElseThrow(() -> new EntityNotFoundException("Poll with id " + pollId + " not found"));
    }

}
