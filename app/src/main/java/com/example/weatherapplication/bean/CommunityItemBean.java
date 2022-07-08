package com.example.weatherapplication.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/*

{"communityId":"1","communityName":"群落1"}]
 */
public class CommunityItemBean {
    @SerializedName("communityName")
    private String communityName;
    @SerializedName("communityId")
    private String communityId;

    public String getCommunityName() {
        return communityName;
    }

    public void setCommunityName(String communityName) {
        this.communityName = communityName;
    }

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }

    @Override
    public String toString() {
        return "CommunityItemBean{" +
                "communityName='" + communityName + '\'' +
                ", communityId='" + communityId + '\'' +
                '}';
    }
}
