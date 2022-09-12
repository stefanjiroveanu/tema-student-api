package com.nagarro.studentapi.persistance.model;

import com.nagarro.studentapi.persistance.converter.JpaContentConvertorJson;
import com.nagarro.studentapi.persistance.model.domain.StudentProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import java.util.UUID;

@Data
@NoArgsConstructor
@Entity
@Table(name = "student")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;
    @Column(name = "uuid")
    private String uuid;
    @Convert(converter = JpaContentConvertorJson.class)
    @Column(columnDefinition = "jsonb", name = "data")
    private StudentProperties data;
    @Column(name = "isvalid")
    private boolean valid;

    @PrePersist
    public void prePersist() {
        if (this.uuid == null) {
            this.uuid = UUID.randomUUID().toString();
        }
    }
}
