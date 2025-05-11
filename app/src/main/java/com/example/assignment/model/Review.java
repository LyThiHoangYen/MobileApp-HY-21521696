package com.example.assignment.model;

public class Review {
    private String id;
    private String userId;
    private String foodId;
    private String comment;
    private int rating;
    private String reviewTime;
    // Thêm các trường khác nếu cần

    public Review() {}

    public Review(String id, String userId, String foodId, String comment, int rating, String reviewTime) {
        this.id = id;
        this.userId = userId;
        this.foodId = foodId;
        this.comment = comment;
        this.rating = rating;
        this.reviewTime = reviewTime;
    }

    // Getter và Setter
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getFoodId() { return foodId; }
    public void setFoodId(String foodId) { this.foodId = foodId; }
    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }
    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }
    public String getReviewTime() { return reviewTime; }
    public void setReviewTime(String reviewTime) { this.reviewTime = reviewTime; }
} 