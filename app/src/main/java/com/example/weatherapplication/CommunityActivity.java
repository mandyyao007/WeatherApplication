package com.example.weatherapplication;

import android.content.Intent;
import android.os.Bundle;

import com.example.weatherapplication.adapter.CommunityAdapter;
import com.example.weatherapplication.bean.CommunityBean;
import com.example.weatherapplication.bean.CommunityItemBean;
import com.example.weatherapplication.util.Def;
import com.example.weatherapplication.util.NetUtil;
import com.example.weatherapplication.util.StatusBarUtils;
import androidx.appcompat.app.AppCompatActivity;

import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class CommunityActivity extends AppCompatActivity implements View.OnClickListener  {
    private ImageView backIv;
    private ListView communityLv;
    private List<CommunityItemBean> mDatas; //列表数据源
    private CommunityAdapter adapter;
    private String userName,weatherStationId,page,weatherStationName;
    private TextView communityName ;
    private static final String TAG = "CommunityActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community);
        StatusBarUtils.setColor(this, getResources().getColor(R.color.stationTop));
        //View view = this.getLayoutInflater().inflate(R.layout.activity_community,null);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        /////
        userName = getIntent().getStringExtra("userName");
        weatherStationId = getIntent().getStringExtra("weatherStationId");
        weatherStationName = getIntent().getStringExtra("weatherStationName");
        page = getIntent().getStringExtra("page");
        if (weatherStationId == null) {
            weatherStationId = Def.DefWeatherStationId;
            weatherStationName = Def.DefWeatherStationName;
        }
        Log.d(TAG,"========userName==:"+ userName);
        Log.d(TAG,"======weatherStationId==:"+ weatherStationId);
        Log.d(TAG,"======page==:"+ page);
        Log.d(TAG,"======weatherStationName==:"+ weatherStationName);
        //initView(view);
        initView();
        initAdapter();
        communityName.setText(weatherStationName);
        communityLv.setOnItemClickListener(listener);
        backIv.setOnClickListener(this);
    }

    private void initAdapter() {
        mDatas = new ArrayList<>();
        // 设置适配器
        Log.d(TAG, "==========initAdapter=============:" +mDatas);
        adapter = new CommunityAdapter(this, mDatas);
        communityLv.setAdapter(adapter);
    }

    private void initView() {
        communityLv = (ListView)findViewById(R.id.community_lv);
        backIv = findViewById(R.id.community_iv_back);
        communityName = findViewById(R.id.tv_community_name);
    }

    AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
            Intent intent = new Intent();
            CommunityItemBean selectedCommunity = mDatas.get(arg2);
            Log.d(TAG,"=$$$$$$$$$$$$=selectedCommunity==:"+ selectedCommunity);
            Log.d(TAG,"=$$$$$$$$$$$$=CommunityId==:"+ selectedCommunity.getCommunityId());
            Log.d(TAG,"=$$$$$$$$$$$$=CommunityName==:"+  selectedCommunity.getCommunityName());
            Log.d(TAG,"=$$$$$$$$$$$$=weatherStationId==:"+ weatherStationId);
            Log.d(TAG,"=$$$$$$$$$$$$=weatherStationName==:"+ weatherStationName);
            intent.putExtra("communityId", selectedCommunity.getCommunityId());
            intent.putExtra("userName", userName);
            intent.putExtra("communityName", selectedCommunity.getCommunityName());
            intent.putExtra("weatherStationId",weatherStationId);
            intent.putExtra("weatherStationName",weatherStationName);
            intent.putExtra("communityFlag","Y");
            //if("home".equals(page) || page ==null){
            //    intent.putExtra("fragment_flag", 2);
            //}else{
            intent.putExtra("fragment_flag", 3);
            //}
            intent.setClass(CommunityActivity.this, MainActivity.class);
            startActivity(intent);
        }
    };
    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "==========1111111111111111=============:" );
        userName = this.getIntent().getStringExtra("userName");
        weatherStationId = this.getIntent().getStringExtra("weatherStationId");
        page = getIntent().getStringExtra("page");
        Log.d(TAG, "=========22222222222222222222=============:" );
        List<CommunityItemBean> communityItemBeans = new ArrayList<>();
        try {
            if(weatherStationId == null){
                weatherStationId =Def.DefWeatherStationId;
            }
            Log.d(TAG, "==========33333333333333333333333=============:" );
            CommunityBean communityBean = NetUtil.getCommunityBean(weatherStationId);
            Log.d(TAG, "==========communityBean=============:" + communityBean);
            if(communityBean != null  && communityBean.getmItemBeans()!= null){
                communityItemBeans = communityBean.getmItemBeans();
                if(communityItemBeans.size() > 0){
                    Log.d(TAG, "========communityItemBeans=============:" + communityItemBeans);
                }
            }
            Log.d(TAG, "=================communityItemBeans==========:"+communityItemBeans);
            mDatas.clear();
            mDatas.addAll(communityItemBeans);
            adapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onClick(View v) {
        finish();
    }
}