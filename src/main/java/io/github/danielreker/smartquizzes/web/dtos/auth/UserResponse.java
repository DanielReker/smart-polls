package io.github.danielreker.smartquizzes.web.dtos.auth;

import io.github.danielreker.smartquizzes.model.auth.UserRole;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.extern.jackson.Jacksonized;

import java.util.List;
import java.util.UUID;

@Builder(toBuilder = true)
@Jacksonized
public record UserResponse(

        @NotNull
        UUID id,

        @Nullable
        String login,

        @NotNull
        List<UserRole> roles,

        @NotNull
        Boolean isRegistered

) {
}
