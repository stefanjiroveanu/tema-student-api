package com.nagarro.studentapi.service;

import com.nagarro.studentapi.exception.AppException;
import com.nagarro.studentapi.service.model.Student;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ExporterServiceImpl implements ExporterService {

    private static final String REQUEST_URL = "http://localhost:8081/api/students/";
    private final RestTemplate restTemplate;
    private final SpringTemplateEngine templateEngine;
    @Override
    public String exportStudent(String uuid) {
        try {
            ResponseEntity<Student> response = restTemplate.getForEntity(new URI(REQUEST_URL + uuid), Student.class);
            if (response.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new AppException("The student is nonexistent");
            } else if (response.getStatusCode() == HttpStatus.OK) {
                if (Objects.requireNonNull(response.getBody()).isValid()) {
                    Context context = new Context();
                    context.setVariable("student", response.getBody());
                    return templateEngine.process("StudentTemplate", context);
                }
            }
            return "Invalid Student";
        } catch (URISyntaxException e) {
            throw new AppException(e.getMessage());
        }
    }
}
