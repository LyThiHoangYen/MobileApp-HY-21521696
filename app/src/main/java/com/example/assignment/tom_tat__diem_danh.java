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
            
            // Kiểm tra phiên bản API để sử dụng LocalDate (chỉ có từ API 26+)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                selectDate = LocalDate.now();
            } else {
                // Với các phiên bản API cũ hơn, không làm gì và xử lý trong setMonthView
                Toast.makeText(this, "Thiết bị của bạn đang chạy phiên bản Android cũ, có thể gặp một số hạn chế", Toast.LENGTH_SHORT).show();
            }
            
            setMonthView();
            toolbar1 = findViewById(R.id.toolbar1);
            toolbar1.setTitle("Tóm tắt điểm danh");
            setSupportActionBar(toolbar1);
            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }
            
            // Thiết lập ProgressBar và Error View
            progressBar = findViewById(R.id.progressBar);
            errorCard = findViewById(R.id.errorCard);
            errorText = findViewById(R.id.errorText);
            
            // Ẩn thông báo lỗi
            errorCard.setVisibility(View.GONE);
            
            // Thiết lập RecyclerView
            diemdanhList = new ArrayList<>();
            lvitems = findViewById(R.id.recycle_result);
            LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            lvitems.setLayoutManager(layoutManager);
            lvitems.setHasFixedSize(true);
            
            adapter = new RecycleViewData(this, diemdanhList);
            lvitems.setAdapter(adapter);
            
            // Tải dữ liệu điểm danh từ Firestore
            loadAttendanceData();
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
        
        // Reload data when returning to the activity
        try {
            if (diemdanhList.isEmpty()) {
                loadAttendanceData();
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
        if (auth.getCurrentUser() == null) {
            Toast.makeText(this, "Vui lòng đăng nhập để xem lịch sử", Toast.LENGTH_SHORT).show();
            return;
        }
        
        String userId = auth.getCurrentUser().getUid();
        progressBar.setVisibility(View.VISIBLE);
        errorCard.setVisibility(View.GONE);
        
        try {
            Log.d(TAG, "Loading attendance data for user: " + userId);
            // Sử dụng truy vấn đơn giản không yêu cầu index
            loadAttendanceWithoutIndex(userId);
        } catch (Exception e) {
            Log.e(TAG, "Error loading attendance data: ", e);
            progressBar.setVisibility(View.GONE);
            showError("Lỗi tải dữ liệu: " + e.getMessage(), true);
            Toast.makeText(this, "Không thể tải dữ liệu điểm danh", Toast.LENGTH_LONG).show();
        }
    }
    
    // Phương thức tải dữ liệu không sử dụng compound query (không cần index)
    private void loadAttendanceWithoutIndex(String userId) {
        // Hiển thị progress bar và thông báo
        progressBar.setVisibility(View.VISIBLE);
        Toast.makeText(this, "Đang tải dữ liệu điểm danh...", Toast.LENGTH_SHORT).show();
        
        // Truy vấn đơn giản chỉ với điều kiện userId
        db.collection("attendance")
            .whereEqualTo("userId", userId)
            .get()
            .addOnSuccessListener(queryDocumentSnapshots -> {
                progressBar.setVisibility(View.GONE);
                diemdanhList.clear();
                
                if (queryDocumentSnapshots.isEmpty()) {
                    Log.d(TAG, "No attendance data found");
                    showError("Không có dữ liệu điểm danh", false);
                    return;
                }
                
                Log.d(TAG, "Data retrieved from Firestore: " + queryDocumentSnapshots.size() + " documents");
                
                // Xử lý dữ liệu mà không cần phụ thuộc vào trường timestamp
                SimpleDateFormat displayDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                
                for (int i = 0; i < queryDocumentSnapshots.size(); i++) {
                    try {
                        Map<String, Object> data = queryDocumentSnapshots.getDocuments().get(i).getData();
                        
                        // Lấy dữ liệu ngày và giờ
                        String date = (String) data.get("date");
                        String time = (String) data.get("time");
                        String type = (String) data.get("type");
                        
                        // Kiểm tra và xử lý dữ liệu null
                        if (date == null) {
                            // Nếu không có trường date, thử lấy từ timestamp
                            Object timestampObj = data.get("timestamp");
                            if (timestampObj instanceof Date) {
                                Date timestamp = (Date) timestampObj;
                                date = displayDateFormat.format(timestamp);
                            } else {
                                // Nếu không có cả date và timestamp, sử dụng ngày hôm nay
                                date = displayDateFormat.format(new Date());
                            }
                        } else {
                            // Chuyển đổi định dạng date từ yyyy-MM-dd sang dd/MM/yyyy
                            try {
                                SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                                Date parsedDate = inputFormat.parse(date);
                                if (parsedDate != null) {
                                    date = displayDateFormat.format(parsedDate);
                                }
                            } catch (Exception e) {
                                // Nếu không thể chuyển đổi, giữ nguyên giá trị
                                Log.e(TAG, "Error formatting date: " + e.getMessage());
                            }
                        }
                        
                        if (time == null) time = "--:--:--";
                        if (type == null) type = "check_in"; // Mặc định là check-in nếu không có loại
                        
                        String content;
                        if (type.equals("check_in")) {
                            content = "Điểm danh vào lúc " + time;
                        } else {
                            content = "Điểm danh ra lúc " + time;
                        }
                        
                        diemdanhList.add(new diemdanh(date, content, time));
                        Log.d(TAG, "Added record: " + date + " - " + content);
                    } catch (Exception e) {
                        Log.e(TAG, "Error processing document at index " + i + ": " + e.getMessage());
                        // Vẫn tiếp tục xử lý các bản ghi khác
                    }
                }
                
                // Sắp xếp danh sách theo ngày giảm dần (mới nhất lên đầu)
                try {
                    diemdanhList.sort((item1, item2) -> {
                        try {
                            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                            Date date1 = sdf.parse(item1.getDate());
                            Date date2 = sdf.parse(item2.getDate());
                            if (date1 != null && date2 != null) {
                                return date2.compareTo(date1); // Sắp xếp giảm dần
                            }
                        } catch (Exception e) {
                            Log.e(TAG, "Error sorting dates: " + e.getMessage());
                        }
                        return 0;
                    });
                } catch (Exception e) {
                    Log.e(TAG, "Error sorting list: " + e.getMessage());
                }
                
                Log.d(TAG, "Processed " + diemdanhList.size() + " attendance records");
                
                if (diemdanhList.isEmpty()) {
                    showError("Không có dữ liệu điểm danh", false);
                } else {
                    errorCard.setVisibility(View.GONE);
                    adapter.notifyDataSetChanged();
                    
                    // Scroll to top
                    if (lvitems != null) {
                        lvitems.scrollToPosition(0);
                    }
                }
            })
            .addOnFailureListener(e -> {
                Log.e(TAG, "Firestore query error: ", e);
                progressBar.setVisibility(View.GONE);
                showError("Lỗi: " + e.getMessage(), true);
                
                // Hiển thị thông báo lỗi
                Toast.makeText(this, "Không thể tải dữ liệu: " + e.getMessage(), Toast.LENGTH_LONG).show();
            });
    }

    private void showError(String message, boolean isFirebaseError) {
        Log.d(TAG, "Showing error: " + message + ", isFirebaseError: " + isFirebaseError);
        errorCard.setVisibility(View.VISIBLE);
        
        if (isFirebaseError && message.contains("FAILED_PRECONDITION")) {
            // Lấy URL tạo index từ thông báo lỗi (nếu có)
            String indexUrl = "";
            if (message.contains("https://")) {
                int startIndex = message.indexOf("https://");
                indexUrl = message.substring(startIndex);
            }
            
            if (!indexUrl.isEmpty()) {
                // Nếu có URL, lưu vào bộ nhớ để quản trị viên có thể sử dụng sau này
                saveIndexUrl(indexUrl);
                
                errorText.setText("Lỗi:\nCần tạo chỉ mục (index) cho Firebase Firestore\n\n" +
                        "Đã lưu đường dẫn tạo index để quản trị viên xử lý.");
            } else {
                errorText.setText("Lỗi:\nCần tạo chỉ mục (index) cho Firebase Firestore\n\n" +
                        "Vui lòng thông báo cho quản trị viên để khắc phục lỗi này.");
            }
        } else if (message.contains("Không có dữ liệu")) {
            errorText.setText("Lỗi:\nKhông thể xử lý dữ liệu điểm danh\n\nVui lòng thông báo cho quản trị viên để khắc phục lỗi này.");
        } else {
            errorText.setText("Lỗi:\n" + message);
        }
        
        // Hiển thị thông báo Toast để người dùng biết
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
    
    // Lưu URL tạo index để sử dụng sau này
    private void saveIndexUrl(String url) {
        // Lưu URL vào SharedPreferences để sử dụng sau này
        getSharedPreferences("FirebaseIndexErrors", MODE_PRIVATE)
            .edit()
            .putString("lastIndexUrl", url)
            .apply();
            
        // Log URL để dễ dàng truy cập trong quá trình phát triển/debug
        Log.d(TAG, "Firebase Index URL: " + url);
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
        calendarRecyclerView = findViewById(R.id.calendarRecycleView);
        monthYearText = findViewById(R.id.monthYearTV);
    }

    private void setMonthView() {
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
        if (!dayText.isEmpty()) {
            // Khi người dùng nhấp vào một ngày cụ thể, lọc và hiển thị dữ liệu cho ngày đó
            filterAttendanceDataByDay(dayText);
        }
    }
    
    private void filterAttendanceDataByDay(String dayText) {
        // Lấy tháng và năm từ ngày hiện tại
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1; // Calendar.MONTH bắt đầu từ 0
        
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Sử dụng LocalDate nếu có
            month = selectDate.getMonthValue();
            year = selectDate.getYear();
        }
        
        // Tạo chuỗi ngày định dạng yyyy-MM-dd để so sánh với dữ liệu Firebase
        String selectedDay = String.format(Locale.getDefault(), "%04d-%02d-%02d", year, month, Integer.parseInt(dayText));
        
        if (auth.getCurrentUser() == null) {
            return;
        }
        
        String userId = auth.getCurrentUser().getUid();
        progressBar.setVisibility(View.VISIBLE);
        errorCard.setVisibility(View.GONE);
        
        try {
            Log.d(TAG, "Filtering attendance for day: " + selectedDay);
            // Sử dụng phương thức không cần index
            filterAttendanceByDayWithoutIndex(userId, selectedDay, dayText);
        } catch (Exception e) {
            Log.e(TAG, "Error filtering by day: ", e);
            progressBar.setVisibility(View.GONE);
            showError("Lỗi: " + e.getMessage(), true);
        }
    }
    
    // Phương thức lọc dữ liệu theo ngày không sử dụng compound query
    private void filterAttendanceByDayWithoutIndex(String userId, String selectedDay, String dayText) {
        // Truy vấn chỉ với điều kiện userId
        progressBar.setVisibility(View.VISIBLE);
        Toast.makeText(this, "Đang lọc dữ liệu cho ngày " + dayText, Toast.LENGTH_SHORT).show();
        
        db.collection("attendance")
            .whereEqualTo("userId", userId)
            .get()
            .addOnSuccessListener(queryDocumentSnapshots -> {
                progressBar.setVisibility(View.GONE);
                diemdanhList.clear();
                
                if (queryDocumentSnapshots.isEmpty()) {
                    showError("Không có dữ liệu điểm danh", false);
                    return;
                }
                
                Log.d(TAG, "Filtering data for date: " + selectedDay);
                SimpleDateFormat displayDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                
                // Chuyển định dạng selectedDay từ yyyy-MM-dd sang dd/MM/yyyy để hiển thị
                String displayDate = "";
                try {
                    SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                    Date parsedDate = inputFormat.parse(selectedDay);
                    if (parsedDate != null) {
                        displayDate = displayDateFormat.format(parsedDate);
                    }
                } catch (Exception e) {
                    displayDate = dayText + "/" + (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ? 
                            selectDate.getMonthValue() : (Calendar.getInstance().get(Calendar.MONTH) + 1)) + "/" + 
                            (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ? 
                            selectDate.getYear() : Calendar.getInstance().get(Calendar.YEAR));
                }
                
                final String formattedDisplayDate = displayDate;
                
                // Đếm số bản ghi hợp lệ
                int recordCount = 0;
                
                // Lọc dữ liệu theo ngày được chọn
                for (int i = 0; i < queryDocumentSnapshots.size(); i++) {
                    try {
                        Map<String, Object> data = queryDocumentSnapshots.getDocuments().get(i).getData();
                        String date = (String) data.get("date");
                        
                        // Nếu date khớp với ngày đã chọn, thêm vào danh sách
                        if (selectedDay.equals(date)) {
                            recordCount++;
                            
                            String time = (String) data.get("time");
                            String type = (String) data.get("type");
                            
                            if (time == null) time = "--:--:--";
                            if (type == null) type = "check_in";
                            
                            String content;
                            if (type.equals("check_in")) {
                                content = "Điểm danh vào lúc " + time;
                            } else {
                                content = "Điểm danh ra lúc " + time;
                            }
                            
                            diemdanhList.add(new diemdanh(formattedDisplayDate, content, time));
                            Log.d(TAG, "Added filtered record: " + formattedDisplayDate + " - " + content);
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "Error processing document while filtering: " + e.getMessage());
                    }
                }
                
                // Sắp xếp dữ liệu theo thời gian (nếu cần)
                try {
                    diemdanhList.sort((item1, item2) -> {
                        try {
                            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
                            Date time1 = sdf.parse(item1.getTime());
                            Date time2 = sdf.parse(item2.getTime());
                            if (time1 != null && time2 != null) {
                                return time1.compareTo(time2); // Sắp xếp tăng dần theo thời gian
                            }
                        } catch (Exception e) {
                            Log.e(TAG, "Error sorting times: " + e.getMessage());
                        }
                        return 0;
                    });
                } catch (Exception e) {
                    Log.e(TAG, "Error sorting filtered list: " + e.getMessage());
                }
                
                if (diemdanhList.isEmpty()) {
                    showError("Không có dữ liệu điểm danh cho ngày " + dayText, false);
                    return;
                }
                
                Log.d(TAG, "Found " + diemdanhList.size() + " records for day " + selectedDay);
                errorCard.setVisibility(View.GONE);
                adapter.notifyDataSetChanged();
                
                // Hiển thị thông báo
                Toast.makeText(this, "Đã tìm thấy " + diemdanhList.size() + " bản ghi", Toast.LENGTH_SHORT).show();
            })
            .addOnFailureListener(e -> {
                Log.e(TAG, "Firestore filter query error: ", e);
                progressBar.setVisibility(View.GONE);
                showError("Lỗi: " + e.getMessage(), true);
                Toast.makeText(this, "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            });
    }

    private String monthYearFromDate(LocalDate selectDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMMM yyyy");
        return selectDate.format(formatter);
    }
}