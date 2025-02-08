package com.carloscorp.task_app.services.task.impl;

import com.carloscorp.task_app.services.dto.TaskDTO;
import com.carloscorp.task_app.services.enums.TaskState;
import com.carloscorp.task_app.services.task.TaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@RabbitListener(queues = "q.task.dlx")
@Slf4j
@Service
@RequiredArgsConstructor
public class RabbitMQErrorMessagesHandler implements TaskService {

    @Override
    @RabbitHandler
    public void consumeTask(TaskDTO dto) {
        dto.setState(TaskState.ERROR);
        log.info("Task rejected {}", dto);
    }
}
