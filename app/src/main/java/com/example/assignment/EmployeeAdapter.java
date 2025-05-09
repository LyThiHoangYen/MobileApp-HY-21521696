package com.example.assignment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class EmployeeAdapter extends RecyclerView.Adapter<EmployeeAdapter.EmployeeViewHolder> {

    private List<EmployeeData> employeeList;
    private Context context;

    public EmployeeAdapter(List<EmployeeData> employeeList, Context context) {
        this.employeeList = employeeList;
        this.context = context;
    }

    @NonNull
    @Override
    public EmployeeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_employee, parent, false);
        
        return new EmployeeViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull EmployeeViewHolder holder, int position) {
        EmployeeData employee = employeeList.get(position);
        
        holder.tvName.setText(employee.getName());
        holder.tvPosition.setText(employee.getPosition());
        holder.tvDepartment.setText(employee.getDepartment());
        
        // Load avatar image if available
        if (employee.getImageUrl() != null && !employee.getImageUrl().isEmpty()) {
            Glide.with(context)
                .load(employee.getImageUrl())
                .placeholder(R.drawable.default_avatar)
                .into(holder.imgAvatar);
        } else {
            holder.imgAvatar.setImageResource(R.drawable.default_avatar);
        }
        
        // Set click listener for employee item
        holder.cardView.setOnClickListener(v -> {
            // Handle employee item click (e.g., show employee details)
            // Intent intent = new Intent(context, EmployeeDetailActivity.class);
            // intent.putExtra("EMPLOYEE_ID", employee.getId());
            // context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return employeeList.size();
    }

    class EmployeeViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvPosition, tvDepartment;
        ImageView imgAvatar;
        CardView cardView;

        EmployeeViewHolder(View view) {
            super(view);
            tvName = view.findViewById(R.id.tv_employee_name);
            tvPosition = view.findViewById(R.id.tv_employee_position);
            tvDepartment = view.findViewById(R.id.tv_employee_department);
            imgAvatar = view.findViewById(R.id.img_employee_avatar);
            cardView = view.findViewById(R.id.card_employee);
        }
    }
} 