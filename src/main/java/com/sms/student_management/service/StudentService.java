package com.sms.student_management.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.sms.student_management.Student;
import com.sms.student_management.repository.StudentRepository;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    // Constructor Injection (BEST PRACTICE)
    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    // Save student
    public Student saveStudent(Student student) {
        return studentRepository.save(student);
    }

    // Get all students
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    // Get student by ID
//    public Student getStudentById(Long id) {
//        return studentRepository.findById(id).orElse(null);
//    }
    public Student getStudentById(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found with id " + id));
    }


    // Delete student by ID
    public void deleteStudent(Long id) {
        studentRepository.deleteById(id);
    }

    // UPDATE student
    public Student updateStudent(Long id, Student updatedStudent) {

        // Step 1: Find existing student
        Student existingStudent = studentRepository.findById(id).orElse(null);

        // Step 2: If student exists, update fields
        if (existingStudent != null) {
            existingStudent.setName(updatedStudent.getName());
            existingStudent.setEmail(updatedStudent.getEmail());
            existingStudent.setCourse(updatedStudent.getCourse());

            // Step 3: Save updated student
            return studentRepository.save(existingStudent);
        }

        // Step 4: If student not found
        return null;
    }

}