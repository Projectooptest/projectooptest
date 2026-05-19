package com.example.Student_Course_Registration_System.repository;

import com.example.Student_Course_Registration_System.model.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, String> {
    List<Schedule> findByLecturerLecturerId(String lecturerId);
    List<Schedule> findByCourseCourseId(String courseId);
}