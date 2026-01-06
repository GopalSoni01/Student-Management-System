package com.sms.student_management.service;

import java.util.List;
import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.sms.student_management.dto.StudentUpdateRequestDTO;

import com.sms.student_management.Student;
import com.sms.student_management.dto.StudentRequestDTO;
import com.sms.student_management.dto.StudentResponseDTO;
import com.sms.student_management.exception.EmailAlreadyExistsException;
import com.sms.student_management.exception.StudentNotFoundException;
import com.sms.student_management.repository.StudentRepository;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    // Constructor Injection
    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    // =========================
    // CREATE STUDENT (DTO)
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
    public StudentResponseDTO updateStudent(Long id, StudentUpdateRequestDTO dto) {

        Student existingStudent = studentRepository.findById(id)
                .orElseThrow(() ->
                        new StudentNotFoundException(
                                "Student not found with id " + id
                        )
                );

        // Email duplication check
        if (!existingStudent.getEmail().equals(dto.getEmail())) {
            if (studentRepository.existsByEmail(dto.getEmail())) {
                throw new EmailAlreadyExistsException(
                        "Email already exists: " + dto.getEmail()
                );
            }
        }

        existingStudent.setName(dto.getName());
        existingStudent.setEmail(dto.getEmail());
        existingStudent.setCourse(dto.getCourse());

        Student updatedStudent = studentRepository.save(existingStudent);

        return mapToResponse(updatedStudent);
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

        // ADMIN → ALL STUDENTS
        if (isAdmin) {
            return studentRepository.findAll()
                    .stream()
                    .map(this::mapToResponse)
                    .toList();
        }

        // STUDENT → ONLY OWN DATA
        Optional<Student> student =
                studentRepository.findByEmail(email);

        return student
                .map(s -> List.of(mapToResponse(s)))
                .orElse(List.of());
    }

    // =========================
    // GET STUDENT BY ID
    // =========================
    public StudentResponseDTO getStudentById(Long id) {

        Student student = studentRepository.findById(id)
                .orElseThrow(() ->
                        new StudentNotFoundException(
                                "Student not found with id " + id
                        )
                );

        return mapToResponse(student);
    }

    // =========================
    // UPDATE STUDENT
    // =========================
    public Student updateStudent(Long id, Student updatedStudent) {

        Student existingStudent = studentRepository.findById(id)
                .orElseThrow(() ->
                        new StudentNotFoundException(
                                "Student not found with id " + id
                        )
                );

        if (!existingStudent.getEmail().equals(updatedStudent.getEmail())) {
            if (studentRepository.existsByEmail(updatedStudent.getEmail())) {
                throw new EmailAlreadyExistsException(
                        "Email already exists: " + updatedStudent.getEmail()
                );
            }
        }

        existingStudent.setName(updatedStudent.getName());
        existingStudent.setEmail(updatedStudent.getEmail());
        existingStudent.setCourse(updatedStudent.getCourse());

        return studentRepository.save(existingStudent);
    }

    // =========================
    // DELETE STUDENT
    // =========================
    public void deleteStudent(Long id) {

        if (!studentRepository.existsById(id)) {
            throw new StudentNotFoundException(
                    "Student not found with id " + id
            );
        }

        studentRepository.deleteById(id);
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
