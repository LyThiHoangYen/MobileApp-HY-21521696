package com.example.assignment;

public class Staff {
    private String staffName;
    private String phongBan;
    private int staffId;
    private String sex;
    private int pathImage;
    private int ssn;

    public Staff(String name, String phong_ban, int id, String sex, int pathImage) {
        this.staffName = name;
        this.phongBan = phong_ban;
        this.staffId = id;
        this.sex = sex;
        this.pathImage = pathImage;
//        this.ssn = ssn;
    }

    public String getStaffName() {
        return staffName;
    }

    public String getPhongBan() {
        return phongBan;
    }

    public int getStaffId() {
        return staffId;
    }

    public String getSex() {
        return sex;
    }

    public int getPathImage() {
        return pathImage;
    }

//    public int getSsn() {
//        return ssn;
//    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public void setPhongBan(String phongBan) {
        this.phongBan = phongBan;
    }

    public void setStaffId(int staffId) {
        this.staffId = staffId;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public void setPathImage(int pathImage) {
        this.pathImage = pathImage;
    }

//    public void setSsn(int ssn) {
//        this.ssn = ssn;
//    }
}
