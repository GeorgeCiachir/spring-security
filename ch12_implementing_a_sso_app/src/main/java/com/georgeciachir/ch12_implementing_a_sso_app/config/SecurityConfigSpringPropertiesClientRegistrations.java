package com.georgeciachir.ch12_implementing_a_sso_app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Profile("spring_properties_client_registration")
@EnableWebSecurity
@Configuration
public class SecurityConfigSpringPropertiesClientRegistrations {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.oauth2Login()
                .and()
                .authorizeHttpRequests()
                .anyRequest().authenticated();

        return http.build();
    }
}
