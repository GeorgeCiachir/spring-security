package com.georgeciachir.ch10_applying_csrf_and_cors.config.csrf;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.logging.Logger;

@Component
public class CsrfTokenLoggingFilter extends OncePerRequestFilter {

    private static final Logger LOG = Logger.getLogger(CsrfTokenLoggingFilter.class.getName());

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        Object csrfAttribute = request.getAttribute("_csrf");
        CsrfToken csrf = (CsrfToken) csrfAttribute;

        LOG.info("CSRF token is: " + csrf.getToken());

        filterChain.doFilter(request, response);
    }
}
