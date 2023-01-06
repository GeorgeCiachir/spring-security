package com.georgeciachir.ch9_implementing_filters.config;

import com.georgeciachir.ch9_implementing_filters.config.filter.AuthenticationLoggingFilter;
import com.georgeciachir.ch9_implementing_filters.config.filter.RequestValidatorFilter;
import com.georgeciachir.ch9_implementing_filters.config.filter.StaticKeyAuthorizationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@EnableWebSecurity
@Configuration
public class SecurityConfigNew {

    @Autowired
    private RequestValidatorFilter requestValidatorFilter;
    @Autowired
    private AuthenticationLoggingFilter authenticationLoggingFilter;
    @Autowired
    private StaticKeyAuthorizationFilter staticKeyAuthorizationFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .addFilterBefore(requestValidatorFilter, BasicAuthenticationFilter.class)
                .addFilterAfter(authenticationLoggingFilter, BasicAuthenticationFilter.class)
                // If multiple filters are at the same position, the order in which they are called is not defined
                // note that in this, case I haven't enabled the BasicAuthenticationFilter because I haven't called the http.httpBasic() method

                // If http.httpBasic() is called, then both StaticKeyAuthorizationFilter and BasicAuthenticationFilter would be at the same
                // position and there is no way to tell in which order they are called
                .addFilterAt(staticKeyAuthorizationFilter, BasicAuthenticationFilter.class);

        return http.build();
    }
}