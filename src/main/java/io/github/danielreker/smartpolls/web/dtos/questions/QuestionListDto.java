package io.github.danielreker.smartpolls.web.dtos.questions;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record QuestionListDto(
        @NotNull @Valid List<QuestionDto> questions
) {
}
