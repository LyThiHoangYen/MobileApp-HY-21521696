package com.example.assignment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    private Button button8;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);


        button8 = (Button) findViewById(R.id.button8);
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
                //finish();

            }
        };

        button8.setOnClickListener(listenerToMainActivity);

    }
}