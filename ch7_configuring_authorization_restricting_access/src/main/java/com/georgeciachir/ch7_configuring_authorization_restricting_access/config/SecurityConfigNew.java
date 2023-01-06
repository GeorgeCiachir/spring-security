package com.georgeciachir.ch7_configuring_authorization_restricting_access.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration
public class SecurityConfigNew {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.httpBasic()
                .and()
                .authorizeHttpRequests()
                // the hasRole() method delegates to the AuthorityAuthorizationManager which ads the ROLE_ prefix to this declared role
                .anyRequest().hasRole("ADMIN")
                .anyRequest().hasAnyAuthority("ROLE_ADMIN", "READ", "WRITE");

        return http.build();
    }
}