package com.nagarro.studentapi.facade.converters;

import com.nagarro.studentapi.integration.model.SolrStudent;
import com.nagarro.studentapi.persistance.model.Student;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StudentForQueueConverter implements Converter<Student, SolrStudent> {

    private final ModelMapper mapper;

    @Override
    public SolrStudent toDto(Student model) {
        return mapper.map(model, SolrStudent.class);
    }

    @Override
    public Student toModel(SolrStudent dto) {
        return mapper.map(dto, Student.class);
    }
}
