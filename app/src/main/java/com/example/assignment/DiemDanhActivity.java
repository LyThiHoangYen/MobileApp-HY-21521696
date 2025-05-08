package com.example.assignment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class DiemDanhActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private Button btnCheckIn, btnCheckOut, btnViewHistory;
    private Toolbar toolbar;
    private TextView tvDate, tvTime, tvStatus;
    private ProgressBar progressBar;
    private Handler timeHandler = new Handler();
    private Runnable timeRunnable;

    // Biến thành viên để lưu trữ trạng thái điểm danh
    private boolean hasCheckIn = false;
    private boolean hasCheckOut = false;
    private String checkInTime = "";
    private String checkOutTime = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.diemdanh);

        // Khởi tạo Firebase
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Ánh xạ view
        btnCheckIn = findViewById(R.id.btnCheckIn);
        btnCheckOut = findViewById(R.id.btnCheckOut);
        btnViewHistory = findViewById(R.id.btnViewHistory);
        toolbar = findViewById(R.id.toolbar);
        tvDate = findViewById(R.id.tvDate);
        tvTime = findViewById(R.id.tvTime);
        tvStatus = findViewById(R.id.tvStatus);
        progressBar = findViewById(R.id.progressBar);

        // Thiết lập toolbar
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Điểm danh / Chấm công");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        // Khởi tạo bộ đếm thời gian
        startTimeUpdater();

        // Kiểm tra trạng thái điểm danh hôm nay
        checkAttendanceStatus();

        // Xử lý sự kiện check-in
        btnCheckIn.setOnClickListener(v -> doDiemDanh("check_in"));

        // Xử lý sự kiện check-out
        btnCheckOut.setOnClickListener(v -> doDiemDanh("check_out"));
        
        // Xử lý sự kiện xem lịch sử chấm công
        btnViewHistory.setOnClickListener(v -> {
            try {
                Log.d("DiemDanhActivity", "Starting attendance history activity");
                Intent intent = new Intent(DiemDanhActivity.this, tom_tat__diem_danh.class);
                // Don't add FLAG_ACTIVITY_NEW_TASK as that can disrupt the back stack
                startActivity(intent);
                // Don't finish this activity - keep it in the back stack
                // Also display a message to assure the user
                Toast.makeText(DiemDanhActivity.this, "Đang mở trang lịch sử điểm danh...", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(DiemDanhActivity.this, "Không thể mở trang lịch sử điểm danh: " + e.getMessage(), Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        });
    }

    private void startTimeUpdater() {
        timeRunnable = new Runnable() {
            @Override
            public void run() {
                updateDateTime();
                timeHandler.postDelayed(this, 1000);
            }
        };
        timeHandler.post(timeRunnable);
    }

    private void updateDateTime() {
        Calendar calendar = Calendar.getInstance();
        Date currentDate = calendar.getTime();

        // Format ngày
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, dd MMMM yyyy", new Locale("vi", "VN"));
        String dateString = dateFormat.format(currentDate);
        tvDate.setText(dateString);

        // Format giờ
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        String timeString = timeFormat.format(currentDate);
        tvTime.setText(timeString);
    }

    private void checkAttendanceStatus() {
        if (auth.getCurrentUser() == null) {
            return;
        }

        String userId = auth.getCurrentUser().getUid();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String currentDate = dateFormat.format(new Date());

        progressBar.setVisibility(View.VISIBLE);

        // Kiểm tra điểm danh vào
        db.collection("attendance")
                .whereEqualTo("userId", userId)
                .whereEqualTo("date", currentDate)
                .whereEqualTo("type", "check_in")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    // Lưu trạng thái điểm danh vào biến thành viên
                    hasCheckIn = !queryDocumentSnapshots.isEmpty();
                    if (hasCheckIn && !queryDocumentSnapshots.isEmpty()) {
                        checkInTime = (String) queryDocumentSnapshots.getDocuments().get(0).getData().get("time");
                    }

                    // Kiểm tra điểm danh ra
                    checkCheckOutStatus(userId, currentDate);
                })
                .addOnFailureListener(e -> {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(DiemDanhActivity.this, "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void checkCheckOutStatus(String userId, String currentDate) {
        db.collection("attendance")
                .whereEqualTo("userId", userId)
                .whereEqualTo("date", currentDate)
                .whereEqualTo("type", "check_out")
                .get()
                .addOnSuccessListener(checkOutDocs -> {
                    progressBar.setVisibility(View.GONE);
                    
                    // Lưu trạng thái điểm danh ra vào biến thành viên
                    hasCheckOut = !checkOutDocs.isEmpty();
                    if (hasCheckOut && !checkOutDocs.isEmpty()) {
                        checkOutTime = (String) checkOutDocs.getDocuments().get(0).getData().get("time");
                    }

                    // Hiển thị trạng thái
                    updateAttendanceStatus();
                })
                .addOnFailureListener(e -> {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(DiemDanhActivity.this, "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void updateAttendanceStatus() {
        String statusMessage = "";
        if (hasCheckIn && hasCheckOut) {
            // Hiển thị trạng thái ngắn hạn (3 giây) rồi ẩn đi
            statusMessage = "Điểm danh vào thành công lúc " + checkInTime + "\n" + 
                           "Điểm danh ra thành công lúc " + checkOutTime;
            showAttendanceStatus(statusMessage, true);
            
            // Sau 3 giây, trả màn hình về trạng thái mặc định
            new Handler().postDelayed(() -> {
                resetScreenToDefault();
            }, 3000);
            
            btnCheckIn.setEnabled(false);
            btnCheckOut.setEnabled(false);
        } else if (hasCheckIn) {
            statusMessage = "Điểm danh vào thành công lúc " + checkInTime +
                           "\nĐừng quên điểm danh ra khi kết thúc!";
            showAttendanceStatus(statusMessage, true);
            btnCheckIn.setEnabled(false);
            btnCheckOut.setEnabled(true);
        } else {
            resetScreenToDefault();
        }
    }

    // Hàm mới để reset màn hình về trạng thái mặc định
    private void resetScreenToDefault() {
        tvStatus.setVisibility(View.GONE);
        btnCheckIn.setEnabled(true);
        btnCheckOut.setEnabled(false);
    }

    private void doDiemDanh(String type) {
        if (auth.getCurrentUser() == null) {
            Toast.makeText(this, "Bạn chưa đăng nhập!", Toast.LENGTH_SHORT).show();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        // Lấy thông tin nhân viên trước khi tạo bản ghi điểm danh
        String userId = auth.getCurrentUser().getUid();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        Date currentTime = new Date();
        String currentDate = dateFormat.format(currentTime);
        String timeString = timeFormat.format(currentTime);

        db.collection("employees").document(userId)
            .get()
            .addOnSuccessListener(documentSnapshot -> {
                // Tạo dữ liệu điểm danh
                Map<String, Object> attendanceData = new HashMap<>();
                attendanceData.put("timestamp", currentTime);
                attendanceData.put("date", currentDate);
                attendanceData.put("time", timeString);
                attendanceData.put("type", type);
                attendanceData.put("userId", userId);
                
                // Thêm thông tin nhân viên nếu có
                if (documentSnapshot.exists()) {
                    String employeeName = documentSnapshot.getString("name");
                    String employeeId = documentSnapshot.getString("employeeId");
                    
                    if (employeeName != null) {
                        attendanceData.put("employeeName", employeeName);
                    }
                    
                    if (employeeId != null) {
                        attendanceData.put("employeeId", employeeId);
                    }
                }
                
                // Lưu tất cả thông tin vào collection attendance
                db.collection("attendance")
                    .add(attendanceData)
                    .addOnSuccessListener(documentReference -> {
                        progressBar.setVisibility(View.GONE);
                        
                        // Cập nhật biến thành viên
                        if (type.equals("check_in")) {
                            hasCheckIn = true;
                            checkInTime = timeString;
                            btnCheckIn.setEnabled(false);
                            btnCheckOut.setEnabled(true);
                        } else {
                            hasCheckOut = true;
                            checkOutTime = timeString;
                            btnCheckOut.setEnabled(false);
                            
                            // Hiển thị trạng thái checkout thành công trong 3 giây
                            String message = "Điểm danh ra thành công lúc " + timeString;
                            showAttendanceStatus(message, true);
                            
                            // Sau 3 giây, đặt lại màn hình cho ngày làm việc mới
                            new Handler().postDelayed(() -> {
                                // Reset trạng thái cho ngày làm việc mới
                                hasCheckIn = false;
                                hasCheckOut = false;
                                checkInTime = "";
                                checkOutTime = "";
                                resetScreenToDefault();
                            }, 3000);
                            
                            Toast.makeText(DiemDanhActivity.this, "Đã lưu điểm danh ra thành công", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        
                        String message = type.equals("check_in") ? 
                                "Điểm danh vào thành công lúc " + timeString : 
                                "Điểm danh ra thành công lúc " + timeString;
                                
                        showAttendanceStatus(message, true);
                        
                        Toast.makeText(DiemDanhActivity.this, "Đã lưu điểm danh thành công", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        progressBar.setVisibility(View.GONE);
                        showAttendanceStatus("Điểm danh thất bại: " + e.getMessage(), false);
                    });
            })
            .addOnFailureListener(e -> {
                // Nếu không lấy được thông tin nhân viên, vẫn lưu thông tin điểm danh cơ bản
                Map<String, Object> basicAttendanceData = new HashMap<>();
                basicAttendanceData.put("timestamp", currentTime);
                basicAttendanceData.put("date", currentDate);
                basicAttendanceData.put("time", timeString);
                basicAttendanceData.put("type", type);
                basicAttendanceData.put("userId", userId);
                
                db.collection("attendance")
                    .add(basicAttendanceData)
                    .addOnSuccessListener(documentReference -> {
                        progressBar.setVisibility(View.GONE);
                        
                        // Cập nhật biến thành viên
                        if (type.equals("check_in")) {
                            hasCheckIn = true;
                            checkInTime = timeString;
                            btnCheckIn.setEnabled(false);
                            btnCheckOut.setEnabled(true);
                        } else {
                            hasCheckOut = true;
                            checkOutTime = timeString;
                            btnCheckOut.setEnabled(false);
                            
                            // Hiển thị trạng thái checkout thành công trong 3 giây
                            String message = "Điểm danh ra thành công lúc " + timeString;
                            showAttendanceStatus(message, true);
                            
                            // Sau 3 giây, đặt lại màn hình cho ngày làm việc mới
                            new Handler().postDelayed(() -> {
                                // Reset trạng thái cho ngày làm việc mới
                                hasCheckIn = false;
                                hasCheckOut = false;
                                checkInTime = "";
                                checkOutTime = "";
                                resetScreenToDefault();
                            }, 3000);
                            
                            Toast.makeText(DiemDanhActivity.this, "Đã lưu điểm danh cơ bản thành công", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        
                        String message = type.equals("check_in") ? 
                                "Điểm danh vào thành công lúc " + timeString : 
                                "Điểm danh ra thành công lúc " + timeString;
                                
                        showAttendanceStatus(message, true);
                        
                        Toast.makeText(DiemDanhActivity.this, "Đã lưu điểm danh cơ bản thành công", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(error -> {
                        progressBar.setVisibility(View.GONE);
                        showAttendanceStatus("Điểm danh thất bại: " + error.getMessage(), false);
                    });
            });
    }
    
    private void showAttendanceStatus(String message, boolean isSuccess) {
        tvStatus.setText(message);
        tvStatus.setBackgroundColor(getResources().getColor(isSuccess ? android.R.color.holo_green_light : android.R.color.holo_red_light));
        tvStatus.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timeHandler != null) {
            timeHandler.removeCallbacks(timeRunnable);
        }
    }
} 