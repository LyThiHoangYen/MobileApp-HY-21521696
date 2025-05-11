package com.example.assignment.activity;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.assignment.R;

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
    }
} 