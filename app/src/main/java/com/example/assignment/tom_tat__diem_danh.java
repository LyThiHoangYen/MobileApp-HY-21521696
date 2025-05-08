package com.example.assignment;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import java.time.LocalDate;
import android.app.Activity;
import android.os.Bundle;


import android.view.View;
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
import com.google.firebase.firestore.Query;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class tom_tat__diem_danh extends AppCompatActivity implements CalendarAdapter.OnItemListener {
    private TextView monthYearText;
    private RecyclerView calendarRecyclerView;
    private LocalDate selectDate;
    private RecyclerView lvitems;
    private Toolbar toolbar1;
    private ProgressBar progressBar;
    private List<diemdanh> diemdanhList;
    private RecycleViewData adapter;
    
    private FirebaseAuth auth;
    private FirebaseFirestore db;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tom_tat__diem_danh);

        // Khởi tạo Firebase
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        initWidgets();
        selectDate = LocalDate.now();
        setMonthView();
        toolbar1 = (Toolbar) findViewById(R.id.toolbar1);
        toolbar1.setTitle("Tóm tắt điểm danh");
        setSupportActionBar(toolbar1);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        
        // Thiết lập ProgressBar
        progressBar = findViewById(R.id.progressBar);
        
        // Thiết lập RecyclerView
        diemdanhList = new ArrayList<>();
        lvitems = (RecyclerView) findViewById(R.id.recycle_result);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        lvitems.setLayoutManager(layoutManager);
        lvitems.setHasFixedSize(true);
        
        adapter = new RecycleViewData(this, diemdanhList);
        lvitems.setAdapter(adapter);
        
        // Tải dữ liệu điểm danh từ Firestore
        loadAttendanceData();
    }
    
    private void loadAttendanceData() {
        if (auth.getCurrentUser() == null) {
            Toast.makeText(this, "Vui lòng đăng nhập để xem lịch sử", Toast.LENGTH_SHORT).show();
            return;
        }
        
        String userId = auth.getCurrentUser().getUid();
        progressBar.setVisibility(View.VISIBLE);
        
        // Lấy lịch sử điểm danh từ Firestore, sắp xếp theo thời gian giảm dần
        db.collection("attendance")
            .whereEqualTo("userId", userId)
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener(queryDocumentSnapshots -> {
                progressBar.setVisibility(View.GONE);
                diemdanhList.clear();
                
                if (queryDocumentSnapshots.isEmpty()) {
                    Toast.makeText(this, "Không có dữ liệu điểm danh", Toast.LENGTH_SHORT).show();
                    return;
                }
                
                SimpleDateFormat displayDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                
                for (int i = 0; i < queryDocumentSnapshots.size(); i++) {
                    Map<String, Object> data = queryDocumentSnapshots.getDocuments().get(i).getData();
                    Date timestamp = (Date) data.get("timestamp");
                    String date = displayDateFormat.format(timestamp);
                    String time = (String) data.get("time");
                    String type = (String) data.get("type");
                    
                    String content;
                    if (type.equals("check_in")) {
                        content = "Điểm danh vào lúc " + time;
                    } else {
                        content = "Điểm danh ra lúc " + time;
                    }
                    
                    diemdanhList.add(new diemdanh(date, content, time));
                }
                
                adapter.notifyDataSetChanged();
            })
            .addOnFailureListener(e -> {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(this, "Lỗi tải dữ liệu: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case android.R.id.home:
                onBackPressed();
                return true;

            default:break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void nextMonth(View view) {
        selectDate=selectDate.plusMonths(1);
        setMonthView();
    }

    public void preMonth(View view) {
        selectDate=selectDate.minusMonths(1);
        setMonthView();
    }
    private void initWidgets() {
        calendarRecyclerView= findViewById((R.id.calendarRecycleView));
        monthYearText=findViewById((R.id.monthYearTV));
    }

    private void setMonthView() {
        monthYearText.setText(monthYearFromDate(selectDate));
        ArrayList<String> daysInMonth = daysInMonthArray(selectDate);
        CalendarAdapter calendarAdapter = new CalendarAdapter(daysInMonth,this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(),7);
        calendarRecyclerView.setLayoutManager(layoutManager);
        calendarRecyclerView.setAdapter(calendarAdapter);
    }

    private ArrayList<String> daysInMonthArray(LocalDate selectDate) {
        ArrayList<String> daysInMonthArray= new ArrayList<>();
        YearMonth yearMonth = YearMonth.from(selectDate);
        int daysInMonth= yearMonth.lengthOfMonth();

        LocalDate firstOfMonth = selectDate.withDayOfMonth(1);
        int dayOfWeek = firstOfMonth.getDayOfWeek().getValue();
        for(int i=1;i<=42;i++)
        {
            if(i<=dayOfWeek||i> daysInMonth + dayOfWeek)
            {
                daysInMonthArray.add("");
            }
            else
            {
                daysInMonthArray.add(String.valueOf(i-dayOfWeek));
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
        // Lấy tháng và năm từ selectDate
        int month = selectDate.getMonthValue();
        int year = selectDate.getYear();
        
        // Tạo chuỗi ngày định dạng yyyy-MM-dd để so sánh với dữ liệu Firebase
        String selectedDay = String.format(Locale.getDefault(), "%04d-%02d-%02d", year, month, Integer.parseInt(dayText));
        
        if (auth.getCurrentUser() == null) {
            return;
        }
        
        String userId = auth.getCurrentUser().getUid();
        progressBar.setVisibility(View.VISIBLE);
        
        db.collection("attendance")
            .whereEqualTo("userId", userId)
            .whereEqualTo("date", selectedDay)
            .orderBy("timestamp", Query.Direction.ASCENDING)
            .get()
            .addOnSuccessListener(queryDocumentSnapshots -> {
                progressBar.setVisibility(View.GONE);
                diemdanhList.clear();
                
                if (queryDocumentSnapshots.isEmpty()) {
                    Toast.makeText(this, "Không có dữ liệu điểm danh cho ngày " + dayText, Toast.LENGTH_SHORT).show();
                    return;
                }
                
                SimpleDateFormat displayDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                
                for (int i = 0; i < queryDocumentSnapshots.size(); i++) {
                    Map<String, Object> data = queryDocumentSnapshots.getDocuments().get(i).getData();
                    Date timestamp = (Date) data.get("timestamp");
                    String date = displayDateFormat.format(timestamp);
                    String time = (String) data.get("time");
                    String type = (String) data.get("type");
                    
                    String content;
                    if (type.equals("check_in")) {
                        content = "Điểm danh vào lúc " + time;
                    } else {
                        content = "Điểm danh ra lúc " + time;
                    }
                    
                    diemdanhList.add(new diemdanh(date, content, time));
                }
                
                adapter.notifyDataSetChanged();
            })
            .addOnFailureListener(e -> {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(this, "Lỗi tải dữ liệu: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            });
    }

    private String monthYearFromDate(LocalDate selectDate) {
        DateTimeFormatter formatter =DateTimeFormatter.ofPattern("MMMMM yyyy");
        return selectDate.format(formatter);
    }
}