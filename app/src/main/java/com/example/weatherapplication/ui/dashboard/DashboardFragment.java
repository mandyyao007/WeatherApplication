package com.example.weatherapplication.ui.dashboard;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
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
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MapViewLayoutParams;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolygonOptions;
import com.baidu.mapapi.map.Stroke;
import com.baidu.mapapi.model.LatLng;
import com.example.weatherapplication.MainActivity;
import com.example.weatherapplication.R;
import com.example.weatherapplication.bean.CollectorItemBean;
import com.example.weatherapplication.bean.WeatherStationItemBean;
import com.example.weatherapplication.buiness.StationFacade;
import com.example.weatherapplication.databinding.FragmentDashboardBinding;
import com.example.weatherapplication.util.NetUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class DashboardFragment extends Fragment {

    private static final String TAG = "DashboardFragment";
    private DashboardViewModel dashboardViewModel;
    private FragmentDashboardBinding binding;
    private MapView mMapView = null;
    private BaiduMap mBaiduMap;
    private LocationClient mLocationClient;
    private String userName,collectorName,collectorId;
    private View pop;
    ////2022/02/15
    private TextView tvStationName,tvAirTem,tvAirHum,tvSoilTem,tvSoilHum,tvSoilSalt,tvPlantFluid,tvPlantDia,tvPlantRad,
            valAirTem,valAirHum,valSoilTem,valSoilHum,valSoilSalt,valPlantFluid,valPlantDia,valPlantRad;
    private ListView detailLv,detailLvLeft,valueLvLeft;
    private String[] mDatas,mValueDatas,mValue,mDatasLeft,mValueDatasLeft; //列表数据源
    private ArrayAdapter detailAdapter,valueAdapter,detailAdapterLeft,valueAdapterLeft;
    private List<LatLng> mLatLnglist;
    private  MapStatusUpdate mapStatusUpdate = null;
    private boolean infoWindowShown = false ;
    private BitmapDescriptor icon,iconClick;
    private Marker lastMarker = null;//定义上次点击的marker

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =  new ViewModelProvider(this).get(DashboardViewModel.class);
        SDKInitializer.initialize(getActivity().getApplicationContext());
        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View fragmentDashboardView = binding.getRoot();
        userName = getActivity().getIntent().getStringExtra("userName");
//        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
//        }else {
            requestLocation(fragmentDashboardView);
        //}
        return fragmentDashboardView;
    }
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults,View fragmentDashboardView) {
        switch (requestCode) {
            case 1:
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getActivity(), "没有定位权限！", Toast.LENGTH_LONG).show();
                    getActivity().finish();
                } else {
                    requestLocation(fragmentDashboardView);
                }
        }
    }
    private void requestLocation(View fragmentDashboardView) {
        initLocation(fragmentDashboardView);
        mLocationClient.start();
    }
    private void initLocation(View fragmentDashboardView) {  //初始化
        mLocationClient = new LocationClient(getActivity().getApplicationContext());
        mMapView = fragmentDashboardView.findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();//获取百度控制器
        mMapView.removeViewAt(1); // 不显示百度地图Logo
        float maxZoomLevel = mBaiduMap.getMaxZoomLevel();//获取地图最大缩放级别
        float minZoomLevel = mBaiduMap.getMinZoomLevel();//获取地图最小缩放级别
        LocationClientOption option = new LocationClientOption();

        //设置地图中心点，默认是上海园林，百度地图的默认中心是天安门
        mapStatusUpdate = MapStatusUpdateFactory.newLatLng(new LatLng( 31.38771,121.531994));
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
        mapStatusUpdate = MapStatusUpdateFactory.zoomTo(17);//设置地图缩放为18
        mBaiduMap.setMapStatus(mapStatusUpdate);
        initMarker();///初始化标志
        addArea(Color.parseColor("#FFFFFFFF"), Color.parseColor("#80FFFFFF"), mLatLnglist);//添加多边形区域
        mBaiduMap.setOnMarkerClickListener(markerClickListener);//注册点击监听器事件
        mBaiduMap.setOnMapClickListener(mapClickListener);
        mBaiduMap.showMapPoi(false);
        //设置扫描时间间隔
        option.setScanSpan(1000);
    }
    private void addArea(int strokeColor, int fillColor, List<LatLng> latLnglist) {
        // 定义多边形的属性信息
        PolygonOptions polygonOptions = new PolygonOptions();
        // 添加多个多边形边框的顶点
        polygonOptions.points(latLnglist);
        // 设置多边形的边框颜色，32位 ARGB格式，默认为黑色，第一个参数是边框宽度，单位是像素
        polygonOptions.stroke(new Stroke(5, strokeColor));
        // 设置多边形的填充颜色，32位ARGB格式
        polygonOptions.fillColor(fillColor);
        // 在地图上添加一个多边形（polygon）对象
        mBaiduMap.addOverlay(polygonOptions);
    }

    private void initLatLngData(WeatherStationItemBean weatherStationItemBean) {
        mLatLnglist = new ArrayList<>();
        int position = 0;
        mLatLnglist.add(position++, new LatLng(Float.parseFloat(weatherStationItemBean.getLatLng1().split(",")[1]),Float.parseFloat(weatherStationItemBean.getLatLng1().split(",")[0])));
        mLatLnglist.add(position++, new LatLng(Float.parseFloat(weatherStationItemBean.getLatLng2().split(",")[1]),Float.parseFloat(weatherStationItemBean.getLatLng2().split(",")[0])));
        mLatLnglist.add(position++, new LatLng(Float.parseFloat(weatherStationItemBean.getLatLng3().split(",")[1]),Float.parseFloat(weatherStationItemBean.getLatLng3().split(",")[0])));
        mLatLnglist.add(position++, new LatLng(Float.parseFloat(weatherStationItemBean.getLatLng4().split(",")[1]),Float.parseFloat(weatherStationItemBean.getLatLng4().split(",")[0])));
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
    BaiduMap.OnMapClickListener mapClickListener = new BaiduMap.OnMapClickListener() {
        @Override
        public void onMapClick(LatLng latLng) {
            if(pop.isShown() && !infoWindowShown && pop!=null){
                infoWindowShown = true;
                pop.setVisibility(View.INVISIBLE);
                lastMarker.setIcon(icon);
                return;
            }
            if(!pop.isShown() && !infoWindowShown){
                infoWindowShown = false;
            }
        }
        @Override
        public void onMapPoiClick(MapPoi mapPoi) {
        }
    };
    //点击标志监听器
    BaiduMap.OnMarkerClickListener markerClickListener = new BaiduMap.OnMarkerClickListener() {
        @Override
        public boolean onMarkerClick(Marker marker) {
            //显示一个泡泡，单击泡泡有事件
            iconClick = BitmapDescriptorFactory.fromResource(R.drawable.icon_click_marker);
            if(lastMarker != null){
                //判断上次点击的和这次点击的是不是同一个
                if(lastMarker.getId() == marker.getId()){

                }else{//不是的话将上次的icon还原
                    lastMarker.setIcon(icon);
                }
            }
            lastMarker = marker ;
            lastMarker.setIcon(iconClick);
            infoWindowShown = false;
            Bundle bundle = marker.getExtraInfo();
            String id = bundle.getString("id");
            String collectorId = bundle.getString("collectorId");
            if("1".equals(id) ||"2".equals(id)||"3".equals(id)||"4".equals(id)){
                if (pop == null) {
                    try {
                        pop = View.inflate(getActivity(), R.layout.marker_layout, null);
                        initPopView(pop,collectorId);
                        mMapView.addView(pop, createLayoutParams(marker.getPosition()));
                    } catch (Exception e) {
                    e.printStackTrace();
                    }
                } else {
                    try {
                        initPopView(pop,collectorId);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    mMapView.updateViewLayout(pop, createLayoutParams(marker.getPosition()));
                }
                pop.setVisibility(View.VISIBLE);
                try {
                    tvStationName.setText(marker.getTitle());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                tvStationName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //将userName和stationId串到StationActity
                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        //从marker中获取info信息
                        String weatherStationId = bundle.getString("weatherStationId");
                        intent.putExtra("weatherStationId", weatherStationId);
                        intent.putExtra("userName", userName);
                        intent.putExtra("fragment_flag", 2);
                        intent.putExtra("collectorId",collectorId);
                        intent.putExtra("collectorName", bundle.getString("collectorName"));
                        /*Log.d(TAG, "-----weatherStationId======" + weatherStationId
                                +"-----userName======" + userName);
                        Log.d(TAG, "-----collectorId======" + collectorId
                                + "-----collectorName======" + bundle.getString("collectorName"));*/
                        startActivity(intent);
                    }
                });
            }else{
                if(pop.isShown()){
                    marker.setIcon(icon);
                    pop.setVisibility(View.INVISIBLE);
                }
            }
            return true;
        }
    };

    private void initPopView(View pop,String collectorId) throws IOException {
        StationFacade stationFacade = StationFacade.getInstance();
        tvStationName = (TextView) pop.findViewById(R.id.tv_station_name);
        tvAirTem =  (TextView) pop.findViewById(R.id.tx_air_tem);
        tvAirHum =  (TextView) pop.findViewById(R.id.tx_air_hum);
        tvSoilTem =  (TextView) pop.findViewById(R.id.tx_soil_tem);
        tvSoilHum = (TextView) pop.findViewById(R.id.tx_soil_hum);
        tvSoilSalt = (TextView) pop.findViewById(R.id.tx_soil_salt);
        tvPlantFluid = (TextView) pop.findViewById(R.id.tx_plant_fluid);
        tvPlantDia = (TextView) pop.findViewById(R.id.tx_plant_dia);
        tvPlantRad = (TextView) pop.findViewById(R.id.tx_plant_rad);
        valAirTem = (TextView) pop.findViewById(R.id.val_air_tem);
        valAirHum = (TextView) pop.findViewById(R.id.val_air_hum);
        valSoilTem = (TextView) pop.findViewById(R.id.val_soil_tem);
        valSoilHum = (TextView) pop.findViewById(R.id.val_soil_hum);
        valSoilSalt = (TextView) pop.findViewById(R.id.val_soil_salt);
        valPlantFluid = (TextView) pop.findViewById(R.id.val_plant_fluid);
        valPlantDia = (TextView) pop.findViewById(R.id.val_plant_dia);
        valPlantRad = (TextView) pop.findViewById(R.id.val_plant_rad);
        Map indexMap = stationFacade.initIndex(collectorId);
        Log.d(TAG,"==================indexMap=====" + indexMap);
        for(Iterator it = indexMap.keySet().iterator();it.hasNext();){
            String indexAndUnit = (String) it.next();
            Log.d(TAG,"==================indexAndUnit=====" + indexAndUnit);
            String index = indexAndUnit.split(",")[0];
            String unit = indexAndUnit.split(",")[1];
            Log.d(TAG,"======index==========:"+index+"======unit==========:"+unit+"########collectorId======" + collectorId);
            String tem = stationFacade.getNewestData(indexAndUnit,collectorId,1)+" "+unit;
            try {
                if("空气温度".equals(index) || "AirTC_Avg1".equals(index) ){
                    tvAirTem.setText(index+":");
                    valAirTem.setText(tem);
                }
                if("空气湿度".equals(index)||"RH_Avg1".equals(index)){
                    tvAirHum.setText(index+":");
                    valAirHum.setText(tem);
                }
                if("土壤温度".equals(index) || "SoilTemp_Avg1".equals(index)){
                    tvSoilTem.setText(index+":");
                    valSoilTem.setText(tem);
                }
                if("土壤湿度".equals(index) || "SoilEC_Avg1".equals(index)){
                    tvSoilHum.setText(index+":");
                    valSoilHum.setText(tem);
                }
                if("土壤盐分".equals(index) || "SoilVWC_Avg1".equals(index)){
                    tvSoilSalt.setText(index+":");
                    valSoilSalt.setText(tem);
                }

                if("树干液流".equals(index)|| "DD_Avg1".equals(index)){
                     tvPlantFluid.setText(index+":");
                     valPlantFluid.setText(tem);
                }
                if("54".equals(collectorId)){
                    tvSoilTem.setText("");
                    valSoilTem.setText("");
                    tvSoilHum.setText("");
                    valSoilHum.setText("");
                    tvSoilSalt.setText("");
                    valSoilSalt.setText("");
                    tvPlantRad.setText("");
                    valPlantRad.setText("");
                    tvPlantDia.setText("");
                    valPlantDia.setText("");
                }
                if("树木胸径增长".equals(index)|| ("PAR_Avg1".equals(index) && !"54".equals(collectorId))){
                    if("树木胸径增长".equals(index)){
                        tvPlantDia.setText(index.substring(0,4)+":");
                    }else{
                        tvPlantDia.setText(index+":");
                    }
                    valPlantDia.setText(tem);
                }else if( ("PAR_Avg1".equals(index) && "54".equals(collectorId))){
                    tvPlantFluid.setText(index+":");
                    valPlantFluid.setText(tem);
                }

                if("光合有效辐射".equals(index)|| "TDP_Avg1".equals(index)){
                    tvPlantRad.setText(index+":");
                    valPlantRad.setText(tem);
                }
            }catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

    //创建布局参数
    private MapViewLayoutParams createLayoutParams(LatLng position){
        MapViewLayoutParams.Builder builder =  new MapViewLayoutParams.Builder();
        builder.layoutMode(MapViewLayoutParams.ELayoutMode.absoluteMode);//mapMode是指定坐标类型为经纬度
        builder.point(new Point(540,700)).width(mMapView.getWidth()).height(700);//设置标志的位置,屏幕坐标
        //builder.position(position);//设置标志的位置，经纬度
        builder.yOffset(100);//设置View的偏移量
        //builder.width(mMapView.getWidth());
        //builder.height(400);
        MapViewLayoutParams params = builder.build();
        return params;
    }
    //////根据username权限抓取站点位置并初始化标记覆盖物
    private void initMarker() {
        icon = BitmapDescriptorFactory.fromResource(R.drawable.icon_location);
        try {
            List<WeatherStationItemBean> stationItems = NetUtil.getWeatherStationItemInfo(userName);
            Iterator it = stationItems.iterator();
            int i = 1;
            mBaiduMap.clear();//先清除图层
            if(it.hasNext()){
                WeatherStationItemBean item = (WeatherStationItemBean) it.next();
                //Log.d(TAG,"############station=====Items==:"+item.toString());
                String weatherStationId = item.getWeatherStationId();
                //Log.d(TAG,"############weatherStationId==:"+weatherStationId);
                String reg = "^[0-9]+(.[0-9]+)?$";
                initLatLngData(item);//初始化四边形的点
                List<CollectorItemBean> collectorItems = NetUtil.getStationItemInfo(userName,weatherStationId);
                //Log.d(TAG,"############collectorItems==:"+collectorItems);
                Iterator iter = collectorItems.iterator();
                while(iter.hasNext()){
                    CollectorItemBean collectorItem =  (CollectorItemBean)iter.next();
                    //Log.d(TAG,"============collectorItem==:"+collectorItem);
                    if((!"".equals(collectorItem.getLatitude()) && collectorItem.getLatitude().matches(reg))
                            && (!"".equals(collectorItem.getLongitude()) && collectorItem.getLongitude().matches(reg)) ){
                        float latitude =  Float.parseFloat(collectorItem.getLatitude()) ;
                        float longitude=  Float.parseFloat(collectorItem.getLongitude());
                        //Log.d(TAG,"############latitude==:"+latitude+"############longitude==:"+longitude);
                        LatLng point = new LatLng( latitude,longitude);
                        collectorId = collectorItem.getId();
                        collectorName = collectorItem.getCollectorName();
                        //Log.d(TAG,"-------------collectorItem==:"+collectorItem);
                        //Log.d(TAG,"############collectorName==:"+collectorName);
                        //Log.d(TAG,"############collectorId==:"+collectorId);
                        if( point!= null){
                            /////Bundle用来传值 也可以识别点击的是哪一个marker
                            Bundle mBundle = new Bundle();
                            mBundle.putString("weatherStationId", item.getWeatherStationId());
                            mBundle.putString("weatherStationName", item.getWeatherStationName());
                            mBundle.putString("collectorId", collectorId);
                            mBundle.putString("collectorName",collectorName);
                            mBundle.putString("id", i+"");
                            OverlayOptions options = new MarkerOptions().position(point)//位置
                                .title(item.getWeatherStationName()+"("+collectorItem.getCollectorName()+")")//标题
                                .icon(icon)             //图标
                                .draggable(false) //设置图标是否可拖动
                                .extraInfo(mBundle) ;//这里bundle 跟maker关联上;
                            mBaiduMap.addOverlay(options);//在地图上添加Marker数组，并显示
                        }
                    }
                    i++;
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