package com.carloscorp.task_app.services.task.impl;

import com.carloscorp.task_app.services.dto.Step;
import com.carloscorp.task_app.services.dto.StepClick;
import com.carloscorp.task_app.services.dto.StepMove;
import com.carloscorp.task_app.services.dto.StepPressKey;
import com.carloscorp.task_app.services.dto.StepWrite;
import com.carloscorp.task_app.services.dto.TaskDTO;
import com.carloscorp.task_app.services.enums.TaskState;
import com.carloscorp.task_app.services.step.robot.RobotStepService;
import com.carloscorp.task_app.services.task.TaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Duration;

@RabbitListener(queues = "q.task")
@Slf4j
@Service
@RequiredArgsConstructor
public class RabbitMQTaskServiceImpl implements TaskService {

    private final RobotStepService robotStepService;

    @RabbitHandler
    public void healthCheck(String message){
        log.info(message);
    }

    @Override
    @RabbitHandler
    public void consumeTask(TaskDTO dto) throws InterruptedException {
        log.info("Receiving task {}", dto.getDescription());
        Thread.sleep(Duration.ofSeconds(2));
        try {
            robotStepService.calibrate();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        //executeTask(dto);
        log.info("Task finished");
        dto.setState(TaskState.FINISH);
    }

    private void executeTask(TaskDTO dto){
        if (dto.getSteps().isEmpty()){
            log.info("No steps to execute");
        }else {
            robotStepService.minimizeAll();
            dto.getSteps().forEach(step -> {
                executeStep(step);
                robotStepService.delay(500);
            });
        }
    }

    private void executeStep(Step step){
        switch (step.getType()){
            case MOVE -> {
                StepMove stepMove = (StepMove) step;
                robotStepService.move(stepMove.getX(),stepMove.getY());
            }
            case CLICK -> {
                StepClick stepClick = (StepClick) step;
                robotStepService.click(stepClick.getNoClicks(),stepClick.getMouseKeyType());
            }
            case WRITE -> {
                StepWrite stepWrite = (StepWrite) step;
                robotStepService.write(stepWrite.getText());
            }
            case PRESS_KEY -> {
                StepPressKey stepPressKey = (StepPressKey) step;
                robotStepService.pressKeys(stepPressKey.getKeys(), stepPressKey.isCombine());
            }
        }
    }
}
