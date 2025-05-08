package com.example.assignment;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.TextView;

import com.google.firebase.auth.*;
import com.google.firebase.firestore.*;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail, etPass;
    private ProgressBar progress;
    private Button btnLogin;
    private TextView tvReset, tvForgot;
    private View loginButton;

    private FirebaseAuth auth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        // Ánh xạ view: (đúng id mà bạn vừa thêm trong XML)
        etEmail = findViewById(R.id.etEmail);
        etPass = findViewById(R.id.etPass);
        progress = findViewById(R.id.progress);
        tvReset = findViewById(R.id.textView23);
        tvForgot = findViewById(R.id.textView24);
        loginButton = findViewById(R.id.loginButtonCard);

        // Init Firebase
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Bấm LOGIN
        loginButton.setOnClickListener(v -> doLogin());
        
        // Reset button
        tvReset.setOnClickListener(v -> {
            etEmail.setText("");
            etPass.setText("");
        });
        
        // Forgot password
        tvForgot.setOnClickListener(v -> forgotPassword());
    }

    private void doLogin() {
        String email = etEmail.getText().toString().trim();
        String pass = etPass.getText().toString().trim();

        if (email.isEmpty() || pass.isEmpty()) {
            Toast.makeText(this, "Nhập email & mật khẩu!", Toast.LENGTH_SHORT).show();
            return;
        }

        progress.setVisibility(View.VISIBLE);

        auth.signInWithEmailAndPassword(email, pass)
                .addOnSuccessListener(res -> fetchRoleAndGo(res.getUser().getUid()))
                .addOnFailureListener(e -> {
                    progress.setVisibility(View.GONE);
                    Toast.makeText(this, "Đăng nhập thất bại: " + e.getLocalizedMessage(),
                            Toast.LENGTH_LONG).show();
                });
    }
    
    private void forgotPassword() {
        String email = etEmail.getText().toString().trim();
        
        if (email.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập email để đặt lại mật khẩu", Toast.LENGTH_SHORT).show();
            return;
        }
        
        progress.setVisibility(View.VISIBLE);
        
        auth.sendPasswordResetEmail(email)
            .addOnSuccessListener(aVoid -> {
                progress.setVisibility(View.GONE);
                Toast.makeText(LoginActivity.this, "Email đặt lại mật khẩu đã được gửi!", Toast.LENGTH_SHORT).show();
            })
            .addOnFailureListener(e -> {
                progress.setVisibility(View.GONE);
                Toast.makeText(LoginActivity.this, "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            });
    }

    private void fetchRoleAndGo(String uid) {
        db.collection("employees").document(uid).get()
                .addOnSuccessListener(doc -> {
                    progress.setVisibility(View.GONE);
                    String role = doc.getString("role");
                    if (role == null) role = "EMPLOYEE";

                    Intent i;
                    switch (role) {
                        case "MANAGER": i = new Intent(this, ManagerHomeActivity.class); break;
                        case "HR":      i = new Intent(this, HrHomeActivity.class);      break;
                        default:        i = new Intent(this, MainActivity.class);
                    }
                    startActivity(i);
                    finish();
                })
                .addOnFailureListener(e -> {
                    progress.setVisibility(View.GONE);
                    Toast.makeText(this, "Không lấy được role!", Toast.LENGTH_LONG).show();
                });
    }
}

