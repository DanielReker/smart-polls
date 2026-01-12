package io.github.danielreker.smartpolls.web.dtos;

import io.github.danielreker.smartpolls.dao.entities.PollStatus;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.time.Instant;

@Builder(toBuilder = true)
public record ShortPollResponse(

    @NotNull
    Long id,

    @NotNull
    Instant createdDate,

    @NotNull
    @NotBlank
    @Size(max = 255)
    String name,

    @Nullable
    String description,

    @NotNull
    PollStatus status

) {
}
