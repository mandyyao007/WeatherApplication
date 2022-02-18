package com.example.weatherapplication.bean;

import com.google.gson.annotations.SerializedName;
public class WeatherStationItemBean {
    @SerializedName("weatherStationName")
    private String weatherStationName;    @SerializedName("weatherStationId")
    private String weatherStationId;

    @SerializedName("latitude")
    private String latitude;
    @SerializedName("longitude")
    private String longitude;

    public String getWeatherStationName() {
        return weatherStationName;
    }

    public void setWeatherStationName(String weatherStationName) {
        this.weatherStationName = weatherStationName;
    }

    public String getWeatherStationId() {
        return weatherStationId;
    }

    public void setWeatherStationId(String weatherStationId) {
        this.weatherStationId = weatherStationId;
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
        return "WeatherStationItemBean{" +
                "weatherStationName='" + weatherStationName + '\'' +
                ", weatherStationId='" + weatherStationId + '\'' +
                ", latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                '}';
    }
}
