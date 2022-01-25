package com.example.weatherapplication.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WeatherStationBean {
    @SerializedName("total")
    private int total;
    @SerializedName("rows")
    private List<WeatherStationItemBean> mItemBeans;

    public void setmItemBeans(List<WeatherStationItemBean> mItemBeans) {
        this.mItemBeans = mItemBeans;
    }

    public List<WeatherStationItemBean> getmItemBeans() {
        return mItemBeans;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return "StationBean{" +
                "total=" + total +
                ", mItemBeans=" + mItemBeans +
                '}';
    }
}
