package com.example.assignment;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import java.time.LocalDate;
import android.widget.ImageView;
import android.content.Intent;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import android.widget.Button;
import android.widget.ImageButton;

public class tom_tat__diem_danh extends AppCompatActivity implements CalendarAdapter.OnItemListener {
    private static final String TAG = "TomTatDiemDanh";
    private TextView monthYearText;
    private RecyclerView calendarRecyclerView;
    private LocalDate selectDate;
    private RecyclerView lvitems;
    private Toolbar toolbar1;
    private ProgressBar progressBar;
    private List<diemdanh> diemdanhList;
    private RecycleViewData adapter;
    private View errorCard;
    private TextView errorText;
    
    private FirebaseAuth auth;
    private FirebaseFirestore db;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tom_tat__diem_danh);

        // Khởi tạo Firebase
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        try {
            Log.d(TAG, "onCreate: Initializing tom_tat__diem_danh activity");
            initWidgets();
            
            // Set up the toolbar
            toolbar1 = findViewById(R.id.toolbar1);
            toolbar1.setTitle("Chấm công");
            setSupportActionBar(toolbar1);
            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }
            
            // Handle back button
            ImageButton backButton = findViewById(R.id.backButton);
            backButton.setOnClickListener(v -> onBackPressed());
            
            // Set up the attendance stats
            TextView lateDaysCount = findViewById(R.id.lateDaysCount);
            TextView attendanceCount = findViewById(R.id.attendanceCount);
            TextView invalidAttendanceCount = findViewById(R.id.invalidAttendanceCount);
            
            // Load stats from Firestore
            if (auth.getCurrentUser() != null) {
                loadAttendanceStats();
                loadRecentAttendance();
            }
            
        } catch (Exception e) {
            // Handle any initialization exceptions
            Log.e(TAG, "onCreate Error: ", e);
            Toast.makeText(this, "Lỗi khởi tạo: " + e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
            
            // Safely finish activity if critical initialization fails
            if (isFinishing()) {
                return;
            }
        }
    }
    
    @Override
    public void onBackPressed() {
        Log.d(TAG, "onBackPressed: Navigating back to DiemDanhActivity");
        // Sử dụng Intent rõ ràng để quay lại DiemDanhActivity thay vì sử dụng finish() mặc định
        try {
            Intent intent = new Intent(tom_tat__diem_danh.this, DiemDanhActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // Xóa các activity phía trên DiemDanhActivity
            startActivity(intent);
            finish();
        } catch (Exception e) {
            Log.e(TAG, "Error returning to DiemDanhActivity: ", e);
            // Nếu có lỗi, sử dụng phương thức mặc định
            super.onBackPressed();
        }
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: Refreshing data");
        
        // Tải lại dữ liệu thống kê và lịch sử gần đây khi quay lại màn hình
        try {
            if (auth.getCurrentUser() != null) {
                loadAttendanceStats();
                loadRecentAttendance();
            }
        } catch (Exception e) {
            Log.e(TAG, "Error in onResume: ", e);
            Toast.makeText(this, "Lỗi khi tải dữ liệu: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    
    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: Activity becoming visible");
        
        // Add additional initialization if needed
    }
    
    private void loadAttendanceData() {
        // Phương thức này không còn được sử dụng trong UI mới
        // Chỉ ghi log để gỡ lỗi
        Log.d(TAG, "loadAttendanceData: Không còn sử dụng trong UI mới");
    }
    
    private void loadAttendanceWithoutIndex(String userId) {
        // Phương thức này không còn được sử dụng trong UI mới
        Log.d(TAG, "loadAttendanceWithoutIndex: Không còn sử dụng trong UI mới");
    }

    private void showError(String message, boolean isFirebaseError) {
        // Phương thức này không còn được sử dụng trong UI mới
        // Thay vào đó, hiển thị Toast và ghi log
        Log.e(TAG, "Error: " + message);
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
    
    // Lưu URL tạo index để sử dụng sau này
    private void saveIndexUrl(String url) {
        // Phương thức này không còn được sử dụng trong UI mới
        Log.d(TAG, "saveIndexUrl: Không còn sử dụng trong UI mới");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Log.d(TAG, "Back button in toolbar clicked");
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void nextMonth(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            selectDate = selectDate.plusMonths(1);
            setMonthView();
        }
    }

    public void preMonth(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            selectDate = selectDate.minusMonths(1);
            setMonthView();
        }
    }
    
    private void initWidgets() {
        // Chỉ khởi tạo các thành phần UI có trong layout mới
        progressBar = findViewById(R.id.progressBar);
        
        // Chúng ta không cần những biến này trong UI mới, đặt chúng thành null
        monthYearText = null;  
        calendarRecyclerView = null;
        errorCard = null;
        errorText = null;
        lvitems = null;
        
        // Tạo danh sách trống để tránh NullPointerException
        diemdanhList = new ArrayList<>();
        
        // Ghi log cho mục đích gỡ lỗi
        Log.d(TAG, "initWidgets: Khởi tạo widgets cho layout UI mới");
    }

    private void setMonthView() {
        // Since we don't have the calendar functionality in the new UI,
        // we'll just log a message and return
        Log.d(TAG, "setMonthView: Calendar view not supported in new UI");
        
        // Comment out the old implementation
        /*
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Sử dụng API mới (LocalDate) nếu phiên bản Android hỗ trợ
            monthYearText.setText(monthYearFromDate(selectDate));
            ArrayList<String> daysInMonth = daysInMonthArray(selectDate);
            CalendarAdapter calendarAdapter = new CalendarAdapter(daysInMonth, this);
            RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 7);
            calendarRecyclerView.setLayoutManager(layoutManager);
            calendarRecyclerView.setAdapter(calendarAdapter);
        } else {
            // Sử dụng Calendar cho các phiên bản cũ
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat monthFormat = new SimpleDateFormat("MMMM yyyy", Locale.getDefault());
            monthYearText.setText(monthFormat.format(calendar.getTime()));
            
            ArrayList<String> daysInMonth = fallbackDaysInMonthArray(calendar);
            CalendarAdapter calendarAdapter = new CalendarAdapter(daysInMonth, this);
            RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 7);
            calendarRecyclerView.setLayoutManager(layoutManager);
            calendarRecyclerView.setAdapter(calendarAdapter);
        }
        */
    }

    // Phương thức thay thế cho các phiên bản Android cũ
    private ArrayList<String> fallbackDaysInMonthArray(Calendar calendar) {
        ArrayList<String> daysInMonthArray = new ArrayList<>();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        
        int daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1; // Java Calendar Sunday = 1
        
        for (int i = 1; i <= 42; i++) {
            if (i <= dayOfWeek || i > daysInMonth + dayOfWeek) {
                daysInMonthArray.add("");
            } else {
                daysInMonthArray.add(String.valueOf(i - dayOfWeek));
            }
        }
        return daysInMonthArray;
    }

    private ArrayList<String> daysInMonthArray(LocalDate selectDate) {
        ArrayList<String> daysInMonthArray = new ArrayList<>();
        YearMonth yearMonth = YearMonth.from(selectDate);
        int daysInMonth = yearMonth.lengthOfMonth();

        LocalDate firstOfMonth = selectDate.withDayOfMonth(1);
        int dayOfWeek = firstOfMonth.getDayOfWeek().getValue();
        for (int i = 1; i <= 42; i++) {
            if (i <= dayOfWeek || i > daysInMonth + dayOfWeek) {
                daysInMonthArray.add("");
            } else {
                daysInMonthArray.add(String.valueOf(i - dayOfWeek));
            }
        }
        return daysInMonthArray;
    }

    @Override
    public void onItemClick(int position, String dayText) {
        // Calendar view is not used in the new UI
        Log.d(TAG, "onItemClick: Calendar functionality not used in new UI");
    }
    
    private void filterAttendanceDataByDay(String dayText) {
        // Phương thức này không còn được sử dụng trong UI mới
        Log.d(TAG, "filterAttendanceDataByDay: Không còn sử dụng trong UI mới");
    }
    
    private void filterAttendanceByDayWithoutIndex(String userId, String selectedDay, String dayText) {
        // Phương thức này không còn được sử dụng trong UI mới
        Log.d(TAG, "filterAttendanceByDayWithoutIndex: Không còn sử dụng trong UI mới");
    }

    private String monthYearFromDate(LocalDate selectDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMMM yyyy");
        return selectDate.format(formatter);
    }

    // New method to load attendance stats
    private void loadAttendanceStats() {
        String userId = auth.getCurrentUser().getUid();
        
        // Show loading
        progressBar.setVisibility(View.VISIBLE);
        
        // Get the current month and year
        Calendar calendar = Calendar.getInstance();
        int currentMonth = calendar.get(Calendar.MONTH) + 1; // Calendar months are 0-based
        int currentYear = calendar.get(Calendar.YEAR);
        
        db.collection("attendance")
            .whereEqualTo("userId", userId)
            .get()
            .addOnSuccessListener(queryDocumentSnapshots -> {
                progressBar.setVisibility(View.GONE);
                
                int totalAttendance = 0;
                int lateCount = 0;
                int invalidCount = 0;
                
                // Process data
                for (int i = 0; i < queryDocumentSnapshots.size(); i++) {
                    try {
                        Map<String, Object> data = queryDocumentSnapshots.getDocuments().get(i).getData();
                        
                        // Count only records from current month
                        String date = (String) data.get("date");
                        if (date != null && date.startsWith(currentYear + "-" + String.format("%02d", currentMonth))) {
                            totalAttendance++;
                            
                            // Check if late (after 8:30 AM)
                            String time = (String) data.get("time");
                            String type = (String) data.get("type");
                            
                            if (type != null && type.equals("check_in") && time != null) {
                                try {
                                    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
                                    Date checkInTime = sdf.parse(time);
                                    Date lateTime = sdf.parse("08:30:00");
                                    
                                    if (checkInTime != null && lateTime != null && checkInTime.after(lateTime)) {
                                        lateCount++;
                                    }
                                } catch (Exception e) {
                                    Log.e(TAG, "Error parsing time: " + e.getMessage());
                                }
                            }
                            
                            // Check if invalid (placeholder for your business logic)
                            Boolean isValid = (Boolean) data.get("isValid");
                            if (isValid != null && !isValid) {
                                invalidCount++;
                            }
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "Error processing attendance record: " + e.getMessage());
                    }
                }
                
                // Update UI
                TextView attendanceCount = findViewById(R.id.attendanceCount);
                TextView lateDaysCount = findViewById(R.id.lateDaysCount);
                TextView invalidAttendanceCount = findViewById(R.id.invalidAttendanceCount);
                
                attendanceCount.setText(totalAttendance + " Lần");
                lateDaysCount.setText(lateCount + " Lần");
                invalidAttendanceCount.setText(invalidCount + " Lần");
                
            })
            .addOnFailureListener(e -> {
                progressBar.setVisibility(View.GONE);
                Log.e(TAG, "Error loading attendance stats: " + e.getMessage());
                Toast.makeText(this, "Lỗi tải dữ liệu thống kê", Toast.LENGTH_SHORT).show();
            });
    }

    // New method to load the most recent attendance history
    private void loadRecentAttendance() {
        if (auth.getCurrentUser() == null) {
            return;
        }
        
        String userId = auth.getCurrentUser().getUid();
        progressBar.setVisibility(View.VISIBLE);
        
        db.collection("attendance")
            .whereEqualTo("userId", userId)
            .orderBy("timestamp", com.google.firebase.firestore.Query.Direction.DESCENDING)
            .limit(3) // Get only the 3 most recent records
            .get()
            .addOnSuccessListener(queryDocumentSnapshots -> {
                progressBar.setVisibility(View.GONE);
                
                if (queryDocumentSnapshots.isEmpty()) {
                    // No recent attendance records
                    return;
                }
                
                // Create a linear layout to hold the recent attendance items
                LinearLayout recentAttendanceContainer = findViewById(R.id.recentAttendanceContainer);
                if (recentAttendanceContainer != null) {
                    // Clear any existing views
                    recentAttendanceContainer.removeAllViews();
                    
                    // Add each recent attendance record
                    for (int i = 0; i < queryDocumentSnapshots.size(); i++) {
                        try {
                            Map<String, Object> data = queryDocumentSnapshots.getDocuments().get(i).getData();
                            
                            String date = (String) data.get("date");
                            String time = (String) data.get("time");
                            
                            // Format the date for display (from yyyy-MM-dd to dd/MM/yyyy)
                            String displayDate = "";
                            try {
                                SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                                SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                                Date parsedDate = inputFormat.parse(date);
                                if (parsedDate != null) {
                                    displayDate = outputFormat.format(parsedDate);
                                }
                            } catch (Exception e) {
                                displayDate = date; // Use as-is if parsing fails
                            }
                            
                            // Create a TextView for this attendance record
                            TextView attendanceRecord = new TextView(this);
                            attendanceRecord.setText("Chấm công ngày " + displayDate + " - " + time);
                            attendanceRecord.setTextSize(14);
                            attendanceRecord.setPadding(
                                    dpToPx(12), dpToPx(12), dpToPx(12), dpToPx(12));
                            attendanceRecord.setBackgroundResource(android.R.color.darker_gray);
                            
                            // Create layout parameters with bottom margin
                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT);
                            if (i < queryDocumentSnapshots.size() - 1) {
                                params.bottomMargin = dpToPx(8);
                            }
                            attendanceRecord.setLayoutParams(params);
                            
                            // Add to the container
                            recentAttendanceContainer.addView(attendanceRecord);
                        } catch (Exception e) {
                            Log.e(TAG, "Error creating recent attendance record: " + e.getMessage());
                        }
                    }
                }
            })
            .addOnFailureListener(e -> {
                progressBar.setVisibility(View.GONE);
                Log.e(TAG, "Error loading recent attendance: " + e.getMessage());
            });
    }
    
    // Helper method to convert dp to pixels
    private int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }
}