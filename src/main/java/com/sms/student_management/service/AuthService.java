package com.sms.student_management.service;

import com.sms.student_management.Student;
import com.sms.student_management.dto.LoginRequestDTO;
import com.sms.student_management.dto.RegisterRequestDTO;
import com.sms.student_management.entity.User;
import com.sms.student_management.jwt.JwtUtil;
import com.sms.student_management.repository.StudentRepository;
import com.sms.student_management.repository.UserRepository;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthService(
            UserRepository userRepository,
            StudentRepository studentRepository,
            PasswordEncoder passwordEncoder,
            JwtUtil jwtUtil) {

        this.userRepository = userRepository;
        this.studentRepository = studentRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    // REGISTER
    public String register(RegisterRequestDTO dto) {

        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Email already registered");
        }

        if (userRepository.existsByMobile(dto.getMobile())) {
            throw new RuntimeException("Mobile already registered");
        }

        // 1️⃣ Create USER
        User user = new User();
        user.setFullName(dto.getFullName());
        user.setEmail(dto.getEmail());
        user.setMobile(dto.getMobile());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRole("STUDENT");

        userRepository.save(user);

        // 2️⃣ AUTO-CREATE STUDENT
        Student student = new Student();
        student.setEmail(dto.getEmail());
        student.setName(dto.getFullName());
        student.setCourse("Not Assigned");

        studentRepository.save(student);

        return "User registered successfully";
    }

    // LOGIN (EMAIL OR MOBILE)
    public String login(LoginRequestDTO dto) {

        User user;

        if (dto.getEmail() != null) {
            user = userRepository.findByEmail(dto.getEmail())
                    .orElseThrow(() -> new RuntimeException("Invalid credentials"));
        } else if (dto.getMobile() != null) {
            user = userRepository.findByMobile(dto.getMobile())
                    .orElseThrow(() -> new RuntimeException("Invalid credentials"));
        } else {
            throw new RuntimeException("Email or Mobile is required");
        }

        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        return jwtUtil.generateToken(user.getEmail());
    }
}
