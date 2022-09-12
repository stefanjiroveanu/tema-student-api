package com.nagarro.studentapi.controller.api;

import com.nagarro.studentapi.controller.model.ExternalStudent;
import com.nagarro.studentapi.facade.StudentFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/students")
public class StudentController {
    private static final String DELETED = "The Student has been deleted";
    public final StudentFacade studentFacade;

    @PostMapping
    public ResponseEntity<ExternalStudent> save(@RequestBody ExternalStudent externalStudent) {
        return new ResponseEntity<>(studentFacade.save(externalStudent), HttpStatus.OK);
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<ExternalStudent> find(@PathVariable String uuid) {
        return new ResponseEntity<>(studentFacade.find(uuid), HttpStatus.OK);
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<String> delete(@PathVariable String uuid) {
        studentFacade.delete(uuid);
        return new ResponseEntity<>(DELETED, HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{uuid}")
    public ResponseEntity<ExternalStudent> update(@PathVariable String uuid, @RequestBody ExternalStudent student) {
        return new ResponseEntity<>(studentFacade.update(uuid, student), HttpStatus.OK);
    }
}
