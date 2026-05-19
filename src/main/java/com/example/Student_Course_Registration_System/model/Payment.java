package com.example.Student_Course_Registration_System.model;

import com.example.Student_Course_Registration_System.enums.PaymentMethod;
import com.example.Student_Course_Registration_System.enums.PaymentStatus;
import jakarta.persistence.*;

@Entity
@Table(name = "payments")
public class Payment {

    @Id
    private String paymentId;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    private double amount;
    private String paymentDate;

    @Enumerated(EnumType.STRING)
    private PaymentMethod method;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    private String receiptFileName;

    public Payment() {}

    public Payment(String paymentId, Student student, double amount, String paymentDate, PaymentMethod method, String receiptFileName) {
        this.paymentId = paymentId;
        this.student = student;
        this.amount = amount;
        this.paymentDate = paymentDate;
        this.method = method;
        this.status = PaymentStatus.PENDING;
        this.receiptFileName = receiptFileName;
    }

    public String getPaymentId() { return paymentId; }
    public Student getStudent() { return student; }
    public String getPaymentDate() { return paymentDate; }
    public PaymentMethod getMethod() { return method; }
    public PaymentStatus getStatus() { return status; }
    public String getReceiptFileName() { return receiptFileName; }
    public double getAmountForReceipt() { return amount; }

    public void processPayment() {
        if (status == PaymentStatus.PENDING) {
            this.status = PaymentStatus.COMPLETED;
        }
    }

    public void cancelPayment() {
        if (status == PaymentStatus.PENDING) {
            this.status = PaymentStatus.FAILED;
        }
    }

    public void saveReceipt(String fileName) {
        if (fileName != null && !fileName.isEmpty()) {
            this.receiptFileName = fileName;
        }
    }
}