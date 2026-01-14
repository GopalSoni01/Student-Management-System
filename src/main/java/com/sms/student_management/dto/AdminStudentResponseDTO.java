package com.sms.student_management.dto;

public class AdminStudentResponseDTO {

    private Long studentId;
    private String name;
    private String email;
    private String course;

    private String fatherName;
    private String fatherOccupation;
    private String address;
    private String profileImageUrl;

    // ✅ NEW FIELD
    private boolean deleted;

    // ✅ UPDATED CONSTRUCTOR
    public AdminStudentResponseDTO(
            Long studentId,
            String name,
            String email,
            String course,
            String fatherName,
            String fatherOccupation,
            String address,
            String profileImageUrl,
            boolean deleted) {

        this.studentId = studentId;
        this.name = name;
        this.email = email;
        this.course = course;
        this.fatherName = fatherName;
        this.fatherOccupation = fatherOccupation;
        this.address = address;
        this.profileImageUrl = profileImageUrl;
        this.deleted = deleted;
    }

    // getters (read-only for admin view)
    public Long getStudentId() { return studentId; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getCourse() { return course; }
    public String getFatherName() { return fatherName; }
    public String getFatherOccupation() { return fatherOccupation; }
    public String getAddress() { return address; }
    public String getProfileImageUrl() { return profileImageUrl; }

    // ✅ NEW GETTER
    public boolean isDeleted() { return deleted; }
}
