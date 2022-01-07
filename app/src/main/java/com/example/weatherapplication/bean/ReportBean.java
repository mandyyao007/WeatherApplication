package com.example.weatherapplication.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ReportBean {
    @SerializedName("total")
    private int total;
    @SerializedName("rows")
    private List<DayReportBean> mDayReportBeans;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<DayReportBean> getmDayReportBeans() {
        return mDayReportBeans;
    }

    public void setmDayReportBeans(List<DayReportBean> mDayReportBeans) {
        this.mDayReportBeans = mDayReportBeans;
    }

    @Override
    public String toString() {
        return "ReportBean{" +
                "total=" + total +
                ", mDayReportBeans=" + mDayReportBeans +
                '}';
    }
}
