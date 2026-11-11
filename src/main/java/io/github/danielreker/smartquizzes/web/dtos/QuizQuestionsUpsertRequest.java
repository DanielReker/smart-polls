package io.github.danielreker.smartquizzes.web.dtos;

import io.github.danielreker.smartquizzes.web.dtos.questions.QuestionDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@Builder(toBuilder = true)
@Jacksonized
public record QuizQuestionsUpsertRequest(
        @NotNull @Valid List<QuestionDto> questions
) {
}
