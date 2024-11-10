package com.carloscorp.task_app.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public void handleExceptionAndRejectMessage(Exception e){
        log.info(e.getMessage());
        throw new AmqpRejectAndDontRequeueException("Message Rejected");
    }
}
