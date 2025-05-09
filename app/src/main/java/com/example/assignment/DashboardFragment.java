package com.example.assignment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

/**
 * Dashboard fragment for showing menu items
 * Use the {@link DashboardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DashboardFragment extends Fragment {
    private static final String ARG_USER_TYPE = "userType";
    
    private String userType;
    private LinearLayout menuContainer;
    private CardView cardSalary, cardBenefits, cardPerformance, cardAttendance;
    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private TextView tvUserName, tvUserRole;

    public DashboardFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param userType The user type (employee, hr, manager).
     * @return A new instance of fragment DashboardFragment.
     */
    public static DashboardFragment newInstance(String userType) {
        DashboardFragment fragment = new DashboardFragment();
        Bundle args = new Bundle();
        args.putString(ARG_USER_TYPE, userType);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Khởi tạo Firebase
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        
        if (getArguments() != null) {
            userType = getArguments().getString(ARG_USER_TYPE, "employee");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        
        // Find views
        menuContainer = view.findViewById(R.id.menu_container);
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
        
        // Load appropriate menu items based on user type
        setupMenuItems();
        
        return view;
    }
    
    private void setupCardClickListeners() {
        // Card Lương
        if (cardSalary != null) {
            cardSalary.setOnClickListener(v -> {
                // Có thể chuyển đến activity chi tiết lương
                // Intent intent = new Intent(getActivity(), SalaryDetailActivity.class);
                // startActivity(intent);
                showNotImplementedMessage("Xem chi tiết lương");
            });
        }
        
        // Card Phúc lợi
        if (cardBenefits != null) {
            cardBenefits.setOnClickListener(v -> {
                // Có thể chuyển đến activity chi tiết phúc lợi
                // Intent intent = new Intent(getActivity(), BenefitsDetailActivity.class);
                // startActivity(intent);
                showNotImplementedMessage("Xem phúc lợi");
            });
        }
        
        // Card Hiệu suất
        if (cardPerformance != null) {
            cardPerformance.setOnClickListener(v -> {
                // Có thể chuyển đến activity thống kê hiệu suất
                // Intent intent = new Intent(getActivity(), PerformanceActivity.class);
                // startActivity(intent);
                showNotImplementedMessage("Xem hiệu suất làm việc");
            });
        }
        
        // Card Chấm công
        if (cardAttendance != null) {
            cardAttendance.setOnClickListener(v -> {
                Intent intent = new Intent(getActivity(), tom_tat__diem_danh.class);
                startActivity(intent);
            });
        }
    }
    
    private void loadUserInfo() {
        if (auth.getCurrentUser() != null && tvUserName != null && tvUserRole != null) {
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
            Toast.makeText(getContext(), 
                feature + " đang được phát triển", 
                Toast.LENGTH_SHORT).show();
        }
    }
    
    private void setupMenuItems() {
        if (menuContainer == null) return;
        
        List<MenuItem> menuItems = getMenuItems();
        
        LayoutInflater inflater = getLayoutInflater();
        for (MenuItem item : menuItems) {
            View menuItemView = inflater.inflate(R.layout.dashboard_menu_item, menuContainer, false);
            
            // Set menu item data
            ImageView icon = menuItemView.findViewById(R.id.menuItemIcon);
            TextView title = menuItemView.findViewById(R.id.menuItemTitle);
            TextView description = menuItemView.findViewById(R.id.menuItemDescription);
            CardView card = menuItemView.findViewById(R.id.menuItemCard);
            
            icon.setImageResource(item.getIconResource());
            title.setText(item.getTitle());
            description.setText(item.getDescription());
            
            // Set click listener
            card.setOnClickListener(v -> {
                Toast.makeText(getContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
                if (item.getActivityClass() != null) {
                    Intent intent = new Intent(getActivity(), item.getActivityClass());
                    startActivity(intent);
                }
            });
            
            // Add to container
            menuContainer.addView(menuItemView);
        }
    }
    
    private List<MenuItem> getMenuItems() {
        // Common menu items for all user types
        List<MenuItem> items;
        
        switch (userType) {
            case "hr":
                items = DashboardMenuItems.getHrMenuItems();
                break;
            case "manager":
                items = DashboardMenuItems.getManagerMenuItems();
                break;
            case "employee":
            default:
                items = DashboardMenuItems.getEmployeeMenuItems();
                break;
        }
        
        return items;
    }
    
    // Inner class for menu items
    public static class MenuItem {
        private final String title;
        private final String description;
        private final int iconResource;
        private final Class<?> activityClass;
        
        public MenuItem(String title, String description, int iconResource, Class<?> activityClass) {
            this.title = title;
            this.description = description;
            this.iconResource = iconResource;
            this.activityClass = activityClass;
        }
        
        public String getTitle() {
            return title;
        }
        
        public String getDescription() {
            return description;
        }
        
        public int getIconResource() {
            return iconResource;
        }
        
        public Class<?> getActivityClass() {
            return activityClass;
        }
    }
}