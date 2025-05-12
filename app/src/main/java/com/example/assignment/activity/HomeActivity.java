package com.example.assignment.activity;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.assignment.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.util.Arrays;
import java.util.List;

public class HomeActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        // TODO: Hiển thị danh sách món ăn, best seller, tìm kiếm

        RecyclerView rvCategories = findViewById(R.id.rvCategories);
        rvCategories.setLayoutManager(new GridLayoutManager(this, 4)); // 4 cột, 2 hàng
        // Khởi tạo danh sách danh mục
        List<Category> categories = Arrays.asList(
            new Category(R.drawable.iccategory1, "Hamburger"),
            new Category(R.drawable.iccategory2, "Pizza"),
            new Category(R.drawable.iccategory3, "Gà rán"),
            new Category(R.drawable.iccategory4, "Burger"),
            new Category(R.drawable.iccategory5, "Cơm"),
            new Category(R.drawable.iccategory6, "Bánh"),
            new Category(R.drawable.iccategory7, "Kem"),
            new Category(R.drawable.iccategory8, "Cookie")
        );
        // Khởi tạo adapter với listener
        CategoryAdapter adapter = new CategoryAdapter(categories, new CategoryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                // Xử lý khi chọn danh mục ở màn hình Home nếu cần
            }
        });
        rvCategories.setAdapter(adapter);
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