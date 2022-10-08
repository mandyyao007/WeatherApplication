package com.example.weatherapplication.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TreeAndCommunityDataBean {
        @SerializedName("total")
        private int total;
        @SerializedName("rows")
        private List<TreeAndCommunityRowDataBean> mTreeAndCommunityRowDataBeanList;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<TreeAndCommunityRowDataBean> getmTreeAndCommunityRowDataBeanList() {
        return mTreeAndCommunityRowDataBeanList;
    }

    public void setmTreeAndCommunityRowDataBeanList(List<TreeAndCommunityRowDataBean> mTreeAndCommunityRowDataBeanList) {
        this.mTreeAndCommunityRowDataBeanList = mTreeAndCommunityRowDataBeanList;
    }

    @Override
    public String toString() {
        return "TreeAndCommunityDataBean{" +
                "total=" + total +
                ", mTreeAndCommunityRowDataBeanList=" + mTreeAndCommunityRowDataBeanList +
                '}';
    }
}
