package com.example.weatherapplication.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class IndexBean {@SerializedName("total")
private int total;
    @SerializedName("rows")
    private List<IndexItemsBean> mIndexItemBeans;


    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<IndexItemsBean> getmIndexItemBeans() {
        return mIndexItemBeans;
    }

    public void setmIndexItemBeans(List<IndexItemsBean> mIndexItemBeans) {
        this.mIndexItemBeans = mIndexItemBeans;
    }

    @Override
    public String toString() {
        return "IndexBeam{" +
                "total=" + total +
                ", mIndexItemBeans=" + mIndexItemBeans +
                '}';
    }
}
