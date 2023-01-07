package com.georgeciachir.ch9_implementing_filters.config.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.logging.Logger;

@Component
public class AuthenticationLoggingFilter extends OncePerRequestFilter {

    private static final Logger LOG = Logger.getLogger(AuthenticationLoggingFilter.class.getName());

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        String requestId = request.getHeader("request-id");

        LOG.info("Successfully authenticated request with id: " + requestId);

        filterChain.doFilter(request, response);
    }
}
