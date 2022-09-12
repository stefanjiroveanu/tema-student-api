package com.nagarro.studentapi.integration.queue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nagarro.studentapi.exception.AppException;
import com.nagarro.studentapi.integration.model.ValidationEntry;
import com.nagarro.studentapi.persistance.model.Student;
import com.nagarro.studentapi.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QueueValidationConsumer implements MessageListener {

    private final ObjectMapper objectMapper;
    private final StudentService studentService;

    @Override
    public void onMessage(Message message) {
        String entry = new String(message.getBody());
        try {
            ValidationEntry validationEntry = objectMapper.readValue(entry, ValidationEntry.class);
            Student student = studentService.find(validationEntry.getStudentUuid());
            student.setValid(validationEntry.isValid());
            studentService.update(validationEntry.getStudentUuid(), student);
        } catch (JsonProcessingException e) {
            throw new AppException("error converting validation entry");
        }
    }
}


