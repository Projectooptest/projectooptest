package com.example.Student_Course_Registration_System.service;

import com.example.Student_Course_Registration_System.model.Admin;
import com.example.Student_Course_Registration_System.model.Lecturer;
import com.example.Student_Course_Registration_System.model.Student;
import com.example.Student_Course_Registration_System.repository.AdminRepository;
import com.example.Student_Course_Registration_System.repository.LecturerRepository;
import com.example.Student_Course_Registration_System.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private LecturerRepository lecturerRepository;
    @Autowired
    private AdminRepository adminRepository;

    public String generateStudentId() {
        List<Student> students = studentRepository.findAll();
        int max = 0;
        for (Student s : students) {
            try {
                int num = Integer.parseInt(s.getStudentId().replace("S", ""));
                if (num > max) max = num;
            } catch (NumberFormatException ignored) {}
        }
        return String.format("S%03d", max + 1);
    }

    public String addStudent(Student student) {
        if (studentRepository.findById(student.getStudentId()).isPresent()) return "Student ID already exists";
        for (Student s : studentRepository.findAll()) {
            if (s.getEmail().equalsIgnoreCase(student.getEmail())) return "A student with this email already exists";
            if (s.getPhone().equals(student.getPhone())) return "A student with this phone number already exists";
        }
        String crossError = checkCrossEntityEmail(student.getEmail(), null);
        if (crossError != null) return crossError;
        studentRepository.save(student);
        return null;
    }

    public List<Student> getAllStudents() { return studentRepository.findAll(); }
    public Student getStudentById(String studentId) { return studentRepository.findById(studentId).orElse(null); }

    public String updateStudent(Student student) {
        if (studentRepository.findById(student.getStudentId()).isEmpty()) return "Student not found";
        for (Student s : studentRepository.findAll()) {
            if (!s.getStudentId().equals(student.getStudentId())) {
                if (s.getEmail().equalsIgnoreCase(student.getEmail())) return "A student with this email already exists";
                if (s.getPhone().equals(student.getPhone())) return "A student with this phone number already exists";
            }
        }
        String crossError = checkCrossEntityEmail(student.getEmail(), null);
        if (crossError != null) return crossError;
        studentRepository.save(student);
        return null;
    }

    public void deleteStudent(String studentId) {
        if (studentRepository.findById(studentId).isEmpty()) return;
        studentRepository.deleteById(studentId);
    }

    public List<Student> searchByName(String name) {
        List<Student> result = new ArrayList<>();
        for (Student s : studentRepository.findAll()) {
            if (s.getName().toLowerCase().contains(name.toLowerCase())) result.add(s);
        }
        return result;
    }

    public Student login(String email, String password) {
        for (Student s : studentRepository.findAll()) {
            if (s.getEmail().equals(email) && s.getPassword().equals(password)) return s;
        }
        return null;
    }

    public int getTotalStudents() { return (int) studentRepository.count(); }

    private String checkCrossEntityEmail(String email, String excludeStudentId) {
        for (Lecturer l : lecturerRepository.findAll()) {
            if (l.getEmail().equalsIgnoreCase(email)) return "This email is already used by a lecturer";
        }
        for (Admin a : adminRepository.findAll()) {
            if (a.getEmail().equalsIgnoreCase(email)) return "This email is already used by an admin";
        }
        return null;
    }
}