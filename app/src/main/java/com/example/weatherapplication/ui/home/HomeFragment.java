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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.weatherapplication.LoginActivity;
import com.example.weatherapplication.R;
import com.example.weatherapplication.StationActivity;
import com.example.weatherapplication.bean.DayReportBean;
import com.example.weatherapplication.bean.IndexBean;
import com.example.weatherapplication.bean.IndexItemsBean;
import com.example.weatherapplication.bean.ReportBean;
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
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class HomeFragment extends Fragment implements View.OnClickListener{

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;
    private String[] mIndexs,indexsUint,selectedIndex;
    private TextView tvStation,tvChartname1,tvChartname2,tvChartname3,tvNewestData1,tvNewestData2,tvNewestData3;
    private ImageView ivAdd,ivPerson;
    String  userName,stationId,stationName;
    private Map indexMap;
    private LineChart lineChart1,lineChart2,lineChart3;
    private List<Entry> entries1,entries2,entries3;
    String weatherStationId = null;
    private Button btOneDay,btSevenDay, btThirtyDay,btNintyDay,btMeteorology,btPlant,btSoil;
    private ScrollView scrollView;

    private Handler mHandler = new Handler(Looper.myLooper()){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if(msg.what == 0){
                String report = (String) msg.obj;
                Gson gson = new Gson();
                ReportBean reportBean = gson.fromJson(report, ReportBean.class);
                Log.d("HomeFragment","====解析后的reportBean==:"+reportBean.toString());
            }
        }
    };

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View fragmentHomeView = binding.getRoot();
        initView(fragmentHomeView);
        try{
            if(stationId!= null) {
                initIndex();
                btMeteorology.setOnClickListener(this);
                btPlant.setOnClickListener(this);
                btSoil.setOnClickListener(this);
                btOneDay.setOnClickListener(dayListener);
                btSevenDay.setOnClickListener(dayListener);
                btThirtyDay.setOnClickListener(dayListener);
                btNintyDay.setOnClickListener(dayListener);
            }else{
                btMeteorology.setVisibility(View.INVISIBLE);//如果没有选中station直接打开数据，就不显示按钮
                btPlant.setVisibility(View.INVISIBLE);
                btSoil.setVisibility(View.INVISIBLE);
                btOneDay.setVisibility(View.INVISIBLE);
                btThirtyDay.setVisibility(View.INVISIBLE);
                btSevenDay.setVisibility(View.INVISIBLE);
                btNintyDay.setVisibility(View.INVISIBLE);
            }
            ivAdd.setOnClickListener(this);
            ivPerson.setOnClickListener(this);
            lineChart1.setNoDataText("");//无数据时显示的文字
            lineChart2.setNoDataText("");
            lineChart3.setNoDataText("");
        }catch (Exception e) {
            e.printStackTrace();
        }
        return fragmentHomeView;
    }

    private void initIndex() {
        try {
            if(stationId!= null){
                IndexBean indexbean = NetUtil.getIndexInfo(stationId);
                mIndexs  =  getIndexOfStation(indexbean);
                Log.d("HomeFragment","===mIndexs==:"+ mIndexs);
                indexMap =  getIndexOfStationMap(indexbean);
                Log.d("HomeFragment","===indexMap==:"+ indexMap.toString());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initView(View fragmentHomeView) {
        userName = getActivity().getIntent().getStringExtra("userName");
        stationId = getActivity().getIntent().getStringExtra("stationId");
        stationName = getActivity().getIntent().getStringExtra("stationName");
        weatherStationId = getActivity().getIntent().getStringExtra("weatherStationId");
        Log.d("HomeFragment","=&&&&&&&&==userName==:"+ userName);
        Log.d("HomeFragment","=&&&&&&&&==stationId==:"+ stationId);
        Log.d("HomeFragment","=&&&&&&&&==stationName==:"+ stationName);
        Log.d("HomeFragment","=&&&&&&&&==weatherStationId==:"+ weatherStationId);
        tvStation = fragmentHomeView.findViewById(R.id.tv_station);
        tvStation.setText(stationName);
        ivAdd = (ImageView) fragmentHomeView.findViewById(R.id.iv_add);
        lineChart1 = (LineChart) fragmentHomeView.findViewById(R.id.linechart_1);
        lineChart2 = (LineChart) fragmentHomeView.findViewById(R.id.linechart_2);
        lineChart3 = (LineChart) fragmentHomeView.findViewById(R.id.linechart_3);
        btOneDay = fragmentHomeView.findViewById(R.id.btn_one_day);
        btSevenDay = fragmentHomeView.findViewById(R.id.btn_seven_day);
        btThirtyDay = fragmentHomeView.findViewById(R.id.btn_thirty_day);
        btNintyDay = fragmentHomeView.findViewById(R.id.btn_ninty_day);
        ivPerson = fragmentHomeView.findViewById(R.id.iv_person);
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
                startActivity(intent);
                break;
            case R.id.iv_person:
                intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_meteorology:
                setBtnEnable(btMeteorology,"category");
                setDaysButtonEnable();
                selectedIndex = new String[]{"AirTC_Avg1-Avg", "RH_Avg1-Avg", "PAR_Avg1-Avg"};
                indexsUint =new String[]{"Dec C","%","umol.s-1.m-2"};
                setScrollInvisiable();
               // clickAndDraw(selectedIndex,indexsUint,v);
                break;
            case R.id.btn_plant:
                setBtnEnable(btPlant,"category");
                setDaysButtonEnable();
                selectedIndex = new String[]{"DD_Avg1-Avg", "TDP_Avg1-Avg"};
                indexsUint =new String[]{"mm","mV"};
                setScrollInvisiable();
                //clickAndDraw(selectedIndex,indexsUint,v);
                break;
            case R.id.btn_soil:
                setBtnEnable(btSoil,"category");
                setDaysButtonEnable();
                selectedIndex = new String[]{"SoilVWC_Avg1-Avg", "SoilEC_Avg1-Avg", "SoilTemp_Avg1-Avg"};
                indexsUint =new String[]{"m3/m3","uS/cm","deg_C"};
                setScrollInvisiable();
               // clickAndDraw(selectedIndex,indexsUint,v);
                break;
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

    private void setDaysButtonEnable() {
        btOneDay.setEnabled(true);
        btSevenDay.setEnabled(true);
        btThirtyDay.setEnabled(true);
        btNintyDay.setEnabled(true);
    }

    View.OnClickListener dayListener =  new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int days = 0;
            switch(v.getId()){
                case R.id.btn_one_day:
                    days = 1;
                    setBtnEnable(btOneDay,"days");
                    drawChart(selectedIndex,indexsUint,days);
                    break;
                case R.id.btn_seven_day:
                    days = 7;
                    setBtnEnable(btSevenDay,"days");
                    drawChart(selectedIndex,indexsUint,days);
                    break;
                case R.id.btn_thirty_day:
                    days = 30;
                    setBtnEnable(btThirtyDay,"days");
                    drawChart(selectedIndex,indexsUint,days);
                    break;
                case R.id.btn_ninty_day:
                    days = 90;
                    setBtnEnable(btNintyDay,"days");
                    drawChart(selectedIndex,indexsUint,days);
                    break;
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
    private void drawChart(String[] selectedIndex, String[] selectedIndexUint,int days) {
        Log.d("HomeFragment", "===selectedIndex  count=:" + selectedIndex.length);
        ReportBean  reportBean = null;
        for(int i=0; i<selectedIndex.length;i++){
            String index = selectedIndex[i];
            String indexUint = selectedIndexUint[i];
            String selectConfigId = (String) indexMap.get(index);
            Log.d("HomeFragment", "===selectConfigId==:" + selectConfigId+"=====indexUint===:"+indexUint+"=====index===:"+index);
            Date endDate = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd:hh:mm");
            Calendar c = Calendar.getInstance();
            c.add(Calendar.DATE, -1);
            String startDateStr = sdf.format(c.getTime());
            String endDateStr = sdf.format(endDate);
            String newestData = "";
            try {
                if(!"-1".equals(selectConfigId)){
                    ///最上面显示的是最新的记录
                    ReportBean newestRrpotData = NetUtil.getNewestData(selectConfigId, stationId,1);
                    if(newestRrpotData != null  && newestRrpotData.getmDayReportBeans() != null) {
                        List<DayReportBean> dayReports = newestRrpotData.getmDayReportBeans();
                        if(dayReports == null){
                            return ;
                        }
                        Iterator it = dayReports.iterator();
                        while(it.hasNext()){
                            DayReportBean dayReportBean = (DayReportBean)it.next();
                            Log.d("HomeFragment", "===dayReportBean.getCol1()==:" + dayReportBean.getCol1());
                            newestData = dayReportBean.getCol1();
                        }
                    }
                    Log.d("HomeFragment", "=============days==:" + days);
                    switch(days){
                        case 1:
                            reportBean= NetUtil.getReportDataOfIndex(selectConfigId, stationId,"2022-01-25", "2022-01-26");
                            break;
                        case 7:
                            reportBean= NetUtil.getReportDataOfIndex(selectConfigId, stationId,"2022-01-27", "2022-01-28");
                            break;
                        case 30:
                            reportBean= NetUtil.getReportDataOfIndex(selectConfigId, stationId,"2022-02-01", "2022-02-02");
                            break;
                        case 90:
                            reportBean= NetUtil.getReportDataOfIndex(selectConfigId, stationId,"2022-02-04", "2022-02-05");
                            break;
                    }
                    Log.d("HomeFragment", "===reportBean==:" + reportBean);
                    if(reportBean != null  && reportBean.getmDayReportBeans() != null) {
                        if(i==0){
                            scrollView.scrollTo(0,0);
                            tvChartname1.setText(index);
                            tvChartname1.setVisibility(View.VISIBLE);
                            tvNewestData1.setText(newestData);
                            tvNewestData1.setVisibility(View.VISIBLE);
                            lineChart1.setVisibility(View.VISIBLE);
                            lineChart1.zoom(0.25f,1f,0,0);
                            setLineChart(reportBean,lineChart1,entries1,index,indexUint);
                            lineChart1.notifyDataSetChanged();
                            lineChart1.getData().notifyDataChanged();
                            lineChart1.invalidate();
                        }
                        if(i==1){
                            tvChartname2.setText(index);
                            tvChartname2.setVisibility(View.VISIBLE);
                            tvNewestData2.setText(newestData);
                            tvNewestData2.setVisibility(View.VISIBLE);
                            lineChart2.setVisibility(View.VISIBLE);
                            lineChart2.zoom(0.25f,1f,0,0);
                            setLineChart(reportBean,lineChart2,entries2,index,indexUint);
                            lineChart2.notifyDataSetChanged();
                            lineChart2.getData().notifyDataChanged();
                            lineChart2.invalidate();
                        }
                        if(i==2){
                            tvChartname3.setText(index);
                            tvChartname3.setVisibility(View.VISIBLE);
                            tvNewestData3.setText(newestData);
                            tvNewestData3.setVisibility(View.VISIBLE);
                            lineChart3.setVisibility(View.VISIBLE);
                            lineChart3.zoom(0.25f,1f,0,0);
                            setLineChart(reportBean,lineChart3,entries3,index,indexUint);
                            lineChart3.notifyDataSetChanged();
                            lineChart3.getData().notifyDataChanged();
                            lineChart3.invalidate();
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
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    private List<Entry> setData(ReportBean reportBean,List<Entry> entries) {
        if(reportBean == null){
            return null;
        }
        List<DayReportBean> dayReports = reportBean.getmDayReportBeans();
        if(dayReports == null){
            return null;
        }
        Iterator it = dayReports.iterator();
        int count = 0;
        if(entries != null){
            entries.clear();
        }
        entries = new ArrayList<>();
        while(it.hasNext() && count < 24){
            DayReportBean dayReportBean = (DayReportBean)it.next();
            float time = Float.parseFloat(dayReportBean.getAcquisitionTime().substring(11,13));
            float value = Float.parseFloat(dayReportBean.getCol1());
            entries.add(new Entry(time,value));
            Log.d("HomeFragment", count+"==============value==:" +value);
            count++;
        }
        Log.d("HomeFragment", "==============entries==:" + entries.toString());
        return entries;
    }
    private ReportBean getData(String reportResult) {
        Gson gson = new Gson();
        ReportBean reportBean = gson.fromJson(reportResult, ReportBean.class);
        Log.d("HomeFragment", "=========getData=====reportBean==:" + reportBean);
        return reportBean;
    }
    private void setLineChart(ReportBean reportBean,LineChart lineChart,List<Entry> entries,String index,String indexUint){
            entries = setData(reportBean,entries);
            Log.d("HomeFragment", "=&&&&&&&&&&&entries==:" + entries);
            if (entries != null) {
                try {
                LineDataSet dataSet = new LineDataSet(entries, index);
                Log.d("HomeFragment", "=&&&&&&&&&&&dataSet==:" + dataSet);
                dataSet.setColor(Color.parseColor("#FFFFFFFF"));//线条颜色
                dataSet.setCircleColor(Color.parseColor("#FFFFFFFF"));//圆点颜色
                dataSet.setCircleRadius(2f);//设置焦点圆心的大小
                dataSet.setLineWidth(1.5f);//线条宽度
                dataSet.setValueTextColor(Color.parseColor("#FFFFFFFF"));//设置显示值的文字颜色
                dataSet.setValueTextSize(7f);//设置显示值的文字大小
                dataSet.setCubicIntensity(0.9f);//设置曲线的平滑度
                dataSet.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);
                YAxis rightAxis = lineChart.getAxisRight();
                rightAxis.setEnabled(false);//右侧Y轴不显示
                YAxis leftAxis = lineChart.getAxisLeft();//左侧Y轴
                leftAxis.setTextSize(10f);
                leftAxis.setDrawGridLines(true);//绘制网格线
                leftAxis.setEnabled(false);
                XAxis xAxis = lineChart.getXAxis();
                xAxis.setTextColor(Color.BLUE);//设置X轴刻度颜色
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
                        return String.valueOf((int) v+":00");
                    }
                });
                Legend legend = lineChart.getLegend();//图例
                legend.setForm(Legend.LegendForm.EMPTY);//设置图例的形状
                legend.setTextSize(13f);//设置图例字体大小
                legend.setTextColor(Color.WHITE);//设置图例颜色
                legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);//设置图例位置
                legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
                legend.setEnabled(false);//显示图例
                Description description = lineChart.getDescription();//图表的描述信息
                description.setText(indexUint);//显示单位
                description.setTextSize(10f);
                description.setTextColor(Color.WHITE);
                description.setEnabled(true);//是否显示图表描述信息
                LineData lineData = new LineData();
                lineData.addDataSet(dataSet);
                lineChart.setTouchEnabled(false);//设置是否可触摸
                lineChart.setScaleEnabled(true);//设置是否可缩放
                lineChart.setData(lineData);
                lineChart.zoom(4,1,0,0);
                lineChart.setExtraBottomOffset(10);//距离底部距离
                lineChart.setTouchEnabled( true);//设置chart是否可以触摸
                lineChart.setDragEnabled( true); //设置是否可以拖拽
                lineChart.invalidate();//图表刷新
                } catch (Exception e) {
                    e.printStackTrace();
                }
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
        int count = indexbean.getTotal();
        String[] indexs = new String[count];
        indexsUint = new String[count];
        int i = 0;
        if(indexItems == null){
            return null;
        }else{
            Iterator it = indexItems.iterator();
            while(it.hasNext()){
                IndexItemsBean item = (IndexItemsBean) it.next();
                indexs[i] = item.getDescription();
                indexsUint[i] = item.getUnit();
                i++;
            }
            for(int j=0; j<count;j++){
                System.out.println("======indexs =====:"+indexs[j] +"("+indexsUint[j]+")");
            }
        }
        return indexs;
    }
}