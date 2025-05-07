package com.example.assignment;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class ThongTinNhanVienActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    private Toolbar toolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.thong_tin_nhan_vien);

        toolbar = (Toolbar) findViewById(R.id.ttnv);
        toolbar.setTitle("Thông tin nhân viên");
        setSupportActionBar(toolbar);

        TextView nameTxt=findViewById(R.id.s_name);
        String s_name="Staff don't have profile";
        Bundle extras=getIntent().getExtras();
        if(extras!=null){
            s_name=extras.getString("s_name");
        }
        nameTxt.setText(s_name);
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
