package com.example.weatherapplication.bean;

import com.google.gson.annotations.SerializedName;

public class CarbonItemDataBean {
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

    @Override
    public String toString() {
        return "CarbonItemDataBean{" +
                "acquisitionTime='" + acquisitionTime + '\'' +
                ", value='" + value + '\'' +
                '}';
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
