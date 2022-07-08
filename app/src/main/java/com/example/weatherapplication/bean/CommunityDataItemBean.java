package com.example.weatherapplication.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/*
{"total":1,"rows":[{"configName":"群落评价1","dataList":[{"acquisitionTime":"2022-07-07 00:00:00","val":"-"},
{"acquisitionTime":"2022-07-06 00:00:00","val":"-"},{"acquisitionTime":"2022-07-05 00:00:00","val":"-"},{"acquisitionTime":"2022-07-04 00:00:00","val":"-"},{"acquisitionTime":"2022-07-03 00:00:00","val":"-"}
,{"acquisitionTime":"2022-07-02 00:00:00","val":"-"},{"acquisitionTime":"2022-07-01 00:00:00","val":"-"}],"treeConfigId":"68"}]}*/
public class CommunityDataItemBean {

    @SerializedName("configName")
    private String configName;
    @SerializedName("dataList")
    private List<CommunityDataItemDetailBean> communityDataItemDetailBeans;
    @SerializedName("treeConfigId")
    private String treeConfigId;

    public String getConfigName() {
        return configName;
    }

    public void setConfigName(String configName) {
        this.configName = configName;
    }

    public List<CommunityDataItemDetailBean> getCommunityDataItemDetailBeans() {
        return communityDataItemDetailBeans;
    }

    public void setCommunityDataItemDetailBeans(List<CommunityDataItemDetailBean> communityDataItemDetailBeans) {
        this.communityDataItemDetailBeans = communityDataItemDetailBeans;
    }

    public String getTreeConfigId() {
        return treeConfigId;
    }

    public void setTreeConfigId(String treeConfigId) {
        this.treeConfigId = treeConfigId;
    }

    @Override
    public String toString() {
        return "CommunityDataItemBean{" +
                "configName='" + configName + '\'' +
                ", communityDataItemDetailBeans=" + communityDataItemDetailBeans +
                ", treeConfigId='" + treeConfigId + '\'' +
                '}';
    }
}
