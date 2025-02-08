package com.carloscorp.task_app.services.step.robot;

import com.carloscorp.task_app.services.step.StepService;

import java.io.IOException;

public interface RobotStepService extends StepService {

    void calibrate() throws IOException;
}
