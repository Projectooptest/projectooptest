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
public class AdminService {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private LecturerRepository lecturerRepository;

    // Generate admin ID
    public String generateAdminId() {
        List<Admin> admins = adminRepository.findAll();
        int max = 0;
        for (Admin a : admins) {
            try {
                int num = Integer.parseInt(a.getAdminId().replace("A", ""));
                if (num > max) max = num;
            } catch (NumberFormatException ignored) {}
        }
        return String.format("A%03d", max + 1);
    }

    // Add new admin - returns error message or null on success
    public String addAdmin(Admin admin) {
        if (adminRepository.findById(admin.getAdminId()).isPresent()) {
            return "Admin ID already exists";
        }
        List<Admin> admins = adminRepository.findAll();
        for (Admin a : admins) {
            if (a.getEmail().equalsIgnoreCase(admin.getEmail())) {
                return "An admin with this email already exists";
            }
            if (a.getPhone().equals(admin.getPhone())) {
                return "An admin with this phone number already exists";
            }
        }
        String crossError = checkCrossEntityEmail(admin.getEmail());
        if (crossError != null) return crossError;
        adminRepository.save(admin);
        return null;
    }

    public List<Admin> getAllAdmins() { return adminRepository.findAll(); }

    public Admin getAdminById(String adminId) {
        return adminRepository.findById(adminId).orElse(null);
    }

    // Update admin - returns error message or null on success
    public String updateAdmin(Admin admin) {
        if (adminRepository.findById(admin.getAdminId()).isEmpty()) {
            return "Admin not found";
        }
        List<Admin> admins = adminRepository.findAll();
        for (Admin a : admins) {
            if (!a.getAdminId().equals(admin.getAdminId())) {
                if (a.getEmail().equalsIgnoreCase(admin.getEmail())) {
                    return "An admin with this email already exists";
                }
                if (a.getPhone().equals(admin.getPhone())) {
                    return "An admin with this phone number already exists";
                }
            }
        }
        String crossError = checkCrossEntityEmail(admin.getEmail());
        if (crossError != null) return crossError;
        adminRepository.save(admin);
        return null;
    }

    public void deleteAdmin(String adminId) {
        if (adminRepository.findById(adminId).isEmpty()) return;
        if (adminRepository.findAll().size() == 1) return;
        adminRepository.deleteById(adminId);
    }

    public Admin login(String email, String password) {
        List<Admin> admins = adminRepository.findAll();
        for (Admin admin : admins) {
            if (admin.getEmail().equals(email) && admin.getPassword().equals(password)) {
                return admin;
            }
        }
        return null;
    }

    public List<Admin> searchByName(String name) {
        List<Admin> admins = adminRepository.findAll();
        List<Admin> result = new ArrayList<>();
        for (Admin admin : admins) {
            if (admin.getName().toLowerCase().contains(name.toLowerCase())) {
                result.add(admin);
            }
        }
        return result;
    }

    public int getTotalAdmins() { return (int) adminRepository.count(); }

    private String checkCrossEntityEmail(String email) {
        for (Student s : studentRepository.findAll()) {
            if (s.getEmail().equalsIgnoreCase(email)) return "This email is already used by a student";
        }
        for (Lecturer l : lecturerRepository.findAll()) {
            if (l.getEmail().equalsIgnoreCase(email)) return "This email is already used by a lecturer";
        }
        return null;
    }
}