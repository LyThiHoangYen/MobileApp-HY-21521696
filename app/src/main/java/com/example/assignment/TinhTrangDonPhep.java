package com.example.assignment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TinhTrangDonPhep extends AppCompatActivity {
    private static final String TAG = "TinhTrangDonPhep";

    private RecyclerView recyclerView;
    private TabLayout tabLayout;
    private ProgressBar progressBar;
    private LinearLayout emptyStateLayout;
    private Button btnCreateLeaveRequest;
    private TextView pendingCount, approvedCount, rejectedCount;

    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private LeaveRequestAdapter adapter;
    private List<LeaveRequest> leaveRequests = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tinh_trang_don_phep);

        // Initialize Firebase components
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Set up the toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        // Initialize views
        recyclerView = findViewById(R.id.recyclerViewLeaveRequests);
        tabLayout = findViewById(R.id.tabLayout);
        progressBar = findViewById(R.id.progressBar);
        emptyStateLayout = findViewById(R.id.emptyStateLayout);
        btnCreateLeaveRequest = findViewById(R.id.btnCreateLeaveRequest);
        pendingCount = findViewById(R.id.pendingCount);
        approvedCount = findViewById(R.id.approvedCount);
        rejectedCount = findViewById(R.id.rejectedCount);

        // Set up RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new LeaveRequestAdapter(leaveRequests);
        recyclerView.setAdapter(adapter);

        // Set up tab selection listener
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                filterLeaveRequests(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        // Set up create leave request button
        btnCreateLeaveRequest.setOnClickListener(v -> {
            Intent intent = new Intent(TinhTrangDonPhep.this, donxinphep.class);
            startActivity(intent);
        });

        // Load leave requests
        loadLeaveRequests();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Reload data when returning to this screen
        loadLeaveRequests();
    }

    private void loadLeaveRequests() {
        if (auth.getCurrentUser() == null) {
            Snackbar.make(recyclerView, "Bạn cần đăng nhập để xem thông tin", Snackbar.LENGTH_LONG).show();
            return;
        }

        String userId = auth.getCurrentUser().getUid();
        showLoading(true);

        db.collection("leave_requests")
                .whereEqualTo("employeeId", userId)
                .orderBy("applicationDate", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    leaveRequests.clear();
                    int pendingCounter = 0;
                    int approvedCounter = 0;
                    int rejectedCounter = 0;

                    for (DocumentSnapshot document : queryDocumentSnapshots) {
                        LeaveRequest leaveRequest = document.toObject(LeaveRequest.class);
                        if (leaveRequest != null) {
                            leaveRequest.setId(document.getId());
                            leaveRequests.add(leaveRequest);

                            // Count by status
                            switch (leaveRequest.getStatus()) {
                                case "pending":
                                    pendingCounter++;
                                    break;
                                case "approved":
                                    approvedCounter++;
                                    break;
                                case "rejected":
                                    rejectedCounter++;
                                    break;
                            }
                        }
                    }

                    // Update counters
                    pendingCount.setText(String.valueOf(pendingCounter));
                    approvedCount.setText(String.valueOf(approvedCounter));
                    rejectedCount.setText(String.valueOf(rejectedCounter));

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

    private void filterLeaveRequests(int tabPosition) {
        if (adapter == null) return;

        List<LeaveRequest> filteredList = new ArrayList<>();
        String statusFilter = null;

        switch (tabPosition) {
            case 0: // All
                filteredList.addAll(leaveRequests);
                break;
            case 1: // Pending
                statusFilter = "pending";
                break;
            case 2: // Approved
                statusFilter = "approved";
                break;
            case 3: // Rejected
                statusFilter = "rejected";
                break;
        }

        if (statusFilter != null) {
            for (LeaveRequest request : leaveRequests) {
                if (statusFilter.equals(request.getStatus())) {
                    filteredList.add(request);
                }
            }
        }

        adapter.updateData(filteredList);
        showEmptyState(filteredList.isEmpty());
    }

    private void showLoading(boolean isLoading) {
        progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        recyclerView.setVisibility(isLoading ? View.GONE : View.VISIBLE);
    }

    private void showEmptyState(boolean isEmpty) {
        emptyStateLayout.setVisibility(isEmpty ? View.VISIBLE : View.GONE);
        recyclerView.setVisibility(isEmpty ? View.GONE : View.VISIBLE);
    }

    // RecyclerView Adapter for leave requests
    private class LeaveRequestAdapter extends RecyclerView.Adapter<LeaveRequestAdapter.ViewHolder> {
        private List<LeaveRequest> data;
        private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

        public LeaveRequestAdapter(List<LeaveRequest> data) {
            this.data = data;
        }

        public void updateData(List<LeaveRequest> newData) {
            this.data = newData;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_don_xin_phep, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            LeaveRequest request = data.get(position);

            // Format date range
            String dateRange = "Từ " + formatDate(request.getFromDate()) + " đến " + formatDate(request.getToDate());
            holder.tvDate.setText(dateRange);

            // Set status with appropriate style
            setStatusText(holder.tvStatus, request.getStatus());

            // Set reason if available
            if (request.getReason() != null && !request.getReason().isEmpty()) {
                holder.tvReason.setText(request.getReason());
            } else {
                holder.tvReason.setText("Không có lý do");
            }

            // Set click listener for the entire item
            holder.itemView.setOnClickListener(v -> {
                // Show dialog or navigate to detail view
                showLeaveRequestDetails(request);
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

        private void setStatusText(TextView textView, String status) {
            switch (status) {
                case "pending":
                    textView.setText("Đang chờ");
                    textView.setTextColor(getResources().getColor(android.R.color.holo_blue_dark));
                    break;
                case "approved":
                    textView.setText("Đã duyệt");
                    textView.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
                    break;
                case "rejected":
                    textView.setText("Từ chối");
                    textView.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
                    break;
                default:
                    textView.setText("Không xác định");
                    textView.setTextColor(getResources().getColor(android.R.color.darker_gray));
            }
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView tvDate, tvStatus, tvReason;

            ViewHolder(View itemView) {
                super(itemView);
                tvDate = itemView.findViewById(R.id.tvDate);
                tvStatus = itemView.findViewById(R.id.tvStatus);
                tvReason = itemView.findViewById(R.id.tvReason);
            }
        }
    }

    private void showLeaveRequestDetails(LeaveRequest request) {
        // Create and show an AlertDialog with leave request details
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        builder.setTitle("Chi tiết đơn xin nghỉ phép");

        // Prepare the detail message
        StringBuilder details = new StringBuilder();
        details.append("Ngày làm đơn: ").append(new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(request.getApplicationDate())).append("\n\n");
        details.append("Thời gian nghỉ: ").append(new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(request.getFromDate()))
              .append(" - ").append(new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(request.getToDate())).append("\n\n");
        
        // Format total days
        String totalDaysText;
        if (request.getTotalDays() == 0.5) {
            totalDaysText = "0.5 ngày";
        } else if (request.getTotalDays() == Math.floor(request.getTotalDays())) {
            totalDaysText = (int)request.getTotalDays() + " ngày";
        } else {
            totalDaysText = request.getTotalDays() + " ngày";
        }
        details.append("Tổng số ngày: ").append(totalDaysText).append("\n\n");
        
        // Leave type
        String leaveType;
        switch (request.getLeaveType()) {
            case "annual_leave":
                leaveType = "Nghỉ phép thường năm";
                break;
            case "medical_leave":
                leaveType = "Nghỉ phép y tế";
                break;
            case "other":
                leaveType = "Lý do khác";
                break;
            default:
                leaveType = "Không xác định";
        }
        details.append("Loại nghỉ phép: ").append(leaveType).append("\n\n");
        
        // Reason if available
        if (request.getReason() != null && !request.getReason().isEmpty()) {
            details.append("Lý do: ").append(request.getReason()).append("\n\n");
        }
        
        // Status
        String status;
        switch (request.getStatus()) {
            case "pending":
                status = "Đang chờ";
                break;
            case "approved":
                status = "Đã duyệt";
                break;
            case "rejected":
                status = "Từ chối";
                break;
            default:
                status = "Không xác định";
        }
        details.append("Trạng thái: ").append(status).append("\n\n");
        
        // Feedback if available
        if (request.getFeedback() != null && !request.getFeedback().isEmpty()) {
            details.append("Phản hồi: ").append(request.getFeedback());
        }
        
        builder.setMessage(details.toString());
        
        // Add buttons
        builder.setPositiveButton("Đóng", null);
        
        // Add cancel button if status is pending
        if ("pending".equals(request.getStatus())) {
            builder.setNegativeButton("Hủy đơn", (dialog, which) -> {
                cancelLeaveRequest(request);
            });
        }
        
        builder.show();
    }

    private void cancelLeaveRequest(LeaveRequest request) {
        if (request == null || request.getId() == null) {
            Snackbar.make(recyclerView, "Không thể hủy đơn này", Snackbar.LENGTH_SHORT).show();
            return;
        }

        showLoading(true);
        db.collection("leave_requests").document(request.getId())
                .delete()
                .addOnSuccessListener(aVoid -> {
                    Snackbar.make(recyclerView, "Đã hủy đơn xin nghỉ phép", Snackbar.LENGTH_SHORT).show();
                    loadLeaveRequests(); // Reload the list
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error canceling leave request", e);
                    Snackbar.make(recyclerView, "Lỗi khi hủy đơn: " + e.getMessage(), Snackbar.LENGTH_LONG).show();
                    showLoading(false);
                });
    }
} 