package com.example.assignment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class ThemNhanVienActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    Toolbar toolbar;
    private ImageView logo;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.them_nhan_vien);

        toolbar = (Toolbar) findViewById(R.id.themnv);
        toolbar.setTitle("Thêm nhân viên");
        setSupportActionBar(toolbar);

        logo = (ImageView) findViewById(R.id.camera);
        View.OnClickListener listenerToUpdateAnhThe = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();

                //Gán context và tên lớp Activity cần chạy
                intent.setClass(view.getContext(), UpdateAnhTheActivity.class);

                //Nếu có truyền tham số cho Activity
//                intent.putExtra("noidung", textView.getText());

                //Gửi Intent cho hệ thống Android để kích hoạt Activity
                startActivity(intent);
                //Muốn Activity thứ nhất kết thúc thì thêm finish();
                //finish();

            }
        };

        logo.setOnClickListener(listenerToUpdateAnhThe);
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
