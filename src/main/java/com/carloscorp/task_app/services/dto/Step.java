package com.carloscorp.task_app.services.dto;

import com.carloscorp.task_app.services.enums.StepType;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = StepMove.class, name = "MOVE"),
        @JsonSubTypes.Type(value = StepWrite.class, name = "WRITE"),
        @JsonSubTypes.Type(value = StepClick.class, name = "CLICK"),
        @JsonSubTypes.Type(value = StepPressKey.class, name = "PRESS_KEY")
})
public interface Step {
    StepType getType();
}
