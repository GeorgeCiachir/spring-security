package com.georgeciachir.ch10_applying_csrf_and_cors.config;

import com.georgeciachir.ch10_applying_csrf_and_cors.config.csrf.CsrfTokenLoggingFilter;
import com.georgeciachir.ch10_applying_csrf_and_cors.config.csrf.CustomCsrfTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.List;

@EnableWebSecurity
@Configuration
public class SecurityConfigNew {

    @Autowired
    private CsrfTokenLoggingFilter csrfTokenLoggingFilter;
    @Autowired
    private CustomCsrfTokenRepository csrfTokenRepository;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // use one or the other
        configureForNoAuthNoCsrfForTheTestCorsController(http);
//        configureForHttpBasicHelloControllerAndCustomCsrfImplementation(http);
//        configureForHttpBasicHelloController(http); // this is for the Hello Controller example
//        configureForFormLoginMainAndProductController(http); // this is for the Main and Product Controller example

        return http.build();
    }

    private void configureForNoAuthNoCsrfForTheTestCorsController(HttpSecurity http) throws Exception {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:8080"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
        CorsConfigurationSource source = request -> config;

        http.authorizeHttpRequests()
                .anyRequest().permitAll()
                .and()
                .csrf().disable()
                // This server's domain is http://127.0.0.1:8080. Even though localhost is resolved as 127.0.0.1
                // the CORS policy checks for the actual strings (127.0.0.1 vs localhost)

                // We can add the global CORS configuration here, or we could
                // define a local CORS rule for a method, using the @CrossOrigin. See the TestCorsController
                .cors(c -> c.configurationSource(source));
    }

    private void configureForHttpBasicHelloControllerAndCustomCsrfImplementation(HttpSecurity http) throws Exception {
        // By default, Spring now uses the XorCsrfTokenRequestAttributeHandler
        // When working with JS clients (React, Angular etc...) that doesn't work out of the box due to the
        // XorCsrfTokenRequestAttributeHandler's implementation
        // I have used the CsrfTokenRequestAttributeHandler and there are some more alternatives
        // here: https://docs.spring.io/spring-security/reference/5.8/migration/servlet/exploits.html#_i_am_using_angularjs_or_another_javascript_framework
        http.httpBasic()
                .and()
                .authorizeHttpRequests()
                .anyRequest().authenticated()
                .and()
                .addFilterAfter(csrfTokenLoggingFilter, CsrfFilter.class)
                .csrf()
                .csrfTokenRepository(csrfTokenRepository)
                .csrfTokenRequestHandler(new CsrfTokenRequestAttributeHandler());
    }

    private void configureForHttpBasicHelloController(HttpSecurity http) throws Exception {
        http.httpBasic()
                .and()
                .authorizeHttpRequests()
                .anyRequest().authenticated()
                .and()
                .addFilterAfter(csrfTokenLoggingFilter, CsrfFilter.class)
                .csrf(c -> c.ignoringRequestMatchers("/hello"));
    }

    private void configureForFormLoginMainAndProductController(HttpSecurity http) throws Exception {
        http.formLogin()
                .defaultSuccessUrl("/main", true)
                .and()
                .authorizeHttpRequests()
                .anyRequest().authenticated()
                .and()
                .addFilterAfter(csrfTokenLoggingFilter, CsrfFilter.class)
                .csrf().csrfTokenRepository(csrfTokenRepository);
    }
}