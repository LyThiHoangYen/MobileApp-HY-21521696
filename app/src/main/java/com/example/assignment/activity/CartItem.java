package com.example.assignment.activity;

public class CartItem {
    private int imageRes;
    private String name;
    private int price;
    private int quantity;

    public CartItem(int imageRes, String name, int price, int quantity) {
        this.imageRes = imageRes;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public int getImageRes() { return imageRes; }
    public String getName() { return name; }
    public int getPrice() { return price; }
    public int getQuantity() { return quantity; }
} 