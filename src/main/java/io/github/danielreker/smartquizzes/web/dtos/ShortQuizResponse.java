package io.github.danielreker.smartquizzes.web.dtos;

import io.github.danielreker.smartquizzes.dao.entities.QuizStatus;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.extern.jackson.Jacksonized;

import java.time.Instant;
import java.util.UUID;

@Builder(toBuilder = true)
@Jacksonized
public record ShortQuizResponse(

    @NotNull
    UUID id,

    @NotNull
    Instant createdDate,

    @NotNull
    @NotBlank
    @Size(max = 255)
    String name,

    @Nullable
    String description,

    @NotNull
    QuizStatus status

) {
}
