package com.nagarro.studentapi.integration.queue;

import com.nagarro.studentapi.integration.model.SolrStudent;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class QueueSenderForValidation {

    private final RabbitTemplate rabbitTemplate;

    @Value("${student-api.validationExchange}")
    private String exchange;

    @Value("${student-api.validationRoutingKey}")
    private String routingKey;

    public void send(SolrStudent solrStudent) {
        rabbitTemplate.setExchange(exchange);
        rabbitTemplate.setRoutingKey(routingKey);
        rabbitTemplate.convertAndSend(exchange, routingKey, solrStudent);
    }

}
