package com.example.weatherapplication.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/*
{"collectorName":"滨江森林公园1号点","collectorPort":"","id":"52","latitude":"31.38771","longitude":"121.531994","status":"1"}
 */
public class CollectorItemBean {
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
    @SerializedName("status")
    private String status;

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "CollectorItemBean{" +
                "collectorName='" + collectorName + '\'' +
                ", collectorPort='" + collectorPort + '\'' +
                ", id='" + id + '\'' +
                ", latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                ", status='" + status + '\'' +
                '}';
    }


}
