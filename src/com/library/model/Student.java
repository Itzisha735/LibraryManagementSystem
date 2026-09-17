package com.library.model;

public class Student extends User {

    private String course;

    public Student(int userId, String name, String email, String course) {
        super(userId, name, email);
        this.course = course;
    }

    public void displayStudentDetails() {
        displayUserDetails();
        System.out.println("Course: " + course);
    }
}