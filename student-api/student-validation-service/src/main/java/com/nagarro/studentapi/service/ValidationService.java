package com.nagarro.studentapi.service;

import com.nagarro.studentapi.integration.model.Student;
import com.nagarro.studentapi.persistance.model.ValidationEntry;

public interface ValidationService {
    ValidationEntry find(String uuid);

    ValidationEntry validateStudent(Student student);
}
