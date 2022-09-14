package com.nagarro.studentapi.integration.converter;

import com.nagarro.studentapi.controller.model.Student;
import com.nagarro.studentapi.integration.model.SolrStudent;
import com.nagarro.studentapi.utils.Converter;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class SolrStudentConverter implements Converter<Student, SolrStudent> {

    private final ModelMapper modelMapper;

    @Override
    public Student toModel(SolrStudent dto) {
        return modelMapper.map(dto, Student.class);
    }

    @Override
    public SolrStudent toDto(Student model) {
        return modelMapper.map(model, SolrStudent.class);
    }
}
