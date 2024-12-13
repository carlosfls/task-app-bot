package com.carloscorp.task_app.services.impl;

import com.carloscorp.task_app.services.TaskService;
import com.carloscorp.task_app.services.dto.StepClick;
import com.carloscorp.task_app.services.dto.StepMove;
import com.carloscorp.task_app.services.dto.StepPressKey;
import com.carloscorp.task_app.services.dto.StepWrite;
import com.carloscorp.task_app.services.dto.TaskDTO;
import com.carloscorp.task_app.services.enums.MouseKeyType;
import com.carloscorp.task_app.services.enums.TaskState;
import com.carloscorp.task_app.services.util.KeyUtilService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.time.Duration;
import java.util.List;

@RabbitListener(queues = "q.task")
@Slf4j
@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final Robot robot;
    private final KeyUtilService keyUtils;

    @RabbitHandler
    public void healthCheck(String message){
        log.info(message);
    }

    @RabbitHandler
    public void consumeTask(TaskDTO dto) throws InterruptedException {
        log.info("Receiving task {}", dto.getDescription());
        Thread.sleep(Duration.ofSeconds(2));
        executeTask(dto);
        log.info("Task finished");
        dto.setState(TaskState.FINISH);
    }

    private void executeTask(TaskDTO dto){
        if (dto.getSteps().isEmpty()){
            log.info("No steps to execute");
        }else {
            minimizeAll();
            dto.getSteps().forEach(step -> {
                switch (step.getType()){
                    case MOVE -> {
                        StepMove stepMove = (StepMove) step;
                        move(stepMove.getX(),stepMove.getY());
                    }
                    case CLICK -> {
                        StepClick stepClick = (StepClick) step;
                        click(stepClick.getNoClicks(),stepClick.getMouseKeyType());
                    }
                    case WRITE -> {
                        StepWrite stepWrite = (StepWrite) step;
                        write(stepWrite.getText());
                    }
                    case PRESS_KEY -> {
                        StepPressKey stepPressKey = (StepPressKey) step;
                        pressKeys(stepPressKey.getKeys(), stepPressKey.isCombine());
                    }
                }
                robot.delay(500);
            });
        }
    }

    private void minimizeAll(){
        robot.keyPress(KeyEvent.VK_WINDOWS);
        robot.keyPress(KeyEvent.VK_M);
        robot.keyRelease(KeyEvent.VK_WINDOWS);
        robot.keyRelease(KeyEvent.VK_M);
    }

    private void pressKeys(List<Integer> keys, boolean combine){
        if (combine){
            keys.forEach(robot::keyPress);
            robot.delay(25);
            keys.forEach(robot::keyRelease);
        }else {
            keys.forEach(k -> {
                robot.keyPress(k);
                robot.delay(25);
                robot.keyRelease(k);
            });
        }
        robot.delay(100);
    }

    private void write(String text){
        log.info("Typing {}", text);
        for (char c : text.toCharArray()){
            typeChar(c);
            robot.delay(100);
        }
    }

    private void click(int noClicks, MouseKeyType keyType){
        for (int i=0; i<noClicks; i++){
            if (MouseKeyType.LEFT_CLICK.equals(keyType)){
                leftClick();
            }else {
                rightClick();
            }
        }
    }

    private void move(int x, int y){
        log.info("Moving to {} {} position", x, y);
        robot.mouseMove(x,y);
    }

    private void leftClick(){
        log.info("Press left click");
        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
    }

    private void rightClick(){
        log.info("Press right click");
        robot.mousePress(InputEvent.BUTTON2_DOWN_MASK);
        robot.mouseRelease(InputEvent.BUTTON2_DOWN_MASK);
    }

    private void typeChar(char key){
        boolean special = keyUtils.typeSpecialCharacter(robot, key);
        if (!special){
            if (Character.isLetter(key) && Character.isUpperCase(key)){
                robot.keyPress(KeyEvent.VK_SHIFT);
                type(key);
                robot.keyRelease(KeyEvent.VK_SHIFT);
            }else {
                type(key);
            }
        }
    }

    private void type(char key){
        try {
            keyPress(key);
            robot.delay(25);
            keyRelease(key);
        }catch (IllegalArgumentException e){
            log.error("Illegal key {} omitting", key);
        }
    }

    private void keyPress(char key){
        int extendedKeyCodeForChar = KeyEvent.getExtendedKeyCodeForChar(key);
        robot.keyPress(extendedKeyCodeForChar);
    }

    private void keyRelease(char key){
        int extendedKeyCodeForChar = KeyEvent.getExtendedKeyCodeForChar(key);
        robot.keyRelease(extendedKeyCodeForChar);
    }
}
