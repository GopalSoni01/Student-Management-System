package com.sms.student_management.controller;

import com.sms.student_management.Student;
import com.sms.student_management.dto.StudentRequestDTO;
import com.sms.student_management.dto.StudentUpdateRequestDTO;
import com.sms.student_management.dto.StudentResponseDTO;
import com.sms.student_management.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;
import java.util.List;

@RestController
@RequestMapping("/students")
public class StudentController {

    private final StudentService studentService;

    // Constructor Injection
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    // CREATE - Add student
    @PostMapping
    public StudentResponseDTO createStudent(
            @Valid @RequestBody StudentRequestDTO dto) {
        return studentService.createStudent(dto);
    }

    // READ - Get all students
    @GetMapping
    public List<StudentResponseDTO> getStudents() {
        return studentService.getStudentsBasedOnRole();
    }


    // READ - Get student by ID
    @GetMapping("/{id}")
    public StudentResponseDTO getStudentById(@PathVariable Long id) {
        return studentService.getStudentById(id);
    }


    // DELETE - Delete student
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public String deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return "Student deleted successfully";
    }
    // UPDATE student
    @PutMapping("/{id}")
    public StudentResponseDTO updateStudent(
            @PathVariable Long id,
            @Valid @RequestBody StudentUpdateRequestDTO dto) {
        return studentService.updateStudent(id, dto);
    }
}
