package io.github.danielreker.smartpolls.web.dtos.stats;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class ChoiceStatsDto {

    @Nullable
    Long id;

    @NotNull
    @Min(0)
    Long count;

}
