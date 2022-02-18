package com.example.weatherapplication.bean;

import com.google.gson.annotations.SerializedName;
public class DayReportBean {
    @SerializedName("acquisitionTime")
    private String acquisitionTime;
    @SerializedName("col1")
    private String col1;
    @SerializedName("collectorName")
    private String collectorName;

    public String getAcquisitionTime() {
        return acquisitionTime;
    }

    public void setAcquisitionTime(String acquisitionTime) {
        this.acquisitionTime = acquisitionTime;
    }

    public String getCol1() {
        return col1;
    }

    public void setCol1(String col1) {
        this.col1 = col1;
    }

    public String getCollectorName() {
        return collectorName;
    }

    public void setCollectorName(String collectorName) {
        this.collectorName = collectorName;
    }

    @Override
    public String toString() {
        return "DayReportBean{" +
                "acquisitionTime='" + acquisitionTime + '\'' +
                ", col1='" + col1 + '\'' +
                ", collectorName='" + collectorName + '\'' +
                '}';
    }
}
