package com.nagarro.studentapi.facade.parser;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nagarro.studentapi.exception.AppException;
import com.nagarro.studentapi.service.model.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class RequestBodyParser {

    private final ObjectMapper mapper;

    public Query parse(String json) {
        try {
            return mapper.readValue(json, Query.class);
        } catch (JsonProcessingException e) {
            throw new AppException(e.getMessage());
        }
    }
}
