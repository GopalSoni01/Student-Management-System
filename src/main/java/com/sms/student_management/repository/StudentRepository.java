package com.sms.student_management.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sms.student_management.Student;

public interface StudentRepository extends JpaRepository<Student, Long> {

    // already used by you
    boolean existsByEmail(String email);

    // 🔴 THIS IS THE MISSING METHOD (ADD THIS)
    Optional<Student> findByEmail(String email);
}
