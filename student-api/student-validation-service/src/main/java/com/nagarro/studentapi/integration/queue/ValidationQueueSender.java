package com.nagarro.studentapi.integration.queue;

import com.nagarro.studentapi.persistance.model.ValidationEntry;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ValidationQueueSender {
    public final RabbitTemplate rabbitTemplate;

    @Value("${student-api.exchange}")
    public String exchange;

    @Value("${student-api.routingKey}")
    public String routingKey;

    public void send(ValidationEntry entry) {
        rabbitTemplate.setExchange(exchange);
        rabbitTemplate.setRoutingKey(routingKey);
        rabbitTemplate.convertAndSend(exchange, routingKey, entry);
    }

}
