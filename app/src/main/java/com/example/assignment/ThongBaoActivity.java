package com.example.assignment;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


public class ThongBaoActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    private Toolbar toolbar;
    private String[] items = {"Bảng tin Công Ty", "Bảng tin Phòng IT", "Bảng tin phòng kinh doanh", "Bảng tin phòng nhân sự"};
    private Button button_taothongbao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.thong_bao);

        toolbar = (Toolbar) findViewById(R.id.ttnv);
        toolbar.setTitle("Tạo thông báo");
        setSupportActionBar(toolbar);

        //xu ly spinner
        Spinner spin = (Spinner) findViewById(R.id.spinner_thongbao);
        spin.setOnItemSelectedListener(this);
        ArrayAdapter<String> aa = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, items);
        // provide a particular design for the drop-down lines
        aa.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);
        // associate GUI spinner and adapter
        spin.setAdapter(aa);


        //xuly handle button dang tin
        button_taothongbao = (Button) findViewById(R.id.button_taothongbao);
        View.OnClickListener listenerToMainActivity = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();

                //Gán context và tên lớp Activity cần chạy
                intent.setClass(view.getContext(), MainActivity.class);
                //Nếu có truyền tham số cho Activity
//                intent.putExtra("noidung", textView.getText());
                //Gửi Intent cho hệ thống Android để kích hoạt Activity
                startActivity(intent);
                //Muốn Activity thứ nhất kết thúc thì thêm finish();
                finish();

            }
        };

        button_taothongbao.setOnClickListener(listenerToMainActivity);
    }

    //xu ly spinner
    public void onItemSelected(
            AdapterView<?> parent, View v, int position, long id) {
    }
    public void onNothingSelected(AdapterView<?> parent) {
    }




    //handle mui ten back

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }



}
