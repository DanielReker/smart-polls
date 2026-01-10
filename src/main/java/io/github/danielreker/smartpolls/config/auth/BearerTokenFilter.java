package io.github.danielreker.smartpolls.config.auth;

import io.github.danielreker.smartpolls.model.auth.SessionAuthenticationToken;
import io.github.danielreker.smartpolls.services.auth.UserSessionService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
@Component
public class BearerTokenFilter extends OncePerRequestFilter {

    private final UserSessionService userSessionService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        final String header = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (header != null && header.startsWith("Bearer ")) {
            final String rawToken = header.substring(7);

            userSessionService
                    .authenticate(rawToken)
                    .map(SessionAuthenticationToken::new)
                    .ifPresent(auth ->
                            SecurityContextHolder
                                    .getContext()
                                    .setAuthentication(auth)
                    );
        }

        filterChain.doFilter(request, response);
    }
}
