package com.carloscorp.task_app.services.util;

import org.springframework.stereotype.Component;

import java.awt.*;

import static java.awt.event.KeyEvent.VK_ALT;
import static java.awt.event.KeyEvent.VK_NUMPAD0;
import static java.awt.event.KeyEvent.VK_NUMPAD1;
import static java.awt.event.KeyEvent.VK_NUMPAD2;
import static java.awt.event.KeyEvent.VK_NUMPAD3;
import static java.awt.event.KeyEvent.VK_NUMPAD4;
import static java.awt.event.KeyEvent.VK_NUMPAD5;
import static java.awt.event.KeyEvent.VK_NUMPAD6;
import static java.awt.event.KeyEvent.VK_NUMPAD7;
import static java.awt.event.KeyEvent.VK_NUMPAD8;
import static java.awt.event.KeyEvent.VK_NUMPAD9;

@Component
public class KeyUtilService {

    public boolean typeSpecialCharacter(Robot robot, Character characterKey) {
        return switch (characterKey) {
            case '!' -> altNumpad(robot, "33");
            case '"' -> altNumpad(robot, "34");
            case '#' -> altNumpad(robot, "35");
            case '$' -> altNumpad(robot, "36");
            case '%' -> altNumpad(robot, "37");
            case '&' -> altNumpad(robot, "38");
            case '\'' -> altNumpad(robot, "39");
            case '(' -> altNumpad(robot, "40");
            case ')' -> altNumpad(robot, "41");
            case '*' -> altNumpad(robot, "42");
            case '+' -> altNumpad(robot, "43");
            case ',' -> altNumpad(robot, "44");
            case '-' -> altNumpad(robot, "45");
            case '/' -> altNumpad(robot, "47");
            case ':' -> altNumpad(robot, "58");
            case '<' -> altNumpad(robot, "60");
            case '=' -> altNumpad(robot, "61");
            case '>' -> altNumpad(robot, "62");
            case '?' -> altNumpad(robot, "63");
            case '@' -> altNumpad(robot, "64");
            case '[' -> altNumpad(robot, "91");
            case '\\' -> altNumpad(robot, "92");
            case ']' -> altNumpad(robot, "93");
            case '^' -> altNumpad(robot, "94");
            case '_' -> altNumpad(robot, "95");
            case '`' -> altNumpad(robot, "96");
            case '{' -> altNumpad(robot, "123");
            case '|' -> altNumpad(robot, "124");
            case '}' -> altNumpad(robot, "125");
            case '~' -> altNumpad(robot, "126");
            default -> false;
        };
    }

    private boolean altNumpad(Robot robot, String numpadCodes) {
        if (numpadCodes == null || !numpadCodes.matches("^\\d+$")) {
            return false;
        }
        robot.keyPress(VK_ALT);
        for (char charater : numpadCodes.toCharArray()) {
            int NUMPAD_KEY = getNumpad(charater);
            if (NUMPAD_KEY != -1) {
                robot.keyPress(NUMPAD_KEY);
                robot.keyRelease(NUMPAD_KEY);
            }
        }
        robot.keyRelease(VK_ALT);
        return true;
    }

    private int getNumpad(char numberChar) {
        return switch (numberChar) {
            case '0' -> VK_NUMPAD0;
            case '1' -> VK_NUMPAD1;
            case '2' -> VK_NUMPAD2;
            case '3' -> VK_NUMPAD3;
            case '4' -> VK_NUMPAD4;
            case '5' -> VK_NUMPAD5;
            case '6' -> VK_NUMPAD6;
            case '7' -> VK_NUMPAD7;
            case '8' -> VK_NUMPAD8;
            case '9' -> VK_NUMPAD9;
            default -> -1;
        };
    }
}
