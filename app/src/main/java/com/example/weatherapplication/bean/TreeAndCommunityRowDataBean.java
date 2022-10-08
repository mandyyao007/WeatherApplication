package com.example.weatherapplication.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TreeAndCommunityRowDataBean {
    @SerializedName("mainConfigId")
    private String mainConfigId;
    @SerializedName("treeDataList")
    private List<TreeAndCommunityDataItemBean> treeAndCommunityDataItemBean;

    public String getMainConfigId() {
        return mainConfigId;
    }

    public void setMainConfigId(String mainConfigId) {
        this.mainConfigId = mainConfigId;
    }

    public List<TreeAndCommunityDataItemBean> getTreeAndCommunityDataItemBean() {
        return treeAndCommunityDataItemBean;
    }

    public void setTreeAndCommunityDataItemBean(List<TreeAndCommunityDataItemBean> treeAndCommunityDataItemBean) {
        this.treeAndCommunityDataItemBean = treeAndCommunityDataItemBean;
    }

    @Override
    public String toString() {
        return "TreeAndCommunityRowDataBean{" +
                "mainConfigId='" + mainConfigId + '\'' +
                ", treeAndCommunityDataItemBean=" + treeAndCommunityDataItemBean +
                '}';
    }
}
