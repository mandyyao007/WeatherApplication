package com.example.weatherapplication.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class StationBean {
    @SerializedName("total")
    private int total;
    private List<StationItemBean> mItemBeans;

    public void setmItemBeans(List<StationItemBean> mItemBeans) {
        this.mItemBeans = mItemBeans;
    }

    public List<StationItemBean> getmItemBeans() {
        return mItemBeans;
    }

    @Override
    public String toString() {
        return "StationBean{" +
                "total=" + total +
                ", mItemBeans=" + mItemBeans +
                '}';
    }
}
