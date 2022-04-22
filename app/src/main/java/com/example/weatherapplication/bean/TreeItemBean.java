package com.example.weatherapplication.bean;

import com.google.gson.annotations.SerializedName;
/*
{"collectorId":"52","treeId":"1","treeName":"树株1"}

*/
public class TreeItemBean {
    @SerializedName("collectorId")
    private String collectorId;
    @SerializedName("treeId")
    private String treeId;
    @SerializedName("treeName")
    private String treeName;

    public String getCollectorId() {
        return collectorId;
    }

    public void setCollectorId(String collectorId) {
        this.collectorId = collectorId;
    }

    public String getTreeId() {
        return treeId;
    }

    public void setTreeId(String treeId) {
        this.treeId = treeId;
    }

    public String getTreeName() {
        return treeName;
    }

    public void setTreeName(String treeName) {
        this.treeName = treeName;
    }

    @Override
    public String toString() {
        return "TreeItemBean{" +
                "collectorId='" + collectorId + '\'' +
                ", treeId='" + treeId + '\'' +
                ", treeName='" + treeName + '\'' +
                '}';
    }
}
