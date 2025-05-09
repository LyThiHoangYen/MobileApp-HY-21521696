package com.example.assignment;

import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AnnouncementAdapter extends RecyclerView.Adapter<AnnouncementAdapter.AnnouncementViewHolder> {

    private List<Announcement> announcementList;
    private Context context;

    public AnnouncementAdapter(List<Announcement> announcementList, Context context) {
        this.announcementList = announcementList;
        this.context = context;
    }

    @NonNull
    @Override
    public AnnouncementViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_announcement, parent, false);
        return new AnnouncementViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AnnouncementViewHolder holder, int position) {
        Announcement announcement = announcementList.get(position);
        
        holder.tvTitle.setText(announcement.getTitle());
        holder.tvContent.setText(announcement.getContent());
        holder.tvAuthor.setText(announcement.getAuthorName());
        
        // Format timestamp to relative time
        if (announcement.getTimestamp() != null) {
            long timeMillis = announcement.getTimestamp().toDate().getTime();
            String timeAgo = DateUtils.getRelativeTimeSpanString(
                    timeMillis,
                    System.currentTimeMillis(),
                    DateUtils.MINUTE_IN_MILLIS
            ).toString();
            holder.tvTimestamp.setText(timeAgo);
        } else {
            holder.tvTimestamp.setText("Không rõ thời gian");
        }
    }

    @Override
    public int getItemCount() {
        return announcementList.size();
    }

    static class AnnouncementViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvContent, tvAuthor, tvTimestamp;

        public AnnouncementViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_announcement_title);
            tvContent = itemView.findViewById(R.id.tv_announcement_content);
            tvAuthor = itemView.findViewById(R.id.tv_announcement_author);
            tvTimestamp = itemView.findViewById(R.id.tv_announcement_timestamp);
        }
    }
} 