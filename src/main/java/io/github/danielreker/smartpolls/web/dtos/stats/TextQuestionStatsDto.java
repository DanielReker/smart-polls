package io.github.danielreker.smartpolls.web.dtos.stats;

import io.github.danielreker.smartpolls.model.QuestionType;
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
public class TextQuestionStatsDto extends QuestionStatsDto {

    @Override
    public QuestionType getQuestionType() {
        return QuestionType.TEXT;
    }

    // TODO: Add AI summary

}
