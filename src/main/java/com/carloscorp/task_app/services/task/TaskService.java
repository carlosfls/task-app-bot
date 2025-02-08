package com.carloscorp.task_app.services.task;

import com.carloscorp.task_app.services.dto.TaskDTO;

public interface TaskService {

    void consumeTask(TaskDTO dto) throws Exception;
}
