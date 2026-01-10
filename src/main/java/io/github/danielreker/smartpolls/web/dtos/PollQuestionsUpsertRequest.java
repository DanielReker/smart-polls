package io.github.danielreker.smartpolls.web.dtos;

import io.github.danielreker.smartpolls.web.dtos.questions.QuestionDto;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.util.List;

@Builder(toBuilder = true)
public record PollQuestionsUpsertRequest(
        @NotNull List<QuestionDto> questions
) {
}
