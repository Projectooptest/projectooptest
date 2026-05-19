package com.example.Student_Course_Registration_System.repository;

import com.example.Student_Course_Registration_System.model.CourseMaterial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseMaterialRepository extends JpaRepository<CourseMaterial, String> {
    List<CourseMaterial> findByCourseId(String courseId);
}