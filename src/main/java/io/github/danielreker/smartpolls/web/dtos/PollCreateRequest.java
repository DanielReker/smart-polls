package io.github.danielreker.smartpolls.web.dtos;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder(toBuilder = true)
@Jacksonized
public class PollCreateRequest {

    @NotNull
    @NotBlank
    @Size(max = 255)
    String name;

    @Nullable
    String description;

}
