package com.example.weatherapplication.ui.me;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MeViewModel extends ViewModel {
    private MutableLiveData<String> mText;

    public MeViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("我的模块待开发！");
    }

    public LiveData<String> getText() {
        return mText;
    }
}