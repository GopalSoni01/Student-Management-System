package com.sms.student_management.service;

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
}