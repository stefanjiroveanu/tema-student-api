package com.nagarro.studentapi.controller.api;

import com.nagarro.studentapi.controller.model.Student;
import com.nagarro.studentapi.facade.StudentImporterFacade;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/students")
public class StudentImporterController {

    private final StudentImporterFacade studentImporterFacade;

    public StudentImporterController(StudentImporterFacade studentImporterFacade) {
        this.studentImporterFacade = studentImporterFacade;
    }

    @PostMapping
    public ResponseEntity<Student> sendStudent(@RequestBody byte[] student) {
        return new ResponseEntity<>(studentImporterFacade.sendStudent(student), HttpStatus.OK);
    }
}
