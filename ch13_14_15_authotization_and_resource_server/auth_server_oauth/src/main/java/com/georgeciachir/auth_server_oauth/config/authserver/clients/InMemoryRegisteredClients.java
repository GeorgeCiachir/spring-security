package com.georgeciachir.auth_server_oauth.config.authserver.clients;

import com.georgeciachir.auth_server_oauth.config.authserver.jdbcauthconfig.JdbcTokenStoreAuthServerConfig;
import com.georgeciachir.auth_server_oauth.config.authserver.jwtbasedauthconfig.JwtSymmetricKeyAuthServerConfig;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
public class InMemoryRegisteredClients {

    private Map<String, BaseClientDetails> clients;

    @PostConstruct
    public void setUp() {
        BaseClientDetails passwordGrantTypeClient = passwordGrantTypeClient();
        BaseClientDetails codeGrantTypeClient = codeGrantTypeClient();
        BaseClientDetails resourceServerClient = resourceServerClient();

        clients = Map.of(passwordGrantTypeClient.getClientId(), passwordGrantTypeClient,
                codeGrantTypeClient.getClientId(), codeGrantTypeClient,
                resourceServerClient.getClientId(), resourceServerClient);
    }

    public Map<String, BaseClientDetails> get() {
        return clients;
    }

    /**
     * For the authorization server, the resource server is a client in the sense of a user that
     * needs some info (check if the token is valid) and it is not involved in the token issuing process.
     * <p>
     * I could have even removed the need for declaring this client details, but then I would need to add the
     * "permitAll()" level of authorization on the "/oauth/check_token" endpoint
     * in the {@link JdbcTokenStoreAuthServerConfig#configure(AuthorizationServerSecurityConfigurer)} and
     * in the {@link JwtSymmetricKeyAuthServerConfig#configure(AuthorizationServerSecurityConfigurer)}
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
