package com.sms.student_management.service;

import java.util.List;
import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.sms.student_management.Student;
import com.sms.student_management.dto.StudentRequestDTO;
import com.sms.student_management.dto.StudentResponseDTO;
import com.sms.student_management.dto.StudentUpdateRequestDTO;
import com.sms.student_management.exception.EmailAlreadyExistsException;
import com.sms.student_management.exception.StudentNotFoundException;
import com.sms.student_management.repository.StudentRepository;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    // =========================
    // CREATE STUDENT
    // =========================
    public StudentResponseDTO createStudent(StudentRequestDTO dto) {

        if (studentRepository.existsByEmail(dto.getEmail())) {
            throw new EmailAlreadyExistsException(
                    "Email already exists: " + dto.getEmail()
            );
        }

        Student student = mapToEntity(dto);
        Student savedStudent = studentRepository.save(student);

        return mapToResponse(savedStudent);
    }

    // =========================
    // UPDATE STUDENT (DTO)
    // =========================
    public StudentResponseDTO updateStudent(Long id, StudentUpdateRequestDTO dto) {

        Student existingStudent = studentRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() ->
                        new StudentNotFoundException(
                                "Student not found with id " + id
                        )
                );

        if (!existingStudent.getEmail().equals(dto.getEmail())
                && studentRepository.existsByEmail(dto.getEmail())) {
            throw new EmailAlreadyExistsException(
                    "Email already exists: " + dto.getEmail()
            );
        }

        existingStudent.setName(dto.getName());
        existingStudent.setEmail(dto.getEmail());
        existingStudent.setCourse(dto.getCourse());

        return mapToResponse(studentRepository.save(existingStudent));
    }

    // =========================
    // ROLE-BASED FETCH
    // =========================
    public List<StudentResponseDTO> getStudentsBasedOnRole() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));

        // ADMIN → ALL ACTIVE STUDENTS
        if (isAdmin) {
            return studentRepository.findByDeletedFalse()
                    .stream()
                    .map(this::mapToResponse)
                    .toList();
        }

        // STUDENT → ONLY OWN ACTIVE DATA
        Optional<Student> student =
                studentRepository.findByEmailAndDeletedFalse(email);

        return student
                .map(s -> List.of(mapToResponse(s)))
                .orElse(List.of());
    }

    // =========================
    // GET STUDENT BY ID
    // =========================
    public StudentResponseDTO getStudentById(Long id) {

        Student student = studentRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() ->
                        new StudentNotFoundException(
                                "Student not found with id " + id
                        )
                );

        return mapToResponse(student);
    }

    // =========================
    // SOFT DELETE STUDENT
    // =========================
    public void deleteStudent(Long id) {

        Student student = studentRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() ->
                        new StudentNotFoundException("Student not found"));

        student.setDeleted(true);
        studentRepository.save(student);
    }

    // =========================
    // DTO → ENTITY
    // =========================
    private Student mapToEntity(StudentRequestDTO dto) {
        Student student = new Student();
        student.setName(dto.getName());
        student.setEmail(dto.getEmail());
        student.setCourse(dto.getCourse());
        return student;
    }

    // =========================
    // ENTITY → DTO
    // =========================
    private StudentResponseDTO mapToResponse(Student student) {
        return new StudentResponseDTO(
                student.getId(),
                student.getName(),
                student.getEmail(),
                student.getCourse()
        );
    }
}
