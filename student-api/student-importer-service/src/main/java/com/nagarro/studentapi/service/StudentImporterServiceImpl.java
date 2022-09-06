package com.nagarro.studentapi.service;

import com.nagarro.studentapi.controller.model.Student;
import com.nagarro.studentapi.exception.AppException;
import com.nagarro.studentapi.util.XmlParser;
import org.springframework.stereotype.Service;

@Service
public class StudentImporterServiceImpl implements StudentImporterService {

    private final XmlParser parser;

    public StudentImporterServiceImpl(XmlParser parser) {
        this.parser = parser;
    }

    @Override
    public Student parse(byte[] studentAsByteArray) {
        try {
            return parser.parsePath(studentAsByteArray);
        } catch (Exception e) {
            throw new AppException("Error while parsing XML file \n" + e.getMessage());
        }
    }
}
