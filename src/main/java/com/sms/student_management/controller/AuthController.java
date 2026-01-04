package com.sms.student_management.controller;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import com.sms.student_management.jwt.JwtUtil;
import com.sms.student_management.repository.StudentRepository;
import com.sms.student_management.Student;

import com.sms.student_management.entity.User;
import com.sms.student_management.repository.UserRepository;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    private final StudentRepository studentRepository;

    public AuthController(UserRepository userRepository,
                          PasswordEncoder passwordEncoder,
                          AuthenticationManager authenticationManager,
                          JwtUtil jwtUtil,
                          StudentRepository studentRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.studentRepository = studentRepository;
    }


    // SIGNUP
    @PostMapping("/signup")
    public String signup(@RequestBody User user) {

        if (userRepository.existsByEmail(user.getEmail())) {
            return "Email already registered";
        }

        // save user
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("STUDENT");   // default role
        userRepository.save(user);

        // ✅ AUTO-CREATE STUDENT RECORD
        Student student = new Student();
        student.setEmail(user.getEmail());
        student.setName("New Student"); // can be updated later
        student.setCourse("Not Assigned");

        studentRepository.save(student);

        return "User and Student created successfully";
    }


    // LOGIN
    @PostMapping("/login")
    public String login(@RequestBody User user) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        user.getEmail(),
                        user.getPassword()
                )
        );

        if (authentication.isAuthenticated()) {
            return jwtUtil.generateToken(user.getEmail());
        }

        throw new RuntimeException("Invalid credentials");
    }

}
