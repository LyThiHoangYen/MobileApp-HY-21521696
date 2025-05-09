package com.example.assignment;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;

public class CreateAnnouncementDialog extends DialogFragment {

    private EditText etTitle;
    private EditText etContent;
    private Button btnCancel;
    private Button btnPost;
    
    private FirebaseAuth auth;
    private FirebaseFirestore db;
    
    private OnAnnouncementCreatedListener listener;
    
    public interface OnAnnouncementCreatedListener {
        void onAnnouncementCreated();
    }
    
    public void setOnAnnouncementCreatedListener(OnAnnouncementCreatedListener listener) {
        this.listener = listener;
    }
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_create_announcement, container, false);
        
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        
        etTitle = view.findViewById(R.id.et_announcement_title);
        etContent = view.findViewById(R.id.et_announcement_content);
        btnCancel = view.findViewById(R.id.btn_cancel);
        btnPost = view.findViewById(R.id.btn_post);
        
        btnCancel.setOnClickListener(v -> dismiss());
        
        btnPost.setOnClickListener(v -> {
            String title = etTitle.getText().toString().trim();
            String content = etContent.getText().toString().trim();
            
            if (title.isEmpty()) {
                etTitle.setError("Vui lòng nhập tiêu đề");
                return;
            }
            
            if (content.isEmpty()) {
                etContent.setError("Vui lòng nhập nội dung");
                return;
            }
            
            createAnnouncement(title, content);
        });
        
        return view;
    }
    
    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.WRAP_CONTENT;
            dialog.getWindow().setLayout(width, height);
        }
    }
    
    private void createAnnouncement(String title, String content) {
        if (auth.getCurrentUser() == null) {
            Toast.makeText(getContext(), "Bạn cần đăng nhập để đăng thông báo", Toast.LENGTH_SHORT).show();
            return;
        }
        
        String uid = auth.getCurrentUser().getUid();
        
        // First get user information
        db.collection("employees").document(uid)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String authorName = documentSnapshot.getString("name");
                        if (authorName == null || authorName.isEmpty()) {
                            authorName = "Người dùng ẩn danh";
                        }
                        
                        // Create the announcement
                        Announcement announcement = new Announcement(
                                title,
                                content,
                                uid,
                                authorName,
                                Timestamp.now()
                        );
                        
                        // Save to Firestore
                        db.collection("announcements")
                                .add(announcement)
                                .addOnSuccessListener(documentReference -> {
                                    Toast.makeText(getContext(), "Đăng thông báo thành công", Toast.LENGTH_SHORT).show();
                                    if (listener != null) {
                                        listener.onAnnouncementCreated();
                                    }
                                    dismiss();
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(getContext(), "Đăng thông báo thất bại: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                });
                    } else {
                        Toast.makeText(getContext(), "Không tìm thấy thông tin người dùng", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Lỗi khi lấy thông tin người dùng: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
} 