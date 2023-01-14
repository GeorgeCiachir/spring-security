package com.georgeciachir.resource_server_oauth.config.withspringseurityoauth;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

@Profile({"with-spring-security-oauth", "token-endpoint-config"})
@EnableResourceServer
@Configuration
public class TokenEndpointCheckResourceServerConfig {
}
