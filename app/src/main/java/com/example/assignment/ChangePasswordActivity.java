package com.example.assignment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangePasswordActivity extends AppCompatActivity {

    private TextInputLayout tilCurrentPassword, tilNewPassword, tilConfirmPassword;
    private TextInputEditText edtCurrentPassword, edtNewPassword, edtConfirmPassword;
    private MaterialButton btnChangePassword, btnCancel;
    private FrameLayout loadingOverlay;
    private Toolbar toolbar;
    
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        
        // Initialize Firebase
        auth = FirebaseAuth.getInstance();
        
        // Set up toolbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        
        // Initialize views
        initViews();
        setupClickListeners();
    }
    
    private void initViews() {
        tilCurrentPassword = findViewById(R.id.tilCurrentPassword);
        tilNewPassword = findViewById(R.id.tilNewPassword);
        tilConfirmPassword = findViewById(R.id.tilConfirmPassword);
        
        edtCurrentPassword = findViewById(R.id.edtCurrentPassword);
        edtNewPassword = findViewById(R.id.edtNewPassword);
        edtConfirmPassword = findViewById(R.id.edtConfirmPassword);
        
        btnChangePassword = findViewById(R.id.btnChangePassword);
        btnCancel = findViewById(R.id.btnCancel);
        
        loadingOverlay = findViewById(R.id.loadingOverlay);
    }
    
    private void setupClickListeners() {
        // Change password button
        btnChangePassword.setOnClickListener(v -> {
            if (validateInputs()) {
                changePassword();
            }
        });
        
        // Cancel button
        btnCancel.setOnClickListener(v -> finish());
    }
    
    private boolean validateInputs() {
        boolean isValid = true;
        
        // Reset errors
        tilCurrentPassword.setError(null);
        tilNewPassword.setError(null);
        tilConfirmPassword.setError(null);
        
        // Get text values
        String currentPassword = edtCurrentPassword.getText().toString().trim();
        String newPassword = edtNewPassword.getText().toString().trim();
        String confirmPassword = edtConfirmPassword.getText().toString().trim();
        
        // Validate current password
        if (TextUtils.isEmpty(currentPassword)) {
            tilCurrentPassword.setError("Vui lòng nhập mật khẩu hiện tại");
            isValid = false;
        }
        
        // Validate new password
        if (TextUtils.isEmpty(newPassword)) {
            tilNewPassword.setError("Vui lòng nhập mật khẩu mới");
            isValid = false;
        } else if (newPassword.length() < 6) {
            tilNewPassword.setError("Mật khẩu phải có ít nhất 6 kí tự");
            isValid = false;
        }
        
        // Validate confirm password
        if (TextUtils.isEmpty(confirmPassword)) {
            tilConfirmPassword.setError("Vui lòng xác nhận mật khẩu mới");
            isValid = false;
        } else if (!confirmPassword.equals(newPassword)) {
            tilConfirmPassword.setError("Mật khẩu xác nhận không khớp");
            isValid = false;
        }
        
        return isValid;
    }
    
    private void changePassword() {
        FirebaseUser user = auth.getCurrentUser();
        if (user == null) {
            Toast.makeText(this, "Bạn cần đăng nhập lại để thực hiện thao tác này", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        
        String email = user.getEmail();
        String currentPassword = edtCurrentPassword.getText().toString().trim();
        String newPassword = edtNewPassword.getText().toString().trim();
        
        if (email == null) {
            Toast.makeText(this, "Không thể xác thực người dùng", Toast.LENGTH_SHORT).show();
            return;
        }
        
        showLoading(true);
        
        // Reauthenticate user before changing password
        AuthCredential credential = EmailAuthProvider.getCredential(email, currentPassword);
        user.reauthenticate(credential)
                .addOnSuccessListener(aVoid -> {
                    // User reauthenticated, now change password
                    user.updatePassword(newPassword)
                            .addOnSuccessListener(aVoid1 -> {
                                showLoading(false);
                                Toast.makeText(ChangePasswordActivity.this, 
                                        "Đổi mật khẩu thành công", Toast.LENGTH_SHORT).show();
                                finish();
                            })
                            .addOnFailureListener(e -> {
                                showLoading(false);
                                Toast.makeText(ChangePasswordActivity.this, 
                                        "Lỗi khi đổi mật khẩu: " + e.getMessage(), 
                                        Toast.LENGTH_SHORT).show();
                            });
                })
                .addOnFailureListener(e -> {
                    showLoading(false);
                    tilCurrentPassword.setError("Mật khẩu hiện tại không đúng");
                });
    }
    
    private void showLoading(boolean isLoading) {
        loadingOverlay.setVisibility(isLoading ? View.VISIBLE : View.GONE);
    }
    
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
} 