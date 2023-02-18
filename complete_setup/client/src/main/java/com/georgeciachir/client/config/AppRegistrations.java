package com.georgeciachir.client.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.oidc.OidcScopes;

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

    @Value("${security.spring.oauth2.client.registration.client-id}")
    private String springOAuth2ClientId;

    @Value("${security.spring.oauth2.client.registration.client-secret}")
    private String springOAuth2ClientSecret;

    @Bean
    public ClientRegistrationRepository clientRegistrationRepository() {
        List<ClientRegistration> clientRegistrations = List.of(
                auth0ClientRegistration(),
                keycloakClientRegistration(),
                springOAuth2ClientRegistration());
        return new InMemoryClientRegistrationRepository(clientRegistrations);
    }

    private ClientRegistration auth0ClientRegistration() {
        return ClientRegistration.withRegistrationId("auth0") // whatever id you want. just has to be unique
                .clientId(auth0ClientId)
                .clientSecret(auth0ClientSecret)
                .scope(OidcScopes.OPENID, OidcScopes.PROFILE)
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
                .scope("product-management-backend", OidcScopes.OPENID, OidcScopes.PROFILE)
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

    // Use http://127.0.0.1:9090/hello as an entry point, instead of http://localhost/hello

    // Need to enter the resource server using the IP literal (127.0.0.1) due to how the
    // spring oauth2 server is implemented. In the authorization server see this method that validates
    // the redirect_uri:
    // OAuth2AuthorizationCodeRequestAuthenticationValidator::validateRedirectUri
    // The use of "localhost" is NOT RECOMMENDED -> it actually throws OAuth2Error

    // Because the auth server does not accept "localhost" as a redirect_uri I need to use 127.0.0.1. The
    // session id that is provided along with the authorization code on the redirect_uri, is then
    // stored in the browser on 127.0.0.1 and does not apply to localhost. This means that when the resource
    // server redirects the user on the initial requested uri (localhost:9090/hello) after a successful login,
    // an exception will be thrown due to the cookie missing in the browser for localhost.

    private ClientRegistration springOAuth2ClientRegistration() {
        return ClientRegistration.withRegistrationId("spring-auth") // whatever id you want. just has to be unique
                .clientId(springOAuth2ClientId)
                .clientSecret(springOAuth2ClientSecret)
                .scope(OidcScopes.OPENID, OidcScopes.PROFILE)
                .authorizationUri("http://localhost:9092/oauth2/authorize")
                .tokenUri("http://localhost:9092/oauth2/token")
                .userInfoUri("http://localhost:9092/userinfo")
                // Required for signature verification in case the userinfo endpoint is not used -> in case of openid scope
                .jwkSetUri("http://localhost:9092/oauth2/jwks")
                // This has been manually added to the claims in the auth server config
                .userNameAttributeName("sub")
                .clientName("Spring OAuth2")
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .redirectUri("http://127.0.0.1:9091/{action}/oauth2/code/{registrationId}")
                .build();
    }
}
