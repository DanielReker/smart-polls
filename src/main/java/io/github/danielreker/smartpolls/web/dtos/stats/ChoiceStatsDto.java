package io.github.danielreker.smartpolls.web.dtos.stats;

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
public class ChoiceStatsDto {

    @Nullable
    UUID id;

    @NotNull
    @Min(0)
    Long count;

}
