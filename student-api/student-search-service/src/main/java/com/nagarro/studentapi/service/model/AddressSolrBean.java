package com.nagarro.studentapi.service.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class AddressSolrBean implements Serializable {

    private String street;
    private Integer number;
    private String city;
    private String country;
}
