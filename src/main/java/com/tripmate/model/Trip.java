package com.tripmate.model;

import com.tripmate.util.DateUtil;

import java.io.Serializable;
import java.util.Date;

/**
 * Model representing a Trip created by a user.
 */
public class Trip implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String userId; // Trip creator/owner
    private String userName; // Populated for UI display
    private String title;
    private String destinationId;
    private String destinationName;
    private String destinationImageUrl;
    private Date startDate;
    private Date endDate;
    private double estimatedBudget;
    private double actualExpenses;
    private String status; // "PLANNING", "ONGOING", "COMPLETED", "CANCELLED"
    private String visibility; // "PRIVATE", "SHARED"
    private String notes;
    private Date createdAt;

    public Trip() {
        this.status = "PLANNING";
        this.visibility = "PRIVATE";
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDestinationId() {
        return destinationId;
    }

    public void setDestinationId(String destinationId) {
        this.destinationId = destinationId;
    }

    public String getDestinationName() {
        return destinationName;
    }

    public void setDestinationName(String destinationName) {
        this.destinationName = destinationName;
    }

    public String getDestinationImageUrl() {
        return (destinationImageUrl != null && !destinationImageUrl.isEmpty()) 
                ? destinationImageUrl 
                : "https://images.unsplash.com/photo-1488646953014-85cb44e25828?auto=format&fit=crop&w=800&q=80";
    }

    public void setDestinationImageUrl(String destinationImageUrl) {
        this.destinationImageUrl = destinationImageUrl;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public double getEstimatedBudget() {
        return estimatedBudget;
    }

    public void setEstimatedBudget(double estimatedBudget) {
        this.estimatedBudget = estimatedBudget;
    }

    public double getActualExpenses() {
        return actualExpenses;
    }

    public void setActualExpenses(double actualExpenses) {
        this.actualExpenses = actualExpenses;
    }

    public double getRemainingBudget() {
        return estimatedBudget - actualExpenses;
    }

    public int getBudgetUsagePercentage() {
        if (estimatedBudget <= 0) return 0;
        int pct = (int) Math.round((actualExpenses / estimatedBudget) * 100);
        return Math.min(pct, 100);
    }

    public long getDurationDays() {
        return DateUtil.calculateDurationDays(startDate, endDate);
    }

    public String getFormattedStartDate() {
        return DateUtil.formatDisplayDate(startDate);
    }

    public String getFormattedEndDate() {
        return DateUtil.formatDisplayDate(endDate);
    }

    public String getInputStartDate() {
        return DateUtil.formatInputDate(startDate);
    }

    public String getInputEndDate() {
        return DateUtil.formatInputDate(endDate);
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getVisibility() {
        return visibility;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
