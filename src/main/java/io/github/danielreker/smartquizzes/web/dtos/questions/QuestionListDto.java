package io.github.danielreker.smartquizzes.web.dtos.questions;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record QuestionListDto(

        @NotNull
        List<@NotNull @Valid QuestionDto> questions

) {
}
