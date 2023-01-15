package com.georgeciachir.resource_server_oauth.config.withspringseurityoauth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;

@ConditionalOnProperty(value = "configuration.mode", havingValue = "with-spring-security-oauth")
@Profile("jwt-asymmetric-key-token-store")
@Configuration
@EnableResourceServer
public class JwtAsymmetricKeyAuthServerConfig extends ResourceServerConfigurerAdapter {

    @Value("${public.key}")
    private String publicKey;

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
        resources.tokenStore(tokenStore());
    }

    @Bean
    public TokenStore tokenStore() {
        return new JwtTokenStore(jwtAccessTokenConverter());
    }

    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        String publicKeyValue = readPublicKey();

        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setVerifierKey(publicKeyValue);
        return converter;
    }

    private String readPublicKey() {
        try {
            URI resource = this.getClass().getClassLoader().getResource(publicKey).toURI();
            return Files.readString(Paths.get(resource));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
