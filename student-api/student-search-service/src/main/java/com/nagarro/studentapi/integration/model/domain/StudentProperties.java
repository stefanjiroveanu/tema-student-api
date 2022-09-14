package com.nagarro.studentapi.integration.model.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.nagarro.studentapi.controller.model.Address;
import com.nagarro.studentapi.controller.model.Grade;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class StudentProperties {

    private String firstname;
    private String lastname;
    private String cnp;
    private Address address;
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate birthDate;
    private List<Grade> grades;
}
