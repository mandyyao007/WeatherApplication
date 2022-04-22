package com.example.weatherapplication.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/*
[{"cdvList":[{"acquisitionTime":"2022-01-26 02:00:00","val":"36.89"},
"col":"`col8`","collectorConfigId":"","configType":"","description":"土壤湿度","maskType":"Avg","name":"SoilVWC_Avg1 (m3/m3)","unit":"m3/m3"}
 */
public class DaysDataItemBean {
    @SerializedName("name")
    private String name;
    @SerializedName("unit")
    private String unit;
    @SerializedName("description")
    private String description;
    @SerializedName("maskType")
    private String maskType;
    @SerializedName("cdvList")
    private List<DaysDataItemDetailBean> mItemDetailBeans;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMaskType() {
        return maskType;
    }

    public void setMaskType(String maskType) {
        this.maskType = maskType;
    }

    public List<DaysDataItemDetailBean> getmItemDetailBeans() {
        return mItemDetailBeans;
    }

    public void setmItemDetailBeans(List<DaysDataItemDetailBean> mItemDetailBeans) {
        this.mItemDetailBeans = mItemDetailBeans;
    }

    @Override
    public String toString() {
        return "TreeDataItemBean{" +
                "name='" + name + '\'' +
                ", unit='" + unit + '\'' +
                ", description='" + description + '\'' +
                ", maskType='" + maskType + '\'' +
                ", mItemDetailBeans=" + mItemDetailBeans +
                '}';
    }
}
