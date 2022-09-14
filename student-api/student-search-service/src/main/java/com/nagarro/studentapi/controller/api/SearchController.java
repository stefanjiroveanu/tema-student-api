package com.nagarro.studentapi.controller.api;

import com.nagarro.studentapi.controller.model.Student;
import com.nagarro.studentapi.facade.SearchFacade;
import com.nagarro.studentapi.service.model.Query;
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
@RequestMapping("api/students")
@RequiredArgsConstructor
public class SearchController {

    private final SearchFacade facade;

    @PostMapping
    public ResponseEntity<Student> saveStudent(@RequestBody Student student) {
        return new ResponseEntity<>(facade.save(student), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<String> findStudent(@RequestBody Query query) {
        return new ResponseEntity<>(facade.find(query), HttpStatus.OK);
    }

    @PutMapping("/{uuid}")
    public ResponseEntity<Student> updateStudent(@PathVariable String uuid,
                                                 @RequestBody Student student) {
        facade.update(uuid, student);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<Student> deleteStudent(@PathVariable String uuid) {
        facade.delete(uuid);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
