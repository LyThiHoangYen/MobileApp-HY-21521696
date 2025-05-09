package com.example.assignment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class ManagerHomeActivity extends AppCompatActivity {

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_home);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Dashboard Quản Lý");
        setSupportActionBar(toolbar);

        loadFragment(new ManagerDashboardFragment());

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    toolbar.setTitle("Dashboard Quản Lý");
                    fragment = new ManagerDashboardFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_dashboard:
                    toolbar.setTitle("Thống Kê");
                    // Chuyển đến thống kê
                    Intent statsIntent = new Intent(ManagerHomeActivity.this, ThongKeBaoCaoActivity.class);
                    startActivity(statsIntent);
                    return true;
                case R.id.navigation_notifications:
                    toolbar.setTitle("Thông Báo");
                    fragment = new NotificationsFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_profile:
                    toolbar.setTitle("Hồ Sơ");
                    fragment = new ProfileFragment();
                    loadFragment(fragment);
                    return true;
            }
            return false;
        }
    };

    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
    
    /**
     * Trình đơn cho Manager Dashboard Fragment
     */
    public static class ManagerMenuItem {
        private final String title;
        private final String description;
        private final int iconResource;
        private final Class<?> activityClass;
        
        public ManagerMenuItem(String title, String description, int iconResource, Class<?> activityClass) {
            this.title = title;
            this.description = description;
            this.iconResource = iconResource;
            this.activityClass = activityClass;
        }
        
        public String getTitle() {
            return title;
        }
        
        public String getDescription() {
            return description;
        }
        
        public int getIconResource() {
            return iconResource;
        }
        
        public Class<?> getActivityClass() {
            return activityClass;
        }
    }
    
    /**
     * Lấy danh sách các mục menu cho Manager
     */
    public static List<ManagerMenuItem> getManagerMenuItems() {
        List<ManagerMenuItem> items = new ArrayList<>();
        
        // Add menu items for manager
        items.add(new ManagerMenuItem(
                "Điểm danh/ Chấm công", 
                "Điểm danh bằng QR code", 
                android.R.drawable.ic_menu_camera, 
                DiemDanhActivity.class));
        
        items.add(new ManagerMenuItem(
                "Tóm tắt chấm công", 
                "Kiểm tra các kết quả chấm công", 
                android.R.drawable.ic_menu_my_calendar, 
                tom_tat__diem_danh.class));
        
        items.add(new ManagerMenuItem(
                "Viết đơn xin nghỉ phép", 
                "Viết đơn xin nghỉ phép", 
                android.R.drawable.ic_menu_edit, 
                donxinphep.class));
        
        items.add(new ManagerMenuItem(
                "Tình trạng đơn xin phép", 
                "Kiểm tra tình trạng các đơn xin nghỉ của mình", 
                android.R.drawable.ic_menu_agenda, 
                TinhTrangDonPhep.class));
        
        items.add(new ManagerMenuItem(
                "Duyệt đơn nghỉ phép", 
                "Phê duyệt các đơn xin nghỉ của nhân viên", 
                android.R.drawable.ic_menu_view, 
                DuyetDonActivity.class));
        
        items.add(new ManagerMenuItem(
                "Tạo thông báo", 
                "Tạo thông báo trên bảng tin", 
                android.R.drawable.ic_dialog_email, 
                ThongBaoActivity.class));
        
        return items;
    }
}