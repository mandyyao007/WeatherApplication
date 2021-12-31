package com.example.weatherapplication.bean;

import com.google.gson.annotations.SerializedName;

public class StationItemBean {
    @SerializedName("collectorName")
    private String collectorName;
    @SerializedName("collectorPort")
    private String collectorPort;
    @SerializedName("id")
    private String id;

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

    @Override
    public String toString() {
        return "StationItemBean{" +
                "collectorName='" + collectorName + '\'' +
                ", collectorPort='" + collectorPort + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}
