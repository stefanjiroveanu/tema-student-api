package com.nagarro.studentapi.integration.queue;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nagarro.studentapi.controller.model.ExternalStudent;
import com.nagarro.studentapi.exception.AppException;
import com.nagarro.studentapi.facade.StudentFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@RabbitListener(queues = {"${student-api.queue}"})
public class QueueStudentConsumer implements MessageListener {

    private final ObjectMapper objectMapper;

    private final StudentFacade studentFacade;

    @Override
    public void onMessage(Message message) {
        System.out.println("We received a message: " + new String(message.getBody()));
        try {
            ExternalStudent student = objectMapper.readValue(message.getBody(), ExternalStudent.class);
            studentFacade.save(student);
        } catch (IOException e) {
            throw new AppException("could not convert student");
        }
    }
}
