package com.tripmate.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Model representing a packing checklist item.
 */
public class ChecklistItem implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String tripId;
    private String assignedTo; // Optional userId
    private String category; // DOCUMENTS, CLOTHING, ELECTRONICS, MEDICINE, TOILETRIES, MISCELLANEOUS
    private String itemName;
    private boolean packed = false;
    private Date createdAt;

    public ChecklistItem() {
        this.category = "MISCELLANEOUS";
        this.createdAt = new Date();
    }

    public ChecklistItem(String tripId, String category, String itemName) {
        this.tripId = tripId;
        this.category = category;
        this.itemName = itemName;
        this.packed = false;
        this.createdAt = new Date();
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

    public String getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(String assignedTo) {
        this.assignedTo = assignedTo;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public boolean isPacked() {
        return packed;
    }

    public void setPacked(boolean packed) {
        this.packed = packed;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
