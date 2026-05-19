package com.example.Student_Course_Registration_System.service;

import com.example.Student_Course_Registration_System.model.CourseMaterial;
import com.example.Student_Course_Registration_System.repository.CourseMaterialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
public class CourseMaterialService {

    @Autowired
    private CourseMaterialRepository courseMaterialRepository;

    private static final String UPLOAD_PATH = "uploads/materials/";

    public void addCourseMaterial(CourseMaterial material, MultipartFile file) {
        if (courseMaterialRepository.findById(material.getMaterialId()).isPresent()) return;
        if (file == null || file.isEmpty()) return;
        String originalFileName = file.getOriginalFilename();
        if (!isValidFileType(originalFileName)) return;
        String savedFileName = saveUploadedFile(file);
        if (savedFileName == null) return;
        material.saveFile(savedFileName);
        courseMaterialRepository.save(material);
    }

    public List<CourseMaterial> getAllMaterials() { return courseMaterialRepository.findAll(); }
    public CourseMaterial getMaterialById(String materialId) { return courseMaterialRepository.findById(materialId).orElse(null); }
    public List<CourseMaterial> getMaterialsByCourseId(String courseId) { return courseMaterialRepository.findByCourseId(courseId); }

    public void updateCourseMaterial(CourseMaterial material) {
        if (courseMaterialRepository.findById(material.getMaterialId()).isEmpty()) return;
        courseMaterialRepository.save(material);
    }

    public void deleteCourseMaterial(String materialId) {
        CourseMaterial existing = courseMaterialRepository.findById(materialId).orElse(null);
        if (existing == null) return;
        if (existing.getFileName() != null && !existing.getFileName().isEmpty()) {
            deleteUploadedFile(existing.getFileName());
        }
        courseMaterialRepository.deleteById(materialId);
    }

    public int getTotalMaterials() { return (int) courseMaterialRepository.count(); }

    private String saveUploadedFile(MultipartFile file) {
        try {
            File uploadDir = new File(UPLOAD_PATH).getAbsoluteFile();
            if (!uploadDir.exists()) uploadDir.mkdirs();
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            File destination = new File(uploadDir, fileName);
            file.transferTo(destination);
            return fileName;
        } catch (IOException e) {
            return null;
        }
    }

    private void deleteUploadedFile(String fileName) {
        File file = new File(UPLOAD_PATH + fileName);
        if (file.exists()) file.delete();
    }

    private boolean isValidFileType(String fileName) {
        if (fileName == null) return false;
        String lower = fileName.toLowerCase();
        return lower.endsWith(".pdf") || lower.endsWith(".pptx") || lower.endsWith(".ppt") ||
                lower.endsWith(".doc") || lower.endsWith(".docx") || lower.endsWith(".png") ||
                lower.endsWith(".jpg") || lower.endsWith(".jpeg");
    }
}