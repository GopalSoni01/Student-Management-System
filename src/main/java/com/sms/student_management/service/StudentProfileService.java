package com.sms.student_management.service;
import org.springframework.web.multipart.MultipartFile;
import java.nio.file.*;
import java.io.IOException;
import java.util.UUID;

import com.sms.student_management.Student;
import com.sms.student_management.dto.StudentProfileRequestDTO;
import com.sms.student_management.entity.StudentProfile;
import com.sms.student_management.exception.StudentNotFoundException;
import com.sms.student_management.repository.StudentProfileRepository;
import com.sms.student_management.repository.StudentRepository;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class StudentProfileService {

    private final StudentRepository studentRepository;
    private final StudentProfileRepository profileRepository;

    public StudentProfileService(
            StudentRepository studentRepository,
            StudentProfileRepository profileRepository) {
        this.studentRepository = studentRepository;
        this.profileRepository = profileRepository;
    }

    // CREATE or UPDATE own profile
    public StudentProfile saveOrUpdateProfile(StudentProfileRequestDTO dto) {

        Authentication auth =
                SecurityContextHolder.getContext().getAuthentication();

        String email = auth.getName();

        Student student = studentRepository.findByEmail(email)
                .orElseThrow(() ->
                        new StudentNotFoundException("Student not found"));

        StudentProfile profile = profileRepository
                .findByStudentId(student.getId())
                .orElse(new StudentProfile());

        profile.setStudent(student);
        profile.setDateOfBirth(dto.getDateOfBirth());
        profile.setAddress(dto.getAddress());
        profile.setPincode(dto.getPincode());
        profile.setFatherName(dto.getFatherName());
        profile.setFatherOccupation(dto.getFatherOccupation());
        profile.setBankAccountNumber(dto.getBankAccountNumber());
        profile.setIfscCode(dto.getIfscCode());
        profile.setCourseName(dto.getCourseName());
        profile.setProfileImageUrl(dto.getProfileImageUrl());

        return profileRepository.save(profile);
    }

    // VIEW own profile
    public StudentProfile getMyProfile() {

        Authentication auth =
                SecurityContextHolder.getContext().getAuthentication();

        String email = auth.getName();

        Student student = studentRepository.findByEmail(email)
                .orElseThrow(() ->
                        new StudentNotFoundException("Student not found"));

        return profileRepository.findByStudentId(student.getId())
                .orElseThrow(() ->
                        new RuntimeException("Profile not created"));
    }
    public String uploadProfileImage(MultipartFile image) {

        if (image.isEmpty()) {
            throw new RuntimeException("Image file is required");
        }

        Authentication auth =
                SecurityContextHolder.getContext().getAuthentication();

        String email = auth.getName();

        Student student = studentRepository.findByEmail(email)
                .orElseThrow(() ->
                        new StudentNotFoundException("Student not found"));

        StudentProfile profile = profileRepository
                .findByStudentId(student.getId())
                .orElseThrow(() ->
                        new RuntimeException("Profile not created"));

        try {
            // Create folder if not exists
            Path uploadPath = Paths.get("uploads/profile-images");
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // Generate unique file name
            String fileName = UUID.randomUUID() + "_" + image.getOriginalFilename();
            Path filePath = uploadPath.resolve(fileName);

            // Save file
            Files.copy(image.getInputStream(), filePath);

            // Save path in DB
            profile.setProfileImageUrl("/uploads/profile-images/" + fileName);
            profileRepository.save(profile);

            return "Profile image uploaded successfully";

        } catch (IOException e) {
            throw new RuntimeException("Failed to upload image");
        }
    }

}