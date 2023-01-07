package com.georgeciachir.ch9_implementing_filters.config.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class StaticKeyAuthorizationFilter implements Filter {

    private String authorizationKey;

    public StaticKeyAuthorizationFilter(String authorizationKey) {
        this.authorizationKey = authorizationKey;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String authentication = request.getHeader("Authorization");

        if (authorizationKey.equals(authentication)) {
            filterChain.doFilter(servletRequest, servletResponse);
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }
}
