package com.georgeciachir.resource_server.config;

import com.georgeciachir.resource_server.config.authserver.Auth0JwtConfig;
import com.georgeciachir.resource_server.config.authserver.KeycloakJwtConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.authentication.OAuth2LoginAuthenticationProvider;
import org.springframework.security.oauth2.client.oidc.authentication.OidcAuthorizationCodeAuthenticationProvider;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;

import java.util.List;

/**
 * Depending on the scopes this registration declares, considering we are using the code flow, the authentication will be handled in different ways:
 * <p>
 * 1. If the declared scope is openid (so the OIDC protocol is used), and no other OIDC specific scopes are declared (such as profile, email and address),
 * then the authentication will be done using the {@link OidcAuthorizationCodeAuthenticationProvider} and the {@link OidcUserService}
 * and no call will be done on the /userinfo endpoint. In this case, the only user specific claim will be "sub" and will contain the user id.
 * <p>
 * 2. If the declared scope is openid (so the OIDC protocol is used), and other OIDC specific scopes are also declared (such as profile, email and address),
 * then the authentication will be done using the {@link OidcAuthorizationCodeAuthenticationProvider} and the {@link OidcUserService}
 * and it will also call the /userinfo endpoint to get the additional requested info (profile/email/address), necessary for authenticating the user.
 * In this case, the call will be delegated by the {@link OidcUserService}, to the {@link DefaultOAuth2UserService}.
 * <p>
 * 3. If the scope is not openid, it means the OAuth2 flow is used, so the authentication is done using
 * the {@link OAuth2LoginAuthenticationProvider} and the {@link DefaultOAuth2UserService}. Also call the /userinfo endpoint to get the
 * required user info necessary for authenticating the user.
 * <p>
 * <p>
 * Just to make it extra clear. All the cases mentioned above are scenarios in which this app is itself logging the user. For example
 * the user is accessing a resource (e.g. the "/hello" endpoint) from the browser. So this app also acts like a client, as
 * it redirects the user to login, then it will also receive the authorization code and with that, the JWT. Based on that JWT,
 * the app will authenticate the user.
 * <p>
 * <p>
 * On the OIDC protocol, for user authentication, the resource server relies on the information presented on the obtained token.
 * This means that it has to be able to verify the signature of the JWT, just as if the server is a client for itself. For this,
 * we also need to configure a {@link ClientRegistration.Builder#jwkSetUri(String)}.
 * <p>
 * <p>
 * Note that in the case of the Auth0 configuration there is a catch: it exposes the /userinfo endpoint only if the JWT contains the
 * "<a href="https://product-management-backend.eu.auth0.com/userinfo"/>" audience. The problem is that I was able to obtain the
 * audience on the token, only if the "openid" scope is mentioned on the initial request.
 * <p>
 * I think this is a pretty strange behavior of Auth0, because the whole point of the OIDC protocol is that the /userinfo endpoint
 * is not required. So basically, I am on the OAuth2 protocol and I have to mention an OIDC scope so that I can access the /userinfo endpoint.
 * <p>
 * <p>
 * Since I mentioned the authentication providers... When the client makes a request with a valid JWT,
 * the {@link JwtAuthenticationProvider} is used for authentication. For decoding the JWT, that one uses the components
 * configured in on of the auth server specific config classes: {@link Auth0JwtConfig} or {@link KeycloakJwtConfig}.
 */
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

    @Value("${security.auth0.client.registration.client-id}")
    private String auth0ClientId;

    @Value("${security.auth0.client.registration.client-secret}")
    private String auth0ClientSecret;

    @Value("${security.spring.oauth2.client.registration.client-id}")
    private String springOAuth2ClientId;

    @Value("${security.spring.oauth2.client.registration.client-secret}")
    private String springOAuth2ClientSecret;

    @Bean
    public ClientRegistrationRepository clientRegistrationRepository() {
        List<ClientRegistration> clientRegistrations = List.of(
                keycloakClientRegistration(),
                auth0ClientRegistration(),
                githubClientRegistration(),
                springOAuth2ClientRegistration());
        return new InMemoryClientRegistrationRepository(clientRegistrations);
    }

    private ClientRegistration githubClientRegistration() {
        return ClientRegistration.withRegistrationId("githubManual") // whatever id you want. just has to be unique
                .clientId(githubClientId)
                .clientSecret(githubClientSecret)
                // If this line is uncommented, this app will also have the right to read some personal data.
                // If left commented, only public info is available
                //.scope("read:user")
                .authorizationUri("https://github.com/login/oauth/authorize")
                .tokenUri("https://github.com/login/oauth/access_token")
                .userInfoUri("https://api.github.com/user")
                .userNameAttributeName("login")
                .clientName("GitHub")
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
                .redirectUri("http://127.0.0.1:9090/{action}/oauth2/code/{registrationId}")
                .build();
    }
}
