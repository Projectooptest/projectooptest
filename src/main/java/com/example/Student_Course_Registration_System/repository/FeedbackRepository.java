package com.example.Student_Course_Registration_System.repository;

import com.example.Student_Course_Registration_System.model.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, String> {
    List<Feedback> findByStudentStudentId(String studentId);
    List<Feedback> findByCourseCourseId(String courseId);
}