package com.nagarro.studentapi.service.model;

import lombok.Data;

@Data
public class Address {

    private String street;
    private Integer number;
    private String city;
    private String country;
}
