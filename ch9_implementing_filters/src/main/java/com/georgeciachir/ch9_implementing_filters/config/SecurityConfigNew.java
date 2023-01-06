package com.georgeciachir.ch9_implementing_filters.config;

import com.georgeciachir.ch9_implementing_filters.config.filter.AuthenticationLoggingFilter;
import com.georgeciachir.ch9_implementing_filters.config.filter.RequestValidatorFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@EnableWebSecurity
@Configuration
public class SecurityConfigNew {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.httpBasic()
                .and()
                    .authorizeHttpRequests()
                    .anyRequest().authenticated()
                .and()
                    .addFilterBefore(new RequestValidatorFilter(), BasicAuthenticationFilter.class)
                    .addFilterAfter(new AuthenticationLoggingFilter(), BasicAuthenticationFilter.class);
                // if multiple filters are at the same position, the order in which they are called is not defined

        return http.build();
    }
}