package com.ravinder.api.blog.admin;

import com.ravinder.api.blog.repository.PostRepository;
import com.ravinder.api.blog.service.CacheService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger logger = LoggerFactory.getLogger(AdminServer.class);
    private final ConfigurableApplicationContext context;
    private final PostRepository postRepository;
    private CacheService cacheService;
    private final int port  = 9999;
    private volatile boolean running = true;

    public AdminServer(ConfigurableApplicationContext context, PostRepository postRepository, CacheService cacheService){
        this.context = context;
        this.postRepository = postRepository;
        this.cacheService = cacheService;
    }
    @Override
    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            logger.info("Admin server running on port {}",port);
            while (running) {
                try (Socket clientSocket = serverSocket.accept();
                     BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                     PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {
                    String command = in.readLine();

                    if(command == null) continue;

                    switch (command.toLowerCase()){
                        case "shutdown" :
                            out.println("Shutting down server ......");
                            logger.info("Shutting down server ......");
                            running = false;
                            SpringApplication.exit(context, () -> 0);
                            break;
                        case "clearcache":
                            cacheService.clearCache();
                            out.println("Cache cleard successfully");
                            logger.info("Cache cleard successfully");
                            break;
                        case "status":
                            long postCount = postRepository.count();
                            out.println("Server status: Running, Posts in DB - " + postCount);
                            logger.info("Server status: Running, Posts in DB - " + postCount);
                            break;
                        default:
                            out.println("Unknown command. Supported commands: shutdown, clearcache, status");
                            logger.warn("Unknown admin command: {}", command);

                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("Admin server error");
        }
    }



    public void stop() {
        running = false;
    }
}
