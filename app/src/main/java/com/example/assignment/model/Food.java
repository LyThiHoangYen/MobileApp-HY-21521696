package com.example.assignment.model;

public class Food {
    private String id;
    private String name;
    private String description;
    private double price;
    private String imageUrl;
    private boolean isBestSeller;
    // Thêm các trường khác nếu cần

    public Food() {}

    public Food(String id, String name, String description, double price, String imageUrl, boolean isBestSeller) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.imageUrl = imageUrl;
        this.isBestSeller = isBestSeller;
    }

    // Getter và Setter
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public boolean isBestSeller() { return isBestSeller; }
    public void setBestSeller(boolean bestSeller) { isBestSeller = bestSeller; }
} 