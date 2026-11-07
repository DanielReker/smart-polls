package io.github.danielreker.smartpolls.web.dtos.questions;

import io.github.danielreker.smartpolls.model.QuestionType;
import jakarta.annotation.Nullable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@Getter
@SuperBuilder(toBuilder = true)
@Jacksonized
@EqualsAndHashCode(callSuper = true)
@ToString
public class TextQuestionDto extends QuestionDto {

    @Override
    public QuestionType getQuestionType() {
        return QuestionType.TEXT;
    }

    @Nullable
    private Integer maxLength;

}
