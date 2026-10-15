package com.library.model;

public class User {

    protected int userId;
    protected String name;
    protected String email;
    protected String role;

    public User(int userId, String name, String email) {
        this(userId, name, email, null);
    }

    public User(int userId, String name, String email, String role) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.role = role;
    }

    public String getRole() {
        return role;
    }
    public String getEmail() {
    return email;
    }

    public void displayUserDetails() {
        System.out.println("User ID: " + userId);
        System.out.println("Name: " + name);
        System.out.println("Email: " + email);
        System.out.println("Role: " + role);
    }
}