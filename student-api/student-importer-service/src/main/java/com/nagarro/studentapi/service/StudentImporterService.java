package com.nagarro.studentapi.service;

import com.nagarro.studentapi.controller.model.Student;
import org.springframework.stereotype.Service;

@Service
public interface StudentImporterService {

    Student parse(byte[] studentAsByteArray);
}
