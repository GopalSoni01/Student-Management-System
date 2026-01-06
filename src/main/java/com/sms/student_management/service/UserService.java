package com.sms.student_management.service;

import com.sms.student_management.Student;
import com.sms.student_management.repository.StudentRepository;
import com.sms.student_management.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

   public String getProfileBasedOnRole(){
       Authentication authentication =
               SecurityContextHolder.getContext().getAuthentication();
       String email = authentication.getName();
       boolean isAdmin = authentication.getAuthorities().stream()
               .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));

       // ADMIN → ALL STUDENTS
       if (isAdmin) {
           return userRepository.findAll()
                   .stream()
                   .map(this::mapToResponse)
                   .toList();
       }

       // STUDENT → ONLY OWN DATA
       Optional<Student> student =
               userRepository.findByEmail(email);

       return student
               .map(s -> List.of(mapToResponse(s)))
               .orElse(List.of());
    }
}
