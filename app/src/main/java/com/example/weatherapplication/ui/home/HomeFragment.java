package com.example.weatherapplication.ui.home;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.weatherapplication.LoginActivity;
import com.example.weatherapplication.R;
import com.example.weatherapplication.StationActivity;
import com.example.weatherapplication.bean.DayReportBean;
import com.example.weatherapplication.bean.DaysDataItemBean;
import com.example.weatherapplication.bean.DaysDataItemDetailBean;
import com.example.weatherapplication.bean.IndexBean;
import com.example.weatherapplication.bean.IndexItemsBean;
import com.example.weatherapplication.bean.ReportBean;
import com.example.weatherapplication.buiness.StationFacade;
import com.example.weatherapplication.databinding.FragmentHomeBinding;
import com.example.weatherapplication.util.NetUtil;
import com.example.weatherapplication.util.ToastUtil;
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
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class HomeFragment extends Fragment implements View.OnClickListener{
//git config --global http.sslVerify "false"
    private static final String TAG = "HomeFragment";
    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;
    private TextView tvStation,tvChartname1,tvChartname2,tvChartname3,tvNewestData1,tvNewestData2,tvNewestData3;
    private ImageView ivAdd;
    private String  userName,collectorId,collectorName,configType,weatherStationName;//??????????????????
    private Map indexMap;
    private LinkedHashMap dataMap = new LinkedHashMap();
    private LineChart lineChart1,lineChart2,lineChart3;
    private List<Entry> entries1,entries2,entries3;
    private String weatherStationId = null;
    private Button btOneDay,btSevenDay, btThirtyDay,btNintyDay,btMeteorology,btPlant,btSoil;
    private ScrollView scrollView;
    List<DaysDataItemBean>  daysDataItemBean = null;
    private int days = 0;//
    private ProgressDialog progressDialog = null;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View fragmentHomeView = binding.getRoot();
        initView(fragmentHomeView);
        StationFacade stationFacade = StationFacade.getInstance();
        try{
            if(collectorId!= null) {
                indexMap = stationFacade.initIndex(collectorId);
                //Log.d("HomeFragment","=&&&&&&&&onCreateView==indexMap==:"+ indexMap);
                btMeteorology.setOnClickListener(this);
                btPlant.setOnClickListener(this);
                btSoil.setOnClickListener(this);
                btOneDay.setOnClickListener(dayListener);
                btSevenDay.setOnClickListener(dayListener);
                btThirtyDay.setOnClickListener(dayListener);
                btNintyDay.setOnClickListener(dayListener);
            }else{
                btMeteorology.setVisibility(View.INVISIBLE);//??????????????????station???????????????????????????????????????
                btPlant.setVisibility(View.INVISIBLE);
                btSoil.setVisibility(View.INVISIBLE);
                btOneDay.setVisibility(View.INVISIBLE);
                btThirtyDay.setVisibility(View.INVISIBLE);
                btSevenDay.setVisibility(View.INVISIBLE);
                btNintyDay.setVisibility(View.INVISIBLE);
            }
            ivAdd.setOnClickListener(this);
            lineChart1.setNoDataText("");//???????????????????????????
            lineChart2.setNoDataText("");
            lineChart3.setNoDataText("");
        }catch (Exception e) {
            e.printStackTrace();
        }
        return fragmentHomeView;
    }
    private void initView(View fragmentHomeView) {
        userName = getActivity().getIntent().getStringExtra("userName");
        collectorId = getActivity().getIntent().getStringExtra("collectorId");
        collectorName = getActivity().getIntent().getStringExtra("collectorName");
        weatherStationId = getActivity().getIntent().getStringExtra("weatherStationId");
        weatherStationName = getActivity().getIntent().getStringExtra("weatherStationName");
        Log.d(TAG,"=&&&&&&&&initView==userName==:"+ userName+"=&&&&&&&&initView==collectorId==:"+ collectorId);
        Log.d(TAG,"=&&&&&&&&initView==collectorName==:"+ collectorName+"=&&&&&&&&initView==weatherStationId==:"+ weatherStationId);
        Log.d(TAG,"=&&&&&&&&iweatherStationName==========:"+ weatherStationName);
        tvStation = fragmentHomeView.findViewById(R.id.tv_station);
        tvStation.setText(collectorName);
        ivAdd = (ImageView) fragmentHomeView.findViewById(R.id.iv_add);
        lineChart1 = (LineChart) fragmentHomeView.findViewById(R.id.linechart_1);
        lineChart2 = (LineChart) fragmentHomeView.findViewById(R.id.linechart_2);
        lineChart3 = (LineChart) fragmentHomeView.findViewById(R.id.linechart_3);
        btOneDay = fragmentHomeView.findViewById(R.id.btn_one_day);
        btSevenDay = fragmentHomeView.findViewById(R.id.btn_seven_day);
        btThirtyDay = fragmentHomeView.findViewById(R.id.btn_thirty_day);
        btNintyDay = fragmentHomeView.findViewById(R.id.btn_ninty_day);
        btMeteorology = fragmentHomeView.findViewById(R.id.btn_meteorology);
        btPlant = fragmentHomeView.findViewById(R.id.btn_plant);
        btSoil  = fragmentHomeView.findViewById(R.id.btn_soil);
        tvChartname1 = fragmentHomeView.findViewById(R.id.chartname_tv_1);
        tvChartname2 = fragmentHomeView.findViewById(R.id.chartname_tv_2);
        tvChartname3 = fragmentHomeView.findViewById(R.id.chartname_tv_3);
        tvNewestData1 = fragmentHomeView.findViewById(R.id.newestData_tv_1);
        tvNewestData2 = fragmentHomeView.findViewById(R.id.newestData_tv_2);
        tvNewestData3 = fragmentHomeView.findViewById(R.id.newestData_tv_3);
        scrollView = fragmentHomeView.findViewById(R.id.scrollview);
    }
    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.iv_add:
                intent = new Intent(getActivity(), StationActivity.class);
                intent.putExtra("userName",userName);
                intent.putExtra("weatherStationId",weatherStationId);
                intent.putExtra("weatherStationName",weatherStationName);
                intent.putExtra("page","home");
                startActivity(intent);
                break;
            case R.id.btn_meteorology:
                setBtnEnable(btMeteorology,"category");
                configType = "1";
                setScrollInvisiable();
                break;
            case R.id.btn_soil:
                setBtnEnable(btSoil,"category");
                configType = "2";
                setScrollInvisiable();
                break;
            case R.id.btn_plant:
                setBtnEnable(btPlant,"category");
                configType = "3";
                setScrollInvisiable();
                break;
        }
        checkDayBtnAndDraw(configType);
    }
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            Log.d(TAG, "============daysDataItemBean===========:"+daysDataItemBean);
            if(daysDataItemBean!=null && daysDataItemBean.size()>0){
                try {
                    drawChart(daysDataItemBean);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            progressDialog.dismiss();
        }
    };
    private void checkDayBtnAndDraw(String configType) {
        if(!btOneDay.isEnabled()){
            days =1;
        }else if(!btSevenDay.isEnabled()){
            days =7;
        }else if(!btThirtyDay.isEnabled()){
            days =30;
        }else if(!btNintyDay.isEnabled()){
            days =90;
        }
        if(days != 0){
            //drawChart(configType,days);
            try {
                if(days == 1){
                    drawChart(configType,days);
                }else{
                    progressDialog = new ProgressDialog(getActivity());
                    progressDialog.setProgressStyle(ProgressDialog.BUTTON_NEUTRAL);
                    progressDialog.setTitle("??????");
                    progressDialog.setMessage("??????????????????");
                    progressDialog.setIndeterminate(false);
                    progressDialog.setCancelable(true);
                    progressDialog.setButton("??????", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            progressDialog.cancel();
                        }
                    });
                    progressDialog.show();
                    new Thread(){
                        @Override
                        public void run() {
                            super.run();
                            try{
                                daysDataItemBean = getDaysData(configType,days);
                                if(daysDataItemBean!=null && daysDataItemBean.size()>0){
                                    Message msg = new Message();
                                    handler.sendMessage(msg);
                                }
                            }catch (Exception e){
                                e.printStackTrace();
                                progressDialog.cancel();
                            }
                        }
                    }.start();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void setScrollInvisiable() {
        tvChartname1.setVisibility(View.INVISIBLE);
        tvNewestData1.setVisibility(View.INVISIBLE);
        lineChart1.setVisibility(View.INVISIBLE);
        tvChartname2.setVisibility(View.INVISIBLE);
        tvNewestData2.setVisibility(View.INVISIBLE);
        lineChart2.setVisibility(View.INVISIBLE);
        tvChartname3.setVisibility(View.INVISIBLE);
        tvNewestData3.setVisibility(View.INVISIBLE);
        lineChart3.setVisibility(View.INVISIBLE);
    }

    private void setDaysButtonEnable(Boolean b) {
        btOneDay.setEnabled(b);
        btSevenDay.setEnabled(b);
        btThirtyDay.setEnabled(b);
        btNintyDay.setEnabled(b);
    }

    View.OnClickListener dayListener =  new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.btn_one_day:
                    days = 1;
                    setBtnEnable(btOneDay,"days");
                    break;
                case R.id.btn_seven_day:
                    days = 7;
                    setBtnEnable(btSevenDay,"days");
                    break;
                case R.id.btn_thirty_day:
                    days = 30;
                    setBtnEnable(btThirtyDay,"days");
                    break;
                case R.id.btn_ninty_day:
                    days = 90;
                    setBtnEnable(btNintyDay,"days");
                    break;
            }
            //drawChart(configType,days);
            try {
                if(days == 1){
                    drawChart(configType,days);
                }else{
                    progressDialog = new ProgressDialog(getActivity());
                    progressDialog.setProgressStyle(ProgressDialog.BUTTON_NEUTRAL);
                    progressDialog.setTitle("??????");
                    progressDialog.setMessage("??????????????????");
                    progressDialog.setIndeterminate(false);
                    progressDialog.setCancelable(true);
                    progressDialog.setButton("??????", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            progressDialog.cancel();
                        }
                    });
                    progressDialog.show();
                    new Thread(){
                        @Override
                        public void run() {
                            super.run();
                            try{
                                daysDataItemBean = getDaysData(configType,days);
                                if(daysDataItemBean!=null && daysDataItemBean.size()>0){
                                    Message msg = new Message();
                                    handler.sendMessage(msg);
                                }
                            }catch (Exception e){
                                e.printStackTrace();
                                progressDialog.cancel();
                            }
                        }
                    }.start();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
    private void setBtnEnable(Button btn,String category) {
        List<Button> buttonList = new ArrayList<>();
        if (buttonList.size() == 0){
            if("category".equals(category)){
                buttonList.add(btMeteorology);
                buttonList.add(btPlant);
                buttonList.add(btSoil);
            }else{
                buttonList.add(btOneDay);
                buttonList.add(btSevenDay);
                buttonList.add(btThirtyDay);
                buttonList.add(btNintyDay);
            }
        }
        for (int i = 0; i <buttonList.size() ; i++) {
            buttonList.get(i).setEnabled(true);
        }
        btn.setEnabled(false);
    }
    private List<DaysDataItemBean> getDaysData(String configType,int days) throws IOException {
        StationFacade stationFacade = StationFacade.getInstance();
        if (configType != null) {
            //Log.d(TAG "=========configType=:" + configType);
            String newestData = "";
            try {
                //Log.dTAG, "===####======days==:" + days);
                daysDataItemBean = NetUtil.getDaysData(collectorId, configType, days);
                Log.d(TAG, "===####======daysDataItemBean==:" + daysDataItemBean);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            String message = "??????????????????";
            Toast toastCenter = Toast.makeText(getActivity().getApplicationContext(), message,Toast.LENGTH_SHORT);
            toastCenter.setGravity(Gravity.CENTER,0,0);
            toastCenter.show();
            setDaysButtonEnable(true);
        }
        return daysDataItemBean;
    }
    private void drawChart(List<DaysDataItemBean> daysDataItemBean) throws Exception{
        StationFacade stationFacade = StationFacade.getInstance();
        int i = 0;
        String newestData = "";
        try {
            if(daysDataItemBean != null && daysDataItemBean.size()>0) {
                Iterator it = daysDataItemBean.iterator();
                while(it.hasNext()){
                    DaysDataItemBean item = (DaysDataItemBean) it.next();
                    Log.d(TAG, "===newestData= ======item=:" + item);
                    newestData = stationFacade.getNewestData((String) item.getDescription()+","+item.getUnit(),collectorId,1);
                    Log.d(TAG, "===newestData==:" + newestData);
                    if(item!=null){
                        if(i==0){
                            scrollView.scrollTo(0,0);
                            tvChartname1.setText(item.getDescription());
                            tvChartname1.setVisibility(View.VISIBLE);
                            tvNewestData1.setText(newestData);
                            tvNewestData1.setVisibility(View.VISIBLE);
                            lineChart1.setVisibility(View.VISIBLE);
                            lineChart1.zoom(0f,1f,0,0);
                            lineChart1.zoom(0.25f,1f,0,0);
                            setLineChart(days,item,lineChart1,entries1,item.getDescription(),item.getUnit());
                            lineChart1.notifyDataSetChanged();
                            lineChart1.getData().notifyDataChanged();
                            lineChart1.invalidate();
                        }
                        if(i==1){
                            tvChartname2.setText(item.getDescription());
                            tvChartname2.setVisibility(View.VISIBLE);
                            tvNewestData2.setText(newestData);
                            tvNewestData2.setVisibility(View.VISIBLE);
                            lineChart2.setVisibility(View.VISIBLE);
                            lineChart2.zoom(0f,1f,0,0);
                            lineChart2.zoom(0.25f,1f,0,0);
                            setLineChart(days,item,lineChart2,entries2,item.getDescription(),item.getUnit());
                            lineChart2.notifyDataSetChanged();
                            lineChart2.getData().notifyDataChanged();
                            lineChart2.invalidate();
                        }
                        if(i==2){
                            tvChartname3.setText(item.getDescription());
                            tvChartname3.setVisibility(View.VISIBLE);
                            tvNewestData3.setText(newestData);
                            tvNewestData3.setVisibility(View.VISIBLE);
                            lineChart3.setVisibility(View.VISIBLE);
                            lineChart3.zoom(0f,1f,0,0);
                            lineChart3.zoom(0.25f,1f,0,0);
                            setLineChart(days,item,lineChart3,entries3,item.getDescription(),item.getUnit());
                            lineChart3.notifyDataSetChanged();
                            lineChart3.getData().notifyDataChanged();
                            lineChart3.invalidate();
                        }
                    }
                    i++;
                }
            }else{
                if(i==0){
                    tvChartname1.setVisibility(View.INVISIBLE);
                    tvNewestData1.setVisibility(View.INVISIBLE);
                    lineChart1.setVisibility(View.INVISIBLE);
                }
                if(i==1){
                    tvChartname2.setVisibility(View.INVISIBLE);
                    tvNewestData2.setVisibility(View.INVISIBLE);
                    lineChart2.setVisibility(View.INVISIBLE);
                }
                if(i==2){
                    tvChartname3.setVisibility(View.INVISIBLE);
                    tvNewestData3.setVisibility(View.INVISIBLE);
                    lineChart3.setVisibility(View.INVISIBLE);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void drawChart(String configType,int days) {
        StationFacade stationFacade = StationFacade.getInstance();
        //if(configType!= null){
            int i = 0;
            String newestData = "";
            try {
                daysDataItemBean = getDaysData(configType,days);
                if(daysDataItemBean != null && daysDataItemBean.size()>0) {
                    Iterator it = daysDataItemBean.iterator();
                    while(it.hasNext()){
                          DaysDataItemBean item = (DaysDataItemBean) it.next();
                          Log.d(TAG, "===newestData======itemitem=:" + item);
                          newestData = stationFacade.getNewestData((String) item.getDescription()+","+item.getUnit(),collectorId,1);
                          Log.d(TAG, "===newestData==:" + newestData);
                          if(i==0){
                               scrollView.scrollTo(0,0);
                               tvChartname1.setText(item.getDescription());
                               tvChartname1.setVisibility(View.VISIBLE);
                               tvNewestData1.setText(newestData+" "+item.getUnit());
                               tvNewestData1.setVisibility(View.VISIBLE);
                               lineChart1.setVisibility(View.VISIBLE);
                               lineChart1.zoom(0f,1f,0,0);
                               lineChart1.zoom(0.25f,1f,0,0);
                               setLineChart(days,item,lineChart1,entries1,item.getDescription(),item.getUnit());
                               lineChart1.notifyDataSetChanged();
                               lineChart1.getData().notifyDataChanged();
                               lineChart1.invalidate();
                          }
                          if(i==1){
                               tvChartname2.setText(item.getDescription());
                               tvChartname2.setVisibility(View.VISIBLE);
                               tvNewestData2.setText(newestData+" "+item.getUnit());
                               tvNewestData2.setVisibility(View.VISIBLE);
                               lineChart2.setVisibility(View.VISIBLE);
                               lineChart2.zoom(0f,1f,0,0);
                               lineChart2.zoom(0.25f,1f,0,0);
                               setLineChart(days,item,lineChart2,entries2,item.getDescription(),item.getUnit());
                               lineChart2.notifyDataSetChanged();
                               lineChart2.getData().notifyDataChanged();
                               lineChart2.invalidate();
                          }
                          if(i==2){
                               tvChartname3.setText(item.getDescription());
                               tvChartname3.setVisibility(View.VISIBLE);
                               tvNewestData3.setText(newestData+" "+item.getUnit());
                               tvNewestData3.setVisibility(View.VISIBLE);
                               lineChart3.setVisibility(View.VISIBLE);
                               lineChart3.zoom(0f,1f,0,0);
                               lineChart3.zoom(0.25f,1f,0,0);
                               setLineChart(days,item,lineChart3,entries3,item.getDescription(),item.getUnit());
                               lineChart3.notifyDataSetChanged();
                               lineChart3.getData().notifyDataChanged();
                               lineChart3.invalidate();
                          }
                          i++;
                    }
                }else{
                    if(i==0){
                        tvChartname1.setVisibility(View.INVISIBLE);
                        tvNewestData1.setVisibility(View.INVISIBLE);
                        lineChart1.setVisibility(View.INVISIBLE);
                    }
                    if(i==1){
                        tvChartname2.setVisibility(View.INVISIBLE);
                        tvNewestData2.setVisibility(View.INVISIBLE);
                        lineChart2.setVisibility(View.INVISIBLE);
                    }
                    if(i==2){
                        tvChartname3.setVisibility(View.INVISIBLE);
                        tvNewestData3.setVisibility(View.INVISIBLE);
                        lineChart3.setVisibility(View.INVISIBLE);
                    }
                }
            } catch (Exception e) {
                    e.printStackTrace();
            }
        /*}else{
            String message = "??????????????????";
            Toast toastCenter = Toast.makeText(getActivity().getApplicationContext(), message,Toast.LENGTH_SHORT);
            toastCenter.setGravity(Gravity.CENTER,0,0);
            toastCenter.show();
            setDaysButtonEnable(true);
        }*/
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    private List<Entry> setData(DaysDataItemBean daysDataItemBean,List<Entry> entries,int days) {
        if(daysDataItemBean == null){
            return null;
        }
        List<DaysDataItemDetailBean> dayDataDetailReports = daysDataItemBean.getmItemDetailBeans();
        if(dayDataDetailReports == null){
            return null;
        }
        //Log.d(TAG, "==============dayDataDetailReports==:" +dayDataDetailReports);
        Collections.reverse(dayDataDetailReports);
        //Log.d(TAG, "==========reverse====dayDataDetailReports==:" + dayDataDetailReports);
        Iterator it = dayDataDetailReports.iterator();
        int count = 0;
        if(entries != null){
            entries.clear();
        }
        entries = new ArrayList<>();
        while(it.hasNext() && count < 24){
            DaysDataItemDetailBean dayDataDetailReportBean = (DaysDataItemDetailBean)it.next();
            //Log.d(TAG, "==========dayDataDetailReportBean==:" + dayDataDetailReportBean);
            float time = 0.0f;
            if(days==1){
                time = Float.parseFloat(dayDataDetailReportBean.getAcquisitionTime().substring(11,13));
            }else if(days==7){
                dataMap.put(count,dayDataDetailReportBean.getAcquisitionTime().substring(0,16));
                time = count;
            }else{
                dataMap.put(count,dayDataDetailReportBean.getAcquisitionTime().substring(0,10));
                time = count;
            }
            float value = 0.0f;
            if(!"".equals(dayDataDetailReportBean.getValue())) {
                value = Float.parseFloat(dayDataDetailReportBean.getValue());
                entries.add(new Entry(time,value));
            }
            //Log.d(TAG, count+"==============time==:" +time+"==============value==:" +value);
            count++;
        }
        return entries;
    }
    private ReportBean getData(String reportResult) {
        Gson gson = new Gson();
        ReportBean reportBean = gson.fromJson(reportResult, ReportBean.class);
        Log.d(TAG, "=========getData=====reportBean==:" + reportBean);
        return reportBean;
    }
    private void setLineChart(int days,DaysDataItemBean reportBean,LineChart lineChart,List<Entry> entries,String index,String indexUint){
            entries = setData(reportBean,entries,days);
            Log.d(TAG, "=&&&&&&&&&&&entries==:" + entries);
            Log.d(TAG, "=&&&&&&&&&&&entries.size()==:" + entries.size());
            if (entries != null && entries.size()>0) {
                try {
                LineDataSet dataSet = new LineDataSet(entries, index);
                //Log.d(TAG, "=&&&&&&&&&&&dataSet==:" + dataSet);
                dataSet.setColor(Color.parseColor("#FFFFFFFF"));//????????????
                dataSet.setCircleColor(Color.parseColor("#FFFFFFFF"));//????????????
                dataSet.setCircleRadius(2f);//???????????????????????????
                dataSet.setLineWidth(1.5f);//????????????
                dataSet.setValueTextColor(Color.parseColor("#FFFFFFFF"));//??????????????????????????????
                dataSet.setValueTextSize(7f);//??????????????????????????????
                dataSet.setCubicIntensity(0.9f);//????????????????????????
                dataSet.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);
                YAxis rightAxis = lineChart.getAxisRight();
                rightAxis.setEnabled(false);//??????Y????????????
                YAxis leftAxis = lineChart.getAxisLeft();//??????Y???
                leftAxis.setDrawGridLines(true);//???????????????
                leftAxis.setEnabled(false);
                XAxis xAxis = lineChart.getXAxis();
                xAxis.setTextColor(Color.BLUE);//??????X???????????????
                xAxis.setDrawAxisLine(true);//?????????true?????????????????????????????????
                xAxis.setDrawGridLines(true);//???????????????
                xAxis.setDrawLabels(true);//??????????????????X?????????????????????
                xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);//
                xAxis.setGranularity(1f);//??????????????????
                xAxis.setEnabled(true);
                xAxis.setAxisMinimum(-0.3f);//??????X????????????
                if(days==1){
                    xAxis.setTextSize(10f);//??????X?????????????????????
                    xAxis.setLabelRotationAngle(0f);//?????????
                    xAxis.setValueFormatter(new IAxisValueFormatter() {
                        @Override
                        public String getFormattedValue(float v, AxisBase axis) {
                            return String.valueOf((int) v+":00");
                        }
                    });
                }else{
                    xAxis.setTextSize(8f);//??????X?????????????????????
                    xAxis.setLabelRotationAngle(45f);//??????45???
                    xAxis.setValueFormatter(new IAxisValueFormatter() {
                        @Override
                        public String getFormattedValue(float v, AxisBase axis) {
                            return String.valueOf(dataMap.get((int) v));
                        }
                    });
                }
                Legend legend = lineChart.getLegend();//??????
                legend.setForm(Legend.LegendForm.EMPTY);//?????????????????????
                legend.setTextSize(13f);//????????????????????????
                legend.setTextColor(Color.WHITE);//??????????????????
                legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);//??????????????????
                legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
                legend.setEnabled(false);//????????????
                Description description = lineChart.getDescription();//?????????????????????
                description.setText(indexUint);//????????????
                description.setTextSize(10f);
                description.setTextColor(Color.WHITE);
                description.setEnabled(true);//??????????????????????????????
                LineData lineData = new LineData();
                lineData.addDataSet(dataSet);
                lineChart.setTouchEnabled(false);//?????????????????????
                lineChart.setScaleEnabled(true);//?????????????????????
                lineChart.setData(lineData);
                lineChart.zoom(4,1,0,0);
                lineChart.setExtraBottomOffset(10);//??????????????????
                lineChart.setTouchEnabled( true);//??????chart??????????????????
                lineChart.setDragEnabled( true); //????????????????????????
                lineChart.invalidate();//????????????
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else{
                //String message = "????????????";
                //lineChart.setVisibility(View.INVISIBLE);
                //Toast toastCenter = Toast.makeText(getActivity().getApplicationContext(), message,Toast.LENGTH_SHORT);
                //toastCenter.setGravity(Gravity.CENTER,0,0);
                //toastCenter.show();
            }
        }
}