package com.georgeciachir.auth_server_oauth.config.authserver.jdbcauthconfig;

import com.georgeciachir.auth_server_oauth.config.authserver.BaseAuthServerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;

import javax.sql.DataSource;

@Profile("jdbc-token-store")
@Configuration
public class JdbcTokenStoreAuthServerConfig extends BaseAuthServerConfig {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private DataSource dataSource;

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        endpoints.authenticationManager(authenticationManager)
                .tokenStore(new JdbcTokenStore(dataSource));
    }
}
