package com.example.Student_Course_Registration_System.model;

public class Feedback {

    private String feedbackId;
    private Student student;
    private Course course;
    private int rating;
    private String comment;
    private String reply;
    private String date;
    private boolean submitted;

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

    // Getters
    public String getFeedbackId() {
        return feedbackId;
    }
    public Student getStudent() {
        return student;
    }
    public Course getCourse() {
        return course;
    }
    public int getRating() {
        return rating;
    }
    public String getComment() {
        return comment;
    }
    public String getReply() {
        return reply;
    }
    public String getDate() {
        return date;
    }
    public boolean isSubmitted() {
        return submitted;
    }

    // Setters only allowed before submission
    public void setRating(int rating) {
        if (!submitted) {
            this.rating = rating;
        } else {
            System.out.println("Cannot edit after submission");
        }
    }

    public void setComment(String comment) {
        if (!submitted) {
            this.comment = comment;
        } else {
            System.out.println("Cannot edit after submission");
        }
    }

    public void setReply(String reply) {
        this.reply = reply;
    }

    // Locks feedback after submission
    public void submitFeedback() {
        if (!submitted) {
            this.submitted = true;
            System.out.println("Feedback submitted successfully");
        } else {
            System.out.println("Feedback already submitted");
        }
    }
}