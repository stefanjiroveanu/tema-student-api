package com.nagarro.studentapi.controller.model;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class ExternalStudent {
    private String firstname;
    private String lastname;
    private String cnp;
    private LocalDate birthDate;
    private Address address;
    private List<Grade> grades;
    private boolean valid;
}
