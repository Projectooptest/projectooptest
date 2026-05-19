package com.example.Student_Course_Registration_System.service;

import com.example.Student_Course_Registration_System.model.Course;
import com.example.Student_Course_Registration_System.model.Feedback;
import com.example.Student_Course_Registration_System.model.Student;
import com.example.Student_Course_Registration_System.repository.FeedbackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class FeedbackService {

    @Autowired
    private FeedbackRepository feedbackRepository;

    public void addFeedback(Student student, Course course, int rating, String comment) {
        List<Feedback> existing = feedbackRepository.findByStudentStudentId(student.getStudentId());
        for (Feedback f : existing) {
            if (f.getCourse().getCourseId().equals(course.getCourseId())) return;
        }
        if (rating < 1 || rating > 5) return;
        if (comment == null || comment.trim().isEmpty()) return;
        String feedbackId = "FBK" + System.currentTimeMillis();
        String date = LocalDate.now().toString();
        Feedback feedback = new Feedback(feedbackId, student, course, rating, comment, date);
        feedbackRepository.save(feedback);
    }

    public List<Feedback> getAllFeedbacks() { return feedbackRepository.findAll(); }
    public Feedback getFeedbackById(String feedbackId) { return feedbackRepository.findById(feedbackId).orElse(null); }
    public List<Feedback> getFeedbacksByStudentId(String studentId) { return feedbackRepository.findByStudentStudentId(studentId); }
    public List<Feedback> getFeedbacksByCourseId(String courseId) { return feedbackRepository.findByCourseCourseId(courseId); }

    public void updateFeedback(String feedbackId, int rating, String comment) {
        Feedback feedback = feedbackRepository.findById(feedbackId).orElse(null);
        if (feedback == null || feedback.isSubmitted()) return;
        if (rating < 1 || rating > 5) return;
        if (comment == null || comment.trim().isEmpty()) return;
        feedback.setRating(rating);
        feedback.setComment(comment);
        feedbackRepository.save(feedback);
    }

    public void submitFeedback(String feedbackId) {
        Feedback feedback = feedbackRepository.findById(feedbackId).orElse(null);
        if (feedback == null || feedback.isSubmitted()) return;
        feedback.submitFeedback();
        feedbackRepository.save(feedback);
    }

    public void replyFeedback(String feedbackId, String reply) {
        Feedback feedback = feedbackRepository.findById(feedbackId).orElse(null);
        if (feedback == null || reply == null || reply.trim().isEmpty()) return;
        feedback.setReply(reply);
        feedbackRepository.save(feedback);
    }

    public void deleteFeedback(String feedbackId) {
        if (feedbackRepository.findById(feedbackId).isEmpty()) return;
        feedbackRepository.deleteById(feedbackId);
    }

    public double getAverageRating(String courseId) {
        List<Feedback> feedbacks = feedbackRepository.findByCourseCourseId(courseId);
        if (feedbacks.isEmpty()) return 0.0;
        int total = 0;
        for (Feedback f : feedbacks) total += f.getRating();
        return (double) total / feedbacks.size();
    }

    public int getTotalFeedbacks() { return (int) feedbackRepository.count(); }
}