package com.example.assignment;

import java.util.Date;

public class Report {
    private String r_name;
    private String r_description;
    private String r_date;

    public Report(String rname, String rdes, String rdate) {
        this.r_name = rname;
        this.r_description = rdes;
        this.r_date = rdate;
    }

    public String getR_name() {
        return r_name;
    }

    public String getR_description() {
        return r_description;
    }

    public String getR_date() {
        return r_date;
    }

    public void setR_name(String rname) {
        this.r_name = rname;
    }

    public void setR_description(String rdes) {
        this.r_description = rdes;
    }

    public void setR_date(String rdate) {
        this.r_date = rdate;
    }

}
