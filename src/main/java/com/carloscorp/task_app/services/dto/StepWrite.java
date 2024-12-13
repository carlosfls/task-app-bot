package com.carloscorp.task_app.services.dto;

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
public class StepWrite implements Step {

    private String text;

    @Override
    public StepType getType() {
        return StepType.WRITE;
    }
}
