package com.sms.student_management.controller;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import com.sms.student_management.dto.RegisterRequestDTO;
import com.sms.student_management.dto.LoginRequestDTO;
import com.sms.student_management.service.AuthService;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public String register(@Valid @RequestBody RegisterRequestDTO dto) {
        return authService.register(dto);
    }

    @PostMapping("/login")
    public String login(@Valid @RequestBody LoginRequestDTO dto) {
        return authService.login(dto);
    }
}
