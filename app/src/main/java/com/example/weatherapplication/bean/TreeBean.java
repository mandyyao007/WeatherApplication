package com.example.weatherapplication.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TreeBean {
    @SerializedName("total")
    private int total;
    @SerializedName("rows")
    private List<TreeItemBean> mTreeItemBeans;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<TreeItemBean> getmTreeItemBeans() {
        return mTreeItemBeans;
    }

    public void setmTreeItemBeans(List<TreeItemBean> mTreeItemBeans) {
        this.mTreeItemBeans = mTreeItemBeans;
    }

    @Override
    public String toString() {
        return "TreeBean{" +
                "total=" + total +
                ", mTreeItemBeans=" + mTreeItemBeans +
                '}';
    }
}
