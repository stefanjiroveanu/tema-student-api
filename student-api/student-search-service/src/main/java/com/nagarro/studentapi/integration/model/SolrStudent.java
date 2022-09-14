package com.nagarro.studentapi.integration.model;

import com.nagarro.studentapi.integration.model.domain.StudentProperties;
import lombok.Data;

@Data
public class SolrStudent {

    private final String uuid;
    private final StudentProperties data;
    private final boolean valid;

    public StudentProperties getData() {
        return data;
    }
}
