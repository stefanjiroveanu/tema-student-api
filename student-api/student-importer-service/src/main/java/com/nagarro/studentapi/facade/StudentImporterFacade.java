package com.nagarro.studentapi.facade;

import com.nagarro.studentapi.controller.model.Student;
import com.nagarro.studentapi.integration.queue.StudentQueueSender;
import com.nagarro.studentapi.service.StudentImporterService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class StudentImporterFacade {

    private final StudentImporterService studentImporterService;
    private final StudentQueueSender studentQueueSender;

    public Student sendStudent(byte[] studentAsByteArray) {
        Student student = studentImporterService.parse(studentAsByteArray);
        studentQueueSender.send(student);
        return student;
    }
}
