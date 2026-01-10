package io.github.danielreker.smartpolls.model.auth;

import lombok.Builder;
import lombok.Value;

import java.util.List;
import java.util.UUID;

@Value
@Builder(toBuilder = true)
public class AuthenticatedUser {

    Long id;

    UUID tokenId;

    String login;

    List<UserRole> roles;

}
