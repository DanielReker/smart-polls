package io.github.danielreker.smartquizzes.web.dtos;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@Builder(toBuilder = true)
@Jacksonized
public record QuizzesResponse(

        @NotNull
        List<@NotNull @Valid ShortQuizResponse> quizzes

) {
}
