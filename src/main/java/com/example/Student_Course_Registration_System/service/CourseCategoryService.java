package com.example.Student_Course_Registration_System.service;

import com.example.Student_Course_Registration_System.model.CourseCategory;
import com.example.Student_Course_Registration_System.repository.CourseCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CourseCategoryService {

    @Autowired
    private CourseCategoryRepository categoryRepository;

    public String addCategory(CourseCategory category) {
        if (categoryRepository.findById(category.getCategoryId()).isPresent()) return "Category ID already exists";
        for (CourseCategory c : categoryRepository.findAll()) {
            if (c.getCategoryName().equalsIgnoreCase(category.getCategoryName())) return "A category with this name already exists";
        }
        categoryRepository.save(category);
        return null;
    }

    public List<CourseCategory> getAllCategories() { return categoryRepository.findAll(); }
    public CourseCategory getCategoryById(String categoryId) { return categoryRepository.findById(categoryId).orElse(null); }

    public String updateCategory(CourseCategory category) {
        if (categoryRepository.findById(category.getCategoryId()).isEmpty()) return "Category not found";
        for (CourseCategory c : categoryRepository.findAll()) {
            if (!c.getCategoryId().equals(category.getCategoryId())) {
                if (c.getCategoryName().equalsIgnoreCase(category.getCategoryName())) return "A category with this name already exists";
            }
        }
        categoryRepository.save(category);
        return null;
    }

    public void deleteCategory(String categoryId) {
        if (categoryRepository.findById(categoryId).isEmpty()) return;
        categoryRepository.deleteById(categoryId);
    }

    public List<CourseCategory> searchByName(String name) {
        List<CourseCategory> result = new ArrayList<>();
        for (CourseCategory c : categoryRepository.findAll()) {
            if (c.getCategoryName().toLowerCase().contains(name.toLowerCase())) result.add(c);
        }
        return result;
    }

    public int getTotalCategories() { return (int) categoryRepository.count(); }
}