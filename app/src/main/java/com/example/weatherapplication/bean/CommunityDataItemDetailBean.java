package com.example.weatherapplication.bean;
/*
{"acquisitionTime":"2022-07-07 00:00:00","val":"4"}
 */

import com.google.gson.annotations.SerializedName;

public class CommunityDataItemDetailBean {
    @SerializedName("acquisitionTime")
    private String acquisitionTime;
    @SerializedName("val")
    private String value;

    public String getAcquisitionTime() {
        return acquisitionTime;
    }

    public void setAcquisitionTime(String acquisitionTime) {
        this.acquisitionTime = acquisitionTime;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "TreeDataItemDetailBean{" +
                "acquisitionTime='" + acquisitionTime + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
