package com.example.weatherapplication.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/*
{"configName":"树株1固碳价值","dataList":[{"acquisitionTime":"2022-01-26 02:00:00","val":"84"},{"acquisitionTime":"2022-01-25 03:00:00","val":"80"}],"treeConfigId":"4"},
 */
public class TreeDataItemBean {

    @SerializedName("configName")
    private String configName;
    @SerializedName("dataList")
    private List<TreeDataItemDetailBean> mTreeDataItemDetailBean;
    @SerializedName("treeConfigId")
    private String treeConfigId;


    public String getConfigName() {
        return configName;
    }

    public void setConfigName(String configName) {
        this.configName = configName;
    }

    public List<TreeDataItemDetailBean> getmTreeDataItemDetailBean() {
        return mTreeDataItemDetailBean;
    }

    public void setmTreeDataItemDetailBean(List<TreeDataItemDetailBean> mTreeDataItemDetailBean) {
        this.mTreeDataItemDetailBean = mTreeDataItemDetailBean;
    }

    public String getTreeConfigId() {
        return treeConfigId;
    }

    public void setTreeConfigId(String treeConfigId) {
        this.treeConfigId = treeConfigId;
    }
    @Override
    public String toString() {
        return "TreeDataItemBean{" +
                "configName='" + configName + '\'' +
                ", mTreeDataItemDetailBean=" + mTreeDataItemDetailBean +
                ", treeConfigId='" + treeConfigId + '\'' +
                '}';
    }

}
