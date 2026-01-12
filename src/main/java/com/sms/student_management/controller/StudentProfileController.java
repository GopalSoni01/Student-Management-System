package com.sms.student_management.controller;

import com.sms.student_management.dto.StudentProfileRequestDTO;
import com.sms.student_management.entity.StudentProfile;
import com.sms.student_management.service.StudentProfileService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/profile")
public class StudentProfileController {

    private final StudentProfileService profileService;

    public StudentProfileController(StudentProfileService profileService) {
        this.profileService = profileService;
    }

    // CREATE / UPDATE profile
    @PostMapping
    public StudentProfile saveProfile(
            @Valid @RequestBody StudentProfileRequestDTO dto) {
        return profileService.saveOrUpdateProfile(dto);
    }

    // VIEW own profile
    @GetMapping
    public StudentProfile getMyProfile() {
        return profileService.getMyProfile();
    }
}
