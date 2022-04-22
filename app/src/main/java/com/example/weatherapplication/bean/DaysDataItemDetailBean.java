package com.example.weatherapplication.bean;

import com.google.gson.annotations.SerializedName;

public class DaysDataItemDetailBean {
   /*
   {"acquisitionTime":"2022-01-26 02:00:00","val":"36.89"}
    */
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
                "acquisitionTime=" + acquisitionTime +
                ", value=" + value +
                '}';
    }
}
