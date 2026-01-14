package com.sms.student_management.controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.sms.student_management.dto.AdminStudentResponseDTO;
import com.sms.student_management.service.AdminDashboardService;

@RestController
@RequestMapping("/admin/dashboard")
public class AdminDashboardController {

    private final AdminDashboardService dashboardService;

    public AdminDashboardController(AdminDashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    // 🔒 ADMIN ONLY
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/students")
    public List<AdminStudentResponseDTO> getAllStudents() {
        return dashboardService.getAllStudents();
    }
}
