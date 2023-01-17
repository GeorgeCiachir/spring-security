package com.georgeciachir.auth_server_oauth.config.authserver;

import com.georgeciachir.auth_server_oauth.config.authserver.clients.InMemoryRegisteredClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.client.InMemoryClientDetailsService;

@Configuration
@EnableAuthorizationServer
public class BaseAuthServerConfig extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private InMemoryRegisteredClients inMemoryClients;

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        // Of course, in a real world scenario, this would most likely be a service that
        // retrieves the client details from a DB
        InMemoryClientDetailsService clientDetailsService = new InMemoryClientDetailsService();
        clientDetailsService.setClientDetailsStore(inMemoryClients.get());
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
        security.tokenKeyAccess("isAuthenticated()");
    }
}
