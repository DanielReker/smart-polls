package io.github.danielreker.smartpolls.web.dtos;

import lombok.Builder;

import javax.validation.constraints.NotNull;
import java.util.List;

@Builder(toBuilder = true)
public record PollsResponse(

        @NotNull
        List<ShortPollResponse> polls

) {
}
