package io.github.danielreker.smartpolls.web.dtos.auth;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder(toBuilder = true)
@Jacksonized
public class SessionResponse {

    @NotNull
    String token;

}
