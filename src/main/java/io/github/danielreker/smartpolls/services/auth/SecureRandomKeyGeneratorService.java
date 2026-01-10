package io.github.danielreker.smartpolls.services.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;

@RequiredArgsConstructor
@Service
public class SecureRandomKeyGeneratorService {

    private final SecureRandom secureRandom;


    public String generate(int bytes) {
        final byte[] buffer = new byte[bytes];
        secureRandom.nextBytes(buffer);
        return Base64
                .getEncoder()
                .encodeToString(buffer);
    }

}
