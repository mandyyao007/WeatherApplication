package com.example.weatherapplication.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CarbonBean {
    @SerializedName("total")
    private int total;
    @SerializedName("rows")
    private List<CarbonItemBean> mCarbonItemBeans;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<CarbonItemBean> getmCarbonItemBeans() {
        return mCarbonItemBeans;
    }

    public void setmCarbonItemBeans(List<CarbonItemBean> mCarbonItemBeans) {
        this.mCarbonItemBeans = mCarbonItemBeans;
    }

    @Override
    public String toString() {
        return "CarbonBean{" +
                "total=" + total +
                ", mCarbonItemBeans=" + mCarbonItemBeans +
                '}';
    }
}
