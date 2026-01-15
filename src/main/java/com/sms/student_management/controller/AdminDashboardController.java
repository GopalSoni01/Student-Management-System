package com.sms.student_management.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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

    // 🔒 ADMIN ONLY – PAGINATION + SORTING
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/students")
    public Page<AdminStudentResponseDTO> getAllStudents(
            @PageableDefault(
                    page = 0,
                    size = 5,
                    sort = "name"
            ) Pageable pageable
    ) {
        return dashboardService.getAllStudents(pageable);
    }
}
