package com.example.weatherapplication.ui.home;

import android.content.Intent;
import android.graphics.Color;
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
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
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
import java.util.Random;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;
    ////
    private AppCompatSpinner mStationSpinner,mIndexSpinner;
    private ArrayAdapter<String> mSpAdapter,mIndexAdapter;
    private String[] mCities,mStations,mIndexs;
    private TextView tvWeather,tvWin,tvAir,tvStation,tvReportData,tvTem;
    private ImageView ivWeather;
    private RecyclerView rlvFutureWeather;
    private FutureWeatherAdapter mWeatherAdapter;
    private DayWeatherBean todayWeather;
    String  userName;
    private Map stationsMap,indexMap;
    private BeforeReportAdapter mReportAdapter;
    //private LineChartView mLineChartView;
    //private HScrollView hScrollView;
    private LineChart lineChart;
    private List<Entry> entries;
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
                //updateUiOfReport(reportBean);
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
        userName = getActivity().getIntent().getStringExtra("userName");
        Log.d("fan","===userName==:"+ userName);
        mStationSpinner = fragmentHomeView.findViewById(R.id.sp_station);
        mIndexSpinner = fragmentHomeView.findViewById(R.id.sp_index);
        tvStation = (TextView) fragmentHomeView.findViewById(R.id.tv_station);
        tvReportData = (TextView) fragmentHomeView.findViewById(R.id.tv_reportdata);
        tvTem = (TextView) fragmentHomeView.findViewById(R.id.tv_tem);
        try {
            StationBean stationBean = NetUtil.getStationInfo(userName);
            mStations = getStationList(stationBean);
            stationsMap = getStationMap(stationBean);
            mSpAdapter = new ArrayAdapter<String>(this.getActivity(),R.layout.sp_item_layout,mStations);
            mStationSpinner.setAdapter(mSpAdapter);
        } catch (IOException e) {
            e.printStackTrace();
        }
       ////为mStationSpinner设置监听事件
        Log.d("fan","===cccccccccccccccccc==:");
        mStationSpinner.setOnItemSelectedListener(listenerStation);
        mIndexSpinner.setOnItemSelectedListener(listenerIndex);
     /////////////
        //setLineChart(fragmentHomeView);
       /* try {
            initView(fragmentHomeView,userName);
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        return fragmentHomeView;
    }
    AdapterView.OnItemSelectedListener listenerStation = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tvStation.setText(stationsMap.get(mStations[position]).toString()) ;
                Log.d("fan","===stationId==:"+ tvStation.getText().toString());
                IndexBean indexbean = null;
                try {
                    indexbean = NetUtil.getIndexInfo(tvStation.getText().toString());
                    mIndexs = getIndexOfStation(indexbean);
                    mIndexAdapter = new ArrayAdapter<String>(parent.getContext(), R.layout.index_item_layout,mIndexs);
                    mIndexSpinner.setAdapter(mIndexAdapter);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                indexMap = getIndexOfStationMap(indexbean);
                Log.d("fan","===indexMap==:"+ indexMap.toString());
                if(mStationSpinner.isSelected() && mIndexSpinner.isSelected()){
                     setLineChart();//绘制折线图
                }

        }
        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };
    AdapterView.OnItemSelectedListener listenerIndex = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedIndex = mIndexs[position];
                Log.d("fan", "===selectedIndex==:" + selectedIndex);
                String selectConfigId = (String) indexMap.get(selectedIndex);
                Log.d("fan", "===selectConfigId==:" + selectConfigId);
                Date endDate = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Calendar c = Calendar.getInstance();
                c.add(Calendar.DATE, -7);
                String startDateStr = sdf.format(c.getTime());
                String endDateStr = sdf.format(endDate);
                try {
                    //String reportOfIndex = NetUtil.getReportOfIndex(selectConfigId, tvStation.getText().toString(), startDateStr, endDateStr);
                    String reportOfIndex = NetUtil.getReportOfIndex(selectConfigId, tvStation.getText().toString(),
                            "2022-01-01", "2022-01-08");
                    tvReportData.setText(reportOfIndex);
                    Log.d("fan","-----reportOfIndex======"+reportOfIndex);
                    if(mStationSpinner.isSelected() && mIndexSpinner.isSelected() &&!"".equals(reportOfIndex)){
                        setLineChart();//绘制折线图
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    private void setData(ReportBean reportBean) {
        if(reportBean == null){
            return ;
        }
        List<DayReportBean> dayReports = reportBean.getmDayReportBeans();
        if(dayReports == null){
            return ;
        }
        Iterator it = dayReports.iterator();
        int count = 0 ;
        entries = new ArrayList<>();
        while(it.hasNext() && count<12 ){
            DayReportBean dayReportBean = (DayReportBean)it.next();
            it.next();
            float time = Float.parseFloat(dayReportBean.getAcquisitionTime().substring(11,13));
            float value = Float.parseFloat(dayReportBean.getCol1());
            entries.add(new Entry(time,value));
            count++;
        }
    }

    private ReportBean getData(String reportResult) {
        Gson gson = new Gson();
        ReportBean reportBean = gson.fromJson(reportResult, ReportBean.class);
        return reportBean;
    }
    private void setLineChart(){
        String reportResult = tvReportData.getText().toString();
        Log.d("fan","=&&&&&&&&&&&reportResult==:"+reportResult);
        ReportBean reportBean = getData(reportResult);
        setData(reportBean);
        lineChart = binding.getRoot().findViewById(R.id.linechart);
        LineDataSet dataSet = new LineDataSet(entries,"");
        dataSet.setColor(Color.parseColor("#000000"));//线条颜色
        dataSet.setCircleColor(Color.parseColor("#000000"));//圆点颜色
        dataSet.setCircleRadius(1f);//设置焦点圆心的大小
        dataSet.setLineWidth(1.5f);//线条宽度
        dataSet.setValueTextColor(Color.parseColor("#000000"));//设置显示值的文字颜色
        dataSet.setValueTextSize(13f);//设置显示值的文字大小
        YAxis rightAxis = lineChart.getAxisRight();
        rightAxis.setEnabled(false);//右侧Y轴不显示
        YAxis leftAxis = lineChart.getAxisLeft();//左侧Y轴
        leftAxis.setTextSize(10f);
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setTextColor(Color.RED);//设置X轴刻度颜色
        xAxis.setTextSize(13f);//设置X轴刻度字体大小
        xAxis.setDrawAxisLine(true);//设置为true，则绘制该行旁边的轴线
        xAxis.setDrawGridLines(true);//绘制网格线
        xAxis.setDrawLabels(true);//绘制标签，即X轴上的对应数值
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);//
        xAxis.setGranularity(1f);//设置最小间隔
        xAxis.setEnabled(true);
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float v, AxisBase axis) {
                return String.valueOf((int)v).concat("点");
            }
        });
        Legend legend = lineChart.getLegend();//图例
        legend.setForm(Legend.LegendForm.LINE);//设置图例的形状
        legend.setTextSize(15f);//设置图例字体大小
        legend.setFormSize(15f);//设置图例形状大小
        legend.setTextColor(Color.BLUE);//设置图例颜色
        legend.setEnabled(false);//不显示图例
        Description description = lineChart.getDescription();//图表的描述信息
        description.setEnabled(true);//是否显示图表描述信息
        LineData lineData = new LineData(dataSet);
        lineChart.setTouchEnabled(false);//设置是否可触摸
        lineChart.setScaleEnabled(true);//设置是否可缩放
        lineChart.setData(lineData);
        lineChart.setNoDataText("暂无数据");//无数据时显示的文字
        lineChart.invalidate();//图表刷新
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
        //tvWeather.setText(todayWeather.getWea()+"("+todayWeather.getDay()+")");
        //tvWin.setText(todayWeather.getWin()[0]+todayWeather.getWinSpeed());
        ///tvAir.setText("空气："+todayWeather.getAir()+todayWeather.getAirLevel()+"\n"+todayWeather.getAirTips());
        //ivWeather.setImageResource(getImgResOfWeather(todayWeather.getWeaImg()));

        dayWeathers.remove(0); // 去掉当天的天气

        // 未来天气的展示
//        mWeatherAdapter = new FutureWeatherAdapter(this.getActivity(), dayWeathers);
//        rlvFutureWeather.setAdapter(mWeatherAdapter);
//        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getActivity(),LinearLayoutManager.HORIZONTAL,false);
//        rlvFutureWeather.setLayoutManager(layoutManager);
    }


   /* private void initView(View fragmentHomeView ,String userName) throws IOException {
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
                            setLineView(mLineChartView,hScrollView);
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
        //setLineView(mLineChartView,hScrollView);
        tvWeather =fragmentHomeView.findViewById(R.id.tv_weather);
        tvWin = fragmentHomeView.findViewById(R.id.tv_win);
        tvAir =fragmentHomeView.findViewById(R.id.tv_air);
        ivWeather =(ImageView)fragmentHomeView.findViewById(R.id.iv_weather);
        rlvFutureWeather = fragmentHomeView.findViewById(R.id.rlv_future_weather);

    }*/

    /*private void initView(View fragmentHomeView ,String userName) throws IOException {
        mStationSpinner = fragmentHomeView.findViewById(R.id.sp_station);
        mIndexSpinner = fragmentHomeView.findViewById(R.id.sp_index);
        tvStation = (TextView) fragmentHomeView.findViewById(R.id.tv_station);
        tvReportData = (TextView) fragmentHomeView.findViewById(R.id.tv_reportdata);
        tvTem = (TextView) fragmentHomeView.findViewById(R.id.tv_tem);
        //mCities = getResources().getStringArray(R.array.cities);
        StationBean stationBean = NetUtil.getStationInfo(userName);
        mStations = getStationList(stationBean);
        stationsMap = getStationMap(stationBean);
        //mSpAdapter = new ArrayAdapter<String>(this.getActivity(),R.layout.sp_item_layout,mCities);
        mSpAdapter = new ArrayAdapter<String>(this.getActivity(),R.layout.sp_item_layout,mStations);
        mStationSpinner.setAdapter(mSpAdapter);
        ReportBean reportBean = null;
        Log.d("fan","====STATION info==:"+stationBean.toString());
        mStationSpinner.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tvStation.setText((String)stationsMap.get(mStations[position])) ;
                Log.d("fan","===stationId==:"+ tvStation.getText().toString());
                IndexBean indexbean = null;
                try {
                    indexbean = NetUtil.getIndexInfo(tvStation.getText().toString());
                    mIndexs = getIndexOfStation(indexbean);
                    mIndexAdapter = new ArrayAdapter<String>(parent.getContext(), R.layout.index_item_layout,mIndexs);
                    mIndexSpinner.setAdapter(mIndexAdapter);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                indexMap = getIndexOfStationMap(indexbean);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        hScrollView = (HScrollView) fragmentHomeView.findViewById(R.id.HScrollView);
        mLineChartView = (LineChartView) fragmentHomeView.findViewById(R.id.simpleLineChart);
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
                    try {
                        String reportOfIndex =  NetUtil.getReportOfIndex(selectConfigId,tvStation.getText().toString(),startDateStr,endDateStr);
                        tvReportData.setText(reportOfIndex);
                        Log.d("fan","====tvReportData.text==:"+tvReportData.getText().toString());

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        String reportResult = tvReportData.getText().toString();
        Log.d("fan","====reportResult==:"+reportResult);
    }*/
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
        //Log.d("fan","====index=====Items==:"+indexItems.toString());
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
        //Log.d("fan","====indexItems==:"+indexItems.toString());
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
        //Log.d("fan","====station=====Items==:"+stationItems.toString());
        if(stationItems == null){
            return null;
        }else{
            Iterator it = stationItems.iterator();
            while(it.hasNext()){
                StationItemBean item = (StationItemBean) it.next();
                //Log.d("fan","*********station=====Items==:"+item.toString());
                stationMap.put(item.getCollectorName(),item.getId());
            }
        }
        Log.d("fan","*********stationMap==:"+stationMap);
        return stationMap;
    }
    private LinkedHashMap getStationMap(StationBean stationBean) {
        LinkedHashMap stationMap = new LinkedHashMap();
        List<StationItemBean> stationItems = stationBean.getmItemBeans();
        //Log.d("fan","====station=====Items==:"+stationItems.toString());
        if(stationItems == null){
            return null;
        }else{
            Iterator it = stationItems.iterator();
            while(it.hasNext()){
                StationItemBean item = (StationItemBean) it.next();
                //Log.d("fan","*********station=====Items==:"+item.toString());
                stationMap.put(item.getCollectorName(),item.getId());
            }
        }
        Log.d("fan","*********stationMap==:"+stationMap);
        return stationMap;
    }

    private String[] getStationList(StationBean stationBean) {
        List<StationItemBean> stationItems = stationBean.getmItemBeans();
        //Log.d("fan","====stationItems==:"+stationItems.toString());
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

}