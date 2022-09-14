package com.nagarro.studentapi.facade.converter;

import com.nagarro.studentapi.controller.model.Student;
import com.nagarro.studentapi.service.model.StudentSolrBean;
import com.nagarro.studentapi.utils.Converter;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StudentBeanConverter implements Converter<StudentSolrBean, Student> {

    private final ModelMapper mapper;

    @Override
    public StudentSolrBean toModel(Student dto) {
        return mapper.map(dto, StudentSolrBean.class);
    }

    @Override
    public Student toDto(StudentSolrBean model) {
        return mapper.map(model, Student.class);
    }
}
