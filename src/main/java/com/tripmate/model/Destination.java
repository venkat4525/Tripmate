package com.tripmate.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Model representing a Travel Destination or Tour Package.
 */
public class Destination implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String name;
    private String country;
    private String city;
    private String categoryId;
    private String categoryName; // Populated via join/lookup
    private String description;
    private String highlights; // Key Highlights
    private String duration; // e.g. "6 Nights 7 Days", "12 Nights 13 Days"
    private String bestSeason;
    private double averageCost;
    private String imageUrl;
    private double rating = 5.0;
    private List<String> tags = new ArrayList<>();
    private Date createdAt;

    public Destination() {
        this.createdAt = new Date();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getHighlights() {
        return highlights;
    }

    public void setHighlights(String highlights) {
        this.highlights = highlights;
    }

    public String getDuration() {
        return (duration != null && !duration.isEmpty()) ? duration : "5 Nights 6 Days";
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getBestSeason() {
        return bestSeason;
    }

    public void setBestSeason(String bestSeason) {
        this.bestSeason = bestSeason;
    }

    public double getAverageCost() {
        return averageCost;
    }

    public void setAverageCost(double averageCost) {
        this.averageCost = averageCost;
    }

    public String getImageUrl() {
        return (imageUrl != null && !imageUrl.isEmpty()) 
                ? imageUrl 
                : "https://images.unsplash.com/photo-1506905925346-21bda4d32df4?auto=format&fit=crop&w=800&q=80";
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
