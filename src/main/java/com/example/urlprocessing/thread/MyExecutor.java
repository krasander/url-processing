package com.example.urlprocessing.thread;

import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class MyExecutor {

    private final ExecutorService executor = Executors.newFixedThreadPool(5);

    public void execute(Runnable runnable) {
        executor.execute(runnable);
    }
}
