package io.github.danielreker.smartpolls.web.dtos.auth;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class SessionResponse {

    @NotNull
    String token;

}
