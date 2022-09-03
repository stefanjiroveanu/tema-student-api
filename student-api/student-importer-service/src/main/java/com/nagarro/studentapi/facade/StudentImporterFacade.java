package com.nagarro.studentapi.facade;

import com.nagarro.studentapi.controller.model.ImportedStudent;
import com.nagarro.studentapi.service.StudentImporterService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class StudentImporterFacade {
    private final StudentImporterService studentImporterService;
    public ImportedStudent sendStudent(String studentPath) {
        return studentImporterService.send(studentPath);
    }
}
