package com.example.Student_Course_Registration_System.repository;

import com.example.Student_Course_Registration_System.model.CourseCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseCategoryRepository extends JpaRepository<CourseCategory, String> {
}