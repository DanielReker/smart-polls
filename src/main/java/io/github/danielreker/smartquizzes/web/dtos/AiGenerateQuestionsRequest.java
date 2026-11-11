package io.github.danielreker.smartquizzes.web.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder(toBuilder = true)
@Jacksonized
public class AiGenerateQuestionsRequest {

    @NotNull
    String prompt;

}
