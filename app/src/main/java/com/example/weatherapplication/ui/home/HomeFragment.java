package com.example.weatherapplication.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weatherapplication.R;
import com.example.weatherapplication.adapter.BeforeReportAdapter;
import com.example.weatherapplication.adapter.FutureWeatherAdapter;
import com.example.weatherapplication.bean.DayReportBean;
import com.example.weatherapplication.bean.DayWeatherBean;
import com.example.weatherapplication.bean.IndexBean;
import com.example.weatherapplication.bean.IndexItemsBean;
import com.example.weatherapplication.bean.ReportBean;
import com.example.weatherapplication.bean.StationBean;
import com.example.weatherapplication.bean.StationItemBean;
import com.example.weatherapplication.bean.WeatherBean;
import com.example.weatherapplication.databinding.FragmentHomeBinding;
import com.example.weatherapplication.util.NetUtil;
import com.example.weatherapplication.view.HScrollView;
import com.example.weatherapplication.view.LineChartView;
import com.google.gson.Gson;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;
    ////
    private AppCompatSpinner mStationSpinner,mIndexSpinner;
    private ArrayAdapter<String> mSpAdapter,mIndexAdapter;
    private String[] mCities,mStations,mIndexs;
    private TextView tvWeather,tvTem,tvTemLowHigh,tvWin,tvAir;
    private ImageView ivWeather;
    private RecyclerView rlvFutureWeather;
    private FutureWeatherAdapter mWeatherAdapter;
    private DayWeatherBean todayWeather;
    String  userName;
    private Map stationsMap,indexMap;
    private BeforeReportAdapter mReportAdapter;
    /////////
    private LineChartView mLineChartView;
    private HScrollView hScrollView;
//    private Handler mHandler = new Handler(Looper.myLooper()){
//        @Override
//        public void handleMessage(@NonNull Message msg) {
//            super.handleMessage(msg);
//            if(msg.what == 0){
//                String weather = (String) msg.obj;
//                Log.d("fan","====weather==:"+weather);
//                Gson gson = new Gson();
//                WeatherBean weatherBean = gson.fromJson(weather, WeatherBean.class);
//                Log.d("fan","====解析后的weatherBean==:"+weatherBean.toString());
//                updateUiOfWeather(weatherBean);
//            }
//        }
//    };
    ///
    private Handler mHandler = new Handler(Looper.myLooper()){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if(msg.what == 0){
                String report = (String) msg.obj;
                Log.d("fan","====report==:"+report);
                Gson gson = new Gson();
                ReportBean reportBean = gson.fromJson(report, ReportBean.class);
                Log.d("fan","====解析后的reportBean==:"+reportBean.toString());
                updateUiOfReport(reportBean);
                setLineView(mLineChartView,hScrollView);
               // setLineView(reportBean);
            }
        }
    };
    private void updateUiOfReport(ReportBean reportBean) {
        if(reportBean == null){
            return ;
        }
        List<DayReportBean> dayReports = reportBean.getmDayReportBeans();
        if(dayReports == null){
            return ;
        }
        DayReportBean todayReport = dayReports.get(6);
        if(todayReport == null){
            return;
        }
        tvTem.setText(todayReport.getCol1());
        tvWeather.setText(todayReport.getAcquisitionTime());
        //tvWeather.setText(todayWeather.getWea()+"("+todayWeather.getDay()+")");
        //tvTemLowHigh.setText(todayWeather.getTem2()+"~"+todayWeather.getTem1());
        //tvWin.setText(todayWeather.getWin()[0]+todayWeather.getWinSpeed());
        ///tvAir.setText("空气："+todayWeather.getAir()+todayWeather.getAirLevel()+"\n"+todayWeather.getAirTips());
        //ivWeather.setImageResource(getImgResOfWeather(todayWeather.getWeaImg()));
        //Collections.reverse(dayReports);
        dayReports.remove(6); // 去掉当天的天气

        mReportAdapter = new BeforeReportAdapter(this.getActivity(), dayReports);
        rlvFutureWeather.setAdapter(mReportAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getActivity(),LinearLayoutManager.HORIZONTAL,false);
        rlvFutureWeather.setLayoutManager(layoutManager);
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View fragmentHomeView = binding.getRoot();
        //fragmentHomeView.findViewById(R.id.rlv_future_weather);
        //rlvFutureWeather= container.findViewById(R.id.rlv_future_weather);
        userName = getActivity().getIntent().getStringExtra("userName");
        //System.out.println("=============userName=====:"+userName);
        try {
            initView(fragmentHomeView,userName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fragmentHomeView;
    }
    /*@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initView();
    }*/
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void updateUiOfWeather(WeatherBean weatherBean) {
        if(weatherBean == null){
            return ;
        }
        List<DayWeatherBean> dayWeathers = weatherBean.getDayWeatherBeans();
        if(dayWeathers == null){
            return ;
        }
        DayWeatherBean todayWeather = dayWeathers.get(0);
        if(todayWeather == null){
            return;
        }
        //tvTem.setText(todayWeather.getTem());
        //tvWeather.setText(todayWeather.getWea()+"("+todayWeather.getDay()+")");
        //tvTemLowHigh.setText(todayWeather.getTem2()+"~"+todayWeather.getTem1());
        //tvWin.setText(todayWeather.getWin()[0]+todayWeather.getWinSpeed());
        ///tvAir.setText("空气："+todayWeather.getAir()+todayWeather.getAirLevel()+"\n"+todayWeather.getAirTips());
        //ivWeather.setImageResource(getImgResOfWeather(todayWeather.getWeaImg()));

        dayWeathers.remove(0); // 去掉当天的天气

        // 未来天气的展示
        mWeatherAdapter = new FutureWeatherAdapter(this.getActivity(), dayWeathers);
        rlvFutureWeather.setAdapter(mWeatherAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getActivity(),LinearLayoutManager.HORIZONTAL,false);
        rlvFutureWeather.setLayoutManager(layoutManager);
    }
    private int getImgResOfWeather(String weaStr){
        int result = 0;
        switch(weaStr){
            case "xue":
                result = R.drawable.icon_weather_daxue;
                break;
            case "lei":
                result = R.drawable.icon_weather_leizhenyu;
                break;
            case "shachen":
                result = R.drawable.icon_weather_mai;
                break;
            case "wu":
                result = R.drawable.icon_weather_wu;
                break;
            case "bingbo":
                result = R.drawable.icon_weather_bingbao;
                break;
            case "yun":
                result = R.drawable.icon_weather_duoyun_day;
                break;
            case "yu":
                result = R.drawable.icon_weather_dayu;
                break;
            case "yin":
                result = R.drawable.icon_weather_yin;
                break;
            case "qing":
                result = R.drawable.icon_weather_qing;
                break;
            default:
                result = R.drawable.icon_weather_dafeng;
        }
        return  result;
    }


    private void initView(View fragmentHomeView ,String userName) throws IOException {
        mStationSpinner = fragmentHomeView.findViewById(R.id.sp_station);
        mIndexSpinner = fragmentHomeView.findViewById(R.id.sp_index);
        hScrollView = (HScrollView) fragmentHomeView.findViewById(R.id.HScrollView);
        mLineChartView = (LineChartView) fragmentHomeView.findViewById(R.id.simpleLineChart);
        mCities = getResources().getStringArray(R.array.cities);
        StationBean stationBean = NetUtil.getStationInfo(userName);
        mStations = getStationList(stationBean);
        stationsMap = getStationMap(stationBean);
        //mSpAdapter = new ArrayAdapter<String>(this.getActivity(),R.layout.sp_item_layout,mCities);
        mSpAdapter = new ArrayAdapter<String>(this.getActivity(),R.layout.sp_item_layout,mStations);
        mStationSpinner.setAdapter(mSpAdapter);
        Log.d("fan","====STATION info==:"+stationBean.toString());
        mStationSpinner.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               //String selectedCity = mCities[position];
                String  stationId = (String) stationsMap.get(mStations[position]);
                Log.d("fan","===stationId==:"+stationId);
               // getWeatherOfCity(selectedCity);
                try {
                    IndexBean indexbean = NetUtil.getIndexInfo(stationId);
                    mIndexs = getIndexOfStation(indexbean);
                    indexMap = getIndexOfStationMap(indexbean);
                    mIndexAdapter = new ArrayAdapter<String>(parent.getContext(),R.layout.index_item_layout,mIndexs);
                    mIndexSpinner.setAdapter(mIndexAdapter);
                    mIndexSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            String selectedIndex = mIndexs[position];
                            Log.d("fan","===selectedIndex==:"+selectedIndex);
                            String selectConfigId = (String) indexMap.get(selectedIndex);
                            Log.d("fan","===selectConfigId==:"+selectConfigId);
                            Date endDate = new Date();
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                            Calendar c = Calendar.getInstance();
                            c.add(Calendar.DATE, -7);
                            String startDateStr = sdf.format(c.getTime());
                            String endDateStr = sdf.format(endDate);
                           // getReportOfIndex(selectConfigId,stationId,startDateStr,endDateStr);
                            Log.d("fan","====222222222222222222==:");
                            getReportOfIndex(selectConfigId,stationId,"2021-11-26","2021-12-02");

                            //setLineView(fragmentHomeView);
                        }
                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        setLineView(mLineChartView,hScrollView);
        tvWeather =fragmentHomeView.findViewById(R.id.tv_weather);
        tvTem =fragmentHomeView.findViewById(R.id.tv_tem);
        tvTemLowHigh = fragmentHomeView.findViewById(R.id.tv_tem_low_high);
        tvWin = fragmentHomeView.findViewById(R.id.tv_win);
        tvAir =fragmentHomeView.findViewById(R.id.tv_air);
        ivWeather =(ImageView)fragmentHomeView.findViewById(R.id.iv_weather);
        rlvFutureWeather = fragmentHomeView.findViewById(R.id.rlv_future_weather);

    }
    private void getReportOfIndex(String selectConfigId, String stationId, String startDateStr, String endDateStr) {
        //开启子线程，请求网络
        new Thread(new Runnable() {
            @Override
            public void run() {
                //请求网络
                try {
                    String reportOfIndex =  NetUtil.getReportOfIndex(selectConfigId,stationId,startDateStr,endDateStr);
                    //使用Handler将数据传送给主线程
                    Message message = new Message();
                    message.what = 0;
                    message.obj = reportOfIndex;
                    mHandler.sendMessage(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private Map getIndexOfStationMap(IndexBean indexbean) {
        LinkedHashMap indexMap = new LinkedHashMap();
        List<IndexItemsBean> indexItems = indexbean.getmIndexItemBeans();
        Log.d("fan","====index=====Items==:"+indexItems.toString());
        if(indexItems == null){
            return null;
        }else{
            Iterator it = indexItems.iterator();
            while(it.hasNext()){
                IndexItemsBean item = (IndexItemsBean) it.next();
                Log.d("fan","*********index=====Items==:"+item.toString());
                indexMap.put(item.getDescription(),item.getCollectorConfigId());
            }

        }
        Log.d("fan","*********indexMap==:"+indexMap);
        return indexMap;
    }

    private String[] getIndexOfStation(IndexBean indexbean) throws IOException {
        List<IndexItemsBean> indexItems = indexbean.getmIndexItemBeans();
        Log.d("fan","====indexItems==:"+indexItems.toString());
        int count = indexbean.getTotal();
        String[] indexs = new String[count];
        int i =0;
        if(indexItems == null){
            return null;
        }else{
            Iterator it = indexItems.iterator();
            while(it.hasNext()){
                IndexItemsBean item = (IndexItemsBean) it.next();
                indexs[i] = item.getDescription();
                i++;
            }
            for(int j=0; j<count;j++){
                System.out.println("======indexs =====:"+indexs[j] );
            }

        }

        return indexs;
    }
    private Map getIndexMap(StationBean stationBean) {
        LinkedHashMap stationMap = new LinkedHashMap();
        List<StationItemBean> stationItems = stationBean.getmItemBeans();
        Log.d("fan","====station=====Items==:"+stationItems.toString());
        if(stationItems == null){
            return null;
        }else{
            Iterator it = stationItems.iterator();
            while(it.hasNext()){
                StationItemBean item = (StationItemBean) it.next();
                Log.d("fan","*********station=====Items==:"+item.toString());
                stationMap.put(item.getCollectorName(),item.getId());
            }

        }
        Log.d("fan","*********stationMap==:"+stationMap);
        return stationMap;
    }
    private LinkedHashMap getStationMap(StationBean stationBean) {
        LinkedHashMap stationMap = new LinkedHashMap();
        List<StationItemBean> stationItems = stationBean.getmItemBeans();
        Log.d("fan","====station=====Items==:"+stationItems.toString());
        if(stationItems == null){
            return null;
        }else{
            Iterator it = stationItems.iterator();
            while(it.hasNext()){
                StationItemBean item = (StationItemBean) it.next();
                Log.d("fan","*********station=====Items==:"+item.toString());
                stationMap.put(item.getCollectorName(),item.getId());
            }

        }
        Log.d("fan","*********stationMap==:"+stationMap);
        return stationMap;
    }

    private String[] getStationList(StationBean stationBean) {
        List<StationItemBean> stationItems = stationBean.getmItemBeans();
        Log.d("fan","====stationItems==:"+stationItems.toString());
        int count = stationBean.getTotal();
        String[] stations = new String[count];
        int i =0;
        if(stationItems == null){
            return null;
        }else{
            Iterator it = stationItems.iterator();
            while(it.hasNext()){
               StationItemBean item = (StationItemBean) it.next();
               stations[i] = item.getCollectorName();
               i++;
            }
            for(int j=0; j<count;j++){
                System.out.println("======station =====:"+stations[j] );
            }

        }

        return stations;
    }

    private void getWeatherOfCity(String selectedCity) {
        //开启子线程，请求网络
        new Thread(new Runnable() {
            @Override
            public void run() {
                //请求网络
                try {
                    String weatherOfCity =  NetUtil.getWeatherOfCity(selectedCity);
                    //使用Handler将数据传送给主线程
                    Message message = new Message();
                    message.what = 0;
                    message.obj = weatherOfCity;
                    mHandler.sendMessage(message);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            //  case R.id.main_iv_add:
            //    intent.setClass(this,CityManagerActivity.class);
            //    break;
        }
        startActivity(intent);
    }

     private void setLineView( LineChartView mLineChartView,HScrollView hScrollView ){
        //时间 (x轴)
        String[] xItem = {"00:00","01:00","02:00","03:00","04:00","05:00","06:00","07:00","08:00","09:00","10:00","11:00","12:00","13:00","14:00","15:00",
                "16:00","17:00","18:00","19:00","20:00","21:00","22:00","23:00"};
        ArrayList xItemArray = new ArrayList();
        for (int i = 0; i < xItem.length; i++) {
            xItemArray.add(xItem[i]);
        }
         Log.d("fan","*********111111111111111111==:");
        //天气
        //String[] weather = {"多云","多云","阴天","小雨","小雨","小雨","小雨","小雨","小雨","小雨","小雨","小雨","阴天","阴天","阴天","多云","多云","多云","中雨","中雨","多云","多云","多云","多云"};
         String[] weather = {"","","","","","","","","","","","","","","","","","","","","","","",""};
         ArrayList weatherArray = new ArrayList();
        for (int i = 0; i < weather.length; i++) {
            weatherArray.add(weather[i]);
        }
        //温度
        Double[] yItem = {0000.24,0000.26,0000.25,0000.30,0000.23,0000.26,0000.30,0009.06,0186.13,0472.95,0727.53,0890.82,0990.19,0937.92,0774.03,0512.55,0243.60,0022.98,0000.18,0000.29,0000.27,0000.27,0000.28,0000.21};
        ArrayList<Double> yItemArray = new ArrayList<>();
        for (int i = 0; i < yItem.length; i++) {
            yItemArray.add(yItem[i]);
        }
         Log.d("fan","*********2222222222222222==:");
        mLineChartView.setXItem(xItemArray);
        mLineChartView.setYItem(yItemArray);
        mLineChartView.setWeather(weatherArray);
        mLineChartView.setmHScrollView(hScrollView);
    }
}