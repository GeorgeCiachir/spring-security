package com.georgeciachir.ch3_managing_users.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;

import javax.sql.DataSource;

import static org.springframework.security.core.userdetails.jdbc.JdbcDaoImpl.DEF_AUTHORITIES_BY_USERNAME_QUERY;
import static org.springframework.security.core.userdetails.jdbc.JdbcDaoImpl.DEF_USERS_BY_USERNAME_QUERY;

@Configuration
public class UserManagementConfig {

    @Bean
    public UserDetailsService userDetailsService(DataSource dataSource) {
        JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager(dataSource);

        // The DEF_USERS_BY_USERNAME_QUERY and DEF_AUTHORITIES_BY_USERNAME_QUERY are the actually ones used by the default JdbcUserDetailsManager
        // In our case, the user and authorities match exactly the table structure that Spring expects in these 2 queries
        // The queries can be changed however, just as exemplified below, so that they match a particular/custom table structure, if we need to use another one
        jdbcUserDetailsManager.setUsersByUsernameQuery(DEF_USERS_BY_USERNAME_QUERY);
        jdbcUserDetailsManager.setAuthoritiesByUsernameQuery(DEF_AUTHORITIES_BY_USERNAME_QUERY);

        return jdbcUserDetailsManager;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }
}
