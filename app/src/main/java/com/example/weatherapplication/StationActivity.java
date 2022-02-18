package com.example.weatherapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.weatherapplication.adapter.StationAdapter;
import com.example.weatherapplication.bean.StationItemBean;
import com.example.weatherapplication.util.NetUtil;
import com.example.weatherapplication.util.StatusBarUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class StationActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView backIv;
    private ListView stationLv;
    private List<StationItemBean> mDatas; //列表数据源
    private StationAdapter adapter;
    private String userName;
    private String weatherStationId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station);
        StatusBarUtils.setColor(this, getResources().getColor(R.color.stationTop));
        //  在Android4.0以后，会发现，只要是写在主线程（就是Activity）中的HTTP请求，运行时都会报错，这是因为Android在4.0以后为了防止应用的ANR（Aplication Not Response）异常，
        //  Android这个设计是为了防止网络请求时间过长而导致界面假死的情况发生。
        //以下适用于数据量很小的情况
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        /////
        userName = getIntent().getStringExtra("userName");
        weatherStationId = getIntent().getStringExtra("weatherStationId");
        Log.d("StationActivity","========userName==:"+ userName);
        Log.d("StationActivity","======weatherStationId==:"+ weatherStationId);
        initView();
        initAdapter();
        stationLv.setOnItemClickListener(listener);
        backIv.setOnClickListener(this);
    }

    private void initAdapter() {
        mDatas = new ArrayList<>();
        // 设置适配器
        adapter = new StationAdapter(this, mDatas);
        stationLv.setAdapter(adapter);
    }

    private void initView() {
        stationLv = (ListView) findViewById(R.id.station_lv);
        backIv = (ImageView)findViewById(R.id.station_iv_back);
    }

    AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
				Intent intent = new Intent();
				StationItemBean selectedStation = mDatas.get(arg2);
                Log.d("StationActivity","=$$$$$$$$$$$$=selectedStation==:"+ selectedStation);
                Log.d("StationActivity","=$$$$$$$$$$$$=stationName==:"+  selectedStation.getCollectorName());
                Log.d("StationActivity","=$$$$$$$$$$$$=weatherStationId==:"+ weatherStationId);
                intent.putExtra("stationId", selectedStation.getId());
                intent.putExtra("userName", userName);
                intent.putExtra("stationName", selectedStation.getCollectorName());
                intent.putExtra("weatherStationId",weatherStationId);
                intent.putExtra("fragment_flag", 2);
				intent.setClass(StationActivity.this, MainActivity.class);
				startActivity(intent);
			}
		};
    @Override
    protected void onResume() {
        super.onResume();
        userName = this.getIntent().getStringExtra("userName");
        weatherStationId = this.getIntent().getStringExtra("weatherStationId");
        try {
            if(weatherStationId == null){
               weatherStationId ="14";
            }
            List<StationItemBean> stationItems = NetUtil.getStationItemInfo(userName,weatherStationId);;
            System.out.println("=================stationItems==========:"+stationItems);
            mDatas.clear();
            mDatas.addAll(stationItems);
            adapter.notifyDataSetChanged();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onClick(View v) {
        finish();
    }
}