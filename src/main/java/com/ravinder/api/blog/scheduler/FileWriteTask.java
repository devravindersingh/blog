package com.ravinder.api.blog.scheduler;

import java.io.FileWriter;
import java.io.IOException;

public class FileWriteTask implements Runnable{
    private final String fileName;

    public FileWriteTask(String fileName) {
        this.fileName = fileName;
    }

    public void run() {
        synchronized (this) {
            try (FileWriter writer = new FileWriter(fileName, true)) {
                writer.write("Task executed at " + System.currentTimeMillis() + "\n");
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }
}
