package io.github.danielreker.smartpolls.web.dtos.auth;

import io.github.danielreker.smartpolls.model.auth.UserRole;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@Builder(toBuilder = true)
@Jacksonized
public record UserResponse(

        @NotNull
        Long id,

        @Nullable
        String login,

        @NotNull
        List<UserRole> roles,

        @NotNull
        Boolean isRegistered

) {
}
