package com.nagarro.studentapi.controller.api;

import com.nagarro.studentapi.controller.model.ImportedStudent;
import com.nagarro.studentapi.facade.StudentImporterFacade;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class StudentImporterController {

    @Value("${student-api.xmlPath}")
    private String path;
    private final StudentImporterFacade studentImporterFacade;

    public StudentImporterController(StudentImporterFacade studentImporterFacade) {
        this.studentImporterFacade = studentImporterFacade;
    }

    @PostMapping("/send")
    public ResponseEntity<ImportedStudent> sendStudent() {
        return new ResponseEntity<>(studentImporterFacade.sendStudent(path), HttpStatus.OK);
    }
}
