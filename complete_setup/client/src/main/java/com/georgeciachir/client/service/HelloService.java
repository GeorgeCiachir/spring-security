package com.georgeciachir.client.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class HelloService {

    @Value("${resource.server.base.url}")
    private String resourceServerBaseUrl;

    @Autowired
    private RestTemplate restTemplate;

    public String getGreetingFromResourceServer() {
        return restTemplate.getForEntity(resourceServerBaseUrl + "/hello", String.class).getBody();
    }
}
