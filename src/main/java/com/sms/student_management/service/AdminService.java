package com.sms.student_management.service;

import com.sms.student_management.dto.CreateAdminRequestDTO;
import com.sms.student_management.entity.User;
import com.sms.student_management.repository.UserRepository;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminService(UserRepository userRepository,
                        PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public String createAdmin(CreateAdminRequestDTO dto) {

        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        if (userRepository.existsByMobile(dto.getMobile())) {
            throw new RuntimeException("Mobile already exists");
        }

        User admin = new User();
        admin.setFullName(dto.getFullName());
        admin.setEmail(dto.getEmail());
        admin.setMobile(dto.getMobile());
        admin.setPassword(passwordEncoder.encode(dto.getPassword()));
        admin.setRole("ADMIN");   // 🔥 IMPORTANT

        userRepository.save(admin);

        return "Admin created successfully";
    }
}
