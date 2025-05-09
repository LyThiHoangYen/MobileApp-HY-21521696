package com.example.assignment;

import android.os.Bundle;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ManagerHomeActivity extends BaseDashboardActivity {

    @Override
    protected String getDashboardTitle() {
        return "Dashboard";
    }

    @Override
    protected List<MenuItem> getMenuItems() {
        List<MenuItem> items = new ArrayList<>();
        
        // Add menu items for manager - similar but with manager-specific items
        items.add(new MenuItem(
                "Điểm danh/ Chấm công", 
                "Điểm danh bằng QR code", 
                android.R.drawable.ic_menu_camera, 
                DiemDanhActivity.class));
        
        items.add(new MenuItem(
                "Tóm tắt chấm công", 
                "Kiểm tra các kết quả chấm công", 
                android.R.drawable.ic_menu_my_calendar, 
                tom_tat__diem_danh.class));
        
        items.add(new MenuItem(
                "Viết đơn xin nghỉ phép", 
                "Viết đơn xin nghỉ phép", 
                android.R.drawable.ic_menu_edit, 
                donxinphep.class));
        
        items.add(new MenuItem(
                "Tình trạng đơn xin phép", 
                "Kiểm tra tình trạng các đơn xin nghỉ của mình", 
                android.R.drawable.ic_menu_agenda, 
                TinhTrangDonPhep.class));
        
        items.add(new MenuItem(
                "Duyệt đơn nghỉ phép", 
                "Phê duyệt các đơn xin nghỉ của nhân viên", 
                android.R.drawable.ic_menu_view, 
                DuyetDonActivity.class));
        
        items.add(new MenuItem(
                "Tạo thông báo", 
                "Tạo thông báo trên bảng tin", 
                android.R.drawable.ic_dialog_email, 
                ThongBaoActivity.class));
        
        return items;
    }
    
    @Override
    protected void setupBottomNavigation() {
        super.setupBottomNavigation();
        
        // You can customize bottom navigation here
        homeNav.setOnClickListener(v -> {
            Toast.makeText(this, "Trang chủ", Toast.LENGTH_SHORT).show();
            // Already on home screen
        });
        
        messageNav.setOnClickListener(v -> {
            Toast.makeText(this, "Tin nhắn", Toast.LENGTH_SHORT).show();
            // Add message activity launch here
        });
        
        profileNav.setOnClickListener(v -> {
            Toast.makeText(this, "Hồ sơ", Toast.LENGTH_SHORT).show();
            // Add profile activity launch here
        });
    }
} 