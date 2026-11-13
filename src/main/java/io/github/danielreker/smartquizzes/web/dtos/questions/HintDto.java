package io.github.danielreker.smartquizzes.web.dtos.questions;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.math.BigDecimal;
import java.util.UUID;

@Value
@Builder(toBuilder = true)
@Jacksonized
public class HintDto {

    @Nullable
    UUID id;

    @NotNull
    Integer sequentialOrder;

    @NotNull
    @PositiveOrZero
    BigDecimal scorePenalty;

    @NotNull
    String text;

}
