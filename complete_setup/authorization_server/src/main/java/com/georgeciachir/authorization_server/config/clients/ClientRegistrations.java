package com.georgeciachir.authorization_server.config.clients;

import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.UUID;

@Component
public class ClientRegistrations {

    @Bean
    public RegisteredClientRepository registeredClientRepository() {
        return new InMemoryRegisteredClientRepository(
                resourceServer(),
                resourceServerClient());
    }

    private static RegisteredClient resourceServer() {
        return RegisteredClient.withId(UUID.randomUUID().toString())
                .clientId("the-resource-server")
                .clientSecret("secret")
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                // OAuth2AuthorizationCodeRequestAuthenticationValidator::validateRedirectUri
                // Need to use the IP literal (127.0.0.1)
                // The use of "localhost" is NOT RECOMMENDED -> it actually throws OAuth2Error
                .redirectUri("http://127.0.0.1:9090/login/oauth2/code/spring-auth")
                .redirectUri("http://127.0.0.1:9090/authorized")
                .scope(OidcScopes.OPENID)
                .scope(OidcScopes.PROFILE)
                .clientSettings(ClientSettings.builder()
                        // Wanted to prevent an additional step in which the user needs
                        // to grant permission for the "profile" scope
                        .requireAuthorizationConsent(false)
                        .setting("approvedAudiences", "product-management-backend")
                        .build())
                .tokenSettings(TokenSettings.builder()
                        .refreshTokenTimeToLive(Duration.ofHours(10))
                        .build())
                .build();
    }

    private static RegisteredClient resourceServerClient() {
        return RegisteredClient.withId(UUID.randomUUID().toString())
                .clientId("resource-server-client")
                .clientSecret("1234")
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                // OAuth2AuthorizationCodeRequestAuthenticationValidator::validateRedirectUri
                // Need to use the IP literal (127.0.0.1)
                // The use of "localhost" is NOT RECOMMENDED -> it actually throws OAuth2Error
                .redirectUri("http://127.0.0.1:9091/login/oauth2/code/spring-auth")
                .redirectUri("http://127.0.0.1:9091/authorized")
                .scope(OidcScopes.OPENID)
                .scope(OidcScopes.PROFILE)
                .clientSettings(ClientSettings.builder()
                        // Wanted to prevent an additional step in which the user needs
                        // to grant permission for the "profile" scope
                        .requireAuthorizationConsent(false)
                        .setting("approvedAudiences", "product-management-backend")
                        .build())
                .tokenSettings(TokenSettings.builder()
                        .refreshTokenTimeToLive(Duration.ofHours(10))
                        .build())
                .build();
    }
}
