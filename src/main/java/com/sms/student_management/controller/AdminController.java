package com.sms.student_management.controller;

import com.sms.student_management.dto.CreateAdminRequestDTO;
import com.sms.student_management.service.AdminService;

import jakarta.validation.Valid;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    // 🔒 ADMIN ONLY
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create-admin")
    public String createAdmin(
            @Valid @RequestBody CreateAdminRequestDTO dto) {

        return adminService.createAdmin(dto);
    }
}
