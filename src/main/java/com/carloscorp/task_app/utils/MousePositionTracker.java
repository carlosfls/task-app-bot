package com.carloscorp.task_app.utils;

import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;

public class MousePositionTracker {
    private static final int DELAY = 1000; // 1 second delay

    public static void main(String[] args) {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Point mousePosition = MouseInfo.getPointerInfo().getLocation();
                System.out.println("Current mouse position: " + mousePosition.x + ", " + mousePosition.y);
            }
        }, 0, DELAY);
    }
}
