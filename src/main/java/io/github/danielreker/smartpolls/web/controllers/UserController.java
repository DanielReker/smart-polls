package io.github.danielreker.smartpolls.web.controllers;

import io.github.danielreker.smartpolls.model.auth.AuthenticatedUser;
import io.github.danielreker.smartpolls.services.auth.UserService;
import io.github.danielreker.smartpolls.web.dtos.auth.ChangeCredentialsRequest;
import io.github.danielreker.smartpolls.web.dtos.auth.LoginRequest;
import io.github.danielreker.smartpolls.web.dtos.auth.SessionResponse;
import io.github.danielreker.smartpolls.web.dtos.auth.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
@CrossOrigin
class UserController {

    private final UserService userService;


    @PostMapping
    public ResponseEntity<SessionResponse> createUser() {
        final SessionResponse response =
                userService.createUser();

        return ResponseEntity
                .ok(response);
    }

    @Operation(security = { @SecurityRequirement(name = "bearer-key") })
    @PatchMapping("/me/credentials")
    public ResponseEntity<Void> updateCredentials(
            @RequestBody @Valid ChangeCredentialsRequest request,
            @AuthenticationPrincipal AuthenticatedUser user
    ) {
        userService.updateCredentials(user, request);

        return ResponseEntity
                .noContent()
                .build();
    }

    @PostMapping("/{userLogin}/sessions")
    public ResponseEntity<SessionResponse> login(
            @PathVariable String userLogin,
            @RequestBody @Valid LoginRequest request
    ) {
        final SessionResponse response =
                userService.login(userLogin, request);

        return ResponseEntity
                .ok(response);
    }

    @Operation(security = { @SecurityRequirement(name = "bearer-key") })
    @DeleteMapping("/me/sessions/current")
    public ResponseEntity<Void> logout(
            @AuthenticationPrincipal AuthenticatedUser user
    ) {
        userService.logout(user);

        return ResponseEntity
                .noContent()
                .build();
    }

    @Operation(security = { @SecurityRequirement(name = "bearer-key") })
    @DeleteMapping("/me/sessions")
    public ResponseEntity<Void> logoutEverywhere(
            @AuthenticationPrincipal AuthenticatedUser user
    ) {
        userService.logoutEverywhere(user);

        return ResponseEntity
                .noContent()
                .build();
    }

    @Operation(security = { @SecurityRequirement(name = "bearer-key") })
    @GetMapping("/me")
    public ResponseEntity<UserResponse> getUserMe(
            @AuthenticationPrincipal AuthenticatedUser user
    ) {
        final UserResponse response =
                userService.getUser(user);

        return ResponseEntity
                .ok(response);
    }


}
