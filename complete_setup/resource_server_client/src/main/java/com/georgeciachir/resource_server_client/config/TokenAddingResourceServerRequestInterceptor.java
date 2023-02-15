package com.georgeciachir.resource_server_client.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class TokenAddingResourceServerRequestInterceptor implements ClientHttpRequestInterceptor {

    @Value("${resource.server.base.url}")
    private String resourceServerBaseUrl;

    @Autowired
    private OAuth2AuthorizedClientService authorizedClientService;

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        if (!request.getURI().toString().startsWith(resourceServerBaseUrl)) {
            return execution.execute(request, body);
        }
        Authentication principal = SecurityContextHolder.getContext().getAuthentication();
        String clientRegistrationId = ((OAuth2AuthenticationToken) principal).getAuthorizedClientRegistrationId();
        OAuth2AuthorizedClient oAuth2AuthorizedClient =
                authorizedClientService.loadAuthorizedClient(clientRegistrationId, principal.getName());

        String tokenValue = oAuth2AuthorizedClient.getAccessToken().getTokenValue();
        request.getHeaders().setBearerAuth(tokenValue);

        return execution.execute(request, body);
    }
}
