package com.georgeciachir.auth_server_oauth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.security.oauth2.provider.client.InMemoryClientDetailsService;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Configuration
@EnableAuthorizationServer
public class AuthServerConfig extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        endpoints.authenticationManager(authenticationManager);
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        // Of course, in a real world scenario, this would most likely be a service that
        // retrieves the client details from a DB
        InMemoryClientDetailsService clientDetailsService = new InMemoryClientDetailsService();

        BaseClientDetails passwordGrantTypeClient = passwordGrantTypeClient();
        BaseClientDetails codeGrantTypeClient = codeGrantTypeClient();

        clientDetailsService.setClientDetailsStore(
                Map.of(passwordGrantTypeClient.getClientId(), passwordGrantTypeClient,
                        codeGrantTypeClient.getClientId(), codeGrantTypeClient));

        clients.withClientDetails(clientDetailsService);
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
