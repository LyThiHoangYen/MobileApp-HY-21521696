package com.example.assignment;

import java.sql.Time;
import java.util.Date;

public class diemdanh {
    private String date;
    private String content;
    private String time;
    public diemdanh(String date, String content, String time){
        this.content=content;
        this.date=date;
        this.time=time;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDate(){
        return date;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime(){
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getContent(){
        return content;
    }
}
