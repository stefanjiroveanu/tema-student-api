package com.nagarro.studentapi.integration.queue;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nagarro.studentapi.exception.AppException;
import com.nagarro.studentapi.facade.ValidationFacade;
import com.nagarro.studentapi.integration.model.Student;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class ValidationQueueConsumer implements MessageListener {
    private final ObjectMapper objectMapper;
    private final ValidationFacade validationFacade;

    @Override
    public void onMessage(Message message) {
        System.out.println("We received a message: " + new String(message.getBody()));
        try {
            Student student = objectMapper.readValue(message.getBody(), Student.class);
            System.out.println("Student is " + validationFacade.validate(student));
        } catch (IOException e) {
            throw new AppException("could not convert student");
        }
    }
}
