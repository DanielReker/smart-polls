package io.github.danielreker.smartpolls.web.dtos.stats;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder(toBuilder = true)
public record AiTextQuestionSummaryTagDto(
        @NotNull String tag,
        @NotNull Long count
) {
}
