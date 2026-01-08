package io.github.danielreker.smartpolls.web.dtos.questions;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class ChoiceDto {

    @Nullable
    Long id;

    @NotNull
    String name;

    @NotNull
    @Min(0)
    Integer position;

}
