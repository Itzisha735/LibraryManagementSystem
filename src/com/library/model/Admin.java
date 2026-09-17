package com.library.model;

public class Admin extends User {

    private String role;

    public Admin(int userId, String name, String email, String role) {
        super(userId, name, email);
        this.role = role;
    }

    public void displayAdminDetails() {
        displayUserDetails();
        System.out.println("Role: " + role);
    }
}