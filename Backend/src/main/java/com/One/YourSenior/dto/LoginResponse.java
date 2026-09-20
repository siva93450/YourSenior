package com.One.YourSenior.dto;

public class LoginResponse {
    private String token;
    private String role;
    private Long id;
    private String name;

    public LoginResponse(String token, String role, Long id, String name) {
        this.token = token;
        this.role = role;
        this.id = id;
        this.name = name;
    }

    public String getToken() { return token; }
    public String getRole() { return role; }
    public Long getId() { return id; }
    public String getName() { return name; }
}