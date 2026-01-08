package io.github.danielreker.smartpolls.services.question.builders;

import io.github.danielreker.smartpolls.dao.entities.answers.SingleChoiceAnswerEntity;
import io.github.danielreker.smartpolls.dao.entities.questions.ChoiceEntity;
import io.github.danielreker.smartpolls.dao.entities.questions.SingleChoiceQuestionEntity;
import io.github.danielreker.smartpolls.mappers.ChoiceMapper;
import io.github.danielreker.smartpolls.mappers.QuestionMapper;
import io.github.danielreker.smartpolls.model.QuestionType;
import io.github.danielreker.smartpolls.web.dtos.answers.SingleChoiceAnswerDto;
import io.github.danielreker.smartpolls.web.dtos.questions.SingleChoiceQuestionDto;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class SingleChoiceQuestionBuilderService
        extends QuestionBuilderService<SingleChoiceQuestionEntity, SingleChoiceQuestionDto, SingleChoiceAnswerDto> {

    private final QuestionMapper questionMapper;

    private final ChoiceMapper choiceMapper;

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
    public SingleChoiceQuestionEntity buildQuestion(SingleChoiceQuestionDto questionDto) {
        final SingleChoiceQuestionEntity question = questionMapper.toEntity(questionDto);

        question.setPossibleChoices(questionDto
                .getPossibleChoices()
                .stream()
                .map(choiceMapper::toEntity)
                .peek(choice -> choice.setQuestion(question))
                .toList());

        return question;
    }

}
