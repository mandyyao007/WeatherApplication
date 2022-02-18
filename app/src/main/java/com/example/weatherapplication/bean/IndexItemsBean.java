package com.example.weatherapplication.bean;

import com.google.gson.annotations.SerializedName;
public class IndexItemsBean {
    @SerializedName("col")
    private String col;
    @SerializedName("collectorConfigId")
    private String collectorConfigId;
    @SerializedName("description")
    private String description;
    @SerializedName("maskType")
    private String maskType;
    @SerializedName("name")
    private String name;
    @SerializedName("unit")
    private String unit;

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getCol() {
        return col;
    }

    public void setCol(String col) {
        this.col = col;
    }

    public String getCollectorConfigId() {
        return collectorConfigId;
    }

    public void setCollectorConfigId(String collectorConfigId) {
        this.collectorConfigId = collectorConfigId;
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



    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    @Override
    public String toString() {
        return "IndexItemsBean{" +
                "col='" + col + '\'' +
                ", collectorConfigId='" + collectorConfigId + '\'' +
                ", description='" + description + '\'' +
                ", maskType='" + maskType + '\'' +
                ", name='" + name + '\'' +
                ", unit='" + unit + '\'' +
                '}';
    }
}

