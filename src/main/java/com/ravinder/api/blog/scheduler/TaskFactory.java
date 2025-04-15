package com.ravinder.api.blog.scheduler;

public class TaskFactory {
    public static Task createFileTask(String id, String description, int priority, String fileName){
        return new Task(id, description, priority, new FileWriteTask(fileName));
    }
}
