package com.ravinder.api.blog;

import com.ravinder.api.blog.admin.AdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
@EnableCaching
public class BlogApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(BlogApplication.class, args);
		AdminServer server = context.getBean(AdminServer.class);
		new Thread(server).start();

	}

}
