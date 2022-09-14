package com.nagarro.studentapi.integration.model;

import lombok.Data;

@Data
public class ValidationEntry {
    private String studentUuid;
    private boolean valid;
}
