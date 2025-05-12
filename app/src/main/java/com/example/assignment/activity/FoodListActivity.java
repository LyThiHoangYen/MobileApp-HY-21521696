package com.example.assignment.activity;

import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.assignment.R;
import java.util.Arrays;
import java.util.List;
import com.google.firebase.FirebaseApp;

public class FoodListActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        setContentView(R.layout.activity_food_list);

        // Sự kiện click vào icon giỏ hàng
        ImageView ivCart = findViewById(R.id.ivCart);
        ivCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FoodListActivity.this, CartActivity.class);
                startActivity(intent);
            }
        });

        RecyclerView rvCategory = findViewById(R.id.rvCategory);
        List<Category> categories = Arrays.asList(
                new Category(R.drawable.iccategory1, "Hamburger"),
                new Category(R.drawable.iccategory2, "Pizza"),
                new Category(R.drawable.iccategory3, "Gà rán"),
                new Category(R.drawable.iccategory4, "Món 4"),
                new Category(R.drawable.iccategory5, "Món 5"),
                new Category(R.drawable.iccategory6, "Món 6"),
                new Category(R.drawable.iccategory7, "Món 7"),
                new Category(R.drawable.iccategory8, "Món 8")
        );
        CategoryAdapter adapter = new CategoryAdapter(categories, new CategoryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                // TODO: Xử lý khi chọn danh mục, ví dụ cập nhật danh sách món ăn
                // Bạn có thể cập nhật TextView tvCategoryTitle hoặc load lại rvFoodList ở đây
            }
        });
        rvCategory.setAdapter(adapter);
        rvCategory.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
    }
} 