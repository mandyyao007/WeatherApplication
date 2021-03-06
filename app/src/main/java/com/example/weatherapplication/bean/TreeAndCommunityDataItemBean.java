package com.example.weatherapplication.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/*
tree:{"configName":"树株1固碳价值","dataList":[{"acquisitionTime":"2022-01-26 02:00:00","val":"84"},{"acquisitionTime":"2022-01-25 03:00:00","val":"80"}],"treeConfigId":"4"},
 {"total":1,"rows":[{"configName":"群落评价1","dataList":[{"acquisitionTime":"2022-07-07 00:00:00","val":"-"},
community:{"acquisitionTime":"2022-07-06 00:00:00","val":"-"},{"acquisitionTime":"2022-07-05 00:00:00","val":"-"},{"acquisitionTime":"2022-07-04 00:00:00","val":"-"},{"acquisitionTime":"2022-07-03 00:00:00","val":"-"}
,{"acquisitionTime":"2022-07-02 00:00:00","val":"-"},{"acquisitionTime":"2022-07-01 00:00:00","val":"-"}],"treeConfigId":"68"}]}
 */
public class TreeAndCommunityDataItemBean {
    @SerializedName("configName")
    private String configName;
    @SerializedName("dataList")
    private List<TreeAndCommunityDataItemDetailBean> treeAndCommunityDataItemDetailBeanList;
    @SerializedName("treeConfigId")
    private String treeConfigId;

    public String getConfigName() {
        return configName;
    }

    public void setConfigName(String configName) {
        this.configName = configName;
    }

    public List<TreeAndCommunityDataItemDetailBean> getTreeAndCommunityDataItemDetailBeanList() {
        return treeAndCommunityDataItemDetailBeanList;
    }

    public void setTreeAndCommunityDataItemDetailBeanList(List<TreeAndCommunityDataItemDetailBean> treeAndCommunityDataItemDetailBeanList) {
        this.treeAndCommunityDataItemDetailBeanList = treeAndCommunityDataItemDetailBeanList;
    }

    public String getTreeConfigId() {
        return treeConfigId;
    }

    public void setTreeConfigId(String treeConfigId) {
        this.treeConfigId = treeConfigId;
    }

    @Override
    public String toString() {
        return "TreeAndCommunityDataItemBean{" +
                "configName='" + configName + '\'' +
                ", treeAndCommunityDataItemDetailBeanList=" + treeAndCommunityDataItemDetailBeanList +
                ", treeConfigId='" + treeConfigId + '\'' +
                '}';
    }
}
