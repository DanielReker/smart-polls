package io.github.danielreker.smartquizzes.web.dtos;

import io.github.danielreker.smartquizzes.web.dtos.answers.AnswerDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Value;

import java.util.List;

@Value
public class SubmissionCreateRequest {

    @NotNull
    List<@NotNull @Valid AnswerDto> answers;

}
