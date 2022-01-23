package com.example.weatherapplication.ui.dashboard;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.model.LatLng;
import com.example.weatherapplication.LoginActivity;
import com.example.weatherapplication.R;
import com.example.weatherapplication.bean.StationItemBean;
import com.example.weatherapplication.databinding.FragmentDashboardBinding;
import com.example.weatherapplication.util.NetUtil;
import com.example.weatherapplication.util.ToastUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class DashboardFragment extends Fragment {

    private DashboardViewModel dashboardViewModel;
    private FragmentDashboardBinding binding;
    private MapView mMapView = null;
    private BaiduMap mBaiduMap;
    private double lingitude;//精度
    private double latitude;//维度
    private LocationClient mLocationClient;
    private List<LatLng> list;///经纬度列表
    private String userName;
    private boolean isFirstLocate = true;
    private BroadcastReceiver receiver;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =  new ViewModelProvider(this).get(DashboardViewModel.class);
        SDKInitializer.initialize(getActivity().getApplicationContext());
        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View rootView = binding.getRoot();
        //userName = this.getIntent().getStringExtra("userName");
        userName = "admin";
        LocationClient mLocationClient;  //定位客户端
        boolean isFirstLocate = true;
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }else {
            requestLocation();
        }
        return rootView;
    }
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getActivity(), "没有定位权限！", Toast.LENGTH_LONG).show();
                    getActivity().finish();
                } else {
                    requestLocation();
                }
        }
    }
    private void requestLocation() {
        initLocation();
        mLocationClient.start();
    }
    private void initLocation() {  //初始化
        mLocationClient = new LocationClient(getActivity().getApplicationContext());
        mMapView = binding.getRoot().findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();//获取百度控制器
        mMapView.showScaleControl(true);//显示比例按钮。默认显示
        mMapView.showZoomControls(true);//显示缩放按钮。默认显示
        mMapView.removeViewAt(1); // 不显示百度地图Logo
        float maxZoomLevel = mBaiduMap.getMaxZoomLevel();//获取地图最大缩放级别
        float minZoomLevel = mBaiduMap.getMinZoomLevel();//获取地图最小缩放级别
        Log.d("fan","-----maxZoomLevel======"+maxZoomLevel+"-----minZoomLevel======"+minZoomLevel);
        LocationClientOption option = new LocationClientOption();
        MapStatusUpdate mapStatusUpdate = null;
        //设置地图中心点，默认是天安门
        mapStatusUpdate = MapStatusUpdateFactory.newLatLng(new LatLng( 24.926552,118.70253));
        mBaiduMap.setMapStatus(mapStatusUpdate);
        mBaiduMap.setMyLocationEnabled(true);
        //定位初始化
        mLocationClient = new LocationClient(getActivity());
        //通过LocationClientOption设置LocationClient相关参数
        LocationClientOption locationOption = new LocationClientOption();
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(1000);
        //设置locationClientOption
        mLocationClient.setLocOption(locationOption);
        //开启地图定位图层
        mLocationClient.start();
        //设置地图缩放为15
        mapStatusUpdate = MapStatusUpdateFactory.zoomTo(15);
        mBaiduMap.setMapStatus(mapStatusUpdate);

        MarkerOptions options = new MarkerOptions();
        BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.icon_location);
        try {
            List<StationItemBean> stationItems = NetUtil.getStationItemInfo(userName);;
            System.out.println("=================stationItems==========:"+stationItems);
            Iterator it = stationItems.iterator();
            List overlayOptionses=new ArrayList();
            mBaiduMap.clear();//先清除图层
            while(it.hasNext()){
                StationItemBean item = (StationItemBean) it.next();
                Log.d("fan","############station=====Items==:"+item.toString());
                float latitude =  Float.parseFloat(item.getLatitude());
                float longitude=  Float.parseFloat(item.getLongitude());
                Log.d("fan","############latitude==:"+latitude+"############longitude==:"+longitude);
                LatLng point = new LatLng( latitude,longitude);
                if( point!= null){
                    options = new MarkerOptions().position(point)//位置
                            .icon(icon)             //图标
                            .draggable(false);     //设置图标是否可拖动
                    overlayOptionses.add(options);
                }
                mBaiduMap.addOverlays(overlayOptionses);// 在地图上添加Marker数组，并显示
                Log.d("fan","22222222222222");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        //设置扫描时间间隔
        option.setScanSpan(1000);
        ////设置定位模式，三选一
       // option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        //设置需要地址信息
       // option.setIsNeedAddress(true);
        //保存定位参数
       /// mLocationClient.setLocOption(option);
    }
    //调用地图后销毁地图的代码
    public void onDestory(){
        super.onDestroy();
        mMapView.onDestroy();
    }
    @Override
    public void onResume(){
        super.onResume();
        mMapView.onResume();
    }
    @Override
    public void onPause(){
        super.onPause();
        mMapView.onPause();
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}