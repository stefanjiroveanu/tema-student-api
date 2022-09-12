package com.nagarro.studentapi.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nagarro.studentapi.controller.model.ExternalStudent;
import com.nagarro.studentapi.facade.StudentFacade;
import com.nagarro.studentapi.integration.queue.QueueStudentConsumer;
import com.nagarro.studentapi.integration.queue.QueueValidationConsumer;
import com.nagarro.studentapi.persistance.model.Student;
import com.nagarro.studentapi.service.StudentService;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.DirectMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.MessageListenerContainer;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StudentConfiguration {

    @Value("${student-api.queue}")
    private String importingQueue;
    @Value("${student-api.validationQueue}")
    private String validationQueue;
    @Value("${student-api.validationRoutingKey}")
    private String routingKey;
    @Value("${student-api.validationExchange}")
    private String exchange;

    @Value("${student-api.receivingValidationQueue}")
    private String receivingValidationQueue;

    @Bean
    public Queue validationQueue() {
        return new Queue(validationQueue);
    }

    @Bean
    public Queue importingQueue() {
        return new Queue(importingQueue);
    }

    @Bean
    public DirectExchange validationExchange() {
        return new DirectExchange(exchange);
    }

    @Bean
    public Queue receivingValidationQueue() {
        return new Queue(receivingValidationQueue);
    }

    @Bean
    public Binding validationBinding() {
        return BindingBuilder.bind(validationQueue())
                .to(validationExchange())
                .with(routingKey);
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

    @Bean
    public MessageListenerContainer validationListenerContainer(ConnectionFactory connectionFactory,
                                                                ObjectMapper objectMapper,
                                                                StudentService studentService) {
        DirectMessageListenerContainer messageListenerContainer = new DirectMessageListenerContainer();
        messageListenerContainer.setConnectionFactory(connectionFactory);
        messageListenerContainer.setQueueNames(receivingValidationQueue);
        messageListenerContainer.setMessageListener(new QueueValidationConsumer(objectMapper, studentService));
        return messageListenerContainer;
    }

    @Bean
    public MessageListenerContainer studentListenerContainer(ConnectionFactory connectionFactory,
                                                             ObjectMapper objectMapper,
                                                             StudentFacade studentFacade) {
        DirectMessageListenerContainer messageListenerContainer = new DirectMessageListenerContainer();
        messageListenerContainer.setConnectionFactory(connectionFactory);
        messageListenerContainer.setQueueNames(importingQueue);
        messageListenerContainer.setMessageListener(new QueueStudentConsumer(objectMapper, studentFacade));
        return messageListenerContainer;
    }

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.addMappings(new PropertyMap<Student, ExternalStudent>() {
            @Override
            protected void configure() {
                map(source.getData().getFirstname(), destination.getFirstname());
                map(source.getData().getLastname(), destination.getLastname());
                map(source.getData().getGrades(), destination.getGrades());
                map(source.getData().getCnp(), destination.getCnp());
                map(source.getData().getAddress(), destination.getAddress());
                map(source.getData().getBirthDate(), destination.getBirthDate());
            }
        });
        modelMapper.addMappings(new PropertyMap<ExternalStudent, Student>() {
            @Override
            protected void configure() {
                map(source.getFirstname(), destination.getData().getFirstname());
                map(source.getLastname(), destination.getData().getLastname());
                map(source.getGrades(), destination.getData().getGrades());
                map(source.getCnp(), destination.getData().getCnp());
                map(source.getAddress(), destination.getData().getAddress());
                map(source.getBirthDate(), destination.getData().getBirthDate());
            }
        });
        return modelMapper;
    }
}
