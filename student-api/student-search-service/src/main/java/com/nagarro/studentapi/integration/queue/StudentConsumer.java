package com.nagarro.studentapi.integration.queue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nagarro.studentapi.exception.AppException;
import com.nagarro.studentapi.facade.SearchFacade;
import com.nagarro.studentapi.integration.converter.SolrStudentConverter;
import com.nagarro.studentapi.integration.model.SolrMessage;
import com.nagarro.studentapi.integration.model.SolrStudent;
import com.nagarro.studentapi.integration.model.domain.HttpRequestEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StudentConsumer implements MessageListener {

    private final ObjectMapper objectMapper;

    private final SearchFacade searchFacade;

    private final SolrStudentConverter converter;

    @Override
    public void onMessage(Message message) {
        try {
            System.out.println("We recieved a message" + new String(message.getBody()));
            SolrMessage solrMessage = objectMapper.readValue(
                    new String(message.getBody()),
                    SolrMessage.class
            );
            SolrStudent student = solrMessage.getSolrStudent();
            if (solrMessage.getHttpRequestEnum() == HttpRequestEnum.DELETE) {
                searchFacade.delete(student.getUuid());
            } else if (solrMessage.getHttpRequestEnum() == HttpRequestEnum.POST) {
                searchFacade.save(converter.toModel(student));
            } else if (solrMessage.getHttpRequestEnum() == HttpRequestEnum.PUT) {
                searchFacade.update(student.getUuid(), converter.toModel(student));
            }
        } catch (JsonProcessingException e) {
            throw new AppException(e.getMessage());
        }
    }
}
