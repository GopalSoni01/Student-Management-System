package com.sms.student_management.service;

import java.util.List;
import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.sms.student_management.Student;
import com.sms.student_management.repository.StudentRepository;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    // Constructor Injection
    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    // =========================
    // SAVE STUDENT (with email check)
    // =========================
    public Student saveStudent(Student student) {

        if (studentRepository.existsByEmail(student.getEmail())) {
            throw new RuntimeException("This email already exists");
        }

        return studentRepository.save(student);
    }

    // =========================
    // ROLE-BASED FETCH (NEW)
    // =========================
    public List<Student> getStudentsBasedOnRole() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));

        // ADMIN → ALL STUDENTS
        if (isAdmin) {
            return studentRepository.findAll();
        }

        // STUDENT → ONLY OWN DATA
        Optional<Student> student =
                studentRepository.findByEmail(email);

        return student.map(List::of).orElse(List.of());
    }

    // =========================
    // GET STUDENT BY ID
    // =========================
    public Student getStudentById(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found with id " + id));
    }

    // =========================
    // UPDATE STUDENT
    // =========================
    public Student updateStudent(Long id, Student updatedStudent) {

        Student existingStudent = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        if (!existingStudent.getEmail().equals(updatedStudent.getEmail())) {
            if (studentRepository.existsByEmail(updatedStudent.getEmail())) {
                throw new RuntimeException("This email already exists");
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
        studentRepository.deleteById(id);
    }
}
