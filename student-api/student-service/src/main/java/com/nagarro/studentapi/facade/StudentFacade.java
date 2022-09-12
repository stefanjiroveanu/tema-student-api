package com.nagarro.studentapi.facade;

import com.nagarro.studentapi.controller.model.ExternalStudent;
import com.nagarro.studentapi.facade.converters.StudentConverter;
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
    private final QueueSenderForValidation queueSenderForValidation;

    public ExternalStudent save(ExternalStudent student) {
        Student returnedStudent = studentService.save(converter.toModel(student));
        queueSenderForValidation.send(returnedStudent);
        return converter.toDto(returnedStudent);
    }

    public ExternalStudent find(String uuid) {
        return converter.toDto(studentService.find(uuid));
    }

    public void delete(String uuid) {
        studentService.delete(uuid);
    }

    public ExternalStudent update(String uuid, ExternalStudent student) {
        Student returnedStudent = studentService.update(uuid, converter.toModel(student));
        queueSenderForValidation.send(returnedStudent);
        return converter.toDto(returnedStudent);
    }
}
