package com.ravinder.api.blog.scheduler;

public class Task {
    private String id;
    private String description;
    private int priority; // 1 - high to 10 - low
    private Runnable action; //what task does

    public Task(String id, String description, int priority, Runnable action) {
        this.id = id;
        this.description = description;
        this.priority = priority;
        this.action = action;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public Runnable getAction() {
        return action;
    }

    public void setAction(Runnable action) {
        this.action = action;
    }
}
