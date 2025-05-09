package com.example.assignment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class DuyetDonActivity extends AppCompatActivity {
    private static final String TAG = "DuyetDonActivity";

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private LinearLayout emptyStateLayout;
    private TextView pendingCount, approvedCount, rejectedCount;

    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private LeaveRequestAdapter adapter;
    private List<LeaveRequest> leaveRequests = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_duyet_don);

        // Khởi tạo Firebase
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Thiết lập toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Duyệt đơn nghỉ phép");
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        // Ánh xạ view
        recyclerView = findViewById(R.id.recyclerViewLeaveRequests);
        progressBar = findViewById(R.id.progressBar);
        emptyStateLayout = findViewById(R.id.emptyStateLayout);
        pendingCount = findViewById(R.id.pendingCount);
        approvedCount = findViewById(R.id.approvedCount);
        rejectedCount = findViewById(R.id.rejectedCount);

        // Thiết lập RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new LeaveRequestAdapter(leaveRequests);
        recyclerView.setAdapter(adapter);

        // Tải danh sách đơn xin nghỉ phép
        loadLeaveRequests();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Tải lại dữ liệu khi quay lại màn hình này
        loadLeaveRequests();
    }

    private void loadLeaveRequests() {
        if (auth.getCurrentUser() == null) {
            Snackbar.make(recyclerView, "Bạn cần đăng nhập để xem thông tin", Snackbar.LENGTH_LONG).show();
            return;
        }

        showLoading(true);

        // Lấy tất cả đơn xin nghỉ phép đang chờ duyệt
        db.collection("leave_requests")
                .whereEqualTo("status", "pending")
                .orderBy("applicationDate", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    leaveRequests.clear();
                    
                    // Count for statistics (get all statuses with separate queries)
                    updateLeaveStatistics();
                    
                    // Only add pending requests to the list
                    for (DocumentSnapshot document : queryDocumentSnapshots) {
                        LeaveRequest leaveRequest = document.toObject(LeaveRequest.class);
                        if (leaveRequest != null) {
                            leaveRequest.setId(document.getId());
                            leaveRequests.add(leaveRequest);
                        }
                    }

                    // Update UI based on results
                    if (leaveRequests.isEmpty()) {
                        showEmptyState(true);
                    } else {
                        showEmptyState(false);
                        adapter.notifyDataSetChanged();
                    }

                    showLoading(false);
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error loading leave requests", e);
                    Snackbar.make(recyclerView, "Lỗi khi tải dữ liệu: " + e.getMessage(), Snackbar.LENGTH_LONG).show();
                    showLoading(false);
                    showEmptyState(true);
                });
    }

    private void showLoading(boolean isLoading) {
        progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        recyclerView.setVisibility(isLoading ? View.GONE : View.VISIBLE);
        
        // Hide empty state when loading
        if (isLoading) {
            emptyStateLayout.setVisibility(View.GONE);
        }
    }

    private void showEmptyState(boolean isEmpty) {
        emptyStateLayout.setVisibility(isEmpty ? View.VISIBLE : View.GONE);
        recyclerView.setVisibility(isEmpty ? View.GONE : View.VISIBLE);
        
        // Always ensure progress bar is hidden when showing empty state
        progressBar.setVisibility(View.GONE);
    }

    // RecyclerView Adapter for leave requests
    private class LeaveRequestAdapter extends RecyclerView.Adapter<LeaveRequestAdapter.ViewHolder> {
        private List<LeaveRequest> data;
        private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

        public LeaveRequestAdapter(List<LeaveRequest> data) {
            this.data = data;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_duyet_don, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            LeaveRequest request = data.get(position);

            // Hiển thị tên nhân viên
            holder.tvEmployeeName.setText(request.getEmployeeName());
            
            // Format date range
            String dateRange = "Từ " + formatDate(request.getFromDate()) + " đến " + formatDate(request.getToDate());
            holder.tvDateRange.setText(dateRange);
            
            // Hiển thị số ngày nghỉ
            holder.tvDays.setText(formatDays(request.getTotalDays()));

            // Hiển thị lý do
            if (request.getReason() != null && !request.getReason().isEmpty()) {
                holder.tvReason.setText(request.getReason());
            } else {
                holder.tvReason.setText("Không có lý do");
            }

            // Xử lý nút Duyệt
            holder.btnApprove.setOnClickListener(v -> {
                updateLeaveRequestStatus(request, "approved", "Đơn nghỉ phép của bạn đã được duyệt");
            });

            // Xử lý nút Từ chối
            holder.btnReject.setOnClickListener(v -> {
                showRejectDialog(request);
            });
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        private String formatDate(Date date) {
            if (date == null) return "";
            return dateFormat.format(date);
        }
        
        private String formatDays(double totalDays) {
            if (totalDays == 0.5) {
                return "0.5 ngày";
            } else if (totalDays == Math.floor(totalDays)) {
                return (int)totalDays + " ngày";
            } else {
                return totalDays + " ngày";
            }
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView tvEmployeeName, tvDateRange, tvDays, tvReason;
            Button btnApprove, btnReject;

            ViewHolder(View itemView) {
                super(itemView);
                tvEmployeeName = itemView.findViewById(R.id.tvEmployeeName);
                tvDateRange = itemView.findViewById(R.id.tvDateRange);
                tvDays = itemView.findViewById(R.id.tvDays);
                tvReason = itemView.findViewById(R.id.tvReason);
                btnApprove = itemView.findViewById(R.id.btnApprove);
                btnReject = itemView.findViewById(R.id.btnReject);
            }
        }
    }

    private void updateLeaveRequestStatus(LeaveRequest request, String status, String feedback) {
        if (request == null || request.getId() == null) {
            Toast.makeText(this, "Không thể cập nhật đơn này", Toast.LENGTH_SHORT).show();
            return;
        }

        showLoading(true);

        Map<String, Object> updates = new HashMap<>();
        updates.put("status", status);
        updates.put("feedback", feedback);
        updates.put("approvedBy", auth.getCurrentUser().getUid());
        updates.put("approvedDate", FieldValue.serverTimestamp());

        db.collection("leave_requests").document(request.getId())
                .update(updates)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, status.equals("approved") ? "Đã duyệt đơn" : "Đã từ chối đơn", Toast.LENGTH_SHORT).show();
                    // Remove the request from the current list
                    leaveRequests.remove(request);
                    
                    // Update UI based on whether there are any requests left
                    if (leaveRequests.isEmpty()) {
                        showEmptyState(true);
                    } else {
                        adapter.notifyDataSetChanged();
                    }
                    
                    // Update statistics
                    updateLeaveStatistics();
                    
                    showLoading(false);
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error updating leave request", e);
                    Toast.makeText(this, "Lỗi khi cập nhật: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    showLoading(false);
                });
    }

    private void showRejectDialog(LeaveRequest request) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Từ chối đơn nghỉ phép");
        
        // Tạo input field cho lý do từ chối
        final View customLayout = getLayoutInflater().inflate(R.layout.dialog_reject_reason, null);
        builder.setView(customLayout);
        
        builder.setPositiveButton("Xác nhận", (dialog, which) -> {
            TextView editTextReason = customLayout.findViewById(R.id.editTextReason);
            String reason = editTextReason.getText().toString().trim();
            
            if (reason.isEmpty()) {
                reason = "Đơn nghỉ phép của bạn đã bị từ chối";
            } else {
                reason = "Đơn nghỉ phép của bạn đã bị từ chối với lý do: " + reason;
            }
            
            updateLeaveRequestStatus(request, "rejected", reason);
        });
        
        builder.setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss());
        
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    // New method to get statistics for all request types
    private void updateLeaveStatistics() {
        // Get pending count
        db.collection("leave_requests")
                .whereEqualTo("status", "pending")
                .get()
                .addOnSuccessListener(pendingDocs -> {
                    pendingCount.setText(String.valueOf(pendingDocs.size()));
                });
                
        // Get approved count
        db.collection("leave_requests")
                .whereEqualTo("status", "approved")
                .get()
                .addOnSuccessListener(approvedDocs -> {
                    approvedCount.setText(String.valueOf(approvedDocs.size()));
                });
                
        // Get rejected count  
        db.collection("leave_requests")
                .whereEqualTo("status", "rejected")
                .get()
                .addOnSuccessListener(rejectedDocs -> {
                    rejectedCount.setText(String.valueOf(rejectedDocs.size()));
                });
    }
} 