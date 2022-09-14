package com.nagarro.studentapi.service.model;
import lombok.Data;

@Data
public class Query {

    private String query;
    private String filter;
    private String offset;
    private String limit;
    private String fields;
    private String sort;
}
