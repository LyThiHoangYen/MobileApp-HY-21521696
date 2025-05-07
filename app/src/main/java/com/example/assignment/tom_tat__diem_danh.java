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
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class tom_tat__diem_danh extends AppCompatActivity implements CalendarAdapter.OnItemListener {
    private TextView monthYearText;
    private RecyclerView calendarRecyclerView;
    private LocalDate selectDate;
    private RecyclerView lvitems;
    private Toolbar toolbar1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tom_tat__diem_danh);


        initWidgets();
        selectDate= LocalDate.now();
        setMonthView();
        toolbar1 = (Toolbar) findViewById(R.id.toolbar1);
        toolbar1.setTitle("Tóm tắt điểm danh");
        setSupportActionBar(toolbar1);
//        ActionBar actionBar = getSupportActionBar();
//        actionBar.setTitle("Tóm tắt điểm danh"); //Thiết lập tiêu đề nếu muốn
//        String title = actionBar.getTitle().toString(); //Lấy tiêu đề nếu muốn
////        actionBar.hide(); //Ẩn ActionBar nếu muốn
//
//
//        actionBar.setDisplayShowHomeEnabled(true);
////        actionBar.setLogo(R.mipmap.back);    //Icon muốn hiện thị
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        List<diemdanh> diemdanh= new ArrayList<diemdanh>();
        diemdanh.add(new diemdanh("2021-04-04","Làm việc 8 tiếng tại BKHCM College","8:00-16:00"));
        diemdanh.add(new diemdanh("2021-04-05","Làm việc 8 tiếng tại BKHCM College","8:00-16:00"));
        diemdanh.add(new diemdanh("2021-04-05","Làm việc 8 tiếng tại BKHCM College","8:00-16:00"));
        diemdanh.add(new diemdanh("2021-04-05","Làm việc 8 tiếng tại BKHCM College","8:00-16:00"));
        diemdanh.add(new diemdanh("2021-04-05","Làm việc 8 tiếng tại BKHCM College","8:00-16:00"));
        lvitems=(RecyclerView) findViewById(R.id.recycle_result);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        lvitems.setLayoutManager(layoutManager);
        lvitems.setHasFixedSize(true);
        lvitems.setAdapter(new RecycleViewData(this,diemdanh));
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
        String message ="Selected Date " + dayText + " " + monthYearFromDate(selectDate);
        Toast.makeText(this,message,Toast.LENGTH_LONG).show();
    }

    private String monthYearFromDate(LocalDate selectDate) {
        DateTimeFormatter formatter =DateTimeFormatter.ofPattern("MMMMM yyyy");
        return selectDate.format(formatter);
    }
}