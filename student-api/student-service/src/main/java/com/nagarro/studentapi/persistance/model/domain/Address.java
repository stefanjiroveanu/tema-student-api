package com.nagarro.studentapi.persistance.model.domain;

import lombok.Data;

import java.io.Serializable;

@Data
public class Address implements Serializable {

    private String street;
    private Integer number;
    private String city;
    private String country;
}
