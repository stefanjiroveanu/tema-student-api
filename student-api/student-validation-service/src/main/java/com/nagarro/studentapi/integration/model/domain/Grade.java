package com.nagarro.studentapi.integration.model.domain;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

@Data
public class Grade implements Serializable {

    private String discipline;
    private LocalDate date;
    private Integer grade;
}
