package com.georgeciachir.businessserver.auth.filter;

import com.georgeciachir.businessserver.auth.OtpAuthentication;
import com.georgeciachir.businessserver.auth.UserNamePasswordAuthentication;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.util.Map;

import static java.nio.charset.StandardCharsets.UTF_8;

@AllArgsConstructor
public class LoginFilter extends OncePerRequestFilter {

    private String signingKey;

    private AuthenticationManager authenticationManager;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String username = request.getHeader("username");
        String password = request.getHeader("password");
        String code = request.getHeader("code");

        if (code == null) {
            UserNamePasswordAuthentication authentication = new UserNamePasswordAuthentication(username, password);
            authenticationManager.authenticate(authentication);
        } else {
            OtpAuthentication authentication = new OtpAuthentication(username, code);
            Authentication authenticated = authenticationManager.authenticate(authentication);

            SecretKey secretKey = Keys.hmacShaKeyFor(signingKey.getBytes(UTF_8));

            String jwt = Jwts.builder()
                    .setClaims(Map.of("username", authenticated.getName()))
                    .signWith(secretKey)
                    .compact();

            response.setHeader("Authorization", jwt);
        }

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return !request.getServletPath().equals("/login");
    }
}
