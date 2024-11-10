package com.carloscorp.task_app.services.dto;

import com.carloscorp.task_app.services.enums.MouseKeyType;
import com.carloscorp.task_app.services.enums.StepType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StepClick implements Step {

    private int noClicks;
    private MouseKeyType mouseKeyType;

    @Override
    public StepType getType() {
        return StepType.CLICK;
    }
}
