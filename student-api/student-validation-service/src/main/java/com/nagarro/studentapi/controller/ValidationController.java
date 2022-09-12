package com.nagarro.studentapi.controller;

import com.nagarro.studentapi.facade.ValidationFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ValidationController {

    private static final String STUDENT = "Student with the uuid: ";
    private static final String VALID = " is valid";
    private static final String INVALID = " is invalid";
    private final ValidationFacade validationFacade;

    @GetMapping("api/students/{uuid}/validation")
    public ResponseEntity<String> find(@PathVariable String uuid) {
        boolean isValid = validationFacade.find(uuid);
        if (isValid) {
            return new ResponseEntity<>(STUDENT + uuid + VALID, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(STUDENT + uuid + INVALID, HttpStatus.OK);
        }
    }
}
