package com.example.assignment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class DanhBaNhanSuActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private Toolbar toolbar;
    private String[] phong_ban = {"Phòng IT", "Phòng HR", "Phòng Marketing", "Phòng Sale"};

    //for recycle view
    RecyclerView recycleView;
    private List<Staff> listStaff;
    private DanhBaRecycleViewAdapter.RecyclerViewClickListener listener;

    //    String name[], danh_ba[],id[],sex[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.danh_ba_nhan_su);

        toolbar = (Toolbar) findViewById(R.id.ttnv);
        toolbar.setTitle("Tạo thông báo");
        setSupportActionBar(toolbar);

        //xu ly spinner
        Spinner spin = (Spinner) findViewById(R.id.phong_ban);
        spin.setOnItemSelectedListener(this);
        ArrayAdapter<String> aa = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, phong_ban);
        // provide a particular design for the drop-down lines
        aa.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);
        // associate GUI spinner and adapter
        spin.setAdapter(aa);

        //for recycle view


        listStaff = new ArrayList<Staff>();
        listStaff.add(new Staff("Lưu Văn Tiến", "Phòng IT-Thực tập sinh", 1814315, "Nam",R.drawable.ava));
        listStaff.add(new Staff("Phan Khánh Thịnh", "Phòng IT-Thực tập sinh", 1814316, "Nữ",R.drawable.ava));
        listStaff.add(new Staff("Phạm Nguyễn Anh Tài", "Phòng IT-Thưởng phòng", 1814319, "Nam",R.drawable.ava));
        listStaff.add(new Staff("Phạm Thị Anh Tài", "Phòng IT-Phó phòng", 1814313, "Nữ",R.drawable.ava));
        listStaff.add(new Staff("Tạ Ngọc Ánh", "Phòng IT-Fresher", 1814311, "Nữ",R.drawable.ava));
        listStaff.add(new Staff("Lưu Văn Thịnh", "Phòng IT-Thực tập sinh", 1814310, "Nam",R.drawable.ava));


        recycleView =findViewById(R.id.danhba);
        
        setOnClickListener();

        LinearLayoutManager layoutManager=new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        DanhBaRecycleViewAdapter danhba = new DanhBaRecycleViewAdapter(this, listStaff,listener);
        recycleView.setLayoutManager(layoutManager);
        recycleView.setHasFixedSize(true);
        recycleView.setAdapter(danhba);
    }

    private void setOnClickListener() {
        listener=new DanhBaRecycleViewAdapter.RecyclerViewClickListener() {
            @Override
            public void onClick(View v, int position) {
                Intent intent=new Intent(getApplicationContext(),ThongTinNhanVienActivity.class);
                intent.putExtra("s_name",listStaff.get(position).getStaffName());
                startActivity(intent);
            }
        };
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
