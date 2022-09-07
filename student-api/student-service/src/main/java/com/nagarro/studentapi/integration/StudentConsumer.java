package com.nagarro.studentapi.integration;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.stereotype.Component;

@Component
public class StudentConsumer implements MessageListener {

    @Override
    public void onMessage(Message message) {
        System.out.println("We received a message: " + new String(message.getBody()));
    }
}
