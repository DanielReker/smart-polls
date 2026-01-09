package io.github.danielreker.smartpolls.services.question;

import io.github.danielreker.smartpolls.dao.entities.answers.AnswerEntity;
import io.github.danielreker.smartpolls.dao.entities.questions.QuestionEntity;
import io.github.danielreker.smartpolls.model.QuestionType;
import io.github.danielreker.smartpolls.web.dtos.answers.AnswerDto;
import io.github.danielreker.smartpolls.web.dtos.questions.QuestionDto;
import io.github.danielreker.smartpolls.web.dtos.stats.QuestionStatsDto;
import lombok.NonNull;

public abstract class QuestionService<Q_ENTITY extends QuestionEntity, Q_DTO extends QuestionDto, A_DTO extends AnswerDto> {

    public abstract QuestionType getQuestionType();

    // Safe if entity/DTO types are overridden correctly
    @SuppressWarnings("unchecked")
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

    public abstract QuestionStatsDto getQuestionStats(Q_ENTITY question);

}
