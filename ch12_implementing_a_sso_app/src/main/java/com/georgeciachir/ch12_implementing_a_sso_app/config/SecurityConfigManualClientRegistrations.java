package com.georgeciachir.ch12_implementing_a_sso_app.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.oauth2.client.CommonOAuth2Provider;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.web.SecurityFilterChain;

import java.util.List;

@Profile("manual_client_registration")
@EnableWebSecurity
@Configuration
public class SecurityConfigManualClientRegistrations {

    @Value("${security.manual.client.github.registration.clientId}")
    private String githubClientId;

    @Value("${security.manual.client.github.registration.clientSecret}")
    private String githubClientSecret;

    @Value("${security.manual.client.keycloak.registration.clientId}")
    private String keycloakClientId;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.oauth2Login(oAuth2LoginConfigurer -> oAuth2LoginConfigurer.clientRegistrationRepository(clientRegistrationRepository()))
                .authorizeHttpRequests()
                .anyRequest().authenticated();

        return http.build();
    }

    // Of course, I could extend the ClientRegistrationRepository and store all the registrations in
    // a DB, so that I don't need to expose the ids and secrets on GitHub
    @Bean
    public ClientRegistrationRepository clientRegistrationRepository() {
        List<ClientRegistration> clientRegistrations = List.of(
                keycloakClientRegistration(),
                manualGithubClientRegistration(),
                commonGithubClientRegistration());
        return new InMemoryClientRegistrationRepository(clientRegistrations);
    }

    private ClientRegistration keycloakClientRegistration() {
        return ClientRegistration.withRegistrationId("keycloak") // whatever id you want. just has to be unique
                .clientId(keycloakClientId)
                // .clientSecret(keycloakClientSecret) // Keycloak has the option to not use a password
                .authorizationUri("http://localhost:8080/auth/realms/online-shopping-products-management/protocol/openid-connect/auth")
                .tokenUri("http://localhost:8080/auth/realms/online-shopping-products-management/protocol/openid-connect/token")
                .userInfoUri("http://localhost:8080/auth/realms/online-shopping-products-management/protocol/openid-connect/userinfo")
                .userNameAttributeName("id")
                .clientName("Manual Keycloak")
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .redirectUri("{baseUrl}/{action}/oauth2/code/{registrationId}")
                .build();
    }

    // This is the same as the commonGithubClientRegistration, and it is used as an example on hot to set
    private ClientRegistration manualGithubClientRegistration() {
        return ClientRegistration.withRegistrationId("githubManual") // whatever id you want. just has to be unique
                .clientId(githubClientId)
                .clientSecret(githubClientSecret)
                // If this line is uncommented, this app will also have the right to read some personal data.
                // If left commented, only public info is available
//                .scope("read:user")
                .authorizationUri("https://github.com/login/oauth/authorize")
                .tokenUri("https://github.com/login/oauth/access_token")
                .userInfoUri("https://api.github.com/user")
                .userNameAttributeName("id")
                .clientName("Manual GitHub")
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .redirectUri("{baseUrl}/{action}/oauth2/code/{registrationId}")
                .build();
    }

    // This is the same as the manualGithubClientRegistration, and it is used as an example on hot to set
    private ClientRegistration commonGithubClientRegistration() {
        return CommonOAuth2Provider.GITHUB
                // The default scope is "read:user"
                .getBuilder("githubDefault") // whatever id you want. just has to be unique
                .clientId(githubClientId)
                .clientSecret(githubClientSecret)
                .build();
    }
}