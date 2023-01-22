package com.georgeciachir.ch18_oauth2_app_resource_server.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;

import java.util.List;

@Configuration
public class AppRegistrations {

    @Value("${security.keycloak.client.registration.client-id}")
    private String keycloakClientId;

    @Value("${security.keycloak.client.registration.client-secret:}")
    private String keycloakClientSecret;

    @Value("${security.manual.client.github.registration.clientId}")
    private String githubClientId;

    @Value("${security.manual.client.github.registration.clientSecret}")
    private String githubClientSecret;


    @Bean
    public ClientRegistrationRepository clientRegistrationRepository() {
        List<ClientRegistration> clientRegistrations = List.of(
                keycloakClientRegistration(),
                manualGithubClientRegistration());
        return new InMemoryClientRegistrationRepository(clientRegistrations);
    }

    private ClientRegistration manualGithubClientRegistration() {
        return ClientRegistration.withRegistrationId("githubManual") // whatever id you want. just has to be unique
                .clientId(githubClientId)
                .clientSecret(githubClientSecret)
                // If this line is uncommented, this app will also have the right to read some personal data.
                // If left commented, only public info is available
                //.scope("read:user")
                .authorizationUri("https://github.com/login/oauth/authorize")
                .tokenUri("https://github.com/login/oauth/access_token")
                .userInfoUri("https://api.github.com/user")
                .userNameAttributeName("id")
                .clientName("Manual GitHub")
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .redirectUri("{baseUrl}/{action}/oauth2/code/{registrationId}")
                .build();
    }

    private ClientRegistration keycloakClientRegistration() {
        return ClientRegistration.withRegistrationId("keycloak") // whatever id you want. just has to be unique
                .clientId(keycloakClientId)
                .clientSecret(keycloakClientSecret)
                .scope("product-management-backend")
                .authorizationUri("http://localhost:8080/auth/realms/online-shopping-products-management/protocol/openid-connect/auth")
                .tokenUri("http://localhost:8080/auth/realms/online-shopping-products-management/protocol/openid-connect/token")
                .userInfoUri("http://localhost:8080/auth/realms/online-shopping-products-management/protocol/openid-connect/userinfo")
                .userNameAttributeName("id")
                .clientName("Manual Keycloak")
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .redirectUri("{baseUrl}/{action}/oauth2/code/{registrationId}")
                .build();
    }
}
