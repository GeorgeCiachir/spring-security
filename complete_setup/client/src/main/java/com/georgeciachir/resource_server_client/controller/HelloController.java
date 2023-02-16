package com.georgeciachir.resource_server_client.controller;

import com.georgeciachir.resource_server_client.service.HelloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hello")
public class HelloController {

    @Autowired
    private HelloService helloService;

    @GetMapping
    public String hello() {
        return helloService.getGreetingFromResourceServer();
    }
}
