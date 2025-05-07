package com.example.assignment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DanhBaRecycleViewAdapter extends RecyclerView.Adapter<DanhBaRecycleViewAdapter.MyViewHolder> {
    private List<Staff> staffList;
    Context context;

    private RecyclerViewClickListener listener;

    public DanhBaRecycleViewAdapter(Context ct, List<Staff> listStaff, RecyclerViewClickListener listener) {
        this.context = ct;
        this.staffList = listStaff;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        LayoutInflater inflater=LayoutInflater.from(context);
//        View view =inflater.inflate(R.layout.danh_ba_nhan_su_row,parent,false);
//        return new DanhBaRecycleViewAdapter.MyViewHolder(view);
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.danh_ba_nhan_su_row, parent, false);

        return new DanhBaRecycleViewAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Staff staff = staffList.get(position);
        holder.name.setText(staff.getStaffName());
        holder.phong_ban.setText(staff.getPhongBan());
        holder.id.setText(String.valueOf(staff.getStaffId()));
        holder.sex.setText(staff.getSex());
        holder.pathImage.setImageResource(staff.getPathImage());
        holder.ssn.setText(String.valueOf(position + 1));
    }

    @Override
    public int getItemCount() {
        return staffList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView name, phong_ban, id, sex, ssn;
        ImageView pathImage;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            phong_ban = itemView.findViewById(R.id.position);
            id = itemView.findViewById(R.id.id_num);
            sex = itemView.findViewById(R.id.sex);
            pathImage = itemView.findViewById(R.id.avatar);
            ssn = itemView.findViewById((R.id.number));

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onClick(v, getAdapterPosition());
        }
    }

    public interface RecyclerViewClickListener {
        void onClick(View v, int position);
    }
}
