package com.example.weatherapplication.ui.notifications;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
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

import com.example.weatherapplication.CommunityActivity;
import com.example.weatherapplication.R;
import com.example.weatherapplication.StationActivity;
import com.example.weatherapplication.bean.TreeAndCommunityDataBean;
import com.example.weatherapplication.bean.TreeAndCommunityDataItemBean;
import com.example.weatherapplication.bean.TreeAndCommunityDataItemDetailBean;
import com.example.weatherapplication.databinding.FragmentNotificationsBinding;
import com.example.weatherapplication.util.NetUtil;
import com.example.weatherapplication.view.ExpandTabView;
import com.example.weatherapplication.view.ViewLeft;
import com.example.weatherapplication.view.ViewRight;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

public class NotificationsFragment extends Fragment implements View.OnClickListener{

    private NotificationsViewModel notificationsViewModel;
    private FragmentNotificationsBinding binding;
    private ImageView ivAdd;
    private TextView tvStation,tvChartname1,tvChartname2,tvChartname3,tvChartname4,tvChartname5,tvChartname6,tvChartname7,tvChartname8,
            tvChartname9,tvChartname10,tvChartname11,tvChartname12,tvChartname13,tvChartname14,tvChartname15,tvChartname16,tvChartname17,
            tvChartname18,tvChartname19,tvChartname20,tvChartname21,tvChartname22,tvChartname23,tvChartname24,tvChartname25,tvChartname26,
            tvChartname27,tvChartname28,tvChartname29,tvChartname30,tvChartname31,tvChartname32,tvChartname33,tvChartname34,tvChartname35,
            tvChartname36,tvChartname37,tvChartname38,tvChartname39,tvChartname40,tvChartname41,tvChartname42,tvChartname43,tvChartname44,
            tvChartname45,tvChartname46,tvChartname47,tvChartname48,tvChartname49,tvChartname50,tvChartname = null;
    private String  userName,collectorId,collectorName,weatherStationId;
    private BarChart barChart1,barChart2,barChart3,barChart4,barChart5,barChart6,barChart7,barChart8,barChart9,barChart10,barChart11,barChart12,
            barChart13,barChart14,barChart15,barChart16,barChart17,barChart18,barChart19,barChart20,barChart21,barChart22,barChart23,barChart24,
            barChart25,barChart26,barChart27,barChart28,barChart29,barChart30,barChart31,barChart32,barChart33,barChart34,barChart35,barChart36,
            barChart37,barChart38,barChart39,barChart40,barChart41,barChart42,barChart43,barChart44,barChart45,barChart46,barChart47,barChart48,
            barChart49,barChart50,barChart = null;
    private Button btSevenDay,btFifDay, btThirtyDay,btNintyDay;
    private ScrollView scrollView;
    private String type = "";//tree or commnuity
    private LinkedHashMap tempMap = new LinkedHashMap();
    private LinkedHashMap dataMap = new LinkedHashMap();
    private int days = 0;
    // 线程变量
    private List<TreeAndCommunityDataItemBean> treeAndCommunityDataItemBeanList = null;
    private ProgressDialog progressDialog =  null;
    private static final String TAG = "NotificationsFragment";

    private ExpandTabView expandTabView;
    private ArrayList<View> mViewArray = new ArrayList<View>();
    private ViewLeft viewLeft;
    private ViewRight viewRight;
    private String selectedId = "";//select tree or community id
    private String weatherStationName;

    public View onCreateView(@NonNull LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel = new ViewModelProvider(this).get(NotificationsViewModel.class);
        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View notificationFragmentView = binding.getRoot();
        initView(notificationFragmentView);
        initVaule();
        initListener();
        try{
            if(collectorId!= null) {
                btSevenDay.setOnClickListener(dayListener);
                btFifDay.setOnClickListener(dayListener);
                btThirtyDay.setOnClickListener(dayListener);
                btNintyDay.setOnClickListener(dayListener);
            }else{
                btThirtyDay.setVisibility(View.INVISIBLE);
                btFifDay.setVisibility(View.INVISIBLE);
                btSevenDay.setVisibility(View.INVISIBLE);
                btNintyDay.setVisibility(View.INVISIBLE);
            }
            ivAdd.setOnClickListener(this);
            barChart1.setNoDataText("");//无数据时显示的文字
            barChart2.setNoDataText("");
            barChart3.setNoDataText("");
            barChart4.setNoDataText("");
            barChart5.setNoDataText("");
        }catch (Exception e) {
            e.printStackTrace();
        }
        return notificationFragmentView;
    }
    private void initView(View notificationFragmentView) {
        userName = getActivity().getIntent().getStringExtra("userName");
        collectorId = getActivity().getIntent().getStringExtra("collectorId");
        collectorName = getActivity().getIntent().getStringExtra("collectorName");
        weatherStationId = getActivity().getIntent().getStringExtra("weatherStationId");
        weatherStationName =  getActivity().getIntent().getStringExtra("weatherStationName");
        //Log.d(TAG,"=***************==userName==:"+ userName+"=***************==collectorId==:"+ collectorId);
        //Log.d(TAG,"=***************==stationName==:"+ collectorName+"=***************==weatherStationId==:"+ weatherStationId);
        Log.d(TAG,"=***************==weatherStationName======:"+ weatherStationName);
        tvStation = notificationFragmentView.findViewById(R.id.tv_station);
        tvStation.setText(collectorName);
        ivAdd =     notificationFragmentView.findViewById(R.id.iv_station);
        barChart1 = notificationFragmentView.findViewById(R.id.barchart_1);
        barChart2 = notificationFragmentView.findViewById(R.id.barchart_2);
        barChart3 = notificationFragmentView.findViewById(R.id.barchart_3);
        barChart4 = notificationFragmentView.findViewById(R.id.barchart_4);
        barChart5 = notificationFragmentView.findViewById(R.id.barchart_5);
        barChart6 = notificationFragmentView.findViewById(R.id.barchart_6);
        barChart7 = notificationFragmentView.findViewById(R.id.barchart_7);
        barChart8 = notificationFragmentView.findViewById(R.id.barchart_8);
        barChart9 = notificationFragmentView.findViewById(R.id.barchart_9);
        barChart10 = notificationFragmentView.findViewById(R.id.barchart_10);
        barChart11 = notificationFragmentView.findViewById(R.id.barchart_11);
        barChart12 = notificationFragmentView.findViewById(R.id.barchart_12);
        barChart13 = notificationFragmentView.findViewById(R.id.barchart_13);
        barChart14 = notificationFragmentView.findViewById(R.id.barchart_14);
        barChart15 = notificationFragmentView.findViewById(R.id.barchart_15);
        barChart16 = notificationFragmentView.findViewById(R.id.barchart_16);
        barChart17 = notificationFragmentView.findViewById(R.id.barchart_17);
        barChart18 = notificationFragmentView.findViewById(R.id.barchart_18);
        barChart19 = notificationFragmentView.findViewById(R.id.barchart_19);
        barChart20 = notificationFragmentView.findViewById(R.id.barchart_20);
        barChart21 = notificationFragmentView.findViewById(R.id.barchart_21);
        barChart22 = notificationFragmentView.findViewById(R.id.barchart_22);
        barChart23 = notificationFragmentView.findViewById(R.id.barchart_23);
        barChart24 = notificationFragmentView.findViewById(R.id.barchart_24);
        barChart25 = notificationFragmentView.findViewById(R.id.barchart_25);
        barChart26 = notificationFragmentView.findViewById(R.id.barchart_26);
        barChart27 = notificationFragmentView.findViewById(R.id.barchart_27);
        barChart28 = notificationFragmentView.findViewById(R.id.barchart_28);
        barChart29 = notificationFragmentView.findViewById(R.id.barchart_29);
        barChart30 = notificationFragmentView.findViewById(R.id.barchart_30);
        barChart31 = notificationFragmentView.findViewById(R.id.barchart_31);
        barChart32 = notificationFragmentView.findViewById(R.id.barchart_32);
        barChart33 = notificationFragmentView.findViewById(R.id.barchart_33);
        barChart34 = notificationFragmentView.findViewById(R.id.barchart_34);
        barChart35 = notificationFragmentView.findViewById(R.id.barchart_35);
        barChart36 = notificationFragmentView.findViewById(R.id.barchart_36);
        barChart37 = notificationFragmentView.findViewById(R.id.barchart_37);
        barChart38 = notificationFragmentView.findViewById(R.id.barchart_38);
        barChart39 = notificationFragmentView.findViewById(R.id.barchart_39);
        barChart40 = notificationFragmentView.findViewById(R.id.barchart_40);
        barChart41 = notificationFragmentView.findViewById(R.id.barchart_41);
        barChart42 = notificationFragmentView.findViewById(R.id.barchart_42);
        barChart43 = notificationFragmentView.findViewById(R.id.barchart_43);
        barChart44 = notificationFragmentView.findViewById(R.id.barchart_44);
        barChart45 = notificationFragmentView.findViewById(R.id.barchart_45);
        barChart46 = notificationFragmentView.findViewById(R.id.barchart_46);
        barChart47 = notificationFragmentView.findViewById(R.id.barchart_47);
        barChart48 = notificationFragmentView.findViewById(R.id.barchart_48);
        barChart49 = notificationFragmentView.findViewById(R.id.barchart_49);
        barChart50 = notificationFragmentView.findViewById(R.id.barchart_50);
        tvChartname1 = notificationFragmentView.findViewById(R.id.chartname_tv_1);
        tvChartname2 = notificationFragmentView.findViewById(R.id.chartname_tv_2);
        tvChartname3 = notificationFragmentView.findViewById(R.id.chartname_tv_3);
        tvChartname4 = notificationFragmentView.findViewById(R.id.chartname_tv_4);
        tvChartname5 = notificationFragmentView.findViewById(R.id.chartname_tv_5);
        tvChartname6 = notificationFragmentView.findViewById(R.id.chartname_tv_6);
        tvChartname7 = notificationFragmentView.findViewById(R.id.chartname_tv_7);
        tvChartname8 = notificationFragmentView.findViewById(R.id.chartname_tv_8);
        tvChartname9 = notificationFragmentView.findViewById(R.id.chartname_tv_9);
        tvChartname10 = notificationFragmentView.findViewById(R.id.chartname_tv_10);
        tvChartname11 = notificationFragmentView.findViewById(R.id.chartname_tv_11);
        tvChartname12 = notificationFragmentView.findViewById(R.id.chartname_tv_12);
        tvChartname13 = notificationFragmentView.findViewById(R.id.chartname_tv_13);
        tvChartname14 = notificationFragmentView.findViewById(R.id.chartname_tv_14);
        tvChartname15 = notificationFragmentView.findViewById(R.id.chartname_tv_15);
        tvChartname16 = notificationFragmentView.findViewById(R.id.chartname_tv_16);
        tvChartname17 = notificationFragmentView.findViewById(R.id.chartname_tv_17);
        tvChartname18 = notificationFragmentView.findViewById(R.id.chartname_tv_18);
        tvChartname19 = notificationFragmentView.findViewById(R.id.chartname_tv_19);
        tvChartname20 = notificationFragmentView.findViewById(R.id.chartname_tv_20);
        tvChartname21 = notificationFragmentView.findViewById(R.id.chartname_tv_21);
        tvChartname22 = notificationFragmentView.findViewById(R.id.chartname_tv_22);
        tvChartname23 = notificationFragmentView.findViewById(R.id.chartname_tv_23);
        tvChartname24 = notificationFragmentView.findViewById(R.id.chartname_tv_24);
        tvChartname25 = notificationFragmentView.findViewById(R.id.chartname_tv_25);
        tvChartname26 = notificationFragmentView.findViewById(R.id.chartname_tv_26);
        tvChartname27 = notificationFragmentView.findViewById(R.id.chartname_tv_27);
        tvChartname28 = notificationFragmentView.findViewById(R.id.chartname_tv_28);
        tvChartname29 = notificationFragmentView.findViewById(R.id.chartname_tv_29);
        tvChartname30 = notificationFragmentView.findViewById(R.id.chartname_tv_30);
        tvChartname31 = notificationFragmentView.findViewById(R.id.chartname_tv_31);
        tvChartname32 = notificationFragmentView.findViewById(R.id.chartname_tv_32);
        tvChartname33 = notificationFragmentView.findViewById(R.id.chartname_tv_33);
        tvChartname34 = notificationFragmentView.findViewById(R.id.chartname_tv_34);
        tvChartname35 = notificationFragmentView.findViewById(R.id.chartname_tv_35);
        tvChartname36 = notificationFragmentView.findViewById(R.id.chartname_tv_36);
        tvChartname37 = notificationFragmentView.findViewById(R.id.chartname_tv_37);
        tvChartname38 = notificationFragmentView.findViewById(R.id.chartname_tv_38);
        tvChartname39 = notificationFragmentView.findViewById(R.id.chartname_tv_39);
        tvChartname40 = notificationFragmentView.findViewById(R.id.chartname_tv_40);
        tvChartname41 = notificationFragmentView.findViewById(R.id.chartname_tv_41);
        tvChartname42 = notificationFragmentView.findViewById(R.id.chartname_tv_42);
        tvChartname43 = notificationFragmentView.findViewById(R.id.chartname_tv_43);
        tvChartname44 = notificationFragmentView.findViewById(R.id.chartname_tv_44);
        tvChartname45 = notificationFragmentView.findViewById(R.id.chartname_tv_45);
        tvChartname46 = notificationFragmentView.findViewById(R.id.chartname_tv_46);
        tvChartname47 = notificationFragmentView.findViewById(R.id.chartname_tv_47);
        tvChartname48 = notificationFragmentView.findViewById(R.id.chartname_tv_48);
        tvChartname49 = notificationFragmentView.findViewById(R.id.chartname_tv_49);
        tvChartname50 = notificationFragmentView.findViewById(R.id.chartname_tv_50);

        scrollView = notificationFragmentView.findViewById(R.id.scrollview_eva);
        btSevenDay = notificationFragmentView.findViewById(R.id.btn_seven_day);
        btFifDay = notificationFragmentView.findViewById(R.id.btn_fifteen_day);
        btThirtyDay = notificationFragmentView.findViewById(R.id.btn_thirty_day);
        btNintyDay = notificationFragmentView.findViewById(R.id.btn_ninty_day);
        expandTabView = notificationFragmentView.findViewById(R.id.expandtab_view);
        viewLeft = new ViewLeft(getActivity(),"tree",collectorId);
        viewRight = new ViewRight(getActivity(),"community",weatherStationId);
    }
    private void initVaule() {
        mViewArray.add(viewLeft);
        mViewArray.add(viewRight);
        ArrayList<String> mTextArray = new ArrayList<String>();
        mTextArray.add("植株");
        mTextArray.add("群落");
        expandTabView.setValue(mTextArray, mViewArray);
        Log.d(TAG,"=***************==viewLeft.getShowText()==:"+ viewLeft.getShowText());
        Log.d(TAG,"=***************==viewRight.getShowText()==:"+ viewRight.getShowText());
        expandTabView.setTitle(viewLeft.getShowText(), 2);
        expandTabView.setTitle(viewRight.getShowText(), 2);
    }
    private void initListener() {
        viewLeft.setOnSelectListener(new ViewLeft.OnSelectListener() {
            @Override
            public void getValue(String distance, String showText) throws IOException {
                Log.d(TAG,"=********left*******=distance==:"+ distance+"=***********left****=showText==:"+ showText);
                selectedId = distance;////selectedTreeId
                type = "tree";
                Log.d(TAG,"=********left*******=selectedId==:"+ selectedId+"=******left*********=days==:"+ days);
                Log.d(TAG,"=*********left******=type==:"+ type);
                if(days!=0){
                    drawChart(selectedId,days,type);
                }
                onRefresh(viewLeft, showText);
            }
        });
        viewRight.setOnSelectListener(new ViewRight.OnSelectListener() {
            @Override
            public void getValue(String distance, String showText) throws IOException {
                tvChartname1.setText("");
                barChart1.clear();
                tvChartname2.setText("");
                barChart2.clear();
                tvChartname3.setText("");
                barChart3.clear();
                tvChartname4.setText("");
                barChart4.clear();
                tvChartname5.setText("");
                barChart5.clear();
                tvChartname6.setText("");
                barChart6.clear();
                tvChartname7.setText("");
                barChart7.clear();
                tvChartname8.setText("");
                barChart8.clear();
                tvChartname9.setText("");
                barChart9.clear();
                tvChartname10.setText("");
                barChart10.clear();
                tvChartname11.setText("");
                barChart11.clear();
                tvChartname12.setText("");
                barChart12.clear();
                tvChartname13.setText("");
                barChart13.clear();
                tvChartname14.setText("");
                barChart14.clear();
                tvChartname15.setText("");
                barChart15.clear();
                tvChartname16.setText("");
                barChart16.clear();
                tvChartname17.setText("");
                barChart17.clear();
                tvChartname18.setText("");
                barChart18.clear();
                tvChartname19.setText("");
                barChart19.clear();
                tvChartname20.setText("");
                barChart20.clear();
                tvChartname21.setText("");
                barChart21.clear();
                tvChartname22.setText("");
                barChart22.clear();
                tvChartname23.setText("");
                barChart23.clear();
                tvChartname24.setText("");
                barChart24.clear();
                tvChartname25.setText("");
                barChart25.clear();
                tvChartname26.setText("");
                barChart26.clear();
                tvChartname27.setText("");
                barChart27.clear();
                tvChartname28.setText("");
                barChart28.clear();
                tvChartname29.setText("");
                barChart29.clear();
                tvChartname30.setText("");
                barChart30.clear();
                tvChartname31.setText("");
                barChart31.clear();
                tvChartname32.setText("");
                barChart32.clear();
                tvChartname33.setText("");
                barChart33.clear();
                tvChartname34.setText("");
                barChart34.clear();
                tvChartname35.setText("");
                barChart35.clear();
                tvChartname36.setText("");
                barChart36.clear();
                tvChartname37.setText("");
                barChart37.clear();
                tvChartname38.setText("");
                barChart38.clear();
                tvChartname39.setText("");
                barChart39.clear();
                tvChartname40.setText("");
                barChart40.clear();
                tvChartname41.setText("");
                barChart41.clear();
                tvChartname42.setText("");
                barChart42.clear();
                tvChartname43.setText("");
                barChart43.clear();
                tvChartname44.setText("");
                barChart44.clear();
                tvChartname45.setText("");
                barChart45.clear();
                tvChartname46.setText("");
                barChart46.clear();
                tvChartname47.setText("");
                barChart47.clear();
                tvChartname48.setText("");
                barChart48.clear();
                tvChartname49.setText("");
                barChart49.clear();
                tvChartname50.setText("");
                barChart50.clear();
                Log.d(TAG,"=======right==========distance==:"+ distance+"=******=right===*********=showText==:"+ showText);
                selectedId = distance;/////selectedWeaherStationId
                type = "commnuity";
                Log.d(TAG,"=********=right===*******=selectedId==:"+ selectedId+"=*****=right===**********=days==:"+ days);
                Log.d(TAG,"=********=right===*******=type==:"+ type);
                if(days!=0){
                    drawChart(selectedId,days,type);
                }
                onRefresh(viewRight, showText);
            }
        });
    }
    private void onRefresh(View view, String showText) {
        expandTabView.onPressBack();
        int position = getPositon(view);
        if (position >= 0 && !expandTabView.getTitle(position).equals(showText)) {
            expandTabView.setTitle(showText, position);
        }
        Toast.makeText(getActivity(), showText, Toast.LENGTH_SHORT).show();
    }

    private int getPositon(View tView) {
        for (int i = 0; i < mViewArray.size(); i++) {
            if (mViewArray.get(i) == tView) {
                return i;
            }
        }
        return -1;
    }
    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.iv_station:
                intent = new Intent(getActivity(), CommunityActivity.class);
                Log.d(TAG,"=***************=iv_station=========");
                intent.putExtra("userName",userName);
                intent.putExtra("weatherStationId",weatherStationId);
                intent.putExtra("page","notification");
                intent.putExtra("weatherStationName",weatherStationName);
                startActivity(intent);
                break;
        }
    }
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            Log.d(TAG, "====dayListener=====treeAndCommunityDataItemBeanList===========:"+treeAndCommunityDataItemBeanList);
            if(treeAndCommunityDataItemBeanList!=null && treeAndCommunityDataItemBeanList.size()>0){
                try {
                    drawBarChart(treeAndCommunityDataItemBeanList);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            progressDialog.dismiss();
        }
    };
    View.OnClickListener dayListener =  new View.OnClickListener() {
      //  int days = 0;
        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.btn_seven_day:
                    days = 7;
                    setBtnEnable(btSevenDay,"days");
                    break;
                case R.id.btn_fifteen_day:
                    days = 15;
                    setBtnEnable(btFifDay,"days");
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
            Log.d(TAG, "====dayListener=====days===========:"+days+"=========selectedId========="+selectedId);
            Log.d(TAG, "====dayListener=====type===========:"+type);
            if("".equals(selectedId)){
                String message = "";
                if("tree".equals(type)){
                    message = "请先选择植株！";
                }else if("community".equals(type)){
                    message = "请先选择群落！";
                }else{
                    message = "请先选择植株或群落！";
                }
                Toast toastCenter = Toast.makeText(getActivity().getApplicationContext(), message,Toast.LENGTH_SHORT);
                toastCenter.setGravity(Gravity.CENTER,0,0);
                toastCenter.show();
            }else{
                try {
                    if(days == 7){
                        drawChart(selectedId,days,type);
                    }else{
                        progressDialog = new ProgressDialog(getActivity());
                        progressDialog.setProgressStyle(ProgressDialog.BUTTON_NEUTRAL);
                        progressDialog.setTitle("提示");
                        progressDialog.setMessage("正在加载数据");
                        progressDialog.setIndeterminate(false);
                        progressDialog.setCancelable(true);
                        progressDialog.incrementProgressBy(10);
                        progressDialog.setMax(100);
                        progressDialog.show();
                        new Thread(){
                            @Override
                            public void run() {
                                super.run();
                                try{
                                    if(!"".equals(type)){
                                        Log.d(TAG, "====dayListener=====thread======type===========:"+type);
                                        treeAndCommunityDataItemBeanList = getReportData(selectedId,days,type);
                                        if(treeAndCommunityDataItemBeanList!=null && treeAndCommunityDataItemBeanList.size()>0){
                                            Message msg = new Message();
                                            handler.sendMessage(msg);
                                        }
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
    };
    private List<TreeAndCommunityDataItemBean> getReportData(String selectedId, int days,String type) {
            Log.d(TAG, "==getReportData======selectedId=============:" + selectedId);
            Log.d(TAG, "==getReportData======type=============:" + type);
            try {
                    TreeAndCommunityDataBean treeAndCommunityDataBean = null;
                    try {
                        if("tree".equals(type)){
                            treeAndCommunityDataBean = NetUtil.getTreeDataBean(selectedId, days);
                        }else{
                            treeAndCommunityDataBean = NetUtil.getCommunityDataBean(selectedId, days);
                        }
                    }catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (treeAndCommunityDataBean != null && treeAndCommunityDataBean.getmTreeAndCommunityDataItemBeanList() != null) {
                        treeAndCommunityDataItemBeanList = treeAndCommunityDataBean.getmTreeAndCommunityDataItemBeanList() ;
                        if (treeAndCommunityDataItemBeanList == null) {
                            return null;
                        }
                    }
            }catch (Exception e) {
                e.printStackTrace();
            }
        Log.d(TAG, "=====getReportData=====treeAndCommunityDataItemBeanList====:" + treeAndCommunityDataItemBeanList);
        return treeAndCommunityDataItemBeanList;
    }
    private void drawBarChart(List<TreeAndCommunityDataItemBean> treeAndCommunityDataItemBeanList) throws IOException {
        Iterator its = treeAndCommunityDataItemBeanList.iterator();
        int i  = 1;
        while(its.hasNext()) {
            try {
                TreeAndCommunityDataItemBean treeAndCommunityDataItemBean = (TreeAndCommunityDataItemBean) its.next();
                if (!"test".equals(treeAndCommunityDataItemBean.getConfigName())) {
                    Log.d(TAG, "======drawBarChart=====name====:" + treeAndCommunityDataItemBean.getConfigName());
                    List<TreeAndCommunityDataItemDetailBean> treeAndCommunityDataItemDetailBeanList = treeAndCommunityDataItemBean.getTreeAndCommunityDataItemDetailBeanList();
                    Log.d(TAG, "======drawBarChart=====treeAndCommunityDataItemDetailBeanList====:" + treeAndCommunityDataItemDetailBeanList);
                    float ratio = 2.0f;
                    if (treeAndCommunityDataItemDetailBeanList != null) {
                        if (i == 1) {
                            tvChartname = tvChartname1;
                            barChart = barChart1;
                        }
                        if (i == 2) {
                            tvChartname = tvChartname2;
                            barChart = barChart2;
                        }
                        if (i == 3) {
                            tvChartname = tvChartname3;
                            barChart = barChart3;
                        }
                        if (i == 4) {
                            tvChartname = tvChartname4;
                            barChart = barChart4;
                        }
                        if (i == 5) {
                            tvChartname = tvChartname5;
                            barChart = barChart5;
                        }
                        if (i == 6) {
                            tvChartname = tvChartname6;
                            barChart = barChart6;
                        }
                        if (i == 7) {
                            tvChartname = tvChartname7;
                            barChart = barChart7;
                        }
                        if (i == 8) {
                            tvChartname = tvChartname8;
                            barChart = barChart8;
                        }
                        if (i == 9) {
                            tvChartname = tvChartname9;
                            barChart = barChart9;
                        }
                        if (i == 10) {
                            tvChartname = tvChartname10;
                            barChart = barChart10;
                        }
                        if (i == 11) {
                            tvChartname = tvChartname11;
                            barChart = barChart11;
                        }
                        if (i == 12) {
                            tvChartname = tvChartname12;
                            barChart = barChart12;
                        }
                        if (i == 13) {
                            tvChartname = tvChartname13;
                            barChart = barChart13;
                        }
                        if (i == 14) {
                            tvChartname = tvChartname14;
                            barChart = barChart14;
                        }
                        if (i == 15) {
                            tvChartname = tvChartname15;
                            barChart = barChart15;
                        }
                        if (i == 16) {
                            tvChartname = tvChartname16;
                            barChart = barChart16;
                        }
                        if (i == 17) {
                            tvChartname = tvChartname17;
                            barChart = barChart17;
                        }
                        if (i == 18) {
                            tvChartname = tvChartname18;
                            barChart = barChart18;
                        }
                        if (i == 19) {
                            tvChartname = tvChartname19;
                            barChart = barChart19;
                        }
                        if (i == 20) {
                            tvChartname = tvChartname20;
                            barChart = barChart20;
                        }
                        if (i == 21) {
                            tvChartname = tvChartname21;
                            barChart = barChart21;
                        }
                        if (i == 22) {
                            tvChartname = tvChartname22;
                            barChart = barChart22;
                        }
                        if (i == 23) {
                            tvChartname = tvChartname23;
                            barChart = barChart23;
                        }
                        if (i == 24) {
                            tvChartname = tvChartname24;
                            barChart = barChart24;
                        }
                        if (i == 25) {
                            tvChartname = tvChartname25;
                            barChart = barChart5;
                        }
                        if (i == 26) {
                            tvChartname = tvChartname26;
                            barChart = barChart26;
                        }
                        if (i == 27) {
                            tvChartname = tvChartname27;
                            barChart = barChart27;
                        }
                        if (i == 28) {
                            tvChartname = tvChartname28;
                            barChart = barChart28;
                        }
                        if (i == 29) {
                            tvChartname = tvChartname29;
                            barChart = barChart29;
                        }
                        if (i == 30) {
                            tvChartname = tvChartname30;
                            barChart = barChart30;
                        }
                        if (i == 31) {
                            tvChartname = tvChartname31;
                            barChart = barChart31;
                        }
                        if (i == 32) {
                            tvChartname = tvChartname32;
                            barChart = barChart32;
                        }
                        if (i == 33) {
                            tvChartname = tvChartname33;
                            barChart = barChart33;
                        }
                        if (i == 34) {
                            tvChartname = tvChartname34;
                            barChart = barChart34;
                        }
                        if (i == 35) {
                            tvChartname = tvChartname35;
                            barChart = barChart35;
                        }
                        if (i == 36) {
                            tvChartname = tvChartname36;
                            barChart = barChart36;
                        }
                        if (i == 37) {
                            tvChartname = tvChartname37;
                            barChart = barChart37;
                        }
                        if (i == 38) {
                            tvChartname = tvChartname38;
                            barChart = barChart38;
                        }
                        if (i == 39) {
                            tvChartname = tvChartname39;
                            barChart = barChart39;
                        }
                        if (i == 40) {
                            tvChartname = tvChartname40;
                            barChart = barChart40;
                        }
                        if (i == 41) {
                            tvChartname = tvChartname41;
                            barChart = barChart41;
                        }
                        if (i == 42) {
                            tvChartname = tvChartname42;
                            barChart = barChart42;
                        }
                        if (i == 43) {
                            tvChartname = tvChartname43;
                            barChart = barChart43;
                        }
                        if (i == 44) {
                            tvChartname = tvChartname44;
                            barChart = barChart44;
                        }
                        if (i == 45) {
                            tvChartname = tvChartname45;
                            barChart = barChart45;
                        }
                        if (i == 46) {
                            tvChartname = tvChartname46;
                            barChart = barChart46;
                        }
                        if (i == 47) {
                            tvChartname = tvChartname47;
                            barChart = barChart47;
                        }
                        if (i == 48) {
                            tvChartname = tvChartname48;
                            barChart = barChart48;
                        }
                        if (i == 49) {
                            tvChartname = tvChartname49;
                            barChart = barChart49;
                        }
                        if (i == 50) {
                            tvChartname = tvChartname50;
                            barChart = barChart50;
                        }
                    }
                    Log.d(TAG, "==drawChart=====ConfigName=============:" + treeAndCommunityDataItemBean.getConfigName());
                    tvChartname.setText(treeAndCommunityDataItemBean.getConfigName());
                    tvChartname.setVisibility(View.VISIBLE);
                    barChart.zoom(0, 1f, 0, 0);//显示的时候是按照多大的比率缩放显示，1f表示不放大缩小
                    barChart.zoom(1 / ratio, 1f, 0, 0);
                    try {
                        setChart(ratio, barChart, treeAndCommunityDataItemDetailBeanList, days);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    barChart.setVisibility(View.VISIBLE);
                    barChart.setExtraBottomOffset(10);
                    barChart.notifyDataSetChanged();
                    barChart.getBarData().notifyDataChanged();
                    barChart.invalidate();
                    i++;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    private void drawChart(String selectedId,int days,String type) throws IOException {
        try {
            Log.d(TAG, "==drawChart=====selectedId=============:" + selectedId+"=====type=============:"+type);
            TreeAndCommunityDataBean treeAndCommunityDataBean = null;
            if("tree".equals(type)){
                treeAndCommunityDataBean = NetUtil.getTreeDataBean(selectedId, days);
            }else{
                treeAndCommunityDataBean = NetUtil.getCommunityDataBean(selectedId, days);
            }
            Log.d(TAG, "==drawChart======drawChart===treeAndCommunityDataBean====:" + treeAndCommunityDataBean);
            if (treeAndCommunityDataBean != null && treeAndCommunityDataBean.getmTreeAndCommunityDataItemBeanList() != null) {
                List<TreeAndCommunityDataItemBean> treeAndCommunityDataItemBeanList = treeAndCommunityDataBean.getmTreeAndCommunityDataItemBeanList();
                if (treeAndCommunityDataItemBeanList == null) {
                    return;
                }
                Log.d(TAG, "==drawChart=====treeAndCommunityDataItemBeanList====:" + treeAndCommunityDataItemBeanList);
                Iterator its = treeAndCommunityDataItemBeanList.iterator();
                int i = 1;
                while (its.hasNext()) {
                    TreeAndCommunityDataItemBean treeAndCommunityDataItemBean = (TreeAndCommunityDataItemBean) its.next();
                    if (!"test".equals(treeAndCommunityDataItemBean.getConfigName())) {
                        List<TreeAndCommunityDataItemDetailBean> treeAndCommunityDataItemDetailBeanList = treeAndCommunityDataItemBean.getTreeAndCommunityDataItemDetailBeanList();
                        Log.d(TAG, "=====drawChart=================treeAndCommunityDataItemDetailBeanList====:" + treeAndCommunityDataItemDetailBeanList);
                        float ratio = 2.0f;
                        if (treeAndCommunityDataItemDetailBeanList != null) {
                            if (i == 1) {
                                tvChartname = tvChartname1;
                                barChart = barChart1;
                            }
                            if (i == 2) {
                                tvChartname = tvChartname2;
                                barChart = barChart2;
                            }
                            if (i == 3) {
                                tvChartname = tvChartname3;
                                barChart = barChart3;
                            }
                            if (i == 4) {
                                tvChartname = tvChartname4;
                                barChart = barChart4;
                            }
                            if (i == 5) {
                                tvChartname = tvChartname5;
                                barChart = barChart5;
                            }
                            if (i == 6) {
                                tvChartname = tvChartname6;
                                barChart = barChart6;
                            }
                            if (i == 7) {
                                tvChartname = tvChartname7;
                                barChart = barChart7;
                            }
                            if (i == 8) {
                                tvChartname = tvChartname8;
                                barChart = barChart8;
                            }
                            if (i == 9) {
                                tvChartname = tvChartname9;
                                barChart = barChart9;
                            }
                            if (i == 10) {
                                tvChartname = tvChartname10;
                                barChart = barChart10;
                            }
                            if (i == 11) {
                                tvChartname = tvChartname11;
                                barChart = barChart11;
                            }
                            if (i == 12) {
                                tvChartname = tvChartname12;
                                barChart = barChart12;
                            }
                            if (i == 13) {
                                tvChartname = tvChartname13;
                                barChart = barChart13;
                            }
                            if (i == 14) {
                                tvChartname = tvChartname14;
                                barChart = barChart14;
                            }
                            if (i == 15) {
                                tvChartname = tvChartname15;
                                barChart = barChart15;
                            }
                            if (i == 16) {
                                tvChartname = tvChartname16;
                                barChart = barChart16;
                            }
                            if (i == 17) {
                                tvChartname = tvChartname17;
                                barChart = barChart17;
                            }
                            if (i == 18) {
                                tvChartname = tvChartname18;
                                barChart = barChart18;
                            }
                            if (i == 19) {
                                tvChartname = tvChartname19;
                                barChart = barChart19;
                            }
                            if (i == 20) {
                                tvChartname = tvChartname20;
                                barChart = barChart20;
                            }
                            if (i == 21) {
                                tvChartname = tvChartname21;
                                barChart = barChart21;
                            }
                            if (i == 22) {
                                tvChartname = tvChartname22;
                                barChart = barChart22;
                            }
                            if (i == 23) {
                                tvChartname = tvChartname23;
                                barChart = barChart23;
                            }
                            if (i == 24) {
                                tvChartname = tvChartname24;
                                barChart = barChart24;
                            }
                            if (i == 25) {
                                tvChartname = tvChartname25;
                                barChart = barChart5;
                            }
                            if (i == 26) {
                                tvChartname = tvChartname26;
                                barChart = barChart26;
                            }
                            if (i == 27) {
                                tvChartname = tvChartname27;
                                barChart = barChart27;
                            }
                            if (i == 28) {
                                tvChartname = tvChartname28;
                                barChart = barChart28;
                            }
                            if (i == 29) {
                                tvChartname = tvChartname29;
                                barChart = barChart29;
                            }
                            if (i == 30) {
                                tvChartname = tvChartname30;
                                barChart = barChart30;
                            }
                            if (i == 31) {
                                tvChartname = tvChartname31;
                                barChart = barChart31;
                            }
                            if (i == 32) {
                                tvChartname = tvChartname32;
                                barChart = barChart32;
                            }
                            if (i == 33) {
                                tvChartname = tvChartname33;
                                barChart = barChart33;
                            }
                            if (i == 34) {
                                tvChartname = tvChartname34;
                                barChart = barChart34;
                            }
                            if (i == 35) {
                                tvChartname = tvChartname35;
                                barChart = barChart35;
                            }
                            if (i == 36) {
                                tvChartname = tvChartname36;
                                barChart = barChart36;
                            }
                            if (i == 37) {
                                tvChartname = tvChartname37;
                                barChart = barChart37;
                            }
                            if (i == 38) {
                                tvChartname = tvChartname38;
                                barChart = barChart38;
                            }
                            if (i == 39) {
                                tvChartname = tvChartname39;
                                barChart = barChart39;
                            }
                            if (i == 40) {
                                tvChartname = tvChartname40;
                                barChart = barChart40;
                            }
                            if (i == 41) {
                                tvChartname = tvChartname41;
                                barChart = barChart41;
                            }
                            if (i == 42) {
                                tvChartname = tvChartname42;
                                barChart = barChart42;
                            }
                            if (i == 43) {
                                tvChartname = tvChartname43;
                                barChart = barChart43;
                            }
                            if (i == 44) {
                                tvChartname = tvChartname44;
                                barChart = barChart44;
                            }
                            if (i == 45) {
                                tvChartname = tvChartname45;
                                barChart = barChart45;
                            }
                            if (i == 46) {
                                tvChartname = tvChartname46;
                                barChart = barChart46;
                            }
                            if (i == 47) {
                                tvChartname = tvChartname47;
                                barChart = barChart47;
                            }
                            if (i == 48) {
                                tvChartname = tvChartname48;
                                barChart = barChart48;
                            }
                            if (i == 49) {
                                tvChartname = tvChartname49;
                                barChart = barChart49;
                            }
                            if (i == 50) {
                                tvChartname = tvChartname50;
                                barChart = barChart50;
                            }
                        }
                        Log.d(TAG, "==drawChart=====ConfigName=============:" + treeAndCommunityDataItemBean.getConfigName());
                        tvChartname.setText(treeAndCommunityDataItemBean.getConfigName());
                        tvChartname.setVisibility(View.VISIBLE);
                        barChart.zoom(0, 1f, 0, 0);
                        barChart.zoom(1 / ratio, 1f, 0, 0);
                        setChart(ratio, barChart, treeAndCommunityDataItemDetailBeanList, days);
                        barChart.setVisibility(View.VISIBLE);
                        barChart.setExtraBottomOffset(10);
                        barChart.notifyDataSetChanged();
                        barChart.getBarData().notifyDataChanged();
                        barChart.invalidate();
                        i++;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /*
     * 设置BarChart的数据
     * */
    private  List<BarEntry> setChartData(List<TreeAndCommunityDataItemDetailBean> treeAndCommunityDataItemDetailBeanList, int days) {
        int count = 0;
        List<BarEntry> list = new ArrayList<>();//实例化一个List用来存储数据
        tempMap.clear();
        dataMap.clear();
        if(treeAndCommunityDataItemDetailBeanList != null){
            Iterator iter = treeAndCommunityDataItemDetailBeanList.iterator();
            while (iter.hasNext() ){
                TreeAndCommunityDataItemDetailBean treeAndCommunityDataItemDetailBean = (TreeAndCommunityDataItemDetailBean) iter.next();
                //Log.d(TAG, "===treeAndCommunityDataItemDetailBean====:" + treeAndCommunityDataItemDetailBean);
                if(treeAndCommunityDataItemDetailBean!=null ){
                    float time = 0.0f;
                    /*if(days ==7){
                        time = Float.parseFloat(treeDataItemDetailBean.getAcquisitionTime().substring(11,13));
                    }else*/ if(days==7 && count< 7){
                        tempMap.put(count,treeAndCommunityDataItemDetailBean.getAcquisitionTime().substring(0,10));
                        time = count;
                    }else if(days == 15 && count < 15){
                        tempMap.put(count,treeAndCommunityDataItemDetailBean.getAcquisitionTime().substring(0,10));
                        time = count;
                    }if((days == 30 ||days == 90)&& count < 15){
                        treeAndCommunityDataItemDetailBean = (TreeAndCommunityDataItemDetailBean) iter.next();
                        tempMap.put(count,treeAndCommunityDataItemDetailBean.getAcquisitionTime().substring(0,10));
                        time = count;
                    }
                    float value = 0.0f;
                    if(!"".equals(treeAndCommunityDataItemDetailBean.getValue())){
                        if(!"-".equals(treeAndCommunityDataItemDetailBean.getValue())) {
                            value = Float.parseFloat(treeAndCommunityDataItemDetailBean.getValue());
                        }
                        list.add(new BarEntry(time,value));
                    }
                }
                count++;
            }
        }
            int j = 0;
            ListIterator<Map.Entry<Float,String>> i = new ArrayList<Map.Entry<Float,String>>(tempMap.entrySet()).listIterator(0);
             while(i.hasNext()) {
                Map.Entry<Float, String> entry=i.next();
                //Log.d(TAG, j+":"+entry.getValue());
                dataMap.put(j,entry.getValue());
                j++;
            }
        Log.d(TAG, "===list不为空====:" + (list!= null)+"===list.size()====:" + (list.size()));
        Log.d(TAG, "===dataMap====:" + dataMap);
        return list ;
    }
    /*
    画柱形图
     */
    private void setChart(float ratio,BarChart barChart,List<TreeAndCommunityDataItemDetailBean> treeAndCommunityDataItemDetailBeanList,int days) {
        List<BarEntry> list = setChartData(treeAndCommunityDataItemDetailBeanList,days);
        Log.d(TAG, "=========setChart======list==:" +list);
        Log.d(TAG, "=====setChart=====dataMap====:" +dataMap);
        barChart.setDescription(null);                             //设置描述文字为null
        barChart.setBackgroundColor(Color.parseColor("#00000000"));  //设置背景颜色
        barChart.setDrawBarShadow(false);                          //绘制当前展示的内容顶部阴影
        barChart.setPinchZoom(false);                              //设置x轴和y轴能否同时缩放。默认否
        barChart.setMaxVisibleValueCount(10);                       //设置图表能显示的最大值，仅当setDrawValues()属性值为true时有用
        barChart.setFitBars(true);                                 //设置X轴范围两侧柱形条是否显示一半
        BarDataSet barDataSet=new BarDataSet(list,"");   //list是你这条线的数据  "语文" 是你对这条线的描述
        BarData barData=new BarData(barDataSet);
        barChart.setData(barData);
        barData.setBarWidth(0.5f);   //设置柱子的宽度
        barData.setValueTextSize(13f);
        barData.setValueTextColor(Color.parseColor("#FFFFFFFF"));
        XAxis xAxis = barChart.getXAxis();                         //x轴
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);             //设置label在底下
        xAxis.setDrawGridLines(false);                             //不设置竖型网格线
        xAxis.setTextColor(Color.parseColor("#ffffff"));
        xAxis.setDrawLabels(true);                                 //是否显示X坐标轴上的刻度，默认是true
        xAxis.setLabelCount(6,false);                //第一个参数是轴坐标的个数，第二个参数是 是否不均匀分布，true是不均匀分布
        //xAxis.setAxisMinimum(-0.4f);   //X轴最小数值
        xAxis.setGranularity(1);

        YAxis leftAxis = barChart.getAxisLeft();              //获取到y轴，分左右
        leftAxis.setLabelCount(3, true);         //第一个参数是轴坐标的个数，第二个参数是 是否不均匀分布，true是不均匀分布
        leftAxis.setDrawGridLines(true);                      //不要横网格
        leftAxis.setGridColor(Color.parseColor("#000000"));   //设置横网格颜色
        leftAxis.setSpaceTop(20f);                            //设置在图表上最高处的值相比轴上最高值的顶端空间（总轴范围的百分比）
        leftAxis.setAxisMinimum(0f);                          //为这个轴设置一个自定义的最小值。如果设置,这个值不会自动根据所提供的数据计算

        leftAxis.setTextColor(Color.WHITE);
        leftAxis.setDrawAxisLine(false);                      //设置为true,绘制轴线
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);       //y轴的数值显示在外侧
        //这里也可以自定义y轴显示样式。和x轴的自定义方法一样
        barChart.getAxisRight().setEnabled(false); // 隐藏右边 的坐标轴
        Legend legend = barChart.getLegend();
        legend.setEnabled(false);//不设置图例
        //显示的时候是按照多大的比率缩放显示，1f表示不放大缩小
        //barChart.zoom(4,1f,0,0);
        if(days== 7){
            barChart.zoom(1,1f,0,0);
        }else{
            barChart.zoom(2,1f,0,0);
        }
        Log.d(TAG, "=====setChart==================days===============:" + days);
        xAxis.setTextSize(10f);//设置X轴刻度字体大小
        xAxis.setLabelRotationAngle(-60f);//旋转45度

        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float v, AxisBase axis) {
                return String.valueOf(dataMap.get((int) v));
            }
        });
        //从Y轴弹出的动画时间
        barChart.animateY(1500);
    }
    private void setScrollInvisiable() {
        tvChartname1.setVisibility(View.INVISIBLE);
        barChart1.setVisibility(View.INVISIBLE);
        tvChartname2.setVisibility(View.INVISIBLE);
        barChart2.setVisibility(View.INVISIBLE);
        tvChartname3.setVisibility(View.INVISIBLE);
        barChart3.setVisibility(View.INVISIBLE);
        tvChartname4.setVisibility(View.INVISIBLE);
        barChart4.setVisibility(View.INVISIBLE);
        tvChartname5.setVisibility(View.INVISIBLE);
        barChart5.setVisibility(View.INVISIBLE);
    }
    private void setBtnEnable(Button btn,String category) {
        List<Button> buttonList = new ArrayList<>();
        if (buttonList.size() == 0){
           /* if("type".equals(category)){
                buttonList.add(btnTree);
                buttonList.add(btnCommunity);
            }else{*/
                buttonList.add(btSevenDay);
                buttonList.add(btFifDay);
                buttonList.add(btThirtyDay);
                buttonList.add(btNintyDay);
            //}
        }
        for (int i = 0; i <buttonList.size() ; i++) {
            buttonList.get(i).setEnabled(true);
        }
        btn.setEnabled(false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}