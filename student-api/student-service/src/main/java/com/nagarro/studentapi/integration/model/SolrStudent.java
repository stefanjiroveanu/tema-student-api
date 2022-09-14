package com.nagarro.studentapi.integration.model;

import com.nagarro.studentapi.integration.model.domain.StudentProperties;
import lombok.Data;

@Data
public class SolrStudent {

    private String uuid;
    private StudentProperties data;
    private boolean valid;
}
