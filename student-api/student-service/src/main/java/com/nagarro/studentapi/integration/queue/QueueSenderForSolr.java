package com.nagarro.studentapi.integration.queue;

import com.nagarro.studentapi.integration.model.SolrMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class QueueSenderForSolr {

    private final RabbitTemplate rabbitTemplate;

    @Value("${student-api.solrExchange}")
    private String exchange;

    @Value("${student-api.solrRoutingKey}")
    private String routingKey;

    public void send(SolrMessage message) {
        rabbitTemplate.setExchange(exchange);
        rabbitTemplate.setRoutingKey(routingKey);
        rabbitTemplate.convertAndSend(exchange, routingKey, message);
    }
}
