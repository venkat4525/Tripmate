package com.tripmate.model;

import com.tripmate.util.DateUtil;

import java.io.Serializable;
import java.util.Date;

/**
 * Model representing an expense recorded for a trip.
 */
public class Expense implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String tripId;
    private String paidBy; // userId
    private String payerName; // Populated for UI display
    private String title;
    private String category; // ACCOMMODATION, FOOD, TRANSPORTATION, ACTIVITIES, SHOPPING, MISCELLANEOUS
    private double amount;
    private Date expenseDate;
    private String paymentMethod; // CASH, CARD, UPI, OTHER
    private String notes;
    private Date createdAt;

    public Expense() {
        this.category = "MISCELLANEOUS";
        this.paymentMethod = "CARD";
        this.expenseDate = new Date();
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

    public String getPaidBy() {
        return paidBy;
    }

    public void setPaidBy(String paidBy) {
        this.paidBy = paidBy;
    }

    public String getPayerName() {
        return payerName;
    }

    public void setPayerName(String payerName) {
        this.payerName = payerName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Date getExpenseDate() {
        return expenseDate;
    }

    public void setExpenseDate(Date expenseDate) {
        this.expenseDate = expenseDate;
    }

    public String getFormattedExpenseDate() {
        return DateUtil.formatDisplayDate(expenseDate);
    }

    public String getInputExpenseDate() {
        return DateUtil.formatInputDate(expenseDate);
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
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
