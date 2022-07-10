package com.example.weatherapplication.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TreeAndCommunityDataBean {
        @SerializedName("total")
        private int total;
        @SerializedName("rows")
        private List<TreeAndCommunityDataItemBean> mTreeAndCommunityDataItemBeanList;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<TreeAndCommunityDataItemBean> getmTreeAndCommunityDataItemBeanList() {
        return mTreeAndCommunityDataItemBeanList;
    }

    public void setmTreeAndCommunityDataItemBeanList(List<TreeAndCommunityDataItemBean> mTreeAndCommunityDataItemBeanList) {
        this.mTreeAndCommunityDataItemBeanList = mTreeAndCommunityDataItemBeanList;
    }

    @Override
    public String toString() {
        return "TreeAndCommunityDataBean{" +
                "total=" + total +
                ", mTreeAndCommunityDataItemBeanList=" + mTreeAndCommunityDataItemBeanList +
                '}';
    }
}
