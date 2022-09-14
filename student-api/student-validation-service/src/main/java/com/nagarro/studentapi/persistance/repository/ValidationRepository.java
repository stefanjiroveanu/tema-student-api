package com.nagarro.studentapi.persistance.repository;

import com.nagarro.studentapi.persistance.model.ValidationEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ValidationRepository extends JpaRepository<ValidationEntry, Long> {
    Optional<ValidationEntry> findByStudentUuid(String uuid);
}
