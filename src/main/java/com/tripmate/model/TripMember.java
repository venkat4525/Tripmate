package com.tripmate.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Model representing a member/collaborator on a Trip.
 */
public class TripMember implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String tripId;
    private String userId;
    private String userName; // For UI display
    private String userEmail; // For UI display
    private String userProfileImage;
    private String role; // "OWNER", "EDITOR", "VIEWER"
    private String status; // "ACTIVE", "PENDING", "REJECTED"
    private Date joinedAt;

    public TripMember() {
        this.status = "ACTIVE";
        this.joinedAt = new Date();
    }

    public TripMember(String tripId, String userId, String role, String status) {
        this.tripId = tripId;
        this.userId = userId;
        this.role = role;
        this.status = status;
        this.joinedAt = new Date();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTripId() {
        return tripId;
    }

    public void setTripId(String tripId) {
        this.tripId = tripId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserProfileImage() {
        return userProfileImage;
    }

    public void setUserProfileImage(String userProfileImage) {
        this.userProfileImage = userProfileImage;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getJoinedAt() {
        return joinedAt;
    }

    public void setJoinedAt(Date joinedAt) {
        this.joinedAt = joinedAt;
    }
}
