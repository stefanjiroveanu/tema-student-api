package com.nagarro.studentapi.service;

import com.nagarro.studentapi.persistance.model.Student;

public interface StudentService {
    Student save(Student student);

    Student find(String uuid);

    void delete(String uuid);

    Student update(String uuid, Student updatedStudent);
}
