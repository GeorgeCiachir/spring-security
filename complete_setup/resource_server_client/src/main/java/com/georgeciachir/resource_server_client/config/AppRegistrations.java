package com.georgeciachir.resource_server_client.config;

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

    @Value("${security.auth0.client.registration.client-id}")
    private String auth0ClientId;

    @Value("${security.auth0.client.registration.client-secret}")
    private String auth0ClientSecret;

    @Value("${security.keycloak.client.registration.client-id}")
    private String keycloakClientId;

    @Value("${security.keycloak.client.registration.client-secret}")
    private String keycloakClientSecret;

    @Bean
    public ClientRegistrationRepository clientRegistrationRepository() {
        List<ClientRegistration> clientRegistrations = List.of(
                auth0ClientRegistration(),
                keycloakClientRegistration());
        return new InMemoryClientRegistrationRepository(clientRegistrations);
    }

    private ClientRegistration auth0ClientRegistration() {
        return ClientRegistration.withRegistrationId("auth0") // whatever id you want. just has to be unique
                .clientId(auth0ClientId)
                .clientSecret(auth0ClientSecret)
                .scope("openid", "profile")
                .authorizationUri("https://product-management-backend.eu.auth0.com/authorize?audience=product-management-backend")
                .tokenUri("https://product-management-backend.eu.auth0.com/oauth/token")
                .userInfoUri("https://product-management-backend.eu.auth0.com/userinfo")
                // Required for signature verification in case the userinfo endpoint is not used -> in case of openid scope
                .jwkSetUri("https://product-management-backend.eu.auth0.com/.well-known/jwks.json")
                // This has been manually added to the claims in the auth server config
                .userNameAttributeName("nickname")
                .clientName("Auth0")
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .redirectUri("{baseUrl}/{action}/oauth2/code/{registrationId}")
                .build();
    }


    private ClientRegistration keycloakClientRegistration() {
        return ClientRegistration.withRegistrationId("keycloak") // whatever id you want. just has to be unique
                .clientId(keycloakClientId)
                .clientSecret(keycloakClientSecret)
                .scope("product-management-backend", "openid", "profile")
                .authorizationUri("http://localhost:8080/auth/realms/online-shopping-products-management/protocol/openid-connect/auth")
                .tokenUri("http://localhost:8080/auth/realms/online-shopping-products-management/protocol/openid-connect/token")
                .userInfoUri("http://localhost:8080/auth/realms/online-shopping-products-management/protocol/openid-connect/userinfo")
                // Required for signature verifier if the userinfo endpoint is not used -> in case of openid scope
                .jwkSetUri("http://localhost:8080/auth/realms/online-shopping-products-management/protocol/openid-connect/certs")
                // This has been manually added to the claims in the auth server config
                .userNameAttributeName("user_name")
                .clientName("Keycloak")
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .redirectUri("{baseUrl}/{action}/oauth2/code/{registrationId}")
                .build();
    }
}
