package com.sms.student_management.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sms.student_management.entity.StudentProfile;

public interface StudentProfileRepository
        extends JpaRepository<StudentProfile, Long> {

    Optional<StudentProfile> findByStudentId(Long studentId);
}
