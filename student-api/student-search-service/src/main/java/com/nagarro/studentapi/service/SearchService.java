package com.nagarro.studentapi.service;

import com.nagarro.studentapi.service.model.Query;
import com.nagarro.studentapi.service.model.StudentSolrBean;

import java.util.Map;

public interface SearchService {
    StudentSolrBean save(StudentSolrBean student);
    String find(Query query);

    void delete(String uuid);

    void update(String uuid, StudentSolrBean studentSolrBean);
}
