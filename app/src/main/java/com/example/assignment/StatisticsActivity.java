package com.example.assignment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

public class StatisticsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private Toolbar toolbar;
    private Spinner timeSpinner;
    private TextView attendanceRateText, leaveRateText, totalEmployeesText;
    private CardView attendanceCard, leaveCard, employeeCard;
    private TextView chartText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        // Set up toolbar
        toolbar = findViewById(R.id.statistics_toolbar);
        toolbar.setTitle("Thống kê");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // Initialize views
        timeSpinner = findViewById(R.id.time_spinner);
        attendanceRateText = findViewById(R.id.attendance_rate);
        leaveRateText = findViewById(R.id.leave_rate);
        totalEmployeesText = findViewById(R.id.total_employees);
        attendanceCard = findViewById(R.id.attendance_card);
        leaveCard = findViewById(R.id.leave_card);
        employeeCard = findViewById(R.id.employee_card);
        chartText = findViewById(R.id.chart_representation);

        // Set up spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.time_periods, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        timeSpinner.setAdapter(adapter);
        timeSpinner.setOnItemSelectedListener(this);

        // Initial data load
        loadStatisticsData("Tháng này");
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String timePeriod = parent.getItemAtPosition(position).toString();
        loadStatisticsData(timePeriod);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // Do nothing
    }

    private void loadStatisticsData(String timePeriod) {
        // Simulate loading different data based on time period
        switch (timePeriod) {
            case "Hôm nay":
                updateStats(92, 5, 150);
                break;
            case "Tuần này":
                updateStats(88, 8, 152);
                break;
            case "Tháng này":
                updateStats(85, 12, 155);
                break;
            case "Quý này":
                updateStats(82, 15, 160);
                break;
            case "Năm nay":
                updateStats(80, 18, 165);
                break;
            default:
                updateStats(85, 12, 155);
                break;
        }
    }

    private void updateStats(int attendancePercent, int leavePercent, int totalEmployees) {
        // Update text views with data
        attendanceRateText.setText(attendancePercent + "%");
        leaveRateText.setText(leavePercent + "%");
        totalEmployeesText.setText(String.valueOf(totalEmployees));

        // Update chart representation (simplified for this example)
        StringBuilder chartBuilder = new StringBuilder();
        // Generate a simple ASCII bar chart
        chartBuilder.append("Chấm công đúng giờ: ");
        for (int i = 0; i < attendancePercent / 10; i++) {
            chartBuilder.append("■");
        }
        chartBuilder.append(" ").append(attendancePercent).append("%\n\n");

        chartBuilder.append("Chấm công muộn: ");
        for (int i = 0; i < (100 - attendancePercent - leavePercent) / 10; i++) {
            chartBuilder.append("■");
        }
        chartBuilder.append(" ").append(100 - attendancePercent - leavePercent).append("%\n\n");

        chartBuilder.append("Vắng mặt: ");
        for (int i = 0; i < leavePercent / 10; i++) {
            chartBuilder.append("■");
        }
        chartBuilder.append(" ").append(leavePercent).append("%");

        chartText.setText(chartBuilder.toString());
    }
} 