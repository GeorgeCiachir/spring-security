package com.georgeciachir.ch7_configuring_authorization_restricting_access.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import java.util.List;

@Configuration
public class UserManagementConfig {

    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails john = User.withUsername("John")
                .password("1234")
                .authorities("read")
                .build();

        UserDetails jane = User.withUsername("Jane")
                .password("1234")
                .authorities("ROLE_ADMIN", "write")
                // this one will add the ROLE_ prefix and the result is the same as the one above
                .roles("ADMIN")
                .build();

        UserDetails alex = User.withUsername("Alex")
                .password("1234")
                .authorities("other")
                .build();

        return new InMemoryUserDetailsManager(List.of(john, jane, alex));
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }
}
