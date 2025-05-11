package com.example.assignment.activity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.assignment.R;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {
    private final int[] icons = {
        R.drawable.iccategory1, R.drawable.iccategory2, R.drawable.iccategory3, R.drawable.iccategory4,
        R.drawable.iccategory5, R.drawable.iccategory6, R.drawable.iccategory7, R.drawable.iccategory8
    };
    private final String[] names = {
        "Thức uống", "Pizza", "Gà rán", "Burger",
        "Cơm", "Tráng miệng", "Đồ ăn nhanh", "Kem"
    };

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        holder.imgCategory.setImageResource(icons[position]);
        holder.txtCategory.setText(names[position]);
    }

    @Override
    public int getItemCount() {
        return icons.length;
    }

    static class CategoryViewHolder extends RecyclerView.ViewHolder {
        ImageView imgCategory;
        TextView txtCategory;
        CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            imgCategory = itemView.findViewById(R.id.imgCategory);
            txtCategory = itemView.findViewById(R.id.txtCategory);
        }
    }
} 