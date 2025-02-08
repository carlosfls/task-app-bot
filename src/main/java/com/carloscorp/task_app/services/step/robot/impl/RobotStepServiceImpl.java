package com.carloscorp.task_app.services.step.robot.impl;

import com.carloscorp.task_app.services.enums.MouseKeyType;
import com.carloscorp.task_app.services.step.robot.RobotStepService;
import com.carloscorp.task_app.services.util.KeyUtilService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;

import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RobotStepServiceImpl implements RobotStepService {

    private final Robot robot;
    private final KeyUtilService keyUtils;
    private final ResourceLoader resourceLoader;

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

    //todo revisar par poder calibrar.
    //no me esta detectando el rojo en la imagen.
    @Override
    public void calibrate() throws IOException {
        minimizeAll();
        Resource resource = resourceLoader.getResource("classpath:/images/desktop-map.png");
        //cargar la imagen
        ByteArrayInputStream inputStream = new ByteArrayInputStream(resource.getContentAsByteArray());
        BufferedImage bufferedImage = ImageIO.read(inputStream);
        //buscar en esa imagen los puntos rojos y obtener la coordenada de cada punto rojo en la imagen.
        List<Integer> coordinates = new ArrayList<>();
        findColorInImage(bufferedImage, Color.red, coordinates);
        //mover el robot hacia esa coordenada.
        for(int i =0 ; i < coordinates.size()-1; i++){
            int x = coordinates.get(i);
            int y = coordinates.get(i+1);
            move(x, y);
        }
    }

    public void findColorInImage(BufferedImage image, Color color, List<Integer> coordinates){
        int width = image.getWidth();
        int height = image.getHeight();

        for(int y = 0; y < height; y++){
            for (int x = 0; x < width; x++){
                Color pixelColor = getRgb(image, x, y);
                if (pixelColor.equals(color)){
                    coordinates.add(x);
                    coordinates.add(y);
                }
            }
        }
    }

    private Color getRgb(BufferedImage image, int x, int y){
        int rgb = image.getRGB(x, y);
        return new Color(rgb);
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
