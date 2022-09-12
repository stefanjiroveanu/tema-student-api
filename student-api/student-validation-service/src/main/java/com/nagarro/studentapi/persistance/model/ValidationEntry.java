package com.nagarro.studentapi.persistance.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "validation")
public class ValidationEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "studentuuid")
    private String studentUuid;
    @Column(name = "isvalid")
    private boolean isValid;

    public ValidationEntry(String studentUuid, boolean isValid) {
        this.studentUuid = studentUuid;
        this.isValid = isValid;
    }
}
