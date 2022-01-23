package com.example.weatherapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.weatherapplication.adapter.StationAdapter;
import com.example.weatherapplication.bean.StationBean;
import com.example.weatherapplication.bean.StationItemBean;
import com.example.weatherapplication.util.NetUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class StationActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView backIv;
    private ListView stationLv;
    private List<StationItemBean> mDatas; //列表数据源
    private StationAdapter adapter;
    private String userName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station);
        stationLv = (ListView) findViewById(R.id.station_lv);
        backIv = (ImageView)findViewById(R.id.station_iv_back);
        mDatas = new ArrayList<>();
        // 设置适配器
        adapter = new StationAdapter(this, mDatas);
        stationLv.setAdapter(adapter);
        stationLv.setOnItemClickListener(listener);
        backIv.setOnClickListener(this);
    }
    AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
				Intent intent = new Intent();
				StationItemBean selectedStation = mDatas.get(arg2);
				System.out.println("====selectedStation===:"+ selectedStation);
                intent.putExtra("stationId", selectedStation.getId());
                intent.putExtra("userName", userName);
                intent.putExtra("stationName", selectedStation.getCollectorName());
				intent.setClass(StationActivity.this, MainActivity.class);
				startActivity(intent);
			}
		};
    @Override
    protected void onResume() {
        super.onResume();
        userName = this.getIntent().getStringExtra("userName");
        try {
            List<StationItemBean> stationItems = NetUtil.getStationItemInfo(userName);;
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