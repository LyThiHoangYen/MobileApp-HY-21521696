package com.example.assignment;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecycleViewData extends RecyclerView.Adapter<RecycleViewData.DataViewHolder> {
    private static final String TAG = "RecycleViewData";
    private List<diemdanh> diemdanhItems;
    private Context context;
    
    public RecycleViewData(Context context, List<diemdanh> diemdanh) {
        this.context = context;
        this.diemdanhItems = diemdanh;
        Log.d(TAG, "RecycleViewData: Initialized with " + (diemdanh != null ? diemdanh.size() : 0) + " items");
    }
    
    @NonNull
    @Override
    public RecycleViewData.DataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_diem_danh, parent, false);
        return new DataViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecycleViewData.DataViewHolder holder, int position) {
        try {
            diemdanh item = diemdanhItems.get(position);
            
            if (item != null) {
                holder.content.setText(item.getContent() != null ? item.getContent() : "");
                holder.date.setText(item.getDate() != null ? item.getDate() : "");
                holder.time.setText(item.getTime() != null ? item.getTime() : "");
                
                // Make view visible in case it was previously set to gone
                holder.itemView.setVisibility(View.VISIBLE);
            } else {
                // If item is null, hide the view
                holder.itemView.setVisibility(View.GONE);
                Log.e(TAG, "Null item at position " + position);
            }
        } catch (Exception e) {
            Log.e(TAG, "Error binding data at position " + position, e);
            holder.itemView.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        int count = diemdanhItems == null ? 0 : diemdanhItems.size();
        Log.d(TAG, "getItemCount: " + count);
        return count;
    }

    public static class DataViewHolder extends RecyclerView.ViewHolder {
        TextView content;
        TextView date;
        TextView time;
        
        public DataViewHolder(View itemView) {
            super(itemView);
            content = itemView.findViewById(R.id.content);
            date = itemView.findViewById(R.id.date);
            time = itemView.findViewById(R.id.time);
        }
    }
    
    // Update data and refresh the view
    public void updateData(List<diemdanh> newData) {
        this.diemdanhItems = newData;
        notifyDataSetChanged();
        Log.d(TAG, "updateData: Updated with " + (newData != null ? newData.size() : 0) + " items");
    }
}
