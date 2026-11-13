package io.github.danielreker.smartquizzes.services.question;

import io.github.danielreker.smartquizzes.dao.entities.answers.SingleChoiceAnswerEntity;
import io.github.danielreker.smartquizzes.dao.entities.questions.ChoiceEntity;
import io.github.danielreker.smartquizzes.dao.entities.questions.SingleChoiceQuestionEntity;
import io.github.danielreker.smartquizzes.dao.repositories.answers.SingleChoiceAnswerRepository;
import io.github.danielreker.smartquizzes.mappers.ChoiceMapper;
import io.github.danielreker.smartquizzes.mappers.HintMapper;
import io.github.danielreker.smartquizzes.mappers.QuestionMapper;
import io.github.danielreker.smartquizzes.model.QuestionType;
import io.github.danielreker.smartquizzes.web.dtos.answers.SingleChoiceAnswerDto;
import io.github.danielreker.smartquizzes.web.dtos.questions.SingleChoiceQuestionDto;
import io.github.danielreker.smartquizzes.web.dtos.stats.ChoiceStatsDto;
import io.github.danielreker.smartquizzes.web.dtos.stats.SingleChoiceQuestionStatsDto;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class SingleChoiceQuestionService
        extends QuestionService<SingleChoiceQuestionEntity, SingleChoiceQuestionDto, SingleChoiceAnswerDto> {

    private final ChoiceMapper choiceMapper;

    private final SingleChoiceAnswerRepository singleChoiceAnswerRepository;


    public SingleChoiceQuestionService(
            HintMapper hintMapper,
            QuestionMapper questionMapper,
            ChoiceMapper choiceMapper,
            SingleChoiceAnswerRepository singleChoiceAnswerRepository
    ) {
        super(hintMapper, questionMapper);
        this.choiceMapper = choiceMapper;
        this.singleChoiceAnswerRepository = singleChoiceAnswerRepository;
    }


    @Override
    public QuestionType getQuestionType() {
        return QuestionType.SINGLE_CHOICE;
    }

    @Override
    public SingleChoiceAnswerEntity buildConcreteAnswer(SingleChoiceQuestionEntity question, SingleChoiceAnswerDto answerDto) {
        final ChoiceEntity choice = question
                .getPossibleChoices()
                .stream()
                .filter(item -> Objects.equals(item.getId(), answerDto.getSelectedChoiceId()))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Could not find choice with id " +
                        answerDto.getSelectedChoiceId() + " in question " + question.getId()));

        final SingleChoiceAnswerEntity answerEntity = new SingleChoiceAnswerEntity();
        answerEntity.setSelectedChoice(choice);
        answerEntity.setQuestion(question);
        return answerEntity;
    }

    @Override
    public SingleChoiceQuestionEntity buildConcreteQuestion(SingleChoiceQuestionDto questionDto) {
        final SingleChoiceQuestionEntity question = questionMapper.toEntity(questionDto);

        question.setPossibleChoices(questionDto
                .getPossibleChoices()
                .stream()
                .map(choiceMapper::toEntity)
                .peek(choice -> choice.setQuestion(question))
                .toList());

        validateCorrectChoicesNumber(question);

        return question;
    }

    private void validateCorrectChoicesNumber(SingleChoiceQuestionEntity questionEntity) {
        final long correctChoicesNumber = questionEntity
                .getPossibleChoices()
                .stream()
                .filter(ChoiceEntity::getIsCorrect)
                .count();

        if (correctChoicesNumber != 1) {
            throw new IllegalArgumentException(
                    getQuestionType().getName() + " question must have exactly 1 correct choice"
            );
        }
    }

    @Override
    public SingleChoiceQuestionStatsDto getQuestionStats(SingleChoiceQuestionEntity question) {
        return SingleChoiceQuestionStatsDto
                .builder()
                .questionId(question.getId())
                .answerCount(singleChoiceAnswerRepository.countByQuestionId(question.getId()))
                .choiceStats(question
                        .getPossibleChoices()
                        .stream()
                        .map(choice -> ChoiceStatsDto
                                .builder()
                                .id(choice.getId())
                                .count(singleChoiceAnswerRepository.countChoiceSelectionsByChoiceId(choice.getId()))
                                .build())
                        .toList())
                .build();
    }

}
