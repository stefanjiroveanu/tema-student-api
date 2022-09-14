package com.nagarro.studentapi.controller;

import com.nagarro.studentapi.facade.ExporterFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/students")
@RequiredArgsConstructor
public class ExporterController {

    private final ExporterFacade exporterFacade;

    @GetMapping("/{uuid}/export")
    public ResponseEntity<String> exportStudent(@PathVariable String uuid) {
        return new ResponseEntity<>(exporterFacade.exportStudent(uuid),HttpStatus.OK);
    }
}
