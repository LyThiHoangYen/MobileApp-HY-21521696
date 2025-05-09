package com.example.assignment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ImageView imgAvatar;
    private TextView tvName, tvEmail, tvPhone, tvDepartment, tvPosition;
    private MaterialButton btnEditProfile;
    private LinearLayout rowChangePassword, rowLogout;
    
    private FirebaseAuth auth;
    private FirebaseFirestore db;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        
        // Khởi tạo Firebase
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        // Ánh xạ view
        imgAvatar = view.findViewById(R.id.imgAvatar);
        tvName = view.findViewById(R.id.tvName);
        tvEmail = view.findViewById(R.id.tvEmail);
        tvPhone = view.findViewById(R.id.tvPhone);
        tvDepartment = view.findViewById(R.id.tvDepartment);
        tvPosition = view.findViewById(R.id.tvPosition);
        btnEditProfile = view.findViewById(R.id.btnEditProfile);
        rowChangePassword = view.findViewById(R.id.rowChangePassword);
        rowLogout = view.findViewById(R.id.rowLogout);
        
        // Thiết lập sự kiện
        setupEventListeners();
        
        // Tải thông tin người dùng
        loadUserProfile();
    }
    
    @Override
    public void onResume() {
        super.onResume();
        
        // Tải lại thông tin người dùng khi quay lại fragment
        loadUserProfile();
    }
    
    private void setupEventListeners() {
        // Nút chỉnh sửa hồ sơ
        btnEditProfile.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), EditProfileActivity.class);
            startActivity(intent);
        });
        
        // Đổi mật khẩu
        rowChangePassword.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ChangePasswordActivity.class);
            startActivity(intent);
        });
        
        // Đăng xuất
        rowLogout.setOnClickListener(v -> {
            // Xác nhận đăng xuất
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getContext());
            builder.setTitle("Đăng xuất");
            builder.setMessage("Bạn có chắc chắn muốn đăng xuất?");
            builder.setPositiveButton("Đăng xuất", (dialog, which) -> {
                // Đăng xuất khỏi Firebase
                auth.signOut();
                
                // Chuyển về màn hình đăng nhập
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            });
            builder.setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss());
            builder.show();
        });
    }
    
    private void loadUserProfile() {
        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            // Hiển thị email
            tvEmail.setText(user.getEmail());
            
            // Tải thông tin từ Firestore
            db.collection("users").document(user.getUid())
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            updateUIWithUserData(documentSnapshot);
                        } else {
                            // Nếu không có thông tin trong Firestore, sử dụng thông tin từ Firebase Auth
                            tvName.setText(user.getDisplayName() != null ? user.getDisplayName() : "Nhân viên");
                            tvPhone.setText("Chưa cập nhật");
                            tvDepartment.setText("Chưa cập nhật");
                            tvPosition.setText("Nhân viên");
                        }
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(getContext(), "Lỗi khi tải thông tin: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        } else {
            // Nếu chưa đăng nhập, chuyển về màn hình đăng nhập
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
            getActivity().finish();
        }
    }
    
    private void updateUIWithUserData(DocumentSnapshot document) {
        // Cập nhật UI với dữ liệu người dùng
        String name = document.getString("fullName");
        String phone = document.getString("phone");
        String department = document.getString("department");
        String position = document.getString("position");
        
        // Cập nhật UI
        tvName.setText(name != null ? name : "Nhân viên");
        tvPhone.setText(phone != null ? phone : "Chưa cập nhật");
        tvDepartment.setText(department != null ? department : "Chưa cập nhật");
        tvPosition.setText(position != null ? position : "Nhân viên");
        
        // TODO: Load avatar from Firebase Storage if available
    }
}