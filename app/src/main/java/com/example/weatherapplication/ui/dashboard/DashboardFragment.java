package com.example.weatherapplication.ui.dashboard;


import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MapViewLayoutParams;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.TextOptions;
import com.baidu.mapapi.model.LatLng;
import com.example.weatherapplication.R;
import com.example.weatherapplication.StationActivity;
import com.example.weatherapplication.bean.WeatherStationItemBean;
import com.example.weatherapplication.databinding.FragmentDashboardBinding;
import com.example.weatherapplication.util.NetUtil;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;


public class DashboardFragment extends Fragment {

    private DashboardViewModel dashboardViewModel;
    private FragmentDashboardBinding binding;
    private MapView mMapView = null;
    private BaiduMap mBaiduMap;
    private LocationClient mLocationClient;
    private String userName;
    private View pop;
    private TextView tvPop;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =  new ViewModelProvider(this).get(DashboardViewModel.class);
        SDKInitializer.initialize(getActivity().getApplicationContext());
        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View rootView = binding.getRoot();
        userName = getActivity().getIntent().getStringExtra("userName");
        Log.d("DashboardFragment","-----userName======"+userName);
        //userName = "admin";
//        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
//        }else {
            requestLocation();
        //}
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
        mMapView.removeViewAt(1); // 不显示百度地图Logo
        float maxZoomLevel = mBaiduMap.getMaxZoomLevel();//获取地图最大缩放级别
        float minZoomLevel = mBaiduMap.getMinZoomLevel();//获取地图最小缩放级别
        Log.d("DashboardFragment","-----maxZoomLevel======"+maxZoomLevel+"-----minZoomLevel======"+minZoomLevel);
        LocationClientOption option = new LocationClientOption();
        MapStatusUpdate mapStatusUpdate = null;
        //设置地图中心点，默认是天安门
        mapStatusUpdate = MapStatusUpdateFactory.newLatLng(new LatLng( 31.24149,121.49203));
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
        mLocationClient.start(); //开启地图定位图层
        mapStatusUpdate = MapStatusUpdateFactory.zoomTo(10);//设置地图缩放为15
        mBaiduMap.setMapStatus(mapStatusUpdate);
        initMarker();///初始化标志
        mBaiduMap.setOnMarkerClickListener(markerClickListener);//注册点击监听器事件
        mBaiduMap.setOnMarkerDragListener(markerDragListener);//注册拖拽监听器事件
        mBaiduMap.showMapPoi(false);
        //设置扫描时间间隔
        option.setScanSpan(1000);
    }
    //拖拽监听器
    BaiduMap.OnMarkerDragListener markerDragListener = new BaiduMap.OnMarkerDragListener() {
        //标志正在拖动
        @Override
        public void onMarkerDrag(Marker marker) {
            mMapView.addView(pop,createLayoutParams(marker.getPosition()));
        }
        //标志开始结束
        @Override
        public void onMarkerDragEnd(Marker marker) {
            mMapView.addView(pop,createLayoutParams(marker.getPosition()));
        }
        //标志开始拖动
        @Override
        public void onMarkerDragStart(Marker marker) {
            mMapView.addView(pop,createLayoutParams(marker.getPosition()));
        }
    };
    //点击标志监听器
    BaiduMap.OnMarkerClickListener markerClickListener = new BaiduMap.OnMarkerClickListener() {
        @Override
        public boolean onMarkerClick(Marker marker) {
            //显示一个泡泡，单击泡泡有事件
            if(pop == null){
                pop = View.inflate(getActivity(),R.layout.layout_pop,null);
                tvPop = (TextView )pop.findViewById(R.id.tv_pop);
                mMapView.addView(pop,createLayoutParams(marker.getPosition()));
            }else{
                mMapView.updateViewLayout(pop,createLayoutParams(marker.getPosition()));
            }
            tvPop.setText(marker.getTitle());
            pop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //将userName和stationId串到StationActity
                    Intent intent = new Intent(getActivity(), StationActivity.class);
                    //从marker中获取info信息
                    Bundle bundle = marker.getExtraInfo();
                    String  weatherStationId = bundle.getString("weatherStationId");
                    intent.putExtra("weatherStationId", weatherStationId);
                    intent.putExtra("userName",userName);
                    Log.d("DashboardFragment","-----weatherStationId======"+weatherStationId+"-----userName======"+userName);
                    startActivity(intent);
                }
            }) ;
            return true;
        }
    };
    //创建布局参数
    private MapViewLayoutParams createLayoutParams(LatLng position){
        MapViewLayoutParams.Builder builder =  new MapViewLayoutParams.Builder();
        builder.layoutMode(MapViewLayoutParams.ELayoutMode.mapMode);//指定坐标类型为经纬度
        builder.position(position);//设置标志的位置
        builder.yOffset(-80);//设置View的偏移量
        MapViewLayoutParams params = builder.build();
        return params;
    }
    //////根据username权限抓取站点位置并初始化标记覆盖物
    private void initMarker() {
        BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.icon_location);
        try {
            List<WeatherStationItemBean> stationItems = NetUtil.getWeatherStationItemInfo(userName);
            Iterator it = stationItems.iterator();
            mBaiduMap.clear();//先清除图层
            while(it.hasNext()){
                WeatherStationItemBean item = (WeatherStationItemBean) it.next();
               /// Log.d("DashboardFragment","############station=====Items==:"+item.toString());
                String reg = "^[0-9]+(.[0-9]+)?$";
                if((!"".equals(item.getLatitude()) && item.getLatitude().matches(reg)) && (!"".equals(item.getLongitude()) && item.getLongitude().matches(reg)) ){
                    float latitude =  Float.parseFloat(item.getLatitude()) ;
                    float longitude=  Float.parseFloat(item.getLongitude());
                    ///Log.d("DashboardFragment","############latitude==:"+latitude+"############longitude==:"+longitude);
                    LatLng point = new LatLng( latitude,longitude);
                    LatLng llText = new LatLng(latitude,longitude);
                    if( point!= null){
                        /////Bundle用来传值 也可以识别点击的是哪一个marker
                        Bundle mBundle = new Bundle();
                        mBundle.putString("weatherStationId", item.getWeatherStationId());
                        mBundle.putString("weatherStationName", item.getWeatherStationName());
                        OverlayOptions options = new MarkerOptions().position(point)//位置
                                .title(item.getWeatherStationName())//标题
                                .icon(icon)             //图标
                                .draggable(false) //设置图标是否可拖动
                                .extraInfo(mBundle) ;//这里bundle 跟maker关联上;
                        mBaiduMap.addOverlay(options);//在地图上添加Marker数组，并显示
//                        OverlayOptions mTextOptions= new TextOptions().text(item.getWeatherStationName())
//                                .fontSize(24)
//                                .fontColor(0xFFFF00FF)
//                                .position(llText);
//                        Overlay mText = mBaiduMap.addOverlay(mTextOptions);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
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