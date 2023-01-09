package com.georgeciachir.businessserver.auth.proxy;

import com.georgeciachir.businessserver.auth.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class AuthorizationServerProxy {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${auth.server.base.url}")
    private String baseUrl;

    public void validateCredentials(String username, String password) {
        String url = baseUrl + "/user/auth";

        User body = new User(username, password, null);

        HttpEntity<User> request = new HttpEntity<>(body);

        restTemplate.postForEntity(url, request, Void.class);
    }

    public void validateOtp(String username, String code) {
        String url = baseUrl + "/otp/check";

        User body = new User(username, null, code);

        HttpEntity<User> request = new HttpEntity<>(body);

        restTemplate.postForEntity(url, request, Void.class);
    }
}
