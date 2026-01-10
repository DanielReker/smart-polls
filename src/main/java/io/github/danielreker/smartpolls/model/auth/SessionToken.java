package io.github.danielreker.smartpolls.model.auth;

import lombok.Builder;
import lombok.Value;

import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Value
@Builder(toBuilder = true)
public class SessionToken {

    private static final Pattern RAW_TOKEN_PATTERN =
            Pattern.compile("^([0-9a-f]{8}(?:-[0-9a-f]{4}){3}-[0-9a-f]{12})\\.([A-Za-z0-9+/]{43}=)$");

    UUID tokenId;

    String token;

    public String getRaw() {
        return tokenId + "." + token;
    }

    public static SessionToken fromRaw(String rawToken) {
        final Matcher matcher = RAW_TOKEN_PATTERN
                .matcher(rawToken);

        if (!matcher.find() && matcher.groupCount() == 2) {
            throw new IllegalArgumentException("Invalid raw session token, should be in format: '<tokenId>.<token>'");
        }

        return SessionToken
                .builder()
                .tokenId(UUID.fromString(matcher.group(1)))
                .token(matcher.group(2))
                .build();

    }

}
