package com.georgeciachir.ch9_implementing_filters.config.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class RequestValidatorFilter extends OncePerRequestFilter {

    // The ExpressionUrlAuthorizationConfigurer has been deprecated, which means that the authorizeRequests() method on the HttpSecurity
    // object has also been deprecated. The ExpressionUrlAuthorizationConfigurer had the option to permit access based on
    // an SPEl expression passed to the access(). Of course, this should no longer be used, even if available.

    // In this case, I would say the request id is not the responsibility of a security filter, but foe the sake of the example, is good enough

    // We could use this filter to do what we should no longer do with the ExpressionUrlAuthorizationConfigurer
    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        String requestId = request.getHeader("request-id");

        if (requestId == null || requestId.isBlank()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        filterChain.doFilter(request, response);
    }
}
