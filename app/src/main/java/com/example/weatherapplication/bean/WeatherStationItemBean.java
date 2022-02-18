package com.example.weatherapplication.bean;

import com.google.gson.annotations.SerializedName;
/*
{"accessFlag":"","areaNo":"周宁县","cityNo":"宁德市","collectorId":"","collectorName":"","columns":"","contactMail":"","contactName":"","contactTelephone":"","contractor":"福建农科院",
"latitude":"27.10","longitude":"119.34","mapInfo":"","owner":"","provinceNo":"福建省","weatherStationId":"6","weatherStationName":"土肥所周宁气象观测站"

 */
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
