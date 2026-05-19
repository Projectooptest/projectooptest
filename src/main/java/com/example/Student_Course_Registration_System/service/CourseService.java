package com.example.Student_Course_Registration_System.service;

import com.example.Student_Course_Registration_System.model.Course;
import com.example.Student_Course_Registration_System.model.Schedule;
import com.example.Student_Course_Registration_System.repository.CourseRepository;
import com.example.Student_Course_Registration_System.repository.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private ScheduleRepository scheduleRepository;

    public String addCourse(Course course) {
        if (courseRepository.findById(course.getCourseId()).isPresent()) return "Course ID already exists";
        for (Course c : courseRepository.findAll()) {
            if (c.getCourseName().equalsIgnoreCase(course.getCourseName())) return "A course with this name already exists";
        }
        courseRepository.save(course);
        return null;
    }

    public List<Course> getAllCourses() { return courseRepository.findAll(); }
    public Course getCourseById(String courseId) { return courseRepository.findById(courseId).orElse(null); }

    public String updateCourse(Course course) {
        if (courseRepository.findById(course.getCourseId()).isEmpty()) return "Course not found";
        for (Course c : courseRepository.findAll()) {
            if (!c.getCourseId().equals(course.getCourseId())) {
                if (c.getCourseName().equalsIgnoreCase(course.getCourseName())) return "A course with this name already exists";
            }
        }
        courseRepository.save(course);
        return null;
    }

    public void deleteCourse(String courseId) {
        if (courseRepository.findById(courseId).isEmpty()) return;
        List<Schedule> schedules = scheduleRepository.findByCourseCourseId(courseId);
        for (Schedule schedule : schedules) {
            scheduleRepository.deleteById(schedule.getScheduleId());
        }
        courseRepository.deleteById(courseId);
    }

    public List<Course> getAvailableCourses() {
        List<Course> result = new ArrayList<>();
        for (Course c : courseRepository.findAll()) { if (c.isAvailable()) result.add(c); }
        return result;
    }

    public List<Course> searchByName(String name) {
        List<Course> result = new ArrayList<>();
        for (Course c : courseRepository.findAll()) {
            if (c.getCourseName().toLowerCase().contains(name.toLowerCase())) result.add(c);
        }
        return result;
    }

    public List<Course> searchByCategory(String categoryId) {
        List<Course> result = new ArrayList<>();
        for (Course c : courseRepository.findAll()) {
            if (c.getCategory() != null && c.getCategory().getCategoryId().equals(categoryId)) result.add(c);
        }
        return result;
    }

    public int getTotalCourses() { return (int) courseRepository.count(); }
}