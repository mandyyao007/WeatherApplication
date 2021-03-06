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
    private String userName,collectorName,collectorId,status;
    private View pop;
    ////2022/02/15
    private TextView tvStationName,tvAirTem,tvAirHum,tvSoilTem,tvSoilHum,tvSoilSalt,tvPlantFluid,tvPlantDia,tvPlantRad,
            valAirTem,valAirHum,valSoilTem,valSoilHum,valSoilSalt,valPlantFluid,valPlantDia,valPlantRad;
    private ListView detailLv,detailLvLeft,valueLvLeft;
    private String[] mDatas,mValueDatas,mValue,mDatasLeft,mValueDatasLeft; //???????????????
    private ArrayAdapter detailAdapter,valueAdapter,detailAdapterLeft,valueAdapterLeft;
    private List<LatLng> mLatLnglist;
    private  MapStatusUpdate mapStatusUpdate = null;
    private boolean infoWindowShown = false ;
    private BitmapDescriptor icon,iconClick;
    private Marker lastMarker = null;//?????????????????????marker

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
                    Toast.makeText(getActivity(), "?????????????????????", Toast.LENGTH_LONG).show();
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
    private void initLocation(View fragmentDashboardView) {  //?????????
        mLocationClient = new LocationClient(getActivity().getApplicationContext());
        mMapView = fragmentDashboardView.findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();//?????????????????????
        mMapView.removeViewAt(1); // ?????????????????????Logo
        float maxZoomLevel = mBaiduMap.getMaxZoomLevel();//??????????????????????????????
        float minZoomLevel = mBaiduMap.getMinZoomLevel();//??????????????????????????????
        LocationClientOption option = new LocationClientOption();

        //???????????????????????????????????????????????????????????????????????????????????????
        mapStatusUpdate = MapStatusUpdateFactory.newLatLng(new LatLng( 31.38771,121.531994));
        mBaiduMap.setMapStatus(mapStatusUpdate);
        mBaiduMap.setMyLocationEnabled(true);
        //???????????????
        mLocationClient = new LocationClient(getActivity());
        //??????LocationClientOption??????LocationClient????????????
        LocationClientOption locationOption = new LocationClientOption();
        option.setOpenGps(true); // ??????gps
        option.setCoorType("bd09ll"); // ??????????????????
        option.setScanSpan(1000);
        //??????locationClientOption
        mLocationClient.setLocOption(locationOption);
        mLocationClient.start(); //????????????????????????
        mapStatusUpdate = MapStatusUpdateFactory.zoomTo(17);//?????????????????????18
        mBaiduMap.setMapStatus(mapStatusUpdate);
        initMarker();///???????????????
        addArea(Color.parseColor("#FFFFFFFF"), Color.parseColor("#80FFFFFF"), mLatLnglist);//?????????????????????
        mBaiduMap.setOnMarkerClickListener(markerClickListener);//???????????????????????????
        mBaiduMap.setOnMapClickListener(mapClickListener);
        mBaiduMap.showMapPoi(false);
        //????????????????????????
        option.setScanSpan(1000);
    }
    private void addArea(int strokeColor, int fillColor, List<LatLng> latLnglist) {
        // ??????????????????????????????
        PolygonOptions polygonOptions = new PolygonOptions();
        // ????????????????????????????????????
        polygonOptions.points(latLnglist);
        // ?????????????????????????????????32??? ARGB???????????????????????????????????????????????????????????????????????????
        polygonOptions.stroke(new Stroke(5, strokeColor));
        // ?????????????????????????????????32???ARGB??????
        polygonOptions.fillColor(fillColor);
        // ????????????????????????????????????polygon?????????
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
    //???????????????
    BaiduMap.OnMarkerDragListener markerDragListener = new BaiduMap.OnMarkerDragListener() {
        //??????????????????
        @Override
        public void onMarkerDrag(Marker marker) {
            mMapView.addView(pop,createLayoutParams(marker.getPosition()));
        }
        //??????????????????
        @Override
        public void onMarkerDragEnd(Marker marker) {
            mMapView.addView(pop,createLayoutParams(marker.getPosition()));
        }
        //??????????????????
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
                Bundle bundle = lastMarker.getExtraInfo();
                String status = bundle.getString("status");
                Log.d(TAG, "---====mapClickListener===mapClickListener--status======"+status);
                if("1".equals(status)){
                    icon = BitmapDescriptorFactory.fromResource(R.drawable.icon_location);
                }else{
                    icon = BitmapDescriptorFactory.fromResource(R.drawable.icon_location_basic);
                }
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
    //?????????????????????
    BaiduMap.OnMarkerClickListener markerClickListener = new BaiduMap.OnMarkerClickListener() {
        @Override
        public boolean onMarkerClick(Marker marker) {
            //??????????????????????????????????????????
            Bundle bundle = marker.getExtraInfo();
            String id = bundle.getString("id");
            String collectorId = bundle.getString("collectorId");
            String status = bundle.getString("status");
            Log.d(TAG, "-----status======" + status);
            if("1".equals(status)){
                iconClick = BitmapDescriptorFactory.fromResource(R.drawable.icon_click_marker);
            }else{
                iconClick = BitmapDescriptorFactory.fromResource(R.drawable.icon_click_marker_basic);
            }
            if(lastMarker != null){
                //?????????????????????????????????????????????????????????
                if(lastMarker.getId() == marker.getId()){

                }else{//????????????????????????icon??????
                    Bundle bundleLast = lastMarker.getExtraInfo();
                    String statusLast = bundleLast.getString("status");
                    if("1".equals(statusLast)){
                        icon = BitmapDescriptorFactory.fromResource(R.drawable.icon_location);
                    }else{
                        icon = BitmapDescriptorFactory.fromResource(R.drawable.icon_location_basic);
                    }
                    lastMarker.setIcon(icon);
                }
            }
            lastMarker = marker ;
            lastMarker.setIcon(iconClick);
            infoWindowShown = false;
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
                        //???userName???stationId??????StationActity
                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        //???marker?????????info??????
                        String weatherStationId = bundle.getString("weatherStationId");
                        String weatherStationName = bundle.getString("weatherStationName");
                        intent.putExtra("weatherStationId", weatherStationId);
                        intent.putExtra("weatherStationName",weatherStationName);
                        intent.putExtra("userName", userName);
                        intent.putExtra("fragment_flag", 2);
                        intent.putExtra("collectorId",collectorId);
                        intent.putExtra("collectorName", bundle.getString("collectorName"));
                        /*Log.d(TAG, "-----weatherStationId======" + weatherStationId"-----userName======" + userName);
                        Log.d(TAG, "-----collectorId======" + collectorId"-----collectorName======" + bundle.getString("collectorName"));*/
                        startActivity(intent);
                    }
                });
            }else{
                if(pop.isShown()){
                    if("1".equals(status)){
                        icon = BitmapDescriptorFactory.fromResource(R.drawable.icon_location);
                    }else{
                        icon = BitmapDescriptorFactory.fromResource(R.drawable.icon_location_basic);
                    }
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
        //Log.d(TAG,"==================indexMap=====" + indexMap);
        for(Iterator it = indexMap.keySet().iterator();it.hasNext();){
            String indexAndUnit = (String) it.next();
            //Log.d(TAG,"==================indexAndUnit=====" + indexAndUnit);
            String index = indexAndUnit.split(",")[0];
            String unit = indexAndUnit.split(",")[1];
           // Log.d(TAG,"======index==========:"+index+"======unit==========:"+unit+"########collectorId======" + collectorId);
            String tem = stationFacade.getNewestData(indexAndUnit,collectorId,1)+" "+unit;
            try {
                if("????????????".equals(index) || "AirTC_Avg1".equals(index) ){
                    tvAirTem.setText(index+":");
                    valAirTem.setText(tem);
                }
                if("????????????".equals(index)||"RH_Avg1".equals(index)){
                    tvAirHum.setText(index+":");
                    valAirHum.setText(tem);
                }
                if("????????????".equals(index) || "SoilTemp_Avg1".equals(index)){
                    tvSoilTem.setText(index+":");
                    valSoilTem.setText(tem);
                }
                if("????????????".equals(index) || "SoilEC_Avg1".equals(index)){
                    tvSoilHum.setText(index+":");
                    valSoilHum.setText(tem);
                }
                if("????????????".equals(index) || "SoilVWC_Avg1".equals(index)){
                    tvSoilSalt.setText(index+":");
                    valSoilSalt.setText(tem);
                }

                if("????????????".equals(index)|| "DD_Avg1".equals(index)){
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
                if("??????????????????".equals(index)|| ("PAR_Avg1".equals(index) && !"54".equals(collectorId))){
                    if("??????????????????".equals(index)){
                        tvPlantDia.setText(index.substring(0,4)+":");
                    }else{
                        tvPlantDia.setText(index+":");
                    }
                    valPlantDia.setText(tem);
                }else if( ("PAR_Avg1".equals(index) && "54".equals(collectorId))){
                    tvPlantFluid.setText(index+":");
                    valPlantFluid.setText(tem);
                }

                if("??????????????????".equals(index)|| "TDP_Avg1".equals(index)){
                    tvPlantRad.setText(index+":");
                    valPlantRad.setText(tem);
                }
            }catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

    //??????????????????
    private MapViewLayoutParams createLayoutParams(LatLng position){
        MapViewLayoutParams.Builder builder =  new MapViewLayoutParams.Builder();
        builder.layoutMode(MapViewLayoutParams.ELayoutMode.absoluteMode);//mapMode?????????????????????????????????
        builder.point(new Point(540,700)).width(mMapView.getWidth()).height(700);//?????????????????????,????????????
        //builder.position(position);//?????????????????????????????????
        builder.yOffset(100);//??????View????????????
        //builder.width(mMapView.getWidth());
        //builder.height(400);
        MapViewLayoutParams params = builder.build();
        return params;
    }
    //////??????username???????????????????????????????????????????????????
    private void initMarker() {

        try {
            List<WeatherStationItemBean> stationItems = NetUtil.getWeatherStationItemInfo(userName);
            Iterator it = stationItems.iterator();
            int i = 1;
            mBaiduMap.clear();//???????????????
            if(it.hasNext()){
                WeatherStationItemBean item = (WeatherStationItemBean) it.next();
                //Log.d(TAG,"############station=====Items==:"+item.toString());
                String weatherStationId = item.getWeatherStationId();
                //Log.d(TAG,"############weatherStationId==:"+weatherStationId);
                String reg = "^[0-9]+(.[0-9]+)?$";
                initLatLngData(item);//????????????????????????
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
                        status = collectorItem.getStatus();
                        //Log.d(TAG,"-------------collectorItem==:"+collectorItem+"############collectorName==:"+collectorName);
                        //Log.d(TAG,"############collectorId==:"+collectorId+"############status==:"+status);
                        if( point!= null){
                            /////Bundle???????????? ????????????????????????????????????marker
                            Bundle mBundle = new Bundle();
                            mBundle.putString("weatherStationId", item.getWeatherStationId());
                            mBundle.putString("weatherStationName", item.getWeatherStationName());
                            mBundle.putString("collectorId", collectorId);
                            mBundle.putString("collectorName",collectorName);
                            mBundle.putString("id", i+"");
                            mBundle.putString("status", status);
                            if("0".equals(status)){
                                icon = BitmapDescriptorFactory.fromResource(R.drawable.icon_location_basic);
                            }else{
                               icon = BitmapDescriptorFactory.fromResource(R.drawable.icon_location);
                            }
                            OverlayOptions options = new MarkerOptions().position(point)//??????
                                    .title(item.getWeatherStationName()+"("+collectorItem.getCollectorName()+")")//??????
                                    .icon(icon)  //??????
                                    .draggable(false) //???????????????????????????
                                    .extraInfo(mBundle) ;//??????bundle ???maker?????????;
                            mBaiduMap.addOverlay(options);//??????????????????Marker??????????????????
                        }
                    }
                    i++;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //????????????????????????????????????
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