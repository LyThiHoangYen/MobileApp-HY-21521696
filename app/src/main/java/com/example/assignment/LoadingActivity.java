package com.example.assignment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class LoadingActivity extends Activity {
    private ImageView logo;
    private ImageView imageView1;
    private ImageView imageView2;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading);
        logo = (ImageView) findViewById(R.id.imageView);


        View.OnClickListener listenerToLogin = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();

                //Gán context và tên lớp Activity cần chạy
                intent.setClass(view.getContext(), LoginActivity.class);

                //Nếu có truyền tham số cho Activity
//                intent.putExtra("noidung", textView.getText());

                //Gửi Intent cho hệ thống Android để kích hoạt Activity
                startActivity(intent);
                //Muốn Activity thứ nhất kết thúc thì thêm finish();
                //finish();

            }
        };

        logo.setOnClickListener(listenerToLogin);

   }
}
