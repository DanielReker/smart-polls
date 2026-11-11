package io.github.danielreker.smartquizzes.web.dtos.auth;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder(toBuilder = true)
@Jacksonized
public class LoginRequest {

    @NonNull
    String password;

}
