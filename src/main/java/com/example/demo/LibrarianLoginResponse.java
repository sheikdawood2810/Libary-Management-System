package com.example.demo;

public class LibrarianLoginResponse {

    private Long id;
    private String username;


    public LibrarianLoginResponse(
            Long id,
            String username) {

        this.id = id;
        this.username = username;
    }


    public Long getId() {
        return id;
    }


    public String getUsername() {
        return username;
    }
}