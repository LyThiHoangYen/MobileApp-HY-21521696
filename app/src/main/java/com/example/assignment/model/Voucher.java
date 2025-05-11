package com.example.assignment.model;

public class Voucher {
    private String id;
    private String code;
    private String description;
    private double discount;
    private boolean isUsed;
    // Thêm các trường khác nếu cần

    public Voucher() {}

    public Voucher(String id, String code, String description, double discount, boolean isUsed) {
        this.id = id;
        this.code = code;
        this.description = description;
        this.discount = discount;
        this.isUsed = isUsed;
    }

    // Getter và Setter
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public double getDiscount() { return discount; }
    public void setDiscount(double discount) { this.discount = discount; }
    public boolean isUsed() { return isUsed; }
    public void setUsed(boolean used) { isUsed = used; }
} 