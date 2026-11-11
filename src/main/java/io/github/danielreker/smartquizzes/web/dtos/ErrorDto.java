package io.github.danielreker.smartquizzes.web.dtos;

import jakarta.validation.constraints.NotNull;

public record ErrorDto(
        @NotNull String message
) {
}
