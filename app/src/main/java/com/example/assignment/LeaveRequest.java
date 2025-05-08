package com.example.assignment;

import java.util.Date;

public class LeaveRequest {
    private String id;
    private String employeeId;
    private String employeeName;
    private Date applicationDate;
    private Date fromDate;
    private Date toDate;
    private double totalDays;
    private String leaveType;
    private String reason;
    private String status;
    private String feedback;
    private String startDayShift;
    private String endDayShift;
    private boolean hasAttachments;

    // Required empty constructor for Firestore
    public LeaveRequest() {
    }

    public LeaveRequest(String employeeId, String employeeName, Date applicationDate,
                       Date fromDate, Date toDate, double totalDays, String leaveType,
                       String reason) {
        this.employeeId = employeeId;
        this.employeeName = employeeName;
        this.applicationDate = applicationDate;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.totalDays = totalDays;
        this.leaveType = leaveType;
        this.reason = reason;
        this.status = "pending";
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public Date getApplicationDate() {
        return applicationDate;
    }

    public void setApplicationDate(Date applicationDate) {
        this.applicationDate = applicationDate;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    public double getTotalDays() {
        return totalDays;
    }

    public void setTotalDays(double totalDays) {
        this.totalDays = totalDays;
    }

    public String getLeaveType() {
        return leaveType;
    }

    public void setLeaveType(String leaveType) {
        this.leaveType = leaveType;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public String getStartDayShift() {
        return startDayShift;
    }

    public void setStartDayShift(String startDayShift) {
        this.startDayShift = startDayShift;
    }

    public String getEndDayShift() {
        return endDayShift;
    }

    public void setEndDayShift(String endDayShift) {
        this.endDayShift = endDayShift;
    }

    public boolean isHasAttachments() {
        return hasAttachments;
    }

    public void setHasAttachments(boolean hasAttachments) {
        this.hasAttachments = hasAttachments;
    }
} 