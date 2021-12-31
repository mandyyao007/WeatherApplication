package com.example.weatherapplication.bean;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WeatherBean {

    @SerializedName("cityid")
    private String cityid;
    @SerializedName("city")
    private String city;
    @SerializedName("cityEn")
    private String cityEn;
    @SerializedName("country")
    private String country;
    @SerializedName("countryEn")
    private String countryEn;
    @SerializedName("update_time")
    private String updateTime;
    @SerializedName("data")
    private List<DayWeatherBean> dayWeatherBeans;

    public String getCityid() {
        return cityid;
    }

    public void setCityid(String cityid) {
        this.cityid = cityid;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCityEn() {
        return cityEn;
    }

    public void setCityEn(String cityEn) {
        this.cityEn = cityEn;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCountryEn() {
        return countryEn;
    }

    public void setCountryEn(String countryEn) {
        this.countryEn = countryEn;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public List<DayWeatherBean> getDayWeatherBeans() {
        return dayWeatherBeans;
    }

    public void setDayWeatherBeans(List<DayWeatherBean> dayWeatherBeans) {
        this.dayWeatherBeans = dayWeatherBeans;
    }

    @Override
    public String toString() {
        return "WeatherBean{" +
                "cityid='" + cityid + '\'' +
                ", city='" + city + '\'' +
                ", cityEn='" + cityEn + '\'' +
                ", country='" + country + '\'' +
                ", countryEn='" + countryEn + '\'' +
                ", updateTime='" + updateTime + '\'' +
                ", dayWeatherBeans=" + dayWeatherBeans +
                '}';
    }
}
