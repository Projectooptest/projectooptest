package com.example.Student_Course_Registration_System.service;

import com.example.Student_Course_Registration_System.model.Admin;
import com.example.Student_Course_Registration_System.model.Lecturer;
import com.example.Student_Course_Registration_System.model.Schedule;
import com.example.Student_Course_Registration_System.model.Student;
import com.example.Student_Course_Registration_System.repository.AdminRepository;
import com.example.Student_Course_Registration_System.repository.LecturerRepository;
import com.example.Student_Course_Registration_System.repository.ScheduleRepository;
import com.example.Student_Course_Registration_System.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class LecturerService {

    @Autowired
    private LecturerRepository lecturerRepository;
    @Autowired
    private ScheduleRepository scheduleRepository;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private AdminRepository adminRepository;

    public String generateLecturerId() {
        List<Lecturer> lecturers = lecturerRepository.findAll();
        int max = 0;
        for (Lecturer l : lecturers) {
            try {
                int num = Integer.parseInt(l.getLecturerId().replace("L", ""));
                if (num > max) max = num;
            } catch (NumberFormatException ignored) {}
        }
        return String.format("L%03d", max + 1);
    }

    public String addLecturer(Lecturer lecturer) {
        if (lecturerRepository.findById(lecturer.getLecturerId()).isPresent()) return "Lecturer ID already exists";
        for (Lecturer l : lecturerRepository.findAll()) {
            if (l.getEmail().equalsIgnoreCase(lecturer.getEmail())) return "A lecturer with this email already exists";
            if (l.getPhone().equals(lecturer.getPhone())) return "A lecturer with this phone number already exists";
        }
        String crossError = checkCrossEntityEmail(lecturer.getEmail());
        if (crossError != null) return crossError;
        lecturerRepository.save(lecturer);
        return null;
    }

    public List<Lecturer> getAllLecturers() { return lecturerRepository.findAll(); }
    public Lecturer getLecturerById(String lecturerId) { return lecturerRepository.findById(lecturerId).orElse(null); }

    public String updateLecturer(Lecturer lecturer) {
        if (lecturerRepository.findById(lecturer.getLecturerId()).isEmpty()) return "Lecturer not found";
        for (Lecturer l : lecturerRepository.findAll()) {
            if (!l.getLecturerId().equals(lecturer.getLecturerId())) {
                if (l.getEmail().equalsIgnoreCase(lecturer.getEmail())) return "A lecturer with this email already exists";
                if (l.getPhone().equals(lecturer.getPhone())) return "A lecturer with this phone number already exists";
            }
        }
        String crossError = checkCrossEntityEmail(lecturer.getEmail());
        if (crossError != null) return crossError;
        lecturerRepository.save(lecturer);
        return null;
    }

    public void deleteLecturer(String lecturerId) {
        if (lecturerRepository.findById(lecturerId).isEmpty()) return;
        List<Schedule> schedules = scheduleRepository.findByLecturerLecturerId(lecturerId);
        for (Schedule schedule : schedules) {
            scheduleRepository.deleteById(schedule.getScheduleId());
        }
        lecturerRepository.deleteById(lecturerId);
    }

    public List<Lecturer> searchByName(String name) {
        List<Lecturer> result = new ArrayList<>();
        for (Lecturer l : lecturerRepository.findAll()) {
            if (l.getName().toLowerCase().contains(name.toLowerCase())) result.add(l);
        }
        return result;
    }

    public List<Lecturer> searchByDepartment(String department) {
        List<Lecturer> result = new ArrayList<>();
        for (Lecturer l : lecturerRepository.findAll()) {
            if (l.getDepartment().toLowerCase().contains(department.toLowerCase())) result.add(l);
        }
        return result;
    }

    public Lecturer login(String email, String password) {
        for (Lecturer l : lecturerRepository.findAll()) {
            if (l.getEmail().equals(email) && l.getPassword().equals(password)) return l;
        }
        return null;
    }

    public int getTotalLecturers() { return (int) lecturerRepository.count(); }

    private String checkCrossEntityEmail(String email) {
        for (Student s : studentRepository.findAll()) {
            if (s.getEmail().equalsIgnoreCase(email)) return "This email is already used by a student";
        }
        for (Admin a : adminRepository.findAll()) {
            if (a.getEmail().equalsIgnoreCase(email)) return "This email is already used by an admin";
        }
        return null;
    }
}