package com.example.assignment.activity;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.assignment.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        // TODO: Hiển thị danh sách món ăn, best seller, tìm kiếm

        RecyclerView rvCategories = findViewById(R.id.rvCategories);
        rvCategories.setLayoutManager(new GridLayoutManager(this, 4)); // 4 cột, 2 hàng
        rvCategories.setAdapter(new CategoryAdapter());
        rvCategories.setNestedScrollingEnabled(false); // Đảm bảo hiển thị đủ trong NestedScrollView

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav_profile) {
                Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);
                startActivity(intent);
                return true;
            } else if (item.getItemId() == R.id.nav_burger) {
                Intent intent = new Intent(HomeActivity.this, FoodListActivity.class);
                startActivity(intent);
                return true;
            }
            // Xử lý các mục khác nếu cần
            return false;
        });
    }
} 