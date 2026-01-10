package io.github.danielreker.smartpolls.model.auth;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public class SessionAuthenticationToken extends AbstractAuthenticationToken {

    private final AuthenticatedUser principal;


    public SessionAuthenticationToken(AuthenticatedUser principal) {
        super(principal
                .getRoles()
                .stream()
                .map(r -> new SimpleGrantedAuthority("ROLE_" + r.name()))
                .toList());
        this.principal = principal;
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }

}
