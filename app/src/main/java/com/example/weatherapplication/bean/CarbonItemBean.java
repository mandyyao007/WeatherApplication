package com.example.weatherapplication.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CarbonItemBean {
    @SerializedName("configName")
    private String configName;
    @SerializedName("groupId")
    private String groupId;
    @SerializedName("mainConfigId")
    private String mainConfigId;
    @SerializedName("treeConfigId")
    private String treeConfigId;
    @SerializedName("unit")
    private String unit;
    @SerializedName("dataList")
    private List<CarbonItemDataBean> mCarbonItemDataBean;

    public String getConfigName() {
        return configName;
    }

    public void setConfigName(String configName) {
        this.configName = configName;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getMainConfigId() {
        return mainConfigId;
    }

    public void setMainConfigId(String mainConfigId) {
        this.mainConfigId = mainConfigId;
    }

    public String getTreeConfigId() {
        return treeConfigId;
    }

    public void setTreeConfigId(String treeConfigId) {
        this.treeConfigId = treeConfigId;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public List<CarbonItemDataBean> getmCarbonItemDataBean() {
        return mCarbonItemDataBean;
    }

    public void setmCarbonItemDataBean(List<CarbonItemDataBean> mCarbonItemDataBean) {
        this.mCarbonItemDataBean = mCarbonItemDataBean;
    }

    @Override
    public String toString() {
        return "CarbonItemBean{" +
                "configName='" + configName + '\'' +
                ", groupId='" + groupId + '\'' +
                ", mainConfigId='" + mainConfigId + '\'' +
                ", treeConfigId='" + treeConfigId + '\'' +
                ", unit='" + unit + '\'' +
                ", mCarbonItemDataBean=" + mCarbonItemDataBean +
                '}';
    }
}
