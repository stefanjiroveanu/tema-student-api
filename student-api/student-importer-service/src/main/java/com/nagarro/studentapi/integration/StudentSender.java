package com.nagarro.studentapi.integration;

import com.nagarro.studentapi.controller.model.ImportedStudent;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Data
@AllArgsConstructor
@Component
public class StudentSender {

    private RabbitTemplate template;
    private static final String routingKey = "student_routingKey";
    private static final String exchange = "student_exchange";

    public void send(ImportedStudent student) {
        template.convertAndSend(exchange, routingKey, student);
    }
}
