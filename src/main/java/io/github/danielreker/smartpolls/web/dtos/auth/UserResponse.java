package io.github.danielreker.smartpolls.web.dtos.auth;

import io.github.danielreker.smartpolls.model.auth.UserRole;
import jakarta.annotation.Nullable;
import lombok.Builder;

import javax.validation.constraints.NotNull;
import java.util.List;

@Builder(toBuilder = true)
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
