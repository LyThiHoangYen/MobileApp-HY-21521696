package com.example.assignment;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ThongKeBaoCaoActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private Toolbar toolbar;
    RecyclerView recycleView;

    private List<Report> listReport;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.thong_ke_bao_cao);

        toolbar = (Toolbar) findViewById(R.id.tkbaocao);
        toolbar.setTitle("Thống kê báo cáo");
        setSupportActionBar(toolbar);

        listReport = new ArrayList<Report>();
//        listReport.add(new Report("Báo cáo tháng", "Điểm danh tháng", "1-1-2000"));
        for (int i = 1; i <= 12; i++) {
            Date curentTime = Calendar.getInstance().getTime();
            listReport.add(new Report("Báo cáo " + i, "Điểm danh tháng " + i, DateFormat.getDateInstance().format(curentTime)));
        }


        recycleView = findViewById(R.id.thongke);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        BaoCaoRecycleViewAdapter baocao = new BaoCaoRecycleViewAdapter(this, listReport);
        recycleView.setLayoutManager(layoutManager);
        recycleView.setHasFixedSize(true);
        recycleView.setAdapter(baocao);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
