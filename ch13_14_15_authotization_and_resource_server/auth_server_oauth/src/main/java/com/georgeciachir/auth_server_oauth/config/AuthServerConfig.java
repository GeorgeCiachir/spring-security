package com.georgeciachir.auth_server_oauth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.security.oauth2.provider.client.InMemoryClientDetailsService;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Profile("jdbc-token-store")
@Configuration
@EnableAuthorizationServer
public class AuthServerConfig extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private DataSource dataSource;

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        endpoints.authenticationManager(authenticationManager)
                .tokenStore(new JdbcTokenStore(dataSource));
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        // Of course, in a real world scenario, this would most likely be a service that
        // retrieves the client details from a DB
        InMemoryClientDetailsService clientDetailsService = new InMemoryClientDetailsService();

        BaseClientDetails passwordGrantTypeClient = passwordGrantTypeClient();
        BaseClientDetails codeGrantTypeClient = codeGrantTypeClient();
        BaseClientDetails resourceServerClient = resourceServerClient();

        clientDetailsService.setClientDetailsStore(
                Map.of(passwordGrantTypeClient.getClientId(), passwordGrantTypeClient,
                        codeGrantTypeClient.getClientId(), codeGrantTypeClient,
                        resourceServerClient.getClientId(), resourceServerClient));

        clients.withClientDetails(clientDetailsService);
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) {
        // Ideally, isAuthenticated() should be used, so that the resource
        // server is also authenticated when checking the token,
        // but permitAll() it's faster for experimenting

        // If we decide to use authentication for this endpoint, which most likely we should,
        // we also need to declare the resource server as a client for the authorization server
        security.checkTokenAccess("isAuthenticated()");
    }

    /**
     * For the authorization server, the resource server is a client in the sense of a user that
     * needs some info (check if the token is valid) and it is not involved in the token issuing process.
     * <p>
     * I could have even removed the need for declaring this client details, but then I would need to add the
     * "permitAll()" level of authorization on the "/oauth/check_token" endpoint
     * in the {@link #configure(AuthorizationServerSecurityConfigurer)}
     */
    private BaseClientDetails resourceServerClient() {
        BaseClientDetails clientDetails = new BaseClientDetails();
        clientDetails.setClientId("resourceServer");
        clientDetails.setClientSecret("resourceServerSecret");
        return clientDetails;
    }

    private BaseClientDetails passwordGrantTypeClient() {
        BaseClientDetails clientDetails = new BaseClientDetails();
        clientDetails.setClientId("client_password_grant");
        clientDetails.setClientSecret("secret");
        clientDetails.setScope(List.of("read"));
        // adding the refresh_token grant type ensure that a refresh token is also provided with the default auth token
        clientDetails.setAuthorizedGrantTypes(List.of("password", "refresh_token"));
        return clientDetails;
    }

    private BaseClientDetails codeGrantTypeClient() {
        BaseClientDetails clientDetails = new BaseClientDetails();
        clientDetails.setClientId("client_code_grant");
        clientDetails.setClientSecret("secret");
        clientDetails.setScope(List.of("read"));
        // adding the refresh_token grant type ensure that a refresh token is also provided with the default auth token
        clientDetails.setAuthorizedGrantTypes(List.of("authorization_code", "refresh_token"));
        clientDetails.setRegisteredRedirectUri(Set.of("http://localhost:8080"));
        return clientDetails;
    }
}
