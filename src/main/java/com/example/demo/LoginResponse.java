package com.example.demo;

public class LoginResponse {

    private Long id;
    private String name;
    private String email;
    private String studentCode;

    public LoginResponse(
            Long id,
            String name,
            String email,
            String studentCode) {

        this.id = id;
        this.name = name;
        this.email = email;
        this.studentCode = studentCode;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getStudentCode() {
        return studentCode;
    }
}