package com.example.assignment;

public class EmployeeData {
    private String id;
    private String name;
    private String position;
    private String department;
    private String imageUrl;

    public EmployeeData(String id, String name, String position, String department, String imageUrl) {
        this.id = id;
        this.name = name;
        this.position = position;
        this.department = department;
        this.imageUrl = imageUrl;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPosition() {
        return position;
    }

    public String getDepartment() {
        return department;
    }

    public String getImageUrl() {
        return imageUrl;
    }
} 