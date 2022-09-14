package com.nagarro.studentapi.facade;

import com.nagarro.studentapi.controller.model.Student;
import com.nagarro.studentapi.facade.converter.StudentBeanConverter;
import com.nagarro.studentapi.service.SearchService;
import com.nagarro.studentapi.service.model.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SearchFacade {

    private final StudentBeanConverter converter;

    private final SearchService service;

    public String find(Query query) {
        return service.find(query);
    }

    public Student save(Student student) {
        return converter.toDto(service.save(converter.toModel(student)));
    }

    public void update(String uuid, Student student) {
        service.update(uuid, converter.toModel(student));
    }

    public void delete(String uuid) {
        service.delete(uuid);
    }
}
