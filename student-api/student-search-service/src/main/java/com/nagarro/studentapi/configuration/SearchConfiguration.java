package com.nagarro.studentapi.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nagarro.studentapi.controller.model.Student;
import com.nagarro.studentapi.facade.SearchFacade;
import com.nagarro.studentapi.integration.converter.SolrStudentConverter;
import com.nagarro.studentapi.integration.model.SolrStudent;
import com.nagarro.studentapi.integration.queue.StudentConsumer;
import org.apache.solr.client.solrj.impl.Http2SolrClient;
import org.apache.solr.client.solrj.impl.XMLResponseParser;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.MessageListenerContainer;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SearchConfiguration {

    @Value("${student-api.solrUrl}")
    private String url;

    @Value("${student-api.solrQueue}")
    private String solrUrl;

    @Bean
    public Queue solrQueue() {
        return new Queue(solrUrl);
    }

    @Bean
    public Http2SolrClient http2SolrClient() {
        Http2SolrClient solrClient = new Http2SolrClient.Builder(url).build();
        solrClient.setParser(new XMLResponseParser());
        return solrClient;
    }


    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.addMappings(new PropertyMap<SolrStudent, Student>() {
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
        return modelMapper;
    }

    @Bean
    public MessageListenerContainer messageListenerContainer(ConnectionFactory connectionFactory,
                                                             ObjectMapper objectMapper,
                                                             SearchFacade searchFacade,
                                                             SolrStudentConverter solrStudentConverter) {
        SimpleMessageListenerContainer messageListenerContainer = new SimpleMessageListenerContainer(connectionFactory);
        messageListenerContainer.setQueues(solrQueue());
        messageListenerContainer.setMessageListener(new StudentConsumer(
                objectMapper,
                searchFacade,
                solrStudentConverter
        ));
        return messageListenerContainer;
    }
}
