package io.github.danielreker.smartquizzes.web.dtos.questions;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.util.UUID;

@Value
@Builder(toBuilder = true)
@Jacksonized
public class ChoiceDto {

    @Nullable
    UUID id;

    @NotNull
    String name;

    @NotNull
    @Min(0)
    Integer position;

}
