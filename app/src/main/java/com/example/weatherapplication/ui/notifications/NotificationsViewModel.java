package com.example.weatherapplication.ui.notifications;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class NotificationsViewModel extends ViewModel {

    private MutableLiveData<String> mText;
    private String mapFlag = null;
    private String communityFlag = null;

    public NotificationsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("评价模块待开发！");
    }

    public LiveData<String> getText() {
        return mText;
    }

    public String getMapFlag() {
        return mapFlag;
    }

    public void setMapFlag(String mapFlag) {
        this.mapFlag = mapFlag;
    }

    public String getCommunityFlag() {
        return communityFlag;
    }

    public void setCommunityFlag(String communityFlag) {
        this.communityFlag = communityFlag;
    }
}