package com.nagarro.studentapi.integration.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.nagarro.studentapi.integration.deserializer.StudentDeserializer;
import com.nagarro.studentapi.integration.model.domain.Address;
import com.nagarro.studentapi.integration.model.domain.Grade;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@JsonDeserialize(using = StudentDeserializer.class)
public class Student {
    private String uuid;
    private String firstname;
    private String lastname;
    private String cnp;
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate birthDate;
    private Address address;
    private List<Grade> grades;
    private boolean valid;
}
