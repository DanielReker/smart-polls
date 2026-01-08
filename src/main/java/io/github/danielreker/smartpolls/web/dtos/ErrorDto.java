package io.github.danielreker.smartpolls.web.dtos;

import jakarta.validation.constraints.NotNull;

public record ErrorDto(
        @NotNull String message
) {
}
