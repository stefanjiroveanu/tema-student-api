package com.nagarro.studentapi.service;

import com.nagarro.studentapi.controller.model.ImportedStudent;
import org.springframework.stereotype.Service;

@Service
public interface StudentImporterService {
    ImportedStudent send(String path);
}
