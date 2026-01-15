package com.sms.student_management.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.sms.student_management.dto.AdminStudentResponseDTO;
import com.sms.student_management.repository.StudentRepository;

@Service
public class AdminDashboardService {

    private final StudentRepository studentRepository;

    public AdminDashboardService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Page<AdminStudentResponseDTO> getAllStudents(Pageable pageable) {
        return studentRepository.findAllStudentsForAdmin(pageable);
    }
}
