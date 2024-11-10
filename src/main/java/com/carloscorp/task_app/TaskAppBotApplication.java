package com.carloscorp.task_app;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class TaskAppBotApplication {

    public static void main(String[] args) {
        SpringApplicationBuilder builder = new SpringApplicationBuilder(TaskAppBotApplication.class);
        builder.headless(false);
        ConfigurableApplicationContext context = builder.run(args);
    }

}
