package io.github.danielreker.smartquizzes.web.dtos;

import io.github.danielreker.smartquizzes.web.dtos.answers.AnswerDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Value;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Value
public class SubmissionResponse {

    @NotNull
    UUID id;

    @NotNull
    UUID quizId;

    @NotNull
    Instant createdDate;

    @NotNull
    @Valid
    List<AnswerDto> answers;

}
