package io.github.danielreker.smartpolls.web.dtos.auth;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class LoginRequest {

    @NonNull
    String password;

}
