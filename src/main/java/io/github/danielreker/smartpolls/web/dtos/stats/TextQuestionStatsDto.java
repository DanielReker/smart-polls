package io.github.danielreker.smartpolls.web.dtos.stats;

import io.github.danielreker.smartpolls.model.QuestionType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

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

    @NotNull
    public Long tagsCount;

    @NotNull
    @Valid
    public List<AiTextQuestionSummaryTagDto> tags;

}
