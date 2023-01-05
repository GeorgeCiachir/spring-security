package com.georgeciachir.ch5_implementing_authentication.controller;

import com.georgeciachir.ch5_implementing_authentication.service.HelloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/")
public class HelloController {

    @Autowired
    private HelloService helloService;

    @GetMapping("/hello-sync")
    public String helloSync(Authentication authentication) {
//        String name = authentication.getName();
        String name = SecurityContextHolder.getContext().getAuthentication().getName();

        return "Hello " + name + "!";
    }

    @GetMapping("/hello-async")
    public CompletableFuture<String> helloAsyncSpringManagedThread(
            @RequestParam(required = false, defaultValue = "notSpringManaged") String asyncMode)
            throws ExecutionException, InterruptedException {
        String name;

        if (asyncMode.equals("springManaged")) {
            name = helloService.getNameAsyncSpringManagedThread().get();
        } else {
            name = helloService.getNameAsyncNotSpringManagedTread().get();
        }

        return CompletableFuture.completedFuture("Hello " + name + "!");
    }
}
