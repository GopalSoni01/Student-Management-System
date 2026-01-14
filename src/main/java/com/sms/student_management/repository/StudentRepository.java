package com.sms.student_management.repository;

import java.util.List;
import java.util.Optional;

import com.sms.student_management.dto.AdminStudentResponseDTO;
import org.springframework.data.jpa.repository.JpaRepository;

import com.sms.student_management.Student;
import org.springframework.data.jpa.repository.Query;

public interface StudentRepository extends JpaRepository<Student, Long> {

    // already used by you
    boolean existsByEmail(String email);

    // 🔴 THIS IS THE MISSING METHOD (ADD THIS)
    Optional<Student> findByEmail(String email);
    @Query("""
    SELECT new com.sms.student_management.dto.AdminStudentResponseDTO(
        s.id,
        s.name,
        s.email,
        s.course,
        p.fatherName,
        p.fatherOccupation,
        p.address,
        p.profileImageUrl
    )
    FROM Student s
    LEFT JOIN StudentProfile p ON p.student = s
""")
    List<AdminStudentResponseDTO> findAllStudentsForAdmin();

}
