package io.github.danielreker.smartpolls.services.auth;

import io.github.danielreker.smartpolls.dao.entities.auth.UserEntity;
import io.github.danielreker.smartpolls.dao.repositories.auth.UserRepository;
import io.github.danielreker.smartpolls.model.auth.AuthenticatedUser;
import io.github.danielreker.smartpolls.model.exceptions.InvalidPasswordException;
import io.github.danielreker.smartpolls.web.dtos.auth.ChangeCredentialsRequest;
import io.github.danielreker.smartpolls.web.dtos.auth.LoginRequest;
import io.github.danielreker.smartpolls.web.dtos.auth.SessionResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static io.github.danielreker.smartpolls.model.auth.UserRole.ANONYMOUS;
import static io.github.danielreker.smartpolls.model.auth.UserRole.REGISTERED;

@RequiredArgsConstructor
@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;

    private final UserSessionService userSessionService;

    private final PasswordEncoder passwordEncoder;


    public SessionResponse createUser() {
        final UserEntity userEntity = new UserEntity();
        userEntity.setRoles(List.of(ANONYMOUS));

        final UserEntity savedUserEntity = userRepository.save(userEntity);

        return userSessionService.createSession(savedUserEntity);
    }

    public void updateCredentials(AuthenticatedUser user, ChangeCredentialsRequest request) {
        final UserEntity userEntity = userRepository
                .findById(user.getId())
                .orElseThrow();

        validateUserPassword(request.getCurrentPassword(), userEntity.getPasswordHash());

        if (userEntity.getPasswordHash() == null &&
                (request.getNewPassword() == null || request.getNewLogin() == null)) {

            throw new IllegalArgumentException("To register you must provide both login and password");
        }

        if (userEntity.getPasswordHash() == null) {
            userEntity.getRoles().clear();
            userEntity.getRoles().add(REGISTERED);
        }

        if (request.getNewPassword() != null) {
            userEntity.setPasswordHash(passwordEncoder.encode(request.getNewPassword()));
        }

        if (request.getNewLogin() != null) {
            userEntity.setLogin(request.getNewLogin());
        }
    }


    private void validateUserPassword(String rawPassword, String passwordHash) {
        if (!(rawPassword == null && passwordHash == null) &&
                !(passwordEncoder.matches(rawPassword, passwordHash))) {

            throw new InvalidPasswordException("Invalid current password");
        }
    }

    public SessionResponse login(String userLogin, @Valid LoginRequest request) {
        final UserEntity userEntity = userRepository
                .findByLogin(userLogin)
                .orElseThrow();

        if (!passwordEncoder.matches(request.getPassword(), userEntity.getPasswordHash())) {
            throw new InvalidPasswordException("User %s tried to login with incorrect password".formatted(userLogin));
        }

        return userSessionService.createSession(userEntity);
    }

    public void logout(AuthenticatedUser user) {
        userSessionService.deleteSessionByTokenId(user.getTokenId());
    }

    public void logoutEverywhere(AuthenticatedUser user) {
        userSessionService.deleteAllSessionsByUserId(user.getId());
    }

    public UserEntity getUserReference(AuthenticatedUser user) {
        return userRepository.getReferenceById(user.getId());
    }
}
