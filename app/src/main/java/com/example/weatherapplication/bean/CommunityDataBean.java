package com.example.weatherapplication.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CommunityDataBean {
    @SerializedName("total")
    private int total;
    @SerializedName("rows")
    private List<CommunityDataItemBean> communityDataItemBeans;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<CommunityDataItemBean> getCommunityDataItemBeans() {
        return communityDataItemBeans;
    }

    public void setCommunityDataItemBeans(List<CommunityDataItemBean> communityDataItemBeans) {
        this.communityDataItemBeans = communityDataItemBeans;
    }

    @Override
    public String toString() {
        return "CommunityDataBean{" +
                "total=" + total +
                ", communityDataItemBeans=" + communityDataItemBeans +
                '}';
    }
}
