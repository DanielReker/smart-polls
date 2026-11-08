package io.github.danielreker.smartpolls.web.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@Builder(toBuilder = true)
@Jacksonized
public record PollsResponse(

        @NotNull
        List<ShortPollResponse> polls

) {
}
