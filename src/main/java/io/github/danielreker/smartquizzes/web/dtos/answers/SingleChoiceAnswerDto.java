package io.github.danielreker.smartquizzes.web.dtos.answers;

import io.github.danielreker.smartquizzes.model.QuestionType;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

import java.util.UUID;

@Getter
@SuperBuilder(toBuilder = true)
@Jacksonized
@EqualsAndHashCode(callSuper = true)
@ToString
public class SingleChoiceAnswerDto extends AnswerDto {

    @Override
    public QuestionType getQuestionType() {
        return QuestionType.SINGLE_CHOICE;
    }

    @NotNull
    private UUID selectedChoiceId;

}
