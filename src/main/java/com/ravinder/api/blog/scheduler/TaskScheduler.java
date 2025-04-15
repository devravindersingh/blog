package com.ravinder.api.blog.scheduler;

import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.TimeUnit;

@Component
public class TaskScheduler {
    private final ExecutorService executor = Executors.newFixedThreadPool(4);
    private final PriorityBlockingQueue<Task> queue = new PriorityBlockingQueue<>(10, Comparator.comparingInt(Task::getPriority));
    private volatile boolean running = true;

    public TaskScheduler() {
        for (int i = 0; i < 4; i++) {
            executor.submit(() -> {
                while (running) {
                    try {
                        Task task = queue.poll(1, TimeUnit.SECONDS);
                        if (task != null) {
                            task.getAction().run();
                        }
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                }
            });
        }
    }

    public static TaskScheduler getInstance() {
        throw new UnsupportedOperationException("Use Spring injection instead");
    }

    public void scheduleTask(Task task) {
        queue.add(task);
    }

    public void shutdown() {
        running = false;
        executor.shutdown();
        try {
            if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
