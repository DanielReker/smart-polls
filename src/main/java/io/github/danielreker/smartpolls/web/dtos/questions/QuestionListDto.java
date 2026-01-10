package io.github.danielreker.smartpolls.web.dtos.questions;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public record QuestionListDto(
        @NotNull List<QuestionDto> questions
) {
}
