package io.github.danielreker.smartpolls.services.question.builders;

import io.github.danielreker.smartpolls.dao.entities.answers.AnswerEntity;
import io.github.danielreker.smartpolls.dao.entities.questions.QuestionEntity;
import io.github.danielreker.smartpolls.model.QuestionType;
import io.github.danielreker.smartpolls.web.dtos.answers.AnswerDto;
import io.github.danielreker.smartpolls.web.dtos.questions.QuestionDto;
import lombok.NonNull;

public abstract class QuestionBuilderService<Q_ENTITY extends QuestionEntity, Q_DTO extends QuestionDto, A_DTO extends AnswerDto> {

    public abstract QuestionType getQuestionType();

    // TODO: Fix/suppress unsafe cast
    public AnswerEntity buildAnswer(@NonNull Q_ENTITY question, @NonNull AnswerDto answerDto) {
        if (question.getQuestionType() == answerDto.getQuestionType()) {
            return buildConcreteAnswer(question, (A_DTO) answerDto);
        } else {
            throw new IllegalArgumentException(
                    "Answer type mismatch: question with id %s has type '%s', answer should have the same type (actual: %s)"
                            .formatted(question.getId(), question.getQuestionType(), answerDto.getQuestionType())
            );
        }
    }

    public abstract AnswerEntity buildConcreteAnswer(Q_ENTITY question, A_DTO answerDto);

    public abstract QuestionEntity buildQuestion(Q_DTO questionDto);

}
