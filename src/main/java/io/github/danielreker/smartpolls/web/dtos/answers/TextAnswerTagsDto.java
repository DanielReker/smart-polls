package io.github.danielreker.smartpolls.web.dtos.answers;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public record TextAnswerTagsDto(
        @NotNull List<String> tags
) {
}
