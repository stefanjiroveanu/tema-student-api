package com.nagarro.studentapi.facade.converters;

import com.nagarro.studentapi.controller.model.ExternalStudent;
import com.nagarro.studentapi.persistance.model.Student;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StudentConverter implements Converter<Student, ExternalStudent> {

    private final ModelMapper mapper;

    @Override
    public ExternalStudent toDto(Student model) {
        return mapper.map(model, ExternalStudent.class);
    }

    @Override
    public Student toModel(ExternalStudent dto) {
        return mapper.map(dto, Student.class);
    }
}
