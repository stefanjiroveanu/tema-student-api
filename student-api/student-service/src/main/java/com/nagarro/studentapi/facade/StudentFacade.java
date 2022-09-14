package com.nagarro.studentapi.facade;

import com.nagarro.studentapi.controller.model.ExternalStudent;
import com.nagarro.studentapi.facade.converters.StudentConverter;
import com.nagarro.studentapi.facade.converters.StudentForQueueConverter;
import com.nagarro.studentapi.integration.model.SolrStudent;
import com.nagarro.studentapi.integration.model.domain.HttpRequestEnum;
import com.nagarro.studentapi.integration.model.SolrMessage;
import com.nagarro.studentapi.integration.queue.QueueSenderForSolr;
import com.nagarro.studentapi.integration.queue.QueueSenderForValidation;
import com.nagarro.studentapi.persistance.model.Student;
import com.nagarro.studentapi.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class StudentFacade {

    private final StudentService studentService;
    private final StudentConverter converter;
    private final StudentForQueueConverter queueConverter;
    private final QueueSenderForValidation queueSenderForValidation;
    private final QueueSenderForSolr queueSenderForSolr;

    public ExternalStudent save(ExternalStudent student) {
        Student returnedStudent = studentService.save(converter.toModel(student));
        SolrStudent queueSolrStudent = queueConverter.toDto(returnedStudent);
        queueSenderForValidation.send(queueSolrStudent);
        SolrMessage solrMessage = new SolrMessage(queueSolrStudent, HttpRequestEnum.POST);
        queueSenderForSolr.send(solrMessage);
        return converter.toDto(returnedStudent);
    }

    public ExternalStudent find(String uuid) {
        return converter.toDto(studentService.find(uuid));
    }

    public void delete(String uuid) {
        SolrStudent solrStudent = new SolrStudent();
        solrStudent.setUuid(uuid);
        SolrMessage solrMessage = new SolrMessage(solrStudent, HttpRequestEnum.DELETE);
        queueSenderForSolr.send(solrMessage);
        studentService.delete(uuid);
    }

    public ExternalStudent update(String uuid, ExternalStudent student) {
        Student returnedStudent = studentService.update(uuid, converter.toModel(student));
        SolrStudent queueSolrStudent = queueConverter.toDto(returnedStudent);
        queueSenderForValidation.send(queueSolrStudent);
        SolrMessage solrMessage = new SolrMessage(queueSolrStudent, HttpRequestEnum.PUT);
        queueSenderForSolr.send(solrMessage);
        return converter.toDto(returnedStudent);
    }
}
