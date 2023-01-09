package com.georgeciachir.businessserver.auth.config;


import com.georgeciachir.businessserver.auth.filter.JwtAuthenticationFilter;
import com.georgeciachir.businessserver.auth.filter.LoginFilter;
import com.georgeciachir.businessserver.auth.provider.OtpAuthenticationProvider;
import com.georgeciachir.businessserver.auth.provider.UserNamePasswordAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@EnableWebSecurity
@Configuration
public class SecurityConfigNew {

    @Value("${jwt.signing.key}")
    private String signingKey;

    @Autowired
    private OtpAuthenticationProvider otpAuthenticationProvider;

    @Autowired
    private UserNamePasswordAuthenticationProvider userNamePasswordAuthenticationProvider;

    @Bean
    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.authenticationProvider(otpAuthenticationProvider);
        authenticationManagerBuilder.authenticationProvider(userNamePasswordAuthenticationProvider);
        return authenticationManagerBuilder.build();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, AuthenticationManager authManager) throws Exception {
        return http.csrf().disable()
                .addFilterAt(new LoginFilter(signingKey, authManager), BasicAuthenticationFilter.class)
                .addFilterAfter(new JwtAuthenticationFilter(signingKey), BasicAuthenticationFilter.class)
                .authorizeHttpRequests()
                .anyRequest().authenticated()
                .and()
                .authenticationManager(authManager)
                .build();
    }
}
