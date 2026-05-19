package com.example.Student_Course_Registration_System.service;

import com.example.Student_Course_Registration_System.enums.RegistrationStatus;
import com.example.Student_Course_Registration_System.model.Course;
import com.example.Student_Course_Registration_System.model.Registration;
import com.example.Student_Course_Registration_System.model.Student;
import com.example.Student_Course_Registration_System.repository.CourseRepository;
import com.example.Student_Course_Registration_System.repository.RegistrationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class RegistrationService {

    @Autowired
    private RegistrationRepository registrationRepository;
    @Autowired
    private CourseRepository courseRepository;

    public void addRegistration(Student student, Course course) {
        List<Registration> existing = registrationRepository.findByStudentStudentId(student.getStudentId());
        for (Registration r : existing) {
            if (r.getCourse().getCourseId().equals(course.getCourseId())) return;
        }
        if (!course.isAvailable()) return;
        String registrationId = "REG" + System.currentTimeMillis();
        String date = LocalDate.now().toString();
        Registration registration = new Registration(registrationId, student, course, date);
        registrationRepository.save(registration);
    }

    public List<Registration> getAllRegistrations() { return registrationRepository.findAll(); }
    public Registration getRegistrationById(String registrationId) { return registrationRepository.findById(registrationId).orElse(null); }
    public List<Registration> getRegistrationsByStudentId(String studentId) { return registrationRepository.findByStudentStudentId(studentId); }

    public void approveRegistration(String registrationId) {
        Registration registration = registrationRepository.findById(registrationId).orElse(null);
        if (registration == null || registration.getStatus() != RegistrationStatus.PENDING) return;
        registration.approve();
        registrationRepository.save(registration);
        courseRepository.save(registration.getCourse());
    }

    public void rejectRegistration(String registrationId) {
        Registration registration = registrationRepository.findById(registrationId).orElse(null);
        if (registration == null || registration.getStatus() != RegistrationStatus.PENDING) return;
        registration.reject();
        registrationRepository.save(registration);
    }

    public void cancelRegistration(String registrationId) {
        Registration registration = registrationRepository.findById(registrationId).orElse(null);
        if (registration == null) return;
        registration.cancel();
        registrationRepository.save(registration);
        courseRepository.save(registration.getCourse());
    }

    public void deleteRegistration(String registrationId) {
        if (registrationRepository.findById(registrationId).isEmpty()) return;
        registrationRepository.deleteById(registrationId);
    }

    public List<Registration> getPendingRegistrations() {
        List<Registration> result = new ArrayList<>();
        for (Registration r : registrationRepository.findAll()) {
            if (r.getStatus() == RegistrationStatus.PENDING) result.add(r);
        }
        return result;
    }

    public int getTotalRegistrations() { return (int) registrationRepository.count(); }
}