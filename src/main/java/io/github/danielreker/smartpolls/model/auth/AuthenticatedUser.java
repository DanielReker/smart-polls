package io.github.danielreker.smartpolls.model.auth;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.util.List;
import java.util.UUID;

@Value
@Builder(toBuilder = true)
@Jacksonized
public class AuthenticatedUser {

    UUID id;

    UUID tokenId;

    String login;

    List<UserRole> roles;

}
