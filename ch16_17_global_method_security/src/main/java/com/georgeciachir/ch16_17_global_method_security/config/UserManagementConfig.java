package com.georgeciachir.ch16_17_global_method_security.config;

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
                .authorities("write")
                .roles("ADMIN")
                .build();

        UserDetails alex = User.withUsername("Alex")
                .password("1234")
                .authorities("read","write")
                .build();

        return new InMemoryUserDetailsManager(List.of(john, jane, alex));
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }
}
