package com.nagarro.studentapi.service;

import com.nagarro.studentapi.controller.model.ImportedStudent;
import com.nagarro.studentapi.integration.StudentSender;
import com.nagarro.studentapi.util.XmlParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

@Service
public class StudentImporterServiceImpl implements StudentImporterService {
    private final XmlParser parser;
    private final StudentSender studentSender;

    @Value("${student-api.xmlPath}")
    private String source;
    @Value("${student-api.archivedDestination}")
    private String archivedDestination;
    @Value("${student-api.errorDestination}")
    private String errorDestination;

    public StudentImporterServiceImpl(XmlParser parser, StudentSender studentSender) {
        this.parser = parser;
        this.studentSender = studentSender;
    }

    public ImportedStudent send(String path) {
        try {
            ImportedStudent student = parser.parse(path);
            studentSender.send(student);
            Files.move(Paths.get(source), Paths.get(archivedDestination), REPLACE_EXISTING);
            return student;
        } catch (Exception e) {
            File file = new File(source);
            file.renameTo(new File(errorDestination));
            file.delete();
            throw new RuntimeException(e);
        }
    }
}
