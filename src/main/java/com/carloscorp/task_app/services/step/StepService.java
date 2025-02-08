package com.carloscorp.task_app.services.step;

import com.carloscorp.task_app.services.enums.MouseKeyType;

import java.util.List;

public interface StepService {

    void move(int x, int y);

    void click(int noClicks, MouseKeyType keyType);

    void write(String text);

    void pressKeys(List<Integer> keys, boolean combine);

    void delay(int ms);

    void minimizeAll();
}
