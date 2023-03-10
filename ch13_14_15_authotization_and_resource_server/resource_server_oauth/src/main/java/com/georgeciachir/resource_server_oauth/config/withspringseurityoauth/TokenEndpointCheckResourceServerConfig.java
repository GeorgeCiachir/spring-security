package com.georgeciachir.resource_server_oauth.config.withspringseurityoauth;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

@ConditionalOnProperty(value = "configuration.mode", havingValue = "with-spring-security-oauth")
@Profile("token-endpoint-config")
@EnableResourceServer
@Configuration
public class TokenEndpointCheckResourceServerConfig {
}
