package com.example.weatherapplication.bean;

import com.google.gson.annotations.SerializedName;
/*
{"accessFlag":"","areaNo":"浦东新区","cityNo":"市辖区","collectorId":"","collectorName":"","columns":"","contactMail":"","contactName":"",
"contactTelephone":"","contractor":"",
"latLng1":"121.537222,31.392622","latLng2":"121.525095,31.391112","latLng3":"121.533018,31.385239","latLng4":"121.544517,31.38581",
"latitude":"","longitude":"","mapInfo":"","overlayType":"Q","owner":"",
"provinceNo":"上海市","radius":"","weatherStationId":"22","weatherStationName":"上海园林"}
 */
public class WeatherStationItemBean {
    @SerializedName("weatherStationName")
    private String weatherStationName;
    @SerializedName("weatherStationId")
    private String weatherStationId;

    @SerializedName("latLng1")
    private String latLng1;
    @SerializedName("latLng2")
    private String latLng2;
    @SerializedName("latLng3")
    private String latLng3;
    @SerializedName("latLng4")
    private String latLng4;

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

    public String getLatLng1() {
        return latLng1;
    }

    public void setLatLng1(String latLng1) {
        this.latLng1 = latLng1;
    }

    public String getLatLng2() {
        return latLng2;
    }

    public void setLatLng2(String latLng2) {
        this.latLng2 = latLng2;
    }

    public String getLatLng3() {
        return latLng3;
    }

    public void setLatLng3(String latLng3) {
        this.latLng3 = latLng3;
    }

    public String getLatLng4() {
        return latLng4;
    }

    public void setLatLng4(String latLng4) {
        this.latLng4 = latLng4;
    }

    @Override
    public String toString() {
        return "WeatherStationItemBean{" +
                "weatherStationName='" + weatherStationName + '\'' +
                ", weatherStationId='" + weatherStationId + '\'' +
                ", latLng1='" + latLng1 + '\'' +
                ", latLng2='" + latLng2 + '\'' +
                ", latLng3='" + latLng3 + '\'' +
                ", latLng4='" + latLng4 + '\'' +
                ", latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                '}';
    }
}
