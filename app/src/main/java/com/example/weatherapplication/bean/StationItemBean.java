package com.example.weatherapplication.bean;

import com.google.gson.annotations.SerializedName;
//{"collectorName":"济南水科院土壤监测系统地表1","collectorPort":"","id":"17","latitude":"36.58","longitude":"116.83"}
public class StationItemBean {
    @SerializedName("collectorName")
    private String collectorName;
    @SerializedName("collectorPort")
    private String collectorPort;
    @SerializedName("id")
    private String id;

    @SerializedName("latitude")
    private String latitude;
    @SerializedName("longitude")
    private String longitude;

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setCollectorName(String collectorName) {
        this.collectorName = collectorName;
    }

    public String getCollectorName() {
        return collectorName;
    }

    public void setCollectorPort(String collectorPort) {
        this.collectorPort = collectorPort;
    }

    public String getCollectorPort() {
        return collectorPort;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return "StationItemBean{" +
                "collectorName='" + collectorName + '\'' +
                ", collectorPort='" + collectorPort + '\'' +
                ", id='" + id + '\'' +
                ", latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                '}';
    }
}
