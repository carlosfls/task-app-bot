package com.carloscorp.task_app.services.step.impl;

import com.carloscorp.task_app.services.enums.MouseKeyType;
import com.carloscorp.task_app.services.step.StepService;
import com.carloscorp.task_app.services.util.KeyUtilService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RobotStepServiceImpl implements StepService {

    private final Robot robot;
    private final KeyUtilService keyUtils;

    @Override
    public void move(int x, int y){
        log.info("Moving to {} {} position", x, y);
        robot.mouseMove(x,y);
    }

    @Override
    public void click(int noClicks, MouseKeyType keyType){
        for (int i=0; i<noClicks; i++){
            if (MouseKeyType.LEFT_CLICK.equals(keyType)){
                leftClick();
            }else {
                rightClick();
            }
        }
    }

    @Override
    public void write(String text){
        log.info("Typing {}", text);
        for (char c : text.toCharArray()){
            typeChar(c);
            robot.delay(100);
        }
    }

    @Override
    public void pressKeys(List<Integer> keys, boolean combine){
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

    @Override
    public void delay(int ms) {
        robot.delay(ms);
    }

    @Override
    public void minimizeAll(){
        robot.keyPress(KeyEvent.VK_WINDOWS);
        robot.keyPress(KeyEvent.VK_M);
        robot.keyRelease(KeyEvent.VK_WINDOWS);
        robot.keyRelease(KeyEvent.VK_M);
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
