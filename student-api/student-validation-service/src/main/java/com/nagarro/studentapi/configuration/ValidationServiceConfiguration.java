package com.nagarro.studentapi.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nagarro.studentapi.facade.ValidationFacade;
import com.nagarro.studentapi.integration.queue.ValidationQueueConsumer;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.KieModule;
import org.kie.api.runtime.KieContainer;
import org.kie.internal.io.ResourceFactory;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.MessageListenerContainer;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ValidationServiceConfiguration {

    @Value("${drools.validationRule}")
    public String DRL_FILE;
    @Value("${student-api.validationQueue}")
    private String receivingValidationQueue;
    @Value("${student-api.replyQueue}")
    private String replyingValidationQueue;

    @Value("${student-api.exchange}")
    private String exchange;

    @Value("${student-api.routingKey}")
    private String routingKey;

    @Bean
    public Queue queueForValidation() {
        return new Queue(receivingValidationQueue);
    }

    @Bean
    public Queue queueForReplying() {
        return new Queue(replyingValidationQueue);
    }

    @Bean
    public DirectExchange exchange() {
        return new DirectExchange(exchange);
    }

    @Bean
    public Binding replyingBinding() {
        return BindingBuilder.bind(queueForReplying())
                .to(exchange())
                .with(routingKey);
    }

    @Bean
    public MessageListenerContainer messageListenerContainer(ConnectionFactory connectionFactory,
                                                             ValidationFacade validationFacade,
                                                             ObjectMapper objectMapper) {
        SimpleMessageListenerContainer messageListenerContainer = new SimpleMessageListenerContainer(connectionFactory);
        messageListenerContainer.setQueues(queueForValidation());
        messageListenerContainer.setMessageListener(new ValidationQueueConsumer(objectMapper, validationFacade));
        return messageListenerContainer;
    }

    @Bean
    public KieContainer kieContainer() {
        KieServices kieServices = KieServices.Factory.get();

        KieFileSystem kieFileSystem = kieServices.newKieFileSystem();
        kieFileSystem.write(ResourceFactory.newClassPathResource(DRL_FILE));
        KieBuilder kieBuilder = kieServices.newKieBuilder(kieFileSystem).buildAll();
        KieModule kieModule = kieBuilder.getKieModule();

        return kieServices.newKieContainer(kieModule.getReleaseId());
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate validationTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }
}
