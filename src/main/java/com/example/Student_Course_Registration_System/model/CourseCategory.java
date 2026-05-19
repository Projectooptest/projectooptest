package com.example.Student_Course_Registration_System.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "course_categories")
public class CourseCategory {

    @Id
    private String categoryId;
    private String categoryName;

    // JPA requires a no-arg constructor
    public CourseCategory() {}

    // Composition - CourseCategory is owned by Course
    public CourseCategory(String categoryId, String categoryName) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
    }

    // Getters
    public String getCategoryId() { return categoryId; }
    public String getCategoryName() { return categoryName; }

    // Setter only for changeable field
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }
}