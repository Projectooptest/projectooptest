package com.example.Student_Course_Registration_System.repository;

import com.example.Student_Course_Registration_System.model.Lecturer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LecturerRepository extends JpaRepository<Lecturer, String> {
}