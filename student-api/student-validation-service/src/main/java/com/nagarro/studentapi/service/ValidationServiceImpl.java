package com.nagarro.studentapi.service;

import com.nagarro.studentapi.exception.AppException;
import com.nagarro.studentapi.integration.model.Student;
import com.nagarro.studentapi.persistance.model.ValidationEntry;
import com.nagarro.studentapi.persistance.repository.ValidationRepository;
import com.nagarro.studentapi.utils.ValidationUtils;
import lombok.RequiredArgsConstructor;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ValidationServiceImpl implements ValidationService {

    private static final String DETAILS = "Can't find the validation details for the student with the uuid: ";
    private final ValidationRepository validationRepository;

    private final KieContainer kieContainer;

    @Override
    public ValidationEntry find(String uuid) {
        return validationRepository.findByStudentUuid(uuid)
                .orElseThrow(() -> new AppException(DETAILS + uuid));
    }

    @Override
    public ValidationEntry validateStudent(Student student) {
        try {
            initializeKie(student);
        } catch (Exception e) {
            throw new AppException("Error validating the student" + e.getMessage());
        }
        Optional<ValidationEntry> studentByUuid = validationRepository.findByStudentUuid(student.getUuid());
        if (!studentByUuid.isPresent()) {
            ValidationEntry entry = new ValidationEntry(student.getUuid(), student.isValid());
            return validationRepository.save(entry);
        } else {
            return studentByUuid.get();
        }
    }

    private void initializeKie(Student student) {
        System.out.println(ValidationUtils.getAverageGrades(student));
        KieSession kieSession = kieContainer.newKieSession();
        kieSession.setGlobal("student", student);
        kieSession.insert(student);
        kieSession.fireAllRules();
        kieSession.dispose();
    }

}
