package com.nagarro.studentapi.integration.model;

import com.nagarro.studentapi.integration.model.domain.HttpRequestEnum;
import lombok.Data;

@Data
public class SolrMessage {

    private final SolrStudent solrStudent;
    private final HttpRequestEnum httpRequestEnum;
}
