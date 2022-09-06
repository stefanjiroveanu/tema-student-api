package com.nagarro.studentapi.integration.queue;

import com.nagarro.studentapi.controller.model.Student;
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

    public void send(Student student) {
        template.convertAndSend(exchange, routingKey, student);
    }
}
