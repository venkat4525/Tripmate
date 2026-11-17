package com.tripmate.model;

import com.tripmate.util.DateUtil;

import java.io.Serializable;
import java.util.Date;

/**
 * Model representing an alert/notification delivered to a user.
 */
public class Notification implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String userId;
    private String title;
    private String message;
    private String type; // "TRIP_INVITATION", "EXPENSE_ADDED", "ITINERARY_UPDATE", "GENERAL"
    private String relatedEntityId;
    private boolean read = false;
    private Date createdAt;

    public Notification() {
        this.createdAt = new Date();
    }

    public Notification(String userId, String title, String message, String type, String relatedEntityId) {
        this.userId = userId;
        this.title = title;
        this.message = message;
        this.type = type;
        this.relatedEntityId = relatedEntityId;
        this.read = false;
        this.createdAt = new Date();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRelatedEntityId() {
        return relatedEntityId;
    }

    public void setRelatedEntityId(String relatedEntityId) {
        this.relatedEntityId = relatedEntityId;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getFormattedCreatedAt() {
        return DateUtil.formatDisplayDateTime(createdAt);
    }
}
