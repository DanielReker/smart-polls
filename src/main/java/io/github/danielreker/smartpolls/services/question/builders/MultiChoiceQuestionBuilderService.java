package io.github.danielreker.smartpolls.services.question.builders;

import io.github.danielreker.smartpolls.dao.entities.answers.MultiChoiceAnswerEntity;
import io.github.danielreker.smartpolls.dao.entities.questions.ChoiceEntity;
import io.github.danielreker.smartpolls.dao.entities.questions.MultiChoiceQuestionEntity;
import io.github.danielreker.smartpolls.mappers.ChoiceMapper;
import io.github.danielreker.smartpolls.mappers.QuestionMapper;
import io.github.danielreker.smartpolls.model.QuestionType;
import io.github.danielreker.smartpolls.web.dtos.answers.MultiChoiceAnswerDto;
import io.github.danielreker.smartpolls.web.dtos.questions.MultiChoiceQuestionDto;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class MultiChoiceQuestionBuilderService
        extends QuestionBuilderService<MultiChoiceQuestionEntity, MultiChoiceQuestionDto, MultiChoiceAnswerDto> {

    private final QuestionMapper questionMapper;

    private final ChoiceMapper choiceMapper;

    @Override
    public QuestionType getQuestionType() {
        return QuestionType.MULTI_CHOICE;
    }

    @Override
    public MultiChoiceAnswerEntity buildConcreteAnswer(MultiChoiceQuestionEntity question, MultiChoiceAnswerDto answerDto) {
        final List<ChoiceEntity> choices = answerDto
                .getSelectedChoiceIds()
                .stream()
                .map(selectedChoiceId -> question
                        .getPossibleChoices()
                        .stream()
                        .filter(item -> Objects.equals(item.getId(), selectedChoiceId))
                        .findFirst()
                        .orElseThrow(() -> new EntityNotFoundException("Could not find choice with id " +
                                selectedChoiceId + " in question " + question.getId())))
                .toList();

        final MultiChoiceAnswerEntity answerEntity = new MultiChoiceAnswerEntity();
        answerEntity.setSelectedChoices(choices);
        answerEntity.setQuestion(question);
        return answerEntity;
    }

    @Override
    public MultiChoiceQuestionEntity buildQuestion(MultiChoiceQuestionDto questionDto) {
        final MultiChoiceQuestionEntity question = questionMapper.toEntity(questionDto);

        question.setPossibleChoices(questionDto
                .getPossibleChoices()
                .stream()
                .map(choiceMapper::toEntity)
                .peek(choice -> choice.setQuestion(question))
                .toList());

        return question;
    }

}
