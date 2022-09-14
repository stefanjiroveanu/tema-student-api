package com.nagarro.studentapi.service;

import com.nagarro.studentapi.exception.AppException;
import com.nagarro.studentapi.service.model.Query;
import com.nagarro.studentapi.service.model.StudentSolrBean;
import lombok.RequiredArgsConstructor;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.Http2SolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SearchServiceImpl implements SearchService {

    private final Http2SolrClient solrClient;

    @Override
    public StudentSolrBean save(StudentSolrBean student) {
        try {
            if (student.getUuid() == null) {
                student.setUuid(UUID.randomUUID().toString());
            }
            solrClient.addBean(student);
            solrClient.commit();
            return student;
        } catch (IOException | SolrServerException e) {
            throw new AppException(e.getMessage());
        }
    }

    @Override
    public String find(Query query) {
        try {
            SolrQuery solrQuery = new SolrQuery();
            if (query.getQuery() != null) {
                solrQuery.setQuery(query.getQuery());
            } else {
                throw new AppException("Query is mandatory");
            }
            if (query.getFilter() != null) {
                solrQuery.setFields(query.getFields());
            }
            if (query.getOffset() != null) {
                solrQuery.setStart(Integer.parseInt(query.getOffset()));
            }
            if (query.getLimit() != null) {
                int rows = Integer.parseInt(query.getLimit());
                if (rows != 0) {
                    solrQuery.setRows(rows);
                }
            }
            String sort = query.getSort();
            if (sort != null && !sort.isEmpty()) {
                String[] s = sort.split(" ");
                solrQuery.setSort(s[0], SolrQuery.ORDER.valueOf(s[1]));
            }
            QueryResponse queryResponse = solrClient.query(solrQuery);
            return queryResponse.getResults().toString();
        } catch (SolrServerException | IOException e) {
            throw new AppException(e.getMessage());
        }
    }

    @Override
    public void delete(String uuid) {
        try {
            solrClient.deleteById(uuid);
            solrClient.commit();
        } catch (SolrServerException | IOException e) {
            throw new AppException(e.getMessage());
        }
    }

    @Override
    public void update(String uuid, StudentSolrBean studentSolrBean) {
        SolrInputDocument inputDocument = new SolrInputDocument();
        inputDocument.setField("id", uuid);


        setField(inputDocument, "cnp", studentSolrBean.getCnp());
        setField(inputDocument, "firstname", studentSolrBean.getFirstname());
        setField(inputDocument, "lastname", studentSolrBean.getLastname());
        setField(inputDocument, "address", studentSolrBean.getAddress().toString());
        setField(inputDocument, "grades", studentSolrBean.getGrades().toString());
        setField(inputDocument, "birthDate", studentSolrBean.getBirthDate().toString());
        try {
            solrClient.add(inputDocument);
            solrClient.commit();
        } catch (SolrServerException | IOException e) {
            throw new AppException(e.getMessage());
        }
    }

    private void setField(SolrInputDocument doc, String field, String updatedField) {
        Map<String, String> partialUpdate = new HashMap<>();
        partialUpdate.put("set", updatedField);
        doc.setField(field, partialUpdate);
    }
}
