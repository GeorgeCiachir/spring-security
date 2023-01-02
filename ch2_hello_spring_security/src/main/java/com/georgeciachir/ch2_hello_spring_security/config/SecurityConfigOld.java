package com.georgeciachir.ch2_hello_spring_security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

@Profile("securityCfgOld")
@Configuration
public class SecurityConfigOld extends WebSecurityConfigurerAdapter {

    @Autowired
    private AuthenticationProvider customAuthenticationProvider;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // You can either inject the customAuthenticationProvider, the userDetailsService and the passwordEncoder,
        // and manually set them here, or let Spring discover and use the beans

        // auth.userDetailsService(userDetailsService)
        //         .passwordEncoder(passwordEncoder);
        // auth.authenticationProvider(customAuthenticationProvider);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.httpBasic()
                .and()
                .authorizeRequests()
                .anyRequest().authenticated();
    }
}
