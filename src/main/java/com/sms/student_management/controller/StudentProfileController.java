package com.sms.student_management.controller;

import com.sms.student_management.dto.StudentProfileRequestDTO;
import com.sms.student_management.entity.StudentProfile;
import com.sms.student_management.service.StudentProfileService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/profile")
public class StudentProfileController {

    private final StudentProfileService profileService;

    public StudentProfileController(StudentProfileService profileService) {
        this.profileService = profileService;
    }

    @PostMapping
    public StudentProfile createOrUpdateProfile(
            @Valid @RequestBody StudentProfileRequestDTO dto) {
        return profileService.saveOrUpdateProfile(dto);
    }

    @PostMapping("/upload-image")
    public String uploadProfileImage(
            @RequestParam("image") MultipartFile image) {
        return profileService.uploadProfileImage(image);
    }

    @GetMapping
    public StudentProfile getMyProfile() {
        return profileService.getMyProfile();
    }
}
