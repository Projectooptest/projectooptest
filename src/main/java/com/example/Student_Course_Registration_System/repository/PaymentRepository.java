package com.example.Student_Course_Registration_System.repository;

import com.example.Student_Course_Registration_System.enums.PaymentStatus;
import com.example.Student_Course_Registration_System.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, String> {
    List<Payment> findByStudentStudentId(String studentId);
    List<Payment> findByStatus(PaymentStatus status);
}