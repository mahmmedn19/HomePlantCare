package com.project.homeplantcare.data.models;

public class User {
    private String username;
    private String email;
    private String uid;
    private boolean isBlocked;  // New field to track blocked status

    public User() {}

    // Constructor
    public User(String username, String email, String uid, boolean isBlocked) {
        this.username = username;
        this.email = email;
        this.uid = uid;
        this.isBlocked = isBlocked;
    }

    // Getters and Setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public boolean isBlocked() {
        return isBlocked;
    }

    public void setBlocked(boolean blocked) {
        isBlocked = blocked;
    }
}