package com.georgeciachir.auth_server_oauth.config.authserver.tokenenhancers;

import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

import java.time.ZoneId;
import java.util.Map;

public class TimeZoneTokenEnhancer implements TokenEnhancer {

    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        Map<String, Object> additionalInfo = Map.of("generatedInZone", ZoneId.systemDefault().toString());

        DefaultOAuth2AccessToken enhancedToken = new DefaultOAuth2AccessToken(accessToken);
        enhancedToken.setAdditionalInformation(additionalInfo);

        return enhancedToken;
    }
}
