package com.georgeciachir.resource_server_oauth.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

@Profile("check-token-endpoint")
@EnableResourceServer
@Configuration
public class ResourceServerConfig {
}
