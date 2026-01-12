package com.sms.student_management.entity;
import com.sms.student_management.Student;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "student_profiles")
public class StudentProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // One-to-One with Student
    @OneToOne
    @JoinColumn(name = "student_id", nullable = false, unique = true)
    private Student student;

    private LocalDate dateOfBirth;

    @Column(length = 500)
    private String address;

    private String pincode;

    private String fatherName;

    private String fatherOccupation;

    private String bankAccountNumber;

    private String ifscCode;

    private String courseName;

    private String profileImageUrl; // store image URL/path

    // getters & setters
    public Long getId() { return id; }

    public Student getStudent() { return student; }
    public void setStudent(Student student) { this.student = student; }

    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(LocalDate dateOfBirth) { this.dateOfBirth = dateOfBirth; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getPincode() { return pincode; }
    public void setPincode(String pincode) { this.pincode = pincode; }

    public String getFatherName() { return fatherName; }
    public void setFatherName(String fatherName) { this.fatherName = fatherName; }

    public String getFatherOccupation() { return fatherOccupation; }
    public void setFatherOccupation(String fatherOccupation) { this.fatherOccupation = fatherOccupation; }

    public String getBankAccountNumber() { return bankAccountNumber; }
    public void setBankAccountNumber(String bankAccountNumber) { this.bankAccountNumber = bankAccountNumber; }

    public String getIfscCode() { return ifscCode; }
    public void setIfscCode(String ifscCode) { this.ifscCode = ifscCode; }

    public String getCourseName() { return courseName; }
    public void setCourseName(String courseName) { this.courseName = courseName; }

    public String getProfileImageUrl() { return profileImageUrl; }
    public void setProfileImageUrl(String profileImageUrl) { this.profileImageUrl = profileImageUrl; }
}