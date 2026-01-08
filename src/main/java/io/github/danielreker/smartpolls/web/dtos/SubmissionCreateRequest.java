package io.github.danielreker.smartpolls.web.dtos;

import io.github.danielreker.smartpolls.web.dtos.answers.AnswerDto;
import jakarta.validation.constraints.NotNull;
import lombok.Value;

import java.util.List;

@Value
public class SubmissionCreateRequest {

    @NotNull
    List<AnswerDto> answers;

}
