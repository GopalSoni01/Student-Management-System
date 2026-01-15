package com.sms.student_management.repository;

import java.util.List;
import java.util.Optional;

import com.sms.student_management.Student;
import com.sms.student_management.dto.AdminStudentResponseDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface StudentRepository extends JpaRepository<Student, Long> {

    // =========================
    // BASIC / EXISTING METHODS
    // =========================

    boolean existsByEmail(String email);

    // Used in authentication / role-based fetch
    Optional<Student> findByEmail(String email);

    // =========================
    // SOFT DELETE SUPPORT
    // =========================

    // Only active students
    List<Student> findByDeletedFalse();

    // Find active student by id
    Optional<Student> findByIdAndDeletedFalse(Long id);

    // Find active student by email
    Optional<Student> findByEmailAndDeletedFalse(String email);

    // =========================
    // ADMIN DASHBOARD (WITHOUT PAGINATION)
    // (Keep this if already used somewhere)
    // =========================

    @Query("""
        SELECT new com.sms.student_management.dto.AdminStudentResponseDTO(
            s.id,
            s.name,
            s.email,
            s.course,
            p.fatherName,
            p.fatherOccupation,
            p.address,
            p.profileImageUrl,
            s.deleted
        )
        FROM Student s
        LEFT JOIN StudentProfile p ON p.student = s
    """)
    List<AdminStudentResponseDTO> findAllStudentsForAdmin();

    // =========================
    // ADMIN DASHBOARD (WITH PAGINATION & SORTING)
    // ⭐ NEW METHOD (DO NOT DELETE ABOVE ONE)
    // =========================

    @Query("""
        SELECT new com.sms.student_management.dto.AdminStudentResponseDTO(
            s.id,
            s.name,
            s.email,
            s.course,
            p.fatherName,
            p.fatherOccupation,
            p.address,
            p.profileImageUrl,
            s.deleted
        )
        FROM Student s
        LEFT JOIN StudentProfile p ON p.student = s
    """)
    Page<AdminStudentResponseDTO> findAllStudentsForAdmin(Pageable pageable);
}
