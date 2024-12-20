package com.carloscorp.task_app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.awt.*;

@Configuration
public class RobotConfig {

    @Bean
    public Robot robot() throws AWTException {
        Robot robot = new Robot();
        robot.setAutoWaitForIdle(true);
        return robot;
    }
}
