package com.georgeciachir.ch5_implementing_authentication.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class HelloService {

    @Async
    public CompletableFuture<String> getNameAsyncSpringManagedThread() {
        return CompletableFuture.completedFuture(getName());
    }

    public CompletableFuture<String> getNameAsyncNotSpringManagedTread() {
        ExecutorService ex = Executors.newSingleThreadExecutor();
        try {
            return CompletableFuture.supplyAsync(this::getName, ex);
        } finally {
            ex.shutdown();
        }
    }

    private String getName() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}
