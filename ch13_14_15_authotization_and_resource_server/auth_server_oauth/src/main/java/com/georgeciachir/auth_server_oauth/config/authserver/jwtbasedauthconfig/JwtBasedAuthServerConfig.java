package com.georgeciachir.auth_server_oauth.config.authserver.jwtbasedauthconfig;

import com.georgeciachir.auth_server_oauth.config.authserver.BaseAuthServerConfig;
import com.georgeciachir.auth_server_oauth.config.authserver.tokenenhancers.TimeZoneTokenEnhancer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import java.util.List;

@Configuration
public abstract class JwtBasedAuthServerConfig extends BaseAuthServerConfig {

    @Value("${use.token.enhancer}")
    private boolean useTokenEnhancer;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        endpoints.authenticationManager(authenticationManager)
                .tokenStore(tokenStore());

        if (!useTokenEnhancer) {
            endpoints.accessTokenConverter(jwtAccessTokenConverter());
            return;
        }

        endpoints.tokenEnhancer(tokenEnhancers());

        // This is really counter-intuitive:

        // 1. If the TokenEnhancerChain contains the jwtAccessTokenConverter(), then the behaviour is the same
        //    if we use the endpoints.accessTokenConverter(jwtAccessTokenConverter()) method, or not
        // 2. If the TokenEnhancerChain does not contain the jwtAccessTokenConverter(), then even if we add it
        //    using the endpoints.accessTokenConverter(jwtAccessTokenConverter()), it will not use it and instead it
        //    will produce a simple UUID token -> "access_token": "921aef6a-696d-4c0d-bcbd-a0b8cabcb096"
        endpoints.accessTokenConverter(jwtAccessTokenConverter());
    }

    private TokenEnhancerChain tokenEnhancers() {
        // The JwtAccessTokenConverter itself is also a TokenEnhancer, so we need to add it on the TokenEnhancerChain
        List<TokenEnhancer> tokenEnhancers = List.of(new TimeZoneTokenEnhancer(), jwtAccessTokenConverter()); //order matters

        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        tokenEnhancerChain.setTokenEnhancers(tokenEnhancers);
        return tokenEnhancerChain;
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) {
        super.configure(security);
        security.tokenKeyAccess("isAuthenticated()");
    }

    private TokenStore tokenStore() {
        return new JwtTokenStore(jwtAccessTokenConverter());
    }

    public abstract JwtAccessTokenConverter jwtAccessTokenConverter();
}
