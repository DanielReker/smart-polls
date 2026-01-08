package io.github.danielreker.smartpolls.services.question.builders;

import io.github.danielreker.smartpolls.dao.entities.answers.TextAnswerEntity;
import io.github.danielreker.smartpolls.dao.entities.questions.TextQuestionEntity;
import io.github.danielreker.smartpolls.mappers.QuestionMapper;
import io.github.danielreker.smartpolls.model.QuestionType;
import io.github.danielreker.smartpolls.model.exceptions.AnswerValidationException;
import io.github.danielreker.smartpolls.web.dtos.answers.TextAnswerDto;
import io.github.danielreker.smartpolls.web.dtos.questions.TextQuestionDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TextQuestionBuilderService
        extends QuestionBuilderService<TextQuestionEntity, TextQuestionDto, TextAnswerDto> {

    private final QuestionMapper questionMapper;

    @Override
    public QuestionType getQuestionType() {
        return QuestionType.TEXT;
    }

    @Override
    public TextAnswerEntity buildConcreteAnswer(TextQuestionEntity question, TextAnswerDto answerDto) {
        if (answerDto.getValue().length() > question.getMaxLength()) {
            throw new AnswerValidationException("Answer to question with id " + question.getId() +
                    " must not be longer than " + question.getMaxLength() + " symbols");
        }

        final TextAnswerEntity answerEntity = new TextAnswerEntity();
        answerEntity.setValue(answerDto.getValue());
        answerEntity.setQuestion(question);
        return answerEntity;
    }

    @Override
    public TextQuestionEntity buildQuestion(TextQuestionDto questionDto) {
        return questionMapper.toEntity(questionDto);
    }

}
