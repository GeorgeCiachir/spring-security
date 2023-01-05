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
        // With this config class enabled by the Spring profile, you can:
        //
        // 1. set the userDetailsService and the passwordEncoder, and the framework will use the DaoAuthenticationProvider for auth
        // 2. set the customAuthenticationProvider on the AuthenticationManagerBuilder and this auth provider will be used for auth
        // 3. set all of them (customAuthenticationProvider, userDetailsService, passwordEncoder) and the framework
        //    will use both the customAuthenticationProvider and the DaoAuthenticationProvider. If one of them returns
        //    a valid Authentication, the other one will be skipped

//         auth.userDetailsService(userDetailsService)
//                 .passwordEncoder(passwordEncoder);
         auth.authenticationProvider(customAuthenticationProvider);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.httpBasic()
                .and()
                .authorizeRequests()
                .anyRequest().authenticated();
    }
}
