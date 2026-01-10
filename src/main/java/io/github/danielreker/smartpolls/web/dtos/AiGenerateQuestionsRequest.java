package io.github.danielreker.smartpolls.web.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class AiGenerateQuestionsRequest {

    @NotNull
    String prompt;

}
