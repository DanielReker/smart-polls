package io.github.danielreker.smartquizzes.web.dtos;

import io.github.danielreker.smartquizzes.dao.entities.QuizStatus;
import io.github.danielreker.smartquizzes.web.dtos.questions.QuestionDto;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Value
@Builder(toBuilder = true)
@Jacksonized
public class QuizResponse {

    @NotNull
    UUID id;

    @NotNull
    Instant createdDate;

    @NotNull
    @NotBlank
    @Size(max = 255)
    String name;

    @Nullable
    String description;

    @NotNull
    QuizStatus status;

    @NotNull
    @Valid
    List<QuestionDto> questions;

    @NotNull
    Long mySubmissionsCount;

}
