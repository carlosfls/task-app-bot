package com.carloscorp.task_app.services.dto;

import com.carloscorp.task_app.services.enums.TaskState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@ToString
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaskDTO {

    private String description;

    private List<Step> steps;

    private TaskState state;
}
