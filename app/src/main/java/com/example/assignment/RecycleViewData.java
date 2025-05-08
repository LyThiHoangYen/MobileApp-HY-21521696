package com.example.assignment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.sql.Time;
import java.util.Date;
import java.util.List;

public class RecycleViewData extends RecyclerView.Adapter<RecycleViewData.DataViewHolder> {
    private List<diemdanh> Diemdanh;
    Context context;
    public RecycleViewData(Context context,List<diemdanh> diemdanh){
        this.context=context;
        this.Diemdanh=diemdanh;
    }
    @NonNull
    @Override

    public RecycleViewData.DataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView ;
        itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_diem_danh, parent, false);
        return new DataViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecycleViewData.DataViewHolder holder, int position) {
        diemdanh item=Diemdanh.get(position);
        holder.content.setText(item.getContent());
        holder.date.setText(item.getDate());
        holder.time.setText(item.getTime());
    }

//    @Override
//    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
//
//    }

    @Override
    public int getItemCount() {
        return Diemdanh==null ?0:Diemdanh.size();
    }

    public static class DataViewHolder extends RecyclerView.ViewHolder {
        TextView content;
        TextView date;
        TextView time;
        public  DataViewHolder(View itemView){
            super(itemView);
            content=(TextView)itemView.findViewById(R.id.content);
            date=(TextView)itemView.findViewById(R.id.date);
            time=(TextView)itemView.findViewById((R.id.time));
        }
    }


}
