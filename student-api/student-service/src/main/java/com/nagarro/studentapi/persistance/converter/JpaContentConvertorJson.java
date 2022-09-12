package com.nagarro.studentapi.persistance.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nagarro.studentapi.exception.AppException;
import com.nagarro.studentapi.persistance.model.domain.StudentProperties;
import lombok.RequiredArgsConstructor;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
@RequiredArgsConstructor
public class JpaContentConvertorJson implements AttributeConverter<StudentProperties, String> {

    private final ObjectMapper objectMapper;

    @Override
    public String convertToDatabaseColumn(StudentProperties studentProperties) {
        try {
            return objectMapper.writeValueAsString(studentProperties);
        } catch (JsonProcessingException e) {
            throw new AppException(e.getMessage());
        }
    }

    @Override
    public StudentProperties convertToEntityAttribute(String s) {
        try {
            return objectMapper.readValue(s, StudentProperties.class);
        } catch (JsonProcessingException e) {
            throw new AppException(e.getMessage());
        }
    }
}
