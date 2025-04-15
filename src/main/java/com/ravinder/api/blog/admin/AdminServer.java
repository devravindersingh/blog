package com.ravinder.api.blog.admin;

import com.ravinder.api.blog.repository.PostRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

@Component
public class AdminServer implements Runnable{

    private final ConfigurableApplicationContext context;
    private final PostRepository postRepository;
    private final int port  = 9999;
    private volatile boolean running = true;

    public AdminServer(ConfigurableApplicationContext context, PostRepository postRepository){
        this.context = context;
        this.postRepository = postRepository;
    }
    @Override
    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Admin server running on port " + port);
            while (running) {
                try (Socket clientSocket = serverSocket.accept();
                     BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                     PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {
                    String command = in.readLine();

                    if(command == null) continue;

                    switch (command.toLowerCase()){
                        case "shutdown" :
                            out.println("Shutting down server ......");
                            running = false;
                            SpringApplication.exit(context, () -> 0);
                            break;
                        case "clearcache":
                            clearCache();
                            out.println("Cache cleard successfully");
                            break;
                        case "status":
                            long postCount = postRepository.count();
                            out.println("Server status: Running, Posts in DB - " + postCount);
                            break;
                        default: out.println("Unknown command. Supported commands: shutdown, clearcache, status");

                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @CacheEvict(value = {"posts","postList"}, allEntries = true)
    private void clearCache() {
    }

    public void stop() {
        running = false;
    }
}
