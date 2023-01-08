package com.georgeciachir.ch10_applying_csrf_and_cors.config.csrf;

import com.georgeciachir.ch10_applying_csrf_and_cors.config.csrf.entity.Token;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.DefaultCsrfToken;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class CustomCsrfTokenRepository implements CsrfTokenRepository {

    private static final String PARAMETER_NAME = "_csrf";
    private static final String HEADER = "X-CSRF-TOKEN";
    private static final String IDENTIFIER = "X-IDENTIFIER";

    @Autowired
    private JpaCsrfTokenRepository tokenRepository;

    @Override
    public CsrfToken generateToken(HttpServletRequest request) {
        String tokenValue = UUID.randomUUID().toString();
        return new DefaultCsrfToken(HEADER, PARAMETER_NAME, tokenValue);
    }

    @Override
    public void saveToken(CsrfToken csrfToken, HttpServletRequest request, HttpServletResponse response) {
        // In the default Spring implementation, the identifier is the session id.
        // However, the solution with the session doesn't scale too well. For example, in a microservices environment,
        // where the client would have multiple sessions (one for each server) in which the csrf tokens are stored.
        // So, a better solution is to store the csrf token in a DB, to be globally available. The client would of course, need
        // to have a unique identifier to query the token
        String clientIdentifier = request.getHeader(IDENTIFIER);

        tokenRepository.findTokenByIdentifier(clientIdentifier)
                .ifPresentOrElse(
                        existingToken -> updateClientToken(existingToken, csrfToken.getToken()),
                        () -> createClientToken(clientIdentifier, csrfToken.getToken()));
    }

    @Override
    public CsrfToken loadToken(HttpServletRequest request) {
        String clientIdentifier = request.getHeader(IDENTIFIER);

        return tokenRepository.findTokenByIdentifier(clientIdentifier)
                .map(token -> new DefaultCsrfToken(HEADER, PARAMETER_NAME, token.getToken()))
                .orElse(null);
    }

    private void updateClientToken(Token token, String newValue) {
        token.setToken(newValue);
    }

    private void createClientToken(String identifier, String tokenValue) {
        Token token = new Token();
        token.setIdentifier(identifier);
        token.setToken(tokenValue);
        tokenRepository.save(token);
    }
}
