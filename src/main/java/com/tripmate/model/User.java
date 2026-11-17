package com.tripmate.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Model representing a registered User in TripMate.
 */
public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String fullName;
    private String email;
    private String password;
    private String role; // "ADMIN", "USER"
    private String phone;
    private String profileImage;
    private boolean active = true;
    private Date createdAt;
    private Date updatedAt;

    public User() {
    }

    public User(String id, String fullName, String email, String password, String role, String phone, String profileImage) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.role = role;
        this.phone = phone;
        this.profileImage = profileImage;
        this.active = true;
        this.createdAt = new Date();
        this.updatedAt = new Date();
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isAdmin() {
        return "ADMIN".equalsIgnoreCase(this.role);
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getProfileImage() {
        return (profileImage != null && !profileImage.isEmpty()) ? profileImage : "assets/images/profiles/default.png";
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
}
