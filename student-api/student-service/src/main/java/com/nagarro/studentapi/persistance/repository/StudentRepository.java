package com.nagarro.studentapi.persistance.repository;

import com.nagarro.studentapi.persistance.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    @Query("SELECT s FROM Student s WHERE s.uuid = ?1")
    Optional<Student> findStudentByUuid(String uuid);
}
