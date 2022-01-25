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
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.weatherapplication.R;
import com.example.weatherapplication.StationActivity;
import com.example.weatherapplication.adapter.BeforeReportAdapter;
import com.example.weatherapplication.bean.DayReportBean;
import com.example.weatherapplication.bean.IndexBean;
import com.example.weatherapplication.bean.IndexItemsBean;
import com.example.weatherapplication.bean.ReportBean;
import com.example.weatherapplication.bean.StationBean;
import com.example.weatherapplication.bean.StationItemBean;
import com.example.weatherapplication.databinding.FragmentHomeBinding;
import com.example.weatherapplication.util.NetUtil;
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

public class HomeFragment extends Fragment implements View.OnClickListener{

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;
    ////
    private AppCompatSpinner  mIndexSpinner;
    private ArrayAdapter<String> mIndexAdapter;
    private String[] mIndexs;
    private TextView tvReportData,tvStation;
    private ImageView ivAdd;
    String  userName,stationId,stationName;
    private Map indexMap;
    private LineChart lineChart;
    private List<Entry> entries;
    boolean first=true;//spinner第一次不触发

    private Handler mHandler = new Handler(Looper.myLooper()){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if(msg.what == 0){
                String report = (String) msg.obj;
                Gson gson = new Gson();
                ReportBean reportBean = gson.fromJson(report, ReportBean.class);
                Log.d("fan","====解析后的reportBean==:"+reportBean.toString());
            }
        }
    };

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View fragmentHomeView = binding.getRoot();
        userName = getActivity().getIntent().getStringExtra("userName");
        stationId = getActivity().getIntent().getStringExtra("stationId");
        stationName = getActivity().getIntent().getStringExtra("stationName");
        Log.d("fan","=&&&&&&&&==userName==:"+ userName);
        Log.d("fan","=&&&&&&&&== ==:"+ stationId);
        Log.d("fan","=&&&&&&&&==stationName==:"+ stationName);
        mIndexSpinner = fragmentHomeView.findViewById(R.id.sp_index);
        tvStation = fragmentHomeView.findViewById(R.id.tv_station);
        tvStation.setText(stationName);
        tvReportData = (TextView) fragmentHomeView.findViewById(R.id.tv_reportdata);
        ivAdd = (ImageView) fragmentHomeView.findViewById(R.id.iv_add);
        lineChart = (LineChart) fragmentHomeView.findViewById(R.id.linechart);
       ///index列表
        try {
            IndexBean indexbean = NetUtil.getIndexInfo(stationId);
            mIndexs = getIndexOfStation(indexbean);
            mIndexAdapter = new ArrayAdapter<String>(this.getActivity(), R.layout.index_item_layout,mIndexs);
            mIndexAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mIndexSpinner.setAdapter(mIndexAdapter);
            indexMap = getIndexOfStationMap(indexbean);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d("fan","===indexMap==:"+ indexMap.toString());
        ////为mIndexSpinner设置监听事件
        mIndexSpinner.setOnItemSelectedListener(listenerIndex);
        ivAdd.setOnClickListener(this);
        setData();
        LineData lineData = new LineData();
        LineDataSet lineDataSet = new LineDataSet(entries,"");
        lineData.addDataSet(lineDataSet);
        lineChart.setData(lineData);
        lineChart.invalidate();

        return fragmentHomeView;
    }
    @Override
    public void onClick(View v) {
        Intent intent = null;
        intent = new Intent(getActivity(), StationActivity.class);
        intent.putExtra("userName",userName);
        startActivity(intent);
    }

    AdapterView.OnItemSelectedListener listenerIndex = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            setData();
            lineChart.notifyDataSetChanged();
                        System.out.println("ccccccccccc");
                        lineChart.getData().notifyDataChanged();
            lineChart.invalidate();
           /*if (first) {
                System.out.println("111111111");
                first= false;//第一次不触发
            } else {
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
                    if(!"-1".equals(selectConfigId)){
                    //String reportOfIndex = NetUtil.getReportOfIndex(selectConfigId, tvStation.getText().toString(), startDateStr, endDateStr);
                    String reportOfIndex = NetUtil.getReportOfIndex(selectConfigId, stationId,
                            "2022-01-01", "2022-01-08");
                    tvReportData.setText(reportOfIndex);
                    Log.d("fan","-----reportOfIndex======"+reportOfIndex);
                    //if(mStationSpinner.isSelected() && mIndexSpinner.isSelected() &&!"".equals(reportOfIndex)){
                        setData();
                        System.out.println("bbbbbbbbbbb");
//                        lineChart.notifyDataSetChanged();
//                        System.out.println("ccccccccccc");
//                        lineChart.getData().notifyDataChanged();
//                        System.out.println("dddddddddddd");
//                        lineChart.invalidate();
                        System.out.println("eeeeeeeeee");
                    // }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }*/
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
    private void setData() {
        int count = 0 ;
        Log.d("fan","=&&&&&&&&==000000000000==:"+(lineChart.getData()!=null) +"======:"+(lineChart.getData().getDataSetCount()));
        if(lineChart.getData()!=null && lineChart.getData().getDataSetCount()>=1){
            lineChart.getData().getDataSetByIndex(0).removeLast();
            Log.d("fan","=&&&&&&&&==11111111111==:");
           lineChart.getData().getDataSetByIndex(0).addEntry(new Entry(11,new Random().nextInt(100)));
        }else{
            entries = new ArrayList<>();
            Log.d("fan","=&&&&&&&&==2222222222==:");
            while( count<12 ){
                entries.add(new Entry(count,new Random().nextInt(100)));
                count++;
            }
        }

//        if(entries!=null){
//            System.out.println("aaaaaaaaaaa");
//            entries.clear();
//        }

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
            entries.add(new Entry(count,new Random().nextInt(100)));
            count++;
        }
        System.out.println("=======entries=======:"+entries.toString());
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
        if(reportBean != null  && reportBean.getmDayReportBeans() != null){
            //setData(reportBean);

            Log.d("fan","=&&&&&&&&&&&entries==:"+entries);
            //if(entries!= null){
              //lineChart.setVisibility(View.VISIBLE);
               // LineDataSet dataSet = new LineDataSet(entries,"数据");
               // Log.d("fan","=&&&&&&&&&&&dataSet==:"+dataSet);
//                dataSet.setColor(Color.parseColor("#000000"));//线条颜色
//                dataSet.setCircleColor(Color.parseColor("#000000"));//圆点颜色
//                dataSet.setCircleRadius(1f);//设置焦点圆心的大小
//                dataSet.setLineWidth(1.5f);//线条宽度
//                dataSet.setValueTextColor(Color.parseColor("#000000"));//设置显示值的文字颜色
//                dataSet.setValueTextSize(13f);//设置显示值的文字大小
               // YAxis rightAxis = lineChart.getAxisRight();
               // rightAxis.setEnabled(false);//右侧Y轴不显示
               // YAxis leftAxis = lineChart.getAxisLeft();//左侧Y轴
               // ///leftAxis.setTextSize(10f);
               // XAxis xAxis = lineChart.getXAxis();
//                xAxis.setTextColor(Color.RED);//设置X轴刻度颜色
//                xAxis.setTextSize(13f);//设置X轴刻度字体大小
//                xAxis.setDrawAxisLine(true);//设置为true，则绘制该行旁边的轴线
//                xAxis.setDrawGridLines(true);//绘制网格线
//                xAxis.setDrawLabels(true);//绘制标签，即X轴上的对应数值
//                xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);//
//                xAxis.setGranularity(1f);//设置最小间隔
//                xAxis.setEnabled(true);
//                xAxis.setValueFormatter(new IAxisValueFormatter() {
//                    @Override
//                    public String getFormattedValue(float v, AxisBase axis) {
//                        return String.valueOf((int)v).concat("点");
//                    }
//                });
//                Legend legend = lineChart.getLegend();//图例
//                legend.setForm(Legend.LegendForm.LINE);//设置图例的形状
//                legend.setTextSize(15f);//设置图例字体大小
//                legend.setFormSize(15f);//设置图例形状大小
//                legend.setTextColor(Color.BLUE);//设置图例颜色
//                legend.setEnabled(false);//不显示图例
//                Description description = lineChart.getDescription();//图表的描述信息
//                description.setEnabled(true);//是否显示图表描述信息
               // LineData lineData = new LineData();
               // lineData.addDataSet(dataSet);
//                lineChart.setTouchEnabled(false);//设置是否可触摸
//                lineChart.setScaleEnabled(true);//设置是否可缩放
               // lineChart.setData(lineData);
                //lineChart.setNoDataText("暂无数据");//无数据时显示的文字
               // lineChart.invalidate();//图表刷新
               // System.out.println("ffffffffffffffffff"+lineChart.isShown());
           // }

        }

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
        //Log.d("fan","====index=====Items==:"+indexItems.toString());
        if(indexItems == null){
            return null;
        }else{
            Iterator it = indexItems.iterator();
            indexMap.put("请选择监测指标","-1");
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
        String[] indexs = new String[count+1];
        int i = 1;
        if(indexItems == null){
            return null;
        }else{
            indexs[0] = "请选择监测指标";
            Iterator it = indexItems.iterator();
            while(it.hasNext()){
                IndexItemsBean item = (IndexItemsBean) it.next();
                indexs[i] = item.getDescription();
                i++;
            }
            for(int j=0; j<=count;j++){
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

}