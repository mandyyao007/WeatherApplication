package com.example.weatherapplication.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DaysDataBean {
    @SerializedName("total")
    private int total;
    @SerializedName("rows")
    private List<DaysDataItemBean> mItemBeans;

    public void setmItemBeans(List<DaysDataItemBean> mItemBeans) {
        this.mItemBeans = mItemBeans;
    }

    public List<DaysDataItemBean> getmItemBeans() {
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
        return "TreeDataBean{" +
                "total=" + total +
                ", mItemBeans=" + mItemBeans +
                '}';
    }
}
