package io.github.danielreker.smartpolls.services.auth;

import io.github.danielreker.smartpolls.dao.entities.auth.UserEntity;
import io.github.danielreker.smartpolls.dao.entities.auth.UserSessionEntity;
import io.github.danielreker.smartpolls.dao.repositories.auth.UserSessionRepository;
import io.github.danielreker.smartpolls.model.auth.AuthenticatedUser;
import io.github.danielreker.smartpolls.model.auth.SessionToken;
import io.github.danielreker.smartpolls.web.dtos.auth.SessionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
@Transactional
public class UserSessionService {

    private final UserSessionRepository userSessionRepository;

    private final SessionTokenService sessionTokenService;


    @Transactional(readOnly = true)
    public Optional<AuthenticatedUser> authenticate(String rawToken) {
        final SessionToken token = SessionToken.fromRaw(rawToken);

        final Instant now = Instant.now();

        return userSessionRepository
                .findByTokenId(token.getTokenId())
                .filter(session -> now.isBefore(session.getExpiryDate()))
                .filter(session -> sessionTokenService
                        .matches(token.getToken(), session.getTokenHash()))
                .map(session -> AuthenticatedUser
                        .builder()
                        .id(session.getUser().getId())
                        .tokenId(token.getTokenId())
                        .login(session.getUser().getLogin())
                        .roles(new ArrayList<>(session.getUser().getRoles()))
                        .build());
    }

    public SessionResponse createSession(UserEntity user) {
        final SessionToken token = sessionTokenService.create();

        final Instant now = Instant.now();

        final UserSessionEntity session = new UserSessionEntity();
        session.setUser(user);
        session.setTokenId(token.getTokenId());
        session.setTokenHash(sessionTokenService.hashToken(token.getToken()));
        session.setCreatedDate(now);
        session.setExpiryDate(now.plus(30, ChronoUnit.DAYS));

        userSessionRepository.save(session);

        return SessionResponse
                .builder()
                .token(token.getRaw())
                .build();
    }

    public void deleteSessionByTokenId(UUID tokenId) {
        userSessionRepository.deleteByTokenId(tokenId);
    }

    public void deleteAllSessionsByUserId(Long userId) {
        userSessionRepository.deleteAllByUserId(userId);
    }
}
