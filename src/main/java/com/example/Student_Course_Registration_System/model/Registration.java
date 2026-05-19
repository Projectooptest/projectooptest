package com.example.Student_Course_Registration_System.model;

import com.example.Student_Course_Registration_System.enums.RegistrationStatus;
import jakarta.persistence.*;

@Entity
@Table(name = "registrations")
public class Registration {

    @Id
    private String registrationId;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    private String registrationDate;

    @Enumerated(EnumType.STRING)
    private RegistrationStatus status;

    // JPA requires a no-arg constructor
    public Registration() {}

    public Registration(String registrationId, Student student, Course course, String registrationDate) {
        this.registrationId = registrationId;
        this.student = student;
        this.course = course;
        this.registrationDate = registrationDate;
        this.status = RegistrationStatus.PENDING;
    }

    // to showing html as
    public String getRegistrationId() { return registrationId; }
    public Student getStudent() { return student; }
    public Course getCourse() { return course; }
    public String getRegistrationDate() { return registrationDate; }
    public RegistrationStatus getStatus() { return status; }

    // Status changing to approve method
    public void approve() {
        if (status == RegistrationStatus.PENDING) {
            this.status = RegistrationStatus.APPROVED;
            course.addEnrolledStudent();
            System.out.println("Registration approved for " + student.getName());
        } else {
            System.out.println("Registration is not in pending state");
        }
    }

    //same as approve method for reject
    public void reject() {
        if (status == RegistrationStatus.PENDING) {
            this.status = RegistrationStatus.REJECTED;
            System.out.println("Registration rejected for " + student.getName());
        } else {
            System.out.println("Registration is not in pending state");
        }
    }

    public void cancel() {
        if (status == RegistrationStatus.APPROVED) {
            this.status = RegistrationStatus.REJECTED;
            course.removeEnrolledStudent();
            System.out.println("Registration cancelled for " + student.getName());
        } else {
            System.out.println("Registration cannot be cancelled");
        }
    }
}