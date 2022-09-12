package com.nagarro.studentapi.service;

import com.nagarro.studentapi.exception.AppException;
import com.nagarro.studentapi.persistance.model.Student;
import com.nagarro.studentapi.persistance.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;


@RequiredArgsConstructor
@Service
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;

    @Override
    public Student save(Student student) {
        Student returnedStudent = studentRepository.save(student);
        return returnedStudent;
    }

    @Override
    public Student find(String uuid) {
        return studentRepository.findStudentByUuid(uuid)
                .orElseThrow(() -> new AppException("Cannot find the user with the uuid: " + uuid));
    }

    @Override
    public void delete(String uuid) {
        studentRepository.delete(find(uuid));
    }

    @Override
    @Transactional
    public Student update(String uuid, Student updatedStudent) {
        Student student = find(uuid);
        student.setData(updatedStudent.getData());
        student.setValid(updatedStudent.isValid());
        return save(student);
    }
}
