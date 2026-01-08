package io.github.danielreker.smartpolls.web.dtos;

import io.github.danielreker.smartpolls.web.dtos.questions.QuestionDto;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record PollQuestionsUpsertRequest(
        @NotNull List<QuestionDto> questions
) {
}
