package io.github.danielreker.smartquizzes.web.dtos.auth;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder(toBuilder = true)
@Jacksonized
public class ChangeCredentialsRequest {

    @Nullable
    String currentPassword;

    @Nullable
    @Pattern(regexp = "^[a-zA-Z0-9._-]{5,30}$")
    String newLogin;

    @Nullable
    @Size(min = 6)
    String newPassword;

}
