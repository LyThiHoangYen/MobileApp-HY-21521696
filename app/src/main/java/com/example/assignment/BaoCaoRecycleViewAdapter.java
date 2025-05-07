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

public class BaoCaoRecycleViewAdapter extends RecyclerView.Adapter<BaoCaoRecycleViewAdapter.MyViewHolder> {

    private List<Report> reportList;
    Context context;
    public BaoCaoRecycleViewAdapter(Context ct, List<Report> listReport){
        this.context=ct;
        this.reportList=listReport;

    }

    @NonNull
    @Override
    public BaoCaoRecycleViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        LayoutInflater inflater=LayoutInflater.from(context);
//        View view =inflater.inflate(R.layout.danh_ba_nhan_su_row,parent,false);
//        return new DanhBaRecycleViewAdapter.MyViewHolder(view);
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.thong_ke_bao_cao_row, parent, false);

        return new BaoCaoRecycleViewAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull BaoCaoRecycleViewAdapter.MyViewHolder holder, int position) {
        Report report=reportList.get(position);
        holder.r_name.setText(report.getR_name());
        holder.r_description.setText(report.getR_description());
        holder.r_date.setText(report.getR_date());
    }

    @Override
    public int getItemCount() {
        return reportList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView r_name,r_description,r_date;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            r_name=itemView.findViewById(R.id.r_name);
            r_description=itemView.findViewById(R.id.r_des);
            r_date=itemView.findViewById(R.id.r_date);
        }
    }
}
