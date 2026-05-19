package com.example.Student_Course_Registration_System.repository;

import com.example.Student_Course_Registration_System.model.Registration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RegistrationRepository extends JpaRepository<Registration, String> {
    List<Registration> findByStudentStudentId(String studentId);
}