package com.example.weatherapplication.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CommunityBean {
    @SerializedName("total")
    private int total;
    @SerializedName("rows")
    private List<CommunityItemBean> mItemBeans;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<CommunityItemBean> getmItemBeans() {
        return mItemBeans;
    }

    public void setmItemBeans(List<CommunityItemBean> mItemBeans) {
        this.mItemBeans = mItemBeans;
    }

    @Override
    public String toString() {
        return "CommunityBean{" +
                "total=" + total +
                ", mItemBeans=" + mItemBeans +
                '}';
    }
}
