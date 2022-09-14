package com.nagarro.studentapi.facade;

import com.nagarro.studentapi.integration.model.Student;
import com.nagarro.studentapi.integration.queue.ValidationQueueSender;
import com.nagarro.studentapi.persistance.model.ValidationEntry;
import com.nagarro.studentapi.service.ValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ValidationFacade {

    private final ValidationService validationService;

    private final ValidationQueueSender queueSender;

    public boolean find(String uuid) {
        return validationService.find(uuid).isValid();
    }

    public ValidationEntry validate(Student student) {
        ValidationEntry validationEntry = validationService.validateStudent(student);
        queueSender.send(validationEntry);
        return validationEntry;
    }
}
