package io.github.danielreker.smartpolls.web.dtos;

import io.github.danielreker.smartpolls.web.dtos.answers.AnswerDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Value;

import java.time.Instant;
import java.util.List;

@Value
public class SubmissionResponse {

    @NotNull
    Long id;

    @NotNull
    Long pollId;

    @NotNull
    Instant createdDate;

    @NotNull
    @Valid
    List<AnswerDto> answers;

}
