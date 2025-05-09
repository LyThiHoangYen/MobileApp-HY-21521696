package com.example.assignment;

import java.util.ArrayList;
import java.util.List;

/**
 * Helper class that provides menu items for different user roles
 */
public class DashboardMenuItems {
    
    /**
     * Get menu items for an employee dashboard
     */
    public static List<DashboardFragment.MenuItem> getEmployeeMenuItems() {
        List<DashboardFragment.MenuItem> items = new ArrayList<>();
        
        items.add(new DashboardFragment.MenuItem(
                "Điểm danh/ Chấm công", 
                "Điểm danh bằng QR code", 
                android.R.drawable.ic_menu_camera, 
                DiemDanhActivity.class));
        
        items.add(new DashboardFragment.MenuItem(
                "Tóm tắt chấm công", 
                "Kiểm tra các kết quả chấm công", 
                android.R.drawable.ic_menu_my_calendar, 
                tom_tat__diem_danh.class));
        
        items.add(new DashboardFragment.MenuItem(
                "Viết đơn xin nghỉ phép", 
                "Viết đơn xin nghỉ phép", 
                android.R.drawable.ic_menu_edit, 
                donxinphep.class));
        
        items.add(new DashboardFragment.MenuItem(
                "Tình trạng đơn xin phép", 
                "Kiểm tra tình trạng các đơn xin nghỉ của mình", 
                android.R.drawable.ic_menu_agenda, 
                TinhTrangDonPhep.class));
        
        return items;
    }
    
    /**
     * Get menu items for an HR dashboard
     */
    public static List<DashboardFragment.MenuItem> getHrMenuItems() {
        List<DashboardFragment.MenuItem> items = new ArrayList<>();
        
        // HR gets all employee items plus more
        items.addAll(getEmployeeMenuItems());
        
        // HR-specific items
        items.add(new DashboardFragment.MenuItem(
                "Tạo thông báo", 
                "Tạo thông báo trên bảng tin", 
                android.R.drawable.ic_dialog_email, 
                ThongBaoActivity.class));
        
        items.add(new DashboardFragment.MenuItem(
                "Quản lý nhân viên", 
                "Thêm, sửa, xóa thông tin nhân viên", 
                android.R.drawable.ic_menu_myplaces, 
                DanhBaNhanSuActivity.class));
        
        return items;
    }
    
    /**
     * Get menu items for a manager dashboard
     */
    public static List<DashboardFragment.MenuItem> getManagerMenuItems() {
        List<DashboardFragment.MenuItem> items = new ArrayList<>();
        
        // Manager gets all employee items
        items.addAll(getEmployeeMenuItems());
        
        // Manager-specific items
        items.add(new DashboardFragment.MenuItem(
                "Duyệt đơn nghỉ phép", 
                "Phê duyệt các đơn xin nghỉ của nhân viên", 
                android.R.drawable.ic_menu_view, 
                DuyetDonActivity.class));
        
        items.add(new DashboardFragment.MenuItem(
                "Tạo thông báo", 
                "Tạo thông báo trên bảng tin", 
                android.R.drawable.ic_dialog_email, 
                ThongBaoActivity.class));
        
        return items;
    }
} 