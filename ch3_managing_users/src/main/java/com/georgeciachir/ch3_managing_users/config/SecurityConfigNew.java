package com.georgeciachir.ch3_managing_users.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Profile("securityCfgNew")
@EnableWebSecurity
@Configuration
public class SecurityConfigNew {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        configureAccessForH2DB(http);

        http.httpBasic()
                .and()
                .authorizeRequests()
                .anyRequest().authenticated();

        return http.build();
    }

    private void configureAccessForH2DB(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/h2-console/**").permitAll();

        http.csrf().ignoringAntMatchers("/h2-console/**");
        http.headers().frameOptions().sameOrigin();
    }
}