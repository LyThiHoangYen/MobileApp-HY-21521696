package com.example.assignment.model;

import java.util.List;

public class Order {
    private String id;
    private String userId;
    private List<Food> foodList;
    private double totalPrice;
    private String status;
    private String orderTime;
    // Thêm các trường khác nếu cần

    public Order() {}

    public Order(String id, String userId, List<Food> foodList, double totalPrice, String status, String orderTime) {
        this.id = id;
        this.userId = userId;
        this.foodList = foodList;
        this.totalPrice = totalPrice;
        this.status = status;
        this.orderTime = orderTime;
    }

    // Getter và Setter
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public List<Food> getFoodList() { return foodList; }
    public void setFoodList(List<Food> foodList) { this.foodList = foodList; }
    public double getTotalPrice() { return totalPrice; }
    public void setTotalPrice(double totalPrice) { this.totalPrice = totalPrice; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getOrderTime() { return orderTime; }
    public void setOrderTime(String orderTime) { this.orderTime = orderTime; }
} 