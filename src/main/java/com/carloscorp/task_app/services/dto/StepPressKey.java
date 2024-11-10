package com.carloscorp.task_app.services.dto;

import com.carloscorp.task_app.services.enums.StepType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StepPressKey implements Step {

    private List<Integer> keys;
    private int delay;
    private boolean combine;

    @Override
    public StepType getType() {
        return StepType.PRESS_KEY;
    }
}
