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
import com.example.weatherapplication.bean.TreeAndCommunityRowDataBean;
import com.example.weatherapplication.databinding.FragmentNotificationsBinding;
import com.example.weatherapplication.util.NetUtil;
import com.example.weatherapplication.view.ExpandTabView;
import com.example.weatherapplication.view.ViewLeft;
import com.example.weatherapplication.view.ViewRight;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
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
            tvChartname18,tvChartname19,tvChartname20,tvChartname = null;
    private String  userName,collectorId,collectorName,weatherStationId;
    private CombinedChart combinedChart1,combinedChart2,combinedChart3,combinedChart4,combinedChart5,
            combinedChart6,combinedChart7,combinedChart8,combinedChart9,combinedChart10,combinedChart11,combinedChart12,
            combinedChart13,combinedChart14,combinedChart15,combinedChart16,combinedChart17,combinedChart18,combinedChart19,combinedChart20,combinedChart = null;
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
    private TextView tvCommunityName;
    private String selectedId = "";//select tree or community id
    private String weatherStationName;
    private String communityName,communityId;
    private StringBuffer chatName = new StringBuffer();

    public View onCreateView(@NonNull LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel = new ViewModelProvider(this).get(NotificationsViewModel.class);
        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View notificationFragmentView = binding.getRoot();
        initView(notificationFragmentView);
        try{
            if(communityName!= null) {
                initVaule();
                initListener();
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
            combinedChart1.setNoDataText("");//无数据时显示的文字
            combinedChart2.setNoDataText("");
            combinedChart3.setNoDataText("");
            combinedChart4.setNoDataText("");
            combinedChart5.setNoDataText("");
            combinedChart6.setNoDataText("");
            combinedChart7.setNoDataText("");
            combinedChart8.setNoDataText("");
            combinedChart9.setNoDataText("");
            combinedChart10.setNoDataText("");
            combinedChart11.setNoDataText("");
            combinedChart12.setNoDataText("");
            combinedChart13.setNoDataText("");
            combinedChart14.setNoDataText("");
            combinedChart15.setNoDataText("");
            combinedChart16.setNoDataText("");
            combinedChart17.setNoDataText("");
            combinedChart18.setNoDataText("");
            combinedChart19.setNoDataText("");
            combinedChart20.setNoDataText("");

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
        communityName = getActivity().getIntent().getStringExtra("communityName");
        communityId = getActivity().getIntent().getStringExtra("communityId");
        if(collectorId == null){
            collectorId = "52";//如果没有选几号点，默认是滨江森林公园1号点
            weatherStationId = "22";
            weatherStationName = "上海园林";
        }
        Log.d(TAG,"=***************==userName==:"+ userName+"=***************==collectorId==:"+ collectorId);
        Log.d(TAG,"=***************==stationName==:"+ collectorName+"=***************==weatherStationId==:"+ weatherStationId);
        Log.d(TAG,"=***************==weatherStationName======:"+ weatherStationName);
        Log.d(TAG,"=***************==weatherStationId======:"+ weatherStationId);
        Log.d(TAG,"=***************==communityName======:"+ communityName);
        Log.d(TAG,"=***************==communityId======:"+ communityId);
        tvStation = notificationFragmentView.findViewById(R.id.tv_station);
        tvStation.setText(communityName);
        ivAdd = notificationFragmentView.findViewById(R.id.iv_station);
        combinedChart1 = notificationFragmentView.findViewById(R.id.combinedChart_1);
        combinedChart2 = notificationFragmentView.findViewById(R.id.combinedChart_2);
        combinedChart3 = notificationFragmentView.findViewById(R.id.combinedChart_3);
        combinedChart4 = notificationFragmentView.findViewById(R.id.combinedChart_4);
        combinedChart5 = notificationFragmentView.findViewById(R.id.combinedChart_5);
        combinedChart6 = notificationFragmentView.findViewById(R.id.combinedChart_6);
        combinedChart7 = notificationFragmentView.findViewById(R.id.combinedChart_7);
        combinedChart8 = notificationFragmentView.findViewById(R.id.combinedChart_8);
        combinedChart9 = notificationFragmentView.findViewById(R.id.combinedChart_9);
        combinedChart10 = notificationFragmentView.findViewById(R.id.combinedChart_10);
        combinedChart11 = notificationFragmentView.findViewById(R.id.combinedChart_11);
        combinedChart12 = notificationFragmentView.findViewById(R.id.combinedChart_12);
        combinedChart13 = notificationFragmentView.findViewById(R.id.combinedChart_13);
        combinedChart14 = notificationFragmentView.findViewById(R.id.combinedChart_14);
        combinedChart15 = notificationFragmentView.findViewById(R.id.combinedChart_15);
        combinedChart16 = notificationFragmentView.findViewById(R.id.combinedChart_16);
        combinedChart17 = notificationFragmentView.findViewById(R.id.combinedChart_17);
        combinedChart18 = notificationFragmentView.findViewById(R.id.combinedChart_18);
        combinedChart19 = notificationFragmentView.findViewById(R.id.combinedChart_19);
        combinedChart20 = notificationFragmentView.findViewById(R.id.combinedChart_20);
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
        scrollView = notificationFragmentView.findViewById(R.id.scrollview_eva);
        btSevenDay = notificationFragmentView.findViewById(R.id.btn_seven_day);
        btFifDay = notificationFragmentView.findViewById(R.id.btn_fifteen_day);
        btThirtyDay = notificationFragmentView.findViewById(R.id.btn_thirty_day);
        btNintyDay = notificationFragmentView.findViewById(R.id.btn_ninty_day);
        expandTabView = notificationFragmentView.findViewById(R.id.expandtab_view);
        tvCommunityName = notificationFragmentView.findViewById(R.id.tv_community_name);
        viewLeft = new ViewLeft(getActivity(),"tree",collectorId);
        //viewRight = new ViewRight(getActivity(),"community",weatherStationId);
    }
    private void initVaule() {
        mViewArray.add(viewLeft);
        //mViewArray.add(viewRight);
        ArrayList<String> mTextArray = new ArrayList<String>();
        mTextArray.add("植株");
        //mTextArray.add("群落");
        expandTabView.setValue(mTextArray, mViewArray);
        Log.d(TAG,"=***************==viewLeft.getShowText()==:"+ viewLeft.getShowText());
        //Log.d(TAG,"=***************==viewRight.getShowText()==:"+ viewRight.getShowText());
        expandTabView.setTitle(viewLeft.getShowText(), 2);
        //expandTabView.setTitle(viewRight.getShowText(), 2);
        tvCommunityName.setText(communityName);
        tvCommunityName.setTextSize(20);
        type="community";
    }
    private void initListener() {
        viewLeft.setOnSelectListener(new ViewLeft.OnSelectListener() {
            @Override
            public void getValue(String distance, String showText) throws Exception {
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
                Log.d(TAG,"=***************=iv_station=========");
                intent = new Intent(getActivity(), CommunityActivity.class);
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
            if("community".equals(type)){
                selectedId = communityId;
            }
            Log.d(TAG, "====dayListener=====days===========:"+days+"=========selectedId========="+selectedId);
            Log.d(TAG, "====dayListener=====type===========:"+type);
            if("".equals(selectedId)){
                String message = "";
                if("tree".equals(type)){
                    message = "请先选择植株！";
                }//else if("community".equals(type)){
                 //   message = "请先选择群落！";
               // }else{
                 //   message = "请先选择植株或群落！";
                //}
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
//                    if (treeAndCommunityDataBean != null && treeAndCommunityDataBean.getmTreeAndCommunityDataItemBeanList() != null) {
//                        treeAndCommunityDataItemBeanList = treeAndCommunityDataBean.getmTreeAndCommunityDataItemBeanList() ;
//                        if (treeAndCommunityDataItemBeanList == null) {
//                            return null;
//                        }
//                    }
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
                            combinedChart = combinedChart1;
                        }
                        if (i == 2) {
                            tvChartname = tvChartname2;
                            combinedChart = combinedChart2;
                        }
                        if (i == 3) {
                            tvChartname = tvChartname3;
                            combinedChart = combinedChart3;
                        }
                        if (i == 4) {
                            tvChartname = tvChartname4;
                            combinedChart = combinedChart4;
                        }
                        if (i == 5) {
                            tvChartname = tvChartname5;
                            combinedChart = combinedChart5;
                        }
                        if (i == 6) {
                            tvChartname = tvChartname6;
                            combinedChart = combinedChart6;
                        }
                        if (i == 7) {
                            tvChartname = tvChartname7;
                            combinedChart = combinedChart7;
                        }
                        if (i == 8) {
                            tvChartname = tvChartname8;
                            combinedChart = combinedChart8;
                        }
                        if (i == 9) {
                            tvChartname = tvChartname9;
                            combinedChart = combinedChart9;
                        }
                        if (i == 10) {
                            tvChartname = tvChartname10;
                            combinedChart = combinedChart10;
                        }
                        if (i == 11) {
                            tvChartname = tvChartname11;
                            combinedChart = combinedChart11;
                        }
                        if (i == 12) {
                            tvChartname = tvChartname12;
                            combinedChart = combinedChart12;
                        }
                        if (i == 13) {
                            tvChartname = tvChartname13;
                            combinedChart = combinedChart13;
                        }
                        if (i == 14) {
                            tvChartname = tvChartname14;
                            combinedChart = combinedChart14;
                        }
                        if (i == 15) {
                            tvChartname = tvChartname15;
                            combinedChart = combinedChart15;
                        }
                        if (i == 16) {
                            tvChartname = tvChartname16;
                            combinedChart = combinedChart16;
                        }
                        if (i == 17) {
                            tvChartname = tvChartname17;
                            combinedChart = combinedChart17;
                        }
                        if (i == 18) {
                            tvChartname = tvChartname18;
                            combinedChart = combinedChart18;
                        }
                        if (i == 19) {
                            tvChartname = tvChartname19;
                            combinedChart = combinedChart19;
                        }
                        if (i == 20) {
                            tvChartname = tvChartname20;
                            combinedChart = combinedChart20;
                        }
                    }
                    Log.d(TAG, "==drawChart=====ConfigName=============:" + treeAndCommunityDataItemBean.getConfigName());
                    tvChartname.setText(treeAndCommunityDataItemBean.getConfigName());
                    tvChartname.setVisibility(View.VISIBLE);
                    combinedChart.zoom(0, 1f, 0, 0);//显示的时候是按照多大的比率缩放显示，1f表示不放大缩小
                    combinedChart.zoom(1 / ratio, 1f, 0, 0);
                    try {
                        //setChart(ratio, barChart, treeAndCommunityDataItemDetailBeanList, days);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    combinedChart.setVisibility(View.VISIBLE);
                    combinedChart.setExtraBottomOffset(10);
                    combinedChart.notifyDataSetChanged();
                    combinedChart.getBarData().notifyDataChanged();
                    combinedChart.invalidate();
                    i++;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    private void drawChart(String selectedId,int days,String type) throws Exception {
        try {
            String mainConfigId ="";
            Log.d(TAG, "==drawChart=====selectedId=============:" + selectedId+"=====type=============:"+type);
            TreeAndCommunityDataBean treeAndCommunityDataBean = null;
            if("tree".equals(type)){
                treeAndCommunityDataBean = NetUtil.getTreeDataBean(selectedId, days);
            }else{
                treeAndCommunityDataBean = NetUtil.getCommunityDataBean(selectedId, days);
            }
            Log.d(TAG, "==drawChart======drawChart===treeAndCommunityDataBean====:" + treeAndCommunityDataBean);
            if (treeAndCommunityDataBean != null && treeAndCommunityDataBean.getmTreeAndCommunityRowDataBeanList()!= null) {
                List<TreeAndCommunityRowDataBean> treeAndCommunityRowDataBeanList = treeAndCommunityDataBean.getmTreeAndCommunityRowDataBeanList();
                if (treeAndCommunityRowDataBeanList == null) {
                    return;
                }
                Log.d(TAG, "==drawChart=====treeAndCommunityRowDataBeanList====:" + treeAndCommunityRowDataBeanList);
                Iterator its = treeAndCommunityRowDataBeanList.iterator();
                int i = 1;

                List<BarEntry> list1 = new ArrayList<>();//实例化一个List用来存储数据
                List<Entry> list2 = new ArrayList<>();
                while (its.hasNext()) {
                    TreeAndCommunityRowDataBean treeAndCommunityRowDataBean = (TreeAndCommunityRowDataBean) its.next();
                    mainConfigId = treeAndCommunityRowDataBean.getMainConfigId();
                    Log.d(TAG, "==drawChart=====mainConfigId====:" + mainConfigId);
                    if(treeAndCommunityRowDataBean != null && treeAndCommunityRowDataBean.getTreeAndCommunityDataItemBean()!= null){//如果有mainConfigId，说明此指标有物质量和价值量，物质量是柱形图，价值量是曲线图
                        List<TreeAndCommunityDataItemBean> treeAndCommunityDataItemBeanList = treeAndCommunityRowDataBean.getTreeAndCommunityDataItemBean();
                        if (treeAndCommunityDataItemBeanList == null) {
                            return;
                        }
                        Iterator iter = treeAndCommunityDataItemBeanList.iterator();
                        int j = 1;
                        chatName= new StringBuffer();
                        while(iter.hasNext()){
                            TreeAndCommunityDataItemBean treeAndCommunityDataItemBean = (TreeAndCommunityDataItemBean)iter.next();
                            Log.d(TAG, "==drawChart=====treeAndCommunityDataItemBean====:" + treeAndCommunityDataItemBean);
                            chatName.append(treeAndCommunityDataItemBean.getConfigName()+"("+treeAndCommunityDataItemBean.getUnit()+"）");
                            if (!"test".equals(treeAndCommunityDataItemBean.getConfigName())) {
                                List<TreeAndCommunityDataItemDetailBean> treeAndCommunityDataItemDetailBeanList = treeAndCommunityDataItemBean.getTreeAndCommunityDataItemDetailBeanList();
                                Log.d(TAG, "=====drawChart=================treeAndCommunityDataItemDetailBeanList====:" + treeAndCommunityDataItemDetailBeanList);
                                if(treeAndCommunityDataItemDetailBeanList!=null && j == 1){
                                     list1 = setBarChartData(treeAndCommunityDataItemDetailBeanList,days);
                                }else if(treeAndCommunityDataItemDetailBeanList!=null && j == 2){
                                     list2 = setChartData(treeAndCommunityDataItemDetailBeanList,days);
                                }
                            }
                            j++;
                        }
                        Log.d(TAG, "=====drawChart=================list1====:" + list1);
                        Log.d(TAG, "=====drawChart=================list2====:" + list2);
                        float ratio = 2.0f;
                        if (list1.size()>0 || list2.size()>0) {
                            if (i == 1) {
                                tvChartname = tvChartname1;
                                combinedChart = combinedChart1;
                            }
                            if (i == 2) {
                                tvChartname = tvChartname2;
                                combinedChart = combinedChart2;
                            }
                            if (i == 3) {
                                tvChartname = tvChartname3;
                                combinedChart = combinedChart3;
                            }
                            if (i == 4) {
                                tvChartname = tvChartname4;
                                combinedChart = combinedChart4;
                            }
                            if (i == 5) {
                                tvChartname = tvChartname5;
                                combinedChart = combinedChart5;
                            }
                            if (i == 6) {
                                tvChartname = tvChartname6;
                                combinedChart = combinedChart6;
                            }
                            if (i == 7) {
                                tvChartname = tvChartname7;
                                combinedChart = combinedChart7;
                            }
                            if (i == 8) {
                                tvChartname = tvChartname8;
                                combinedChart = combinedChart8;
                            }
                            if (i == 9) {
                                tvChartname = tvChartname9;
                                combinedChart = combinedChart9;
                            }
                            if (i == 10) {
                                tvChartname = tvChartname10;
                                combinedChart = combinedChart10;
                            }
                            if (i == 11) {
                                tvChartname = tvChartname11;
                                combinedChart = combinedChart11;
                            }
                            if (i == 12) {
                                tvChartname = tvChartname12;
                                combinedChart = combinedChart12;
                            }
                            if (i == 13) {
                                tvChartname = tvChartname13;
                                combinedChart = combinedChart13;
                            }
                            if (i == 14) {
                                tvChartname = tvChartname14;
                                combinedChart = combinedChart14;
                            }
                            if (i == 15) {
                                tvChartname = tvChartname15;
                                combinedChart = combinedChart15;
                            }
                            if (i == 16) {
                                tvChartname = tvChartname16;
                                combinedChart = combinedChart16;
                            }
                            if (i == 17) {
                                tvChartname = tvChartname17;
                                combinedChart = combinedChart17;
                            }
                            if (i == 18) {
                                tvChartname = tvChartname18;
                                combinedChart = combinedChart18;
                            }
                            if (i == 19) {
                                tvChartname = tvChartname19;
                                combinedChart = combinedChart19;
                            }
                            if (i == 20) {
                                tvChartname = tvChartname20;
                                combinedChart = combinedChart20;
                            }
                        }
                        tvChartname.setText(chatName);//增加单位显示
                        tvChartname.setVisibility(View.VISIBLE);
                        combinedChart.zoom(0, 1f, 0, 0);
                        combinedChart.zoom(1 / ratio, 1f, 0, 0);
                        setChart(ratio, combinedChart, list1,list2, days);
                        combinedChart.setVisibility(View.VISIBLE);
                        combinedChart.setExtraBottomOffset(10);
                        combinedChart.notifyDataSetChanged();
                        combinedChart.getBarData().notifyDataChanged();
                        combinedChart.invalidate();
                        i++;
                        }
                    }
                }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /*
     * 设置LineChart的数据
     * */
    private  List<Entry> setChartData(List<TreeAndCommunityDataItemDetailBean> treeAndCommunityDataItemDetailBeanList, int days) {
        int count = 0;
        List<Entry> list = new ArrayList<>();//实例化一个List用来存储数据
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
     * 设置BarChart的数据
     * */
    private  List<BarEntry> setBarChartData(List<TreeAndCommunityDataItemDetailBean> treeAndCommunityDataItemDetailBeanList, int days) {
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
    画柱形图和曲线图
     */
    private void setChart(float ratio,CombinedChart combinedChart,List<BarEntry> list1,List<Entry> list2,int days) {
        Log.d(TAG, "=========setChart======list1==:" +list1);
        Log.d(TAG, "=========setChart======list2==:" +list2);
        Log.d(TAG, "=========setChart======dataMap==:" +dataMap);
        combinedChart.setDrawBorders(false);//不显示边界
        combinedChart.setPinchZoom(true);//比例缩放
        combinedChart.animateY(1500);
        combinedChart.setBackgroundColor(Color.WHITE);
        combinedChart.setDrawGridBackground(false);
        combinedChart.setHighlightFullBarEnabled(false);

        XAxis xAxis = combinedChart.getXAxis();
        xAxis.setTextColor(Color.parseColor("#ffffff"));
        xAxis.setDrawGridLines(false);
        /*解决左右两端柱形图只显示一半的情况，只有使用conbinedChart时出现，如果单独使用barchart不会有这个问题*/
        xAxis.setAxisMinimum(-0.5f);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);//设置X轴标签的位置，BOTTOM在底部显示，TOP在顶部显示
        xAxis.setDrawLabels(true);     //是否显示X坐标轴上的刻度，默认是true
        xAxis.setLabelCount(6,false);   //第一个参数是轴坐标的个数，第二个参数是 是否不均匀分布，true是不均匀分布
        //xAxis.setAxisMinimum(-0.4f);   //X轴最小数值
        xAxis.setGranularity(1);
        xAxis.setTextSize(10f);//设置X轴刻度字体大小
        xAxis.setLabelRotationAngle(-60);//旋转60度
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float v, AxisBase axis) {
                return String.valueOf(dataMap.get((int) v));
            }
        });
        YAxis axisLeft = combinedChart.getAxisRight();//获取Y轴右边操作类
        axisLeft.setAxisMinimum(0f);//设置最小值
        axisLeft.setGranularity(10);//设置label间隔
        axisLeft.setLabelCount(3, true);         //第一个参数是轴坐标的个数，第二个参数是 是否不均匀分布，true是不均匀分布
        axisLeft.setDrawGridLines(true);                      //不要横网格
        axisLeft.setGridColor(Color.parseColor("#000000"));   //设置横网格颜色
        axisLeft.setSpaceTop(20f);                            //设置在图表上最高处的值相比轴上最高值的顶端空间（总轴范围的百分比）
        axisLeft.setTextColor(Color.WHITE);
        axisLeft.setDrawAxisLine(false);                      //设置为true,绘制轴线
        axisLeft.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);       //y轴的数值显示在外侧
        axisLeft.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return value+"";
            }
        });

        YAxis axisRight = combinedChart.getAxisRight();//获取Y轴右边操作类
        axisRight.setDrawAxisLine(false);//不绘制背景线
        axisRight.setAxisMinimum(0f);//设置最小值
        axisRight.setGranularity(10);//设置label间隔
        axisRight.setLabelCount(10);//设置标签数量
        axisRight.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return value+"";
            }
        });
        /**
         * 初始化柱形图的数据
         * 此处用的时list的数量做循环，因为总共所需要的数据数量应该和标签个数一直
         * 其中BarEntry时柱形图数据源的实体类，包装xy坐标数据
         */
        Log.d(TAG,"*****************BarChart Start*******************");
        BarDataSet barDataSet = new BarDataSet(list1,"LAR");//新建一组柱形图，LAR为本柱形图的label
        barDataSet.setColor(Color.parseColor("#0288d1"));//设置柱形图颜色
        BarData barData = new BarData();
        barData.addDataSet(barDataSet);//添加一组柱形图，如果有多组柱形图数据，则可以多次addDataSet来设置
        barData.setBarWidth(0.5f);   //设置柱子的宽度
        barData.setValueTextSize(13f);
        barData.setValueTextColor(Color.parseColor("#f44336"));
        Log.d(TAG,"*****************BarChart End*******************");
        /**
         * 初始化折线图数据
         * 说明同上
         */
        Log.d(TAG,"*****************LineChart Start*******************");
        LineDataSet lineDataSet = new LineDataSet(list2,"MAR");
        lineDataSet.setColor(Color.parseColor("#b71c1c"));
        lineDataSet.setCircleColor(Color.parseColor("#b71c1c"));
        lineDataSet.setValueTextColor(Color.parseColor("#f44336"));
        lineDataSet.setLineWidth(3f);
        lineDataSet.setHighlightEnabled(false);
        LineData lineData = new LineData();
        lineData.addDataSet(lineDataSet);
        Log.d(TAG,"*****************LineChart Start*******************");
        CombinedData combinedData =  new CombinedData();//创建组合图的数据源
        combinedData.setData(barData);//添加柱形图数据源
        combinedData.setData(lineData);//添加曲线图数据源
        combinedChart.setData(combinedData);//为组合图设置数据源
        combinedChart.setBackgroundColor(Color.parseColor("#00000000"));  //设置背景颜色
        combinedChart.setDescription(null);                             //设置描述文字为null
        combinedChart.setDrawBarShadow(false);                          //绘制当前展示的内容顶部阴影
        combinedChart.setPinchZoom(false);                              //设置x轴和y轴能否同时缩放。默认否
        combinedChart.setMaxVisibleValueCount(10);                       //设置图表能显示的最大值，仅当setDrawValues()属性值为true时有用

        Legend legend = combinedChart.getLegend();
        legend.setEnabled(false);//不设置图例
        //显示的时候是按照多大的比率缩放显示，1f表示不放大缩小
        //barChart.zoom(4,1f,0,0);
        if(days== 7){
           combinedChart.zoom(1,1f,0,0);
        }else{
           combinedChart.zoom(2,1f,0,0);
        }
        Log.d(TAG, "=====setChart==================days===============:" + days);
        //从Y轴弹出的动画时间
        combinedChart.animateY(1500);
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