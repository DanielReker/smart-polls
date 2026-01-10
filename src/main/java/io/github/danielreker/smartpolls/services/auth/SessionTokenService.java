package io.github.danielreker.smartpolls.services.auth;

import io.github.danielreker.smartpolls.model.auth.SessionToken;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class SessionTokenService {

    private final PasswordEncoder passwordEncoder;

    private final SecureRandomKeyGeneratorService secureRandomKeyGeneratorService;


    public SessionToken create() {
        return SessionToken
                .builder()
                .tokenId(UUID.randomUUID())
                .token(secureRandomKeyGeneratorService.generate(32))
                .build();
    }

    public String hashToken(String token) {
        return passwordEncoder.encode(token);
    }

    public boolean matches(String token, String hash) {
        return passwordEncoder.matches(token, hash);
    }

}
