package com.example.Student_Course_Registration_System.service;

import com.example.Student_Course_Registration_System.enums.PaymentStatus;
import com.example.Student_Course_Registration_System.model.Payment;
import com.example.Student_Course_Registration_System.model.Student;
import com.example.Student_Course_Registration_System.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    private static final String UPLOAD_PATH = "uploads/payments/";

    public void addPayment(Student student, double amount, String method, MultipartFile receiptFile) {
        if (receiptFile == null || receiptFile.isEmpty()) return;
        String originalFileName = receiptFile.getOriginalFilename();
        if (originalFileName == null || !originalFileName.toLowerCase().endsWith(".pdf")) return;
        if (amount <= 0) return;
        String savedFileName = saveUploadedFile(receiptFile);
        if (savedFileName == null) return;
        String paymentId = "PAY" + System.currentTimeMillis();
        String date = LocalDate.now().toString();
        Payment payment = new Payment(paymentId, student, amount, date,
                com.example.Student_Course_Registration_System.enums.PaymentMethod.valueOf(method), savedFileName);
        paymentRepository.save(payment);
    }

    public List<Payment> getAllPayments() { return paymentRepository.findAll(); }
    public Payment getPaymentById(String paymentId) { return paymentRepository.findById(paymentId).orElse(null); }
    public List<Payment> getPaymentsByStudentId(String studentId) { return paymentRepository.findByStudentStudentId(studentId); }
    public List<Payment> getPendingPayments() { return paymentRepository.findByStatus(PaymentStatus.PENDING); }

    public void approvePayment(String paymentId) {
        Payment payment = paymentRepository.findById(paymentId).orElse(null);
        if (payment == null || payment.getStatus() != PaymentStatus.PENDING) return;
        payment.processPayment();
        paymentRepository.save(payment);
    }

    public void cancelPayment(String paymentId) {
        Payment payment = paymentRepository.findById(paymentId).orElse(null);
        if (payment == null || payment.getStatus() != PaymentStatus.PENDING) return;
        payment.cancelPayment();
        paymentRepository.save(payment);
    }

    public void deletePayment(String paymentId) {
        Payment payment = paymentRepository.findById(paymentId).orElse(null);
        if (payment == null) return;
        if (payment.getReceiptFileName() != null && !payment.getReceiptFileName().isEmpty()) {
            deleteUploadedFile(payment.getReceiptFileName());
        }
        paymentRepository.deleteById(paymentId);
    }

    public int getTotalPayments() { return (int) paymentRepository.count(); }

    public List<Payment> getCompletedPayments() {
        List<Payment> result = new ArrayList<>();
        for (Payment p : paymentRepository.findAll()) {
            if (p.getStatus() == PaymentStatus.COMPLETED) result.add(p);
        }
        return result;
    }

    private String saveUploadedFile(MultipartFile file) {
        try {
            File uploadDir = new File(UPLOAD_PATH).getAbsoluteFile();
            if (!uploadDir.exists()) uploadDir.mkdirs();
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            File destination = new File(uploadDir, fileName);
            file.transferTo(destination);
            return fileName;
        } catch (IOException e) { return null; }
    }

    private void deleteUploadedFile(String fileName) {
        File file = new File(UPLOAD_PATH + fileName);
        if (file.exists()) file.delete();
    }
}