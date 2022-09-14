package com.nagarro.studentapi.service.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.solr.client.solrj.beans.Field;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@Getter
@NoArgsConstructor
public class StudentSolrBean {
    private String uuid = UUID.randomUUID().toString();
    private String firstname;
    private String lastname;
    private String cnp;
    private AddressSolrBean address;
    private LocalDate birthDate;
    private List<GradeSolrBean> grades;
    private boolean valid;

    @Field("id")
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
    @Field("firstname")
    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    @Field("lastname")
    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    @Field("cnp")
    public void setCnp(String cnp) {
        this.cnp = cnp;
    }

    @Field("address")
    public void setAddress(AddressSolrBean address) {
        this.address = address;
    }

    @Field("grades")
    public void setGrades(List<GradeSolrBean> grades) {
        this.grades = grades;
    }

    @Field("isValid")
    public void setValid(boolean valid) {
        this.valid = valid;
    }

    @Field("birthDate")
    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public boolean getValid() {
        return valid;
    }
}
