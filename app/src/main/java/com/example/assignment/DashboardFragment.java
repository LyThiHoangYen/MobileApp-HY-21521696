package com.example.assignment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentSnapshot;

/**
 * Fragment hiển thị thống kê dữ liệu
 */
public class DashboardFragment extends Fragment {

    private CardView cardSalary, cardBenefits, cardPerformance, cardAttendance;
    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private TextView tvUserName, tvUserRole;

    public DashboardFragment() {
        // Required empty public constructor
    }

    public static DashboardFragment newInstance() {
        return new DashboardFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Khởi tạo Firebase
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                            Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        
        // Ánh xạ các thành phần UI
        tvUserName = view.findViewById(R.id.tv_user_name);
        tvUserRole = view.findViewById(R.id.tv_user_role);
        cardSalary = view.findViewById(R.id.card_salary);
        cardBenefits = view.findViewById(R.id.card_benefits);
        cardPerformance = view.findViewById(R.id.card_performance);
        cardAttendance = view.findViewById(R.id.card_attendance);
        
        // Thiết lập sự kiện click cho các card
        setupCardClickListeners();
        
        // Tải thông tin người dùng
        loadUserInfo();
        
        return view;
    }
    
    private void setupCardClickListeners() {
        // Card Lương
        cardSalary.setOnClickListener(v -> {
            // Có thể chuyển đến activity chi tiết lương
            // Intent intent = new Intent(getActivity(), SalaryDetailActivity.class);
            // startActivity(intent);
            showNotImplementedMessage("Xem chi tiết lương");
        });
        
        // Card Phúc lợi
        cardBenefits.setOnClickListener(v -> {
            // Có thể chuyển đến activity chi tiết phúc lợi
            // Intent intent = new Intent(getActivity(), BenefitsDetailActivity.class);
            // startActivity(intent);
            showNotImplementedMessage("Xem phúc lợi");
        });
        
        // Card Hiệu suất
        cardPerformance.setOnClickListener(v -> {
            // Có thể chuyển đến activity thống kê hiệu suất
            // Intent intent = new Intent(getActivity(), PerformanceActivity.class);
            // startActivity(intent);
            showNotImplementedMessage("Xem hiệu suất làm việc");
        });
        
        // Card Chấm công
        cardAttendance.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), tom_tat__diem_danh.class);
            startActivity(intent);
        });
    }
    
    private void loadUserInfo() {
        if (auth.getCurrentUser() != null) {
            String uid = auth.getCurrentUser().getUid();
            db.collection("employees").document(uid)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String fullName = documentSnapshot.getString("fullName");
                        String role = documentSnapshot.getString("role");
                        
                        if (fullName != null) {
                            tvUserName.setText(fullName);
                        }
                        
                        if (role != null) {
                            String displayRole = role.equals("MANAGER") ? "Quản lý" : 
                                               (role.equals("HR") ? "Nhân sự" : "Nhân viên");
                            tvUserRole.setText(displayRole);
                        }
                    }
                });
        }
    }
    
    private void showNotImplementedMessage(String feature) {
        if (getContext() != null) {
            android.widget.Toast.makeText(getContext(), 
                feature + " đang được phát triển", 
                android.widget.Toast.LENGTH_SHORT).show();
        }
    }
} 