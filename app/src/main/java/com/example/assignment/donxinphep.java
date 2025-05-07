package com.example.assignment;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

public class donxinphep extends AppCompatActivity {
    private Toolbar toolbar1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donxinphep);
//        ActionBar actionBar = getSupportActionBar();
//        actionBar.setTitle("Đơn xin phép"); //Thiết lập tiêu đề nếu muốn
//        String title = actionBar.getTitle().toString(); //Lấy tiêu đề nếu muốn
////        actionBar.hide(); //Ẩn ActionBar nếu muốn
////
//
//        actionBar.setDisplayShowHomeEnabled(true);
////        actionBar.setLogo(R.mipmap.back);    //Icon muốn hiện thị
//        actionBar.setDisplayUseLogoEnabled(true);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar1 = (Toolbar) findViewById(R.id.toolbar2);
        toolbar1.setTitle("Đơn xin phép");
        setSupportActionBar(toolbar1);

    }

    //handle mui ten back

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
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
}