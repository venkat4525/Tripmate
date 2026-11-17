package com.tripmate.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Model representing a bookmarked favourite destination.
 */
public class Favourite implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String userId;
    private String destinationId;
    private Destination destination; // Joined for UI rendering
    private Date addedAt;

    public Favourite() {
        this.addedAt = new Date();
    }

    public Favourite(String userId, String destinationId) {
        this.userId = userId;
        this.destinationId = destinationId;
        this.addedAt = new Date();
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

    public String getDestinationId() {
        return destinationId;
    }

    public void setDestinationId(String destinationId) {
        this.destinationId = destinationId;
    }

    public Destination getDestination() {
        return destination;
    }

    public void setDestination(Destination destination) {
        this.destination = destination;
    }

    public Date getAddedAt() {
        return addedAt;
    }

    public void setAddedAt(Date addedAt) {
        this.addedAt = addedAt;
    }
}
