package com.georgeciachir.resource_server_oauth.config.withoutspringsecurityoauth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;

import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@ConditionalOnProperty(value = "configuration.mode", havingValue = "no-spring-security-oauth")
@Profile("jwt-asymmetric-key-token-store")
@Configuration
public class JwtAsymmetricKeyAuthServerConfig extends WebSecurityConfigurerAdapter {

    @Value("${public.key}")
    private String publicKey;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .anyRequest().authenticated()
                .and()
                .oauth2ResourceServer(
                        serverConfigurer -> serverConfigurer.jwt(
                                jwtConfigurer -> jwtConfigurer
                                        .decoder(jwtDecoder())
                                        .jwtAuthenticationConverter(customAuthConverter())
                        ));
    }

    private JwtAuthenticationConverter customAuthConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setPrincipalClaimName("user_name");
        return converter;
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        String publicKeyValue = readPublicKey();
        publicKeyValue = sanitizePublicKey(publicKeyValue);

        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            byte[] key = Base64.getDecoder().decode(publicKeyValue);

            X509EncodedKeySpec x509 = new X509EncodedKeySpec(key);
            RSAPublicKey rsaPublicKey = (RSAPublicKey) keyFactory.generatePublic(x509);

            return NimbusJwtDecoder.withPublicKey(rsaPublicKey).build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String sanitizePublicKey(String publicKeyValue) {
        return publicKeyValue
                .replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replace("\r\n", "");
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
