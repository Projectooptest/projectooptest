package com.example.Student_Course_Registration_System.model;

import jakarta.persistence.*;

@Entity
@Table(name = "feedbacks")
public class Feedback {

    @Id
    private String feedbackId;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    private int rating;
    private String comment;
    private String reply;
    private String date;
    private boolean submitted;

    public Feedback() {}

    public Feedback(String feedbackId, Student student, Course course, int rating, String comment, String date) {
        this.feedbackId = feedbackId;
        this.student = student;
        this.course = course;
        this.rating = rating;
        this.comment = comment;
        this.reply = "";
        this.date = date;
        this.submitted = false;
    }

    public String getFeedbackId() { return feedbackId; }
    public Student getStudent() { return student; }
    public Course getCourse() { return course; }
    public int getRating() { return rating; }
    public String getComment() { return comment; }
    public String getReply() { return reply; }
    public String getDate() { return date; }
    public boolean isSubmitted() { return submitted; }

    public void setRating(int rating) {
        if (!submitted) { this.rating = rating; }
    }

    public void setComment(String comment) {
        if (!submitted) { this.comment = comment; }
    }

    public void setReply(String reply) { this.reply = reply; }

    public void submitFeedback() {
        if (!submitted) { this.submitted = true; }
    }
}