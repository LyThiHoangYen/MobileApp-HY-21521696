package com.example.assignment.activity;

public class Category {
    private int iconRes;
    private String title;

    public Category(int iconRes, String title) {
        this.iconRes = iconRes;
        this.title = title;
    }

    public int getIconRes() {
        return iconRes;
    }

    public String getTitle() {
        return title;
    }
} 