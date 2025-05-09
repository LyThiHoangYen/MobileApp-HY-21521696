package com.example.assignment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class EditProfileActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_PICK = 1001;
    private static final int REQUEST_PERMISSION_CODE = 1002;

    // UI components
    private ImageView imgAvatar;
    private TextInputLayout tilFullName, tilPhone, tilDepartment, tilPosition;
    private TextInputEditText edtFullName, edtPhone;
    private AutoCompleteTextView edtDepartment, edtPosition;
    private MaterialButton btnSave, btnCancel;
    private FloatingActionButton fabChangePhoto;
    private FrameLayout loadingOverlay;
    private Toolbar toolbar;

    // Firebase
    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private Uri selectedImageUri;
    private boolean isPhotoChanged = false;

    // Department and position options
    private String[] departments = {"Phòng IT", "Phòng HR", "Phòng Marketing", "Phòng Sale", "Phòng Kế toán"};
    private String[] positions = {"Nhân viên", "Trưởng nhóm", "Quản lý", "Giám đốc", "CEO"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        // Initialize Firebase
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Set up toolbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // Initialize UI components
        initViews();
        setupClickListeners();
        setupDropdowns();

        // Load user data
        loadUserData();
    }

    private void initViews() {
        imgAvatar = findViewById(R.id.imgAvatar);
        tilFullName = findViewById(R.id.tilFullName);
        tilPhone = findViewById(R.id.tilPhone);
        tilDepartment = findViewById(R.id.tilDepartment);
        tilPosition = findViewById(R.id.tilPosition);
        
        edtFullName = findViewById(R.id.edtFullName);
        edtPhone = findViewById(R.id.edtPhone);
        edtDepartment = findViewById(R.id.edtDepartment);
        edtPosition = findViewById(R.id.edtPosition);
        
        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);
        fabChangePhoto = findViewById(R.id.fabChangePhoto);
        loadingOverlay = findViewById(R.id.loadingOverlay);
    }

    private void setupClickListeners() {
        // Change photo button
        fabChangePhoto.setOnClickListener(v -> {
            if (checkStoragePermission()) {
                openGallery();
            } else {
                requestStoragePermission();
            }
        });

        // Save button
        btnSave.setOnClickListener(v -> {
            if (validateInputs()) {
                saveUserData();
            }
        });

        // Cancel button
        btnCancel.setOnClickListener(v -> finish());
    }

    private void setupDropdowns() {
        // Department dropdown
        ArrayAdapter<String> departmentAdapter = new ArrayAdapter<>(
                this, R.layout.dropdown_item, departments);
        edtDepartment.setAdapter(departmentAdapter);

        // Position dropdown
        ArrayAdapter<String> positionAdapter = new ArrayAdapter<>(
                this, R.layout.dropdown_item, positions);
        edtPosition.setAdapter(positionAdapter);
    }

    private void loadUserData() {
        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            showLoading(true);
            
            db.collection("users").document(user.getUid())
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        showLoading(false);
                        
                        if (documentSnapshot.exists()) {
                            updateUIWithUserData(documentSnapshot);
                        } else {
                            // If no data exists in Firestore, use data from Firebase Auth
                            edtFullName.setText(user.getDisplayName() != null ? user.getDisplayName() : "");
                            // Rest will be empty
                        }
                    })
                    .addOnFailureListener(e -> {
                        showLoading(false);
                        Toast.makeText(this, "Lỗi khi tải dữ liệu: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        } else {
            // If not logged in, return to login screen
            Toast.makeText(this, "Bạn cần đăng nhập để thực hiện thao tác này", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void updateUIWithUserData(DocumentSnapshot document) {
        String name = document.getString("fullName");
        String phone = document.getString("phone");
        String department = document.getString("department");
        String position = document.getString("position");

        // Update UI
        edtFullName.setText(name != null ? name : "");
        edtPhone.setText(phone != null ? phone : "");
        edtDepartment.setText(department != null ? department : "");
        edtPosition.setText(position != null ? position : "");
    }

    private boolean validateInputs() {
        boolean isValid = true;

        // Validate name
        String name = edtFullName.getText().toString().trim();
        if (TextUtils.isEmpty(name)) {
            tilFullName.setError("Vui lòng nhập họ và tên");
            isValid = false;
        } else {
            tilFullName.setError(null);
        }

        // Validate phone
        String phone = edtPhone.getText().toString().trim();
        if (TextUtils.isEmpty(phone)) {
            tilPhone.setError("Vui lòng nhập số điện thoại");
            isValid = false;
        } else if (!isValidPhone(phone)) {
            tilPhone.setError("Số điện thoại không hợp lệ");
            isValid = false;
        } else {
            tilPhone.setError(null);
        }

        // Validate department
        String department = edtDepartment.getText().toString().trim();
        if (TextUtils.isEmpty(department)) {
            tilDepartment.setError("Vui lòng chọn phòng ban");
            isValid = false;
        } else {
            tilDepartment.setError(null);
        }

        // Validate position
        String position = edtPosition.getText().toString().trim();
        if (TextUtils.isEmpty(position)) {
            tilPosition.setError("Vui lòng chọn chức vụ");
            isValid = false;
        } else {
            tilPosition.setError(null);
        }

        return isValid;
    }

    private boolean isValidPhone(String phone) {
        // Simple phone validation - more complex validation could be added
        return phone.length() >= 9 && phone.length() <= 11 && TextUtils.isDigitsOnly(phone);
    }

    private void saveUserData() {
        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            showLoading(true);

            // Create user data map
            Map<String, Object> userData = new HashMap<>();
            userData.put("fullName", edtFullName.getText().toString().trim());
            userData.put("phone", edtPhone.getText().toString().trim());
            userData.put("department", edtDepartment.getText().toString().trim());
            userData.put("position", edtPosition.getText().toString().trim());

            // Check if document exists or create a new one
            db.collection("users").document(user.getUid())
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            // Document exists, update it
                            db.collection("users").document(user.getUid())
                                    .update(userData)
                                    .addOnSuccessListener(aVoid -> {
                                        showLoading(false);
                                        Toast.makeText(this, "Cập nhật thông tin thành công", Toast.LENGTH_SHORT).show();
                                        finish();
                                    })
                                    .addOnFailureListener(e -> {
                                        showLoading(false);
                                        Toast.makeText(this, "Lỗi khi cập nhật: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    });
                        } else {
                            // Document doesn't exist, set it
                            db.collection("users").document(user.getUid())
                                    .set(userData)
                                    .addOnSuccessListener(aVoid -> {
                                        showLoading(false);
                                        Toast.makeText(this, "Thêm thông tin thành công", Toast.LENGTH_SHORT).show();
                                        finish();
                                    })
                                    .addOnFailureListener(e -> {
                                        showLoading(false);
                                        Toast.makeText(this, "Lỗi khi thêm: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    });
                        }
                    })
                    .addOnFailureListener(e -> {
                        showLoading(false);
                        Toast.makeText(this, "Lỗi khi kiểm tra dữ liệu: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        }
    }

    private boolean checkStoragePermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED;
    }

    private void requestStoragePermission() {
        ActivityCompat.requestPermissions(
                this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                REQUEST_PERMISSION_CODE
        );
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_IMAGE_PICK);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openGallery();
            } else {
                Toast.makeText(this, "Cần quyền truy cập bộ nhớ để chọn ảnh", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_PICK && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.getData();
            if (selectedImageUri != null) {
                imgAvatar.setImageURI(selectedImageUri);
                isPhotoChanged = true;
                Toast.makeText(this, "Tính năng thay đổi ảnh sẽ được cập nhật trong phiên bản sau", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void showLoading(boolean isLoading) {
        loadingOverlay.setVisibility(isLoading ? View.VISIBLE : View.GONE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
} 