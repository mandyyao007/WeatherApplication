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
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.weatherapplication.CommunityActivity;
import com.example.weatherapplication.R;
import com.example.weatherapplication.bean.CommunityBean;
import com.example.weatherapplication.bean.CommunityItemBean;
import com.example.weatherapplication.bean.TreeAndCommunityDataBean;
import com.example.weatherapplication.bean.TreeAndCommunityDataItemBean;
import com.example.weatherapplication.bean.TreeAndCommunityDataItemDetailBean;
import com.example.weatherapplication.bean.TreeAndCommunityRowDataBean;
import com.example.weatherapplication.databinding.FragmentNotificationsBinding;
import com.example.weatherapplication.util.Def;
import com.example.weatherapplication.util.NetUtil;
import com.example.weatherapplication.view.ExpandTabView;
import com.example.weatherapplication.view.ViewLeft;
import com.example.weatherapplication.view.ViewRight;
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
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

public class NotificationsFragment extends Fragment implements View.OnClickListener{

    private NotificationsViewModel notificationsViewModel;
    private FragmentNotificationsBinding binding;
    private ImageView ivAdd,ivBarCart1,ivBarCart2,ivBarCart3,ivBarCart4,ivBarCart5,ivBarCart6,ivBarCart7,ivBarCart8,ivBarCart;
    private ImageView ivLineCart1,ivLineCart2,ivLineCart3,ivLineCart4,ivLineCart5,ivLineCart6,ivLineCart7,ivLineCart8,ivLineCart;
    private TextView tvLine1,tvLine2,tvLine3,tvLine4,tvLine5,tvLine6,tvLine7,tvLine8,tvLine;
    private TextView tvStation,tvChartname1,tvChartname2,tvChartname3,tvChartname4,tvChartname5,tvChartname6,tvChartname7,tvChartname8,
            tvChartnamel1,tvChartnamel2,tvChartnamel3,tvChartnamel4,tvChartnamel5,tvChartnamel6,tvChartnamel7,
            tvChartnamel8,tvChartname = null,tvChartnameL =null;
    private String  userName,collectorId,collectorName,weatherStationId;
    private CombinedChart combinedChart1,combinedChart2,combinedChart3,combinedChart4,combinedChart5,
            combinedChart6,combinedChart7,combinedChart8,combinedChart = null;
    private Button btSevenDay,btFifDay, btThirtyDay,btNintyDay;
    private ScrollView scrollView;
    private String type = "";//tree or commnuity
    private LinkedHashMap tempMap = new LinkedHashMap();
    private LinkedHashMap dataMap = new LinkedHashMap();
    private int days = 0;
    // 线程变量
    private List<TreeAndCommunityDataItemBean> treeAndCommunityDataItemBeanList = null;
    private List<TreeAndCommunityRowDataBean> treeAndCommunityRowDataBeanList = null;
    private ProgressDialog progressDialog =  null;
    private static final String TAG = "NotificationsFragment";

    private ExpandTabView expandTabView;
    private ArrayList<View> mViewArray = new ArrayList<View>();
    private ViewLeft viewLeft;
    private ViewRight viewRight;
    private Button btCommunityName;
    private String selectedId = "";//select tree or community id
    private String weatherStationName;
    private String communityName,communityId;
    private String chatName,chatNameL;
    private String mapFlag,communityFlag ;

    public View onCreateView(@NonNull LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel = new ViewModelProvider(this).get(NotificationsViewModel.class);
        //Log.d(TAG, "==-============onCreateView===MapFlag=:" + notificationsViewModel.getMapFlag());
        //Log.d(TAG, "==-============onCreateView===CommunityFlag=:" + notificationsViewModel.getCommunityFlag());
        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View notificationFragmentView = binding.getRoot();
        try{
            initView(notificationFragmentView);
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
            //if(mapFlag == null && communityFlag == null){
            if(communityId == null){
                communityId= Def.DefCommunityId;
            }
                btCommunityName.setEnabled(false);
                btCommunityName.setTextColor(Color.YELLOW);
                btFifDay.setEnabled(false);
                selectedId = communityId;
                type="community";
                days = 15;
                Log.d(TAG, "==mapFlag=null====selectedId====:" + selectedId + "=====type=====:" + type+"======days====:"+days);
                drawChart(selectedId,days,type);
            //}
        }catch (Exception e) {
            e.printStackTrace();
        }
        return notificationFragmentView;
    }
    private void initView(View notificationFragmentView) throws Exception {
        userName = getActivity().getIntent().getStringExtra("userName");
        collectorId = getActivity().getIntent().getStringExtra("collectorId");
        collectorName = getActivity().getIntent().getStringExtra("collectorName");
        weatherStationId = getActivity().getIntent().getStringExtra("weatherStationId");
        weatherStationName =  getActivity().getIntent().getStringExtra("weatherStationName");
        communityName = getActivity().getIntent().getStringExtra("communityName");
        communityId = getActivity().getIntent().getStringExtra("communityId");
        mapFlag = getActivity().getIntent().getStringExtra("map_flag");
        communityFlag = getActivity().getIntent().getStringExtra("communityFlag");
        Log.d(TAG,"=***************==mapFlag======:"+ mapFlag);
        List<CommunityItemBean> communityItemBeans = new ArrayList<>();
        //if(mapFlag == null && communityFlag == null){//如果是从底部菜单进入，需要默认选中 ：群落和七天
        if(weatherStationId == null){
            collectorId = Def.DefCollectorId;//如果没有选几号点，默认是1号站点
            collectorName = Def.DefCollectorName;
            weatherStationId = Def.DefWeatherStationId;
            weatherStationName = Def.DefWeatherStationName;
            try{
                CommunityBean communityBean = NetUtil.getCommunityBean(weatherStationId);
                Log.d(TAG, "====***********======communityBean=============:" + communityBean);
                if(communityBean != null  && communityBean.getmItemBeans()!= null){
                    communityItemBeans = communityBean.getmItemBeans();
                    if(communityItemBeans.size() > 0){
                        Iterator<CommunityItemBean> iter = communityItemBeans.iterator();
                        if (iter.hasNext()) {
                            CommunityItemBean communityItemBean = iter.next();
                            Log.d(TAG, "====***********======communityItemBean=============:" + communityItemBean);
                            communityId =  communityItemBean.getCommunityId();
                            communityName = communityItemBean.getCommunityName();
//                            Log.d(TAG, "====***********======communityId=============:" + communityId);
//                            Log.d(TAG, "====***********======communityName=============:" + communityName);
                        }
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        //if(mapFlag == null || communityFlag == null){
        if(communityId == null){
            communityId= Def.DefCommunityId;
            communityName = Def.DefCommunityName;
        }
        Log.d(TAG,"=***************==userName==:"+ userName+"=***************==collectorId==:"+ collectorId);
        Log.d(TAG,"=***************==collectorName==:"+ collectorName+"=***************==weatherStationId==:"+ weatherStationId);
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
        tvChartname1 = notificationFragmentView.findViewById(R.id.chartname_tv_1);
        tvChartname2 = notificationFragmentView.findViewById(R.id.chartname_tv_2);
        tvChartname3 = notificationFragmentView.findViewById(R.id.chartname_tv_3);
        tvChartname4 = notificationFragmentView.findViewById(R.id.chartname_tv_4);
        tvChartname5 = notificationFragmentView.findViewById(R.id.chartname_tv_5);
        tvChartname6 = notificationFragmentView.findViewById(R.id.chartname_tv_6);
        tvChartname7 = notificationFragmentView.findViewById(R.id.chartname_tv_7);
        tvChartname8 = notificationFragmentView.findViewById(R.id.chartname_tv_8);
        tvChartnamel1 = notificationFragmentView.findViewById(R.id.chartname_tv_l1);
        tvChartnamel2 = notificationFragmentView.findViewById(R.id.chartname_tv_l2);
        tvChartnamel3 = notificationFragmentView.findViewById(R.id.chartname_tv_l3);
        tvChartnamel4 = notificationFragmentView.findViewById(R.id.chartname_tv_l4);
        tvChartnamel5 = notificationFragmentView.findViewById(R.id.chartname_tv_l5);
        tvChartnamel6 = notificationFragmentView.findViewById(R.id.chartname_tv_l6);
        tvChartnamel7 = notificationFragmentView.findViewById(R.id.chartname_tv_l7);
        tvChartnamel8 = notificationFragmentView.findViewById(R.id.chartname_tv_l8);
        tvLine1 = notificationFragmentView.findViewById(R.id.line_1);
        tvLine2 = notificationFragmentView.findViewById(R.id.line_2);
        tvLine3 = notificationFragmentView.findViewById(R.id.line_3);
        tvLine4 = notificationFragmentView.findViewById(R.id.line_4);
        tvLine5 = notificationFragmentView.findViewById(R.id.line_5);
        tvLine6 = notificationFragmentView.findViewById(R.id.line_6);
        tvLine7 = notificationFragmentView.findViewById(R.id.line_7);
        tvLine8 = notificationFragmentView.findViewById(R.id.line_8);

        ivBarCart1 = notificationFragmentView.findViewById(R.id.iv_bar_chart_1);
        ivBarCart2 = notificationFragmentView.findViewById(R.id.iv_bar_chart_2);
        ivBarCart3 = notificationFragmentView.findViewById(R.id.iv_bar_chart_3);
        ivBarCart4 = notificationFragmentView.findViewById(R.id.iv_bar_chart_4);
        ivBarCart5 = notificationFragmentView.findViewById(R.id.iv_bar_chart_5);
        ivBarCart6 = notificationFragmentView.findViewById(R.id.iv_bar_chart_6);
        ivBarCart7 = notificationFragmentView.findViewById(R.id.iv_bar_chart_7);
        ivBarCart8 = notificationFragmentView.findViewById(R.id.iv_bar_chart_8);

        ivLineCart1 = notificationFragmentView.findViewById(R.id.iv_line_chart_1);
        ivLineCart2 = notificationFragmentView.findViewById(R.id.iv_line_chart_2);
        ivLineCart3 = notificationFragmentView.findViewById(R.id.iv_line_chart_3);
        ivLineCart4 = notificationFragmentView.findViewById(R.id.iv_line_chart_4);
        ivLineCart5 = notificationFragmentView.findViewById(R.id.iv_line_chart_5);
        ivLineCart6 = notificationFragmentView.findViewById(R.id.iv_line_chart_6);
        ivLineCart7 = notificationFragmentView.findViewById(R.id.iv_line_chart_7);
        ivLineCart8 = notificationFragmentView.findViewById(R.id.iv_line_chart_8);

        scrollView = notificationFragmentView.findViewById(R.id.scrollview_eva);
        btSevenDay = notificationFragmentView.findViewById(R.id.btn_seven_day);
        btFifDay = notificationFragmentView.findViewById(R.id.btn_fifteen_day);
        btThirtyDay = notificationFragmentView.findViewById(R.id.btn_thirty_day);
        btNintyDay = notificationFragmentView.findViewById(R.id.btn_ninty_day);
        expandTabView = notificationFragmentView.findViewById(R.id.expandtab_view);
        btCommunityName = notificationFragmentView.findViewById(R.id.bt_community_name);
        viewLeft = new ViewLeft(getActivity(),"tree",weatherStationId);
        //viewRight = new ViewRight(getActivity(),"community",weatherStationId);
    }
    private void initVaule() {
        mViewArray.add(viewLeft);
        //mViewArray.add(viewRight);
        ArrayList<String> mTextArray = new ArrayList<String>();
        mTextArray.add("植株");
        //mTextArray.add("群落");
        expandTabView.setValue(mTextArray, mViewArray);
        //Log.d(TAG,"=***************==viewLeft.getShowText()==:"+ viewLeft.getShowText());
        //Log.d(TAG,"=***************==viewRight.getShowText()==:"+ viewRight.getShowText());
        expandTabView.setTitle(viewLeft.getShowText(), 2);
        //expandTabView.setTitle(viewRight.getShowText(), 2);
        btCommunityName.setText(communityName);
        btCommunityName.setTextSize(20);
    }
    private void initListener() {
        btCommunityName.setOnClickListener(this);
        viewLeft.setOnSelectListener(new ViewLeft.OnSelectListener() {
            @Override
            public void getValue(String distance, String showText) throws Exception {
                btCommunityName.setBackgroundColor(Color.TRANSPARENT);
                viewLeft.setBackgroundColor(Color.parseColor("#FFDEE3E4"));
                Log.d(TAG,"=********left*******=distance==:"+ distance+"=***********left****=showText==:"+ showText);
                selectedId = distance;////selectedTreeId
                type = "tree";
                btCommunityName.setEnabled(true);
                btCommunityName.setTextColor(Color.WHITE);
                Log.d(TAG,"=********left*******=selectedId==:"+ selectedId+"=******left*********=days==:"+ days);
                //Log.d(TAG,"=*********left******=type==:"+ type);
                if(days!=0){
                    drawChart(selectedId,days,type);
                }
                onRefresh(viewLeft, showText);
            }
        });
    }
    private void onRefresh(View view, String showText) {
        Log.d(TAG,"=*********onRefresh===============:");
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
                //Log.d(TAG,"=***************=iv_station=========");
                btCommunityName.setEnabled(true);
                btCommunityName.setTextColor(Color.WHITE);
                intent = new Intent(getActivity(), CommunityActivity.class);
                intent.putExtra("userName",userName);
                intent.putExtra("weatherStationId",weatherStationId);
                intent.putExtra("page","notification");
                intent.putExtra("weatherStationName",weatherStationName);
                startActivity(intent);
                break;
            case R.id.bt_community_name:
                btCommunityName.setEnabled(false);
                btCommunityName.setTextColor(Color.YELLOW);
                ToggleButton toggleButton = expandTabView.getSelectedButton();
                if (toggleButton!=null) {
                    toggleButton.getCurrentTextColor();
                    Log.d(TAG,"=**111111*************=bt_community_name====== toggleButton.getTextColors()==="+ toggleButton.getCurrentTextColor());
                    toggleButton.setTextColor(Color.WHITE);
                    Log.d(TAG,"=**22222222222*************=bt_community_name====== toggleButton.getTextColors()==="+ toggleButton.getCurrentTextColor());

                }
                type="community";
                Log.d(TAG,"=**111111*************=bt_community_name=========");
                selectedId = communityId;
                Log.d(TAG,"=**111111*************=bt_community_name======selectedId===:"+selectedId);
                Log.d(TAG,"=**111111*************=bt_community_name======days===:"+days);
                Log.d(TAG,"=**111111*************=bt_community_name======type===:"+type);
                if(days>0){
                    try {
                        drawChart(selectedId,days,type);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            //Log.d(TAG, "====dayListener=====treeAndCommunityRowDataBeanList===========:"+treeAndCommunityRowDataBeanList);
            if(treeAndCommunityRowDataBeanList!=null && treeAndCommunityRowDataBeanList.size()>0){
                try {
                    drawChart(treeAndCommunityRowDataBeanList);
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
            ////如果选择了植株，就把群落的按钮置于可用状态
            Log.d(TAG, "===========selectedId==========="+selectedId);
            if(!Def.DefCommunityId.equals(selectedId)){
                Log.d(TAG, "===========btCommunityName is Enabled=111111111111111==========");
                btCommunityName.setEnabled(true);
                btCommunityName.setTextColor(Color.WHITE);
            }
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
//            if("community".equals(type)){
//                selectedId = communityId;
//            }
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
                                        //Log.d(TAG, "====dayListener=====thread======type===========:"+type);
                                        treeAndCommunityRowDataBeanList = getReportData(selectedId,days,type);
                                        if(treeAndCommunityRowDataBeanList!=null && treeAndCommunityRowDataBeanList.size()>0){
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
    private List<TreeAndCommunityRowDataBean> getReportData(String selectedId, int days,String type) {
        //Log.d(TAG, "==getReportData======selectedId=============:" + selectedId);
        //Log.d(TAG, "==getReportData======type=============:" + type);
        List<TreeAndCommunityRowDataBean> treeAndCommunityRowDataBeanList = null;
        try {
            TreeAndCommunityDataBean treeAndCommunityDataBean = null;
            try {
                if("tree".equals(type)){
                    treeAndCommunityDataBean = NetUtil.getTreeDataBean(selectedId, days);
                }else{
                    treeAndCommunityDataBean = NetUtil.getCommunityDataBean(selectedId, days);
                }
            }catch (Exception e) {
                e.printStackTrace();
            }
            if (treeAndCommunityDataBean != null && treeAndCommunityDataBean.getmTreeAndCommunityRowDataBeanList() != null) {
                treeAndCommunityRowDataBeanList = treeAndCommunityDataBean.getmTreeAndCommunityRowDataBeanList() ;
                if (treeAndCommunityRowDataBeanList == null) {
                    return null;
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        //Log.d(TAG, "=====getReportData=====treeAndCommunityRowDataBeanList====:" + treeAndCommunityRowDataBeanList);
        return treeAndCommunityRowDataBeanList;
    }
    private void drawChart(List<TreeAndCommunityRowDataBean> treeAndCommunityRowDataBeanList) throws IOException {
        try {
           if(treeAndCommunityRowDataBeanList!=null){
               drawCombinedChart(treeAndCommunityRowDataBeanList);
           }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void drawChart(String selectedId,int days,String type) throws Exception {
        try {
            Log.d(TAG, "==drawChart=====selectedId========:" + selectedId + "=====type========:" + type);
            TreeAndCommunityDataBean treeAndCommunityDataBean = null;
            if ("tree".equals(type)) {
                treeAndCommunityDataBean = NetUtil.getTreeDataBean(selectedId, days);
            } else {
                treeAndCommunityDataBean = NetUtil.getCommunityDataBean(selectedId, days);
            }
            Log.d(TAG, "==drawChart======drawChart===treeAndCommunityDataBean====:" + treeAndCommunityDataBean);
            if (treeAndCommunityDataBean != null && treeAndCommunityDataBean.getmTreeAndCommunityRowDataBeanList() != null) {
                List<TreeAndCommunityRowDataBean> treeAndCommunityRowDataBeanList = treeAndCommunityDataBean.getmTreeAndCommunityRowDataBeanList();
                drawCombinedChart(treeAndCommunityRowDataBeanList);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void drawCombinedChart(List<TreeAndCommunityRowDataBean> treeAndCommunityRowDataBeanList){
        String mainConfigId ="";
        if (treeAndCommunityRowDataBeanList == null) {
            return;
        }
        //Log.d(TAG, "==drawChart=====treeAndCommunityRowDataBeanList====:" + treeAndCommunityRowDataBeanList);
        Iterator its = treeAndCommunityRowDataBeanList.iterator();
        int i = 1;

        List<BarEntry> list1 = new ArrayList<>();//实例化一个List用来存储数据
        List<Entry> list2 = new ArrayList<>();
        chatName= "";
        chatNameL = "";
        try{
            while (its.hasNext()) {
                TreeAndCommunityRowDataBean treeAndCommunityRowDataBean = (TreeAndCommunityRowDataBean) its.next();
                mainConfigId = treeAndCommunityRowDataBean.getMainConfigId();
                //Log.d(TAG, "==drawChart=====mainConfigId====:" + mainConfigId);

                if(treeAndCommunityRowDataBean != null && treeAndCommunityRowDataBean.getTreeAndCommunityDataItemBean()!= null){//如果有mainConfigId，说明此指标有物质量和价值量，物质量是柱形图，价值量是曲线图
                    List<TreeAndCommunityDataItemBean> treeAndCommunityDataItemBeanList = treeAndCommunityRowDataBean.getTreeAndCommunityDataItemBean();
                    if (treeAndCommunityDataItemBeanList == null) {
                        return;
                    }
                    Iterator iter = treeAndCommunityDataItemBeanList.iterator();
                    int j = 1;

                    if("".equals(mainConfigId) || mainConfigId ==null){
                        while(iter.hasNext()){
                            TreeAndCommunityDataItemBean treeAndCommunityDataItemBean = (TreeAndCommunityDataItemBean)iter.next();
                            //Log.d(TAG, "==drawChart=====treeAndCommunityDataItemBean====:" + treeAndCommunityDataItemBean);
                            chatName = treeAndCommunityDataItemBean.getConfigName()+"("+treeAndCommunityDataItemBean.getUnit()+"）";
                            if (!"test".equals(treeAndCommunityDataItemBean.getConfigName())) {
                                List<TreeAndCommunityDataItemDetailBean> treeAndCommunityDataItemDetailBeanList = treeAndCommunityDataItemBean.getTreeAndCommunityDataItemDetailBeanList();
                                //Log.d(TAG, "=====drawChart=================treeAndCommunityDataItemDetailBeanList====:" + treeAndCommunityDataItemDetailBeanList);
                                if(treeAndCommunityDataItemDetailBeanList!=null && j == 1){
                                    list1 = setBarChartData(treeAndCommunityDataItemDetailBeanList,days);
                                }
                            }
                        }
                    }else{
                        while(iter.hasNext()){
                            TreeAndCommunityDataItemBean treeAndCommunityDataItemBean = (TreeAndCommunityDataItemBean)iter.next();
                            //Log.d(TAG, "==drawChart=====treeAndCommunityDataItemBean====:" + treeAndCommunityDataItemBean);
                            if(j==1){
                                chatName=treeAndCommunityDataItemBean.getConfigName()+"("+treeAndCommunityDataItemBean.getUnit()+"）";
                            }else if(j==2){
                                chatNameL=treeAndCommunityDataItemBean.getConfigName()+"("+treeAndCommunityDataItemBean.getUnit()+"）";
                            }
                            if (!"test".equals(treeAndCommunityDataItemBean.getConfigName())) {
                                List<TreeAndCommunityDataItemDetailBean> treeAndCommunityDataItemDetailBeanList = treeAndCommunityDataItemBean.getTreeAndCommunityDataItemDetailBeanList();
                               // Log.d(TAG, "=====drawChart=================treeAndCommunityDataItemDetailBeanList====:" + treeAndCommunityDataItemDetailBeanList);
                                if(treeAndCommunityDataItemDetailBeanList!=null && j == 1){
                                    list1 = setBarChartData(treeAndCommunityDataItemDetailBeanList,days);
                                }else if(treeAndCommunityDataItemDetailBeanList!=null && j == 2){
                                    list2 = setChartData(treeAndCommunityDataItemDetailBeanList,days);
                                }
                            }
                            j++;
                        }
                    }
//                    Log.d(TAG, "=====drawChart=================list1====:" + list1.size());
//                    Log.d(TAG, "=====drawChart=================list2====:" + list2.size());
                    float ratio = 2.0f;
                    if (list1.size()>0 || list2.size()>0) {
                        if (i == 1) {
                            ivBarCart = ivBarCart1;
                            ivLineCart = ivLineCart1;
                            tvLine = tvLine1;
                            tvChartnameL =  tvChartnamel1;
                            tvChartname = tvChartname1;
                            combinedChart = combinedChart1;
                        }
                        if (i == 2) {
                            ivBarCart = ivBarCart2;
                            ivLineCart = ivLineCart2;
                            tvLine = tvLine2;
                            tvChartnameL =  tvChartnamel2;
                            tvChartname = tvChartname2;
                            combinedChart = combinedChart2;
                        }
                        if (i == 3) {
                            ivBarCart = ivBarCart3;
                            ivLineCart = ivLineCart3;
                            tvLine = tvLine3;
                            tvChartnameL =  tvChartnamel3;
                            tvChartname = tvChartname3;
                            combinedChart = combinedChart3;
                        }
                        if (i == 4) {
                            ivBarCart = ivBarCart4;
                            ivLineCart = ivLineCart4;
                            tvLine = tvLine4;
                            tvChartnameL =  tvChartnamel4;
                            tvChartname = tvChartname4;
                            combinedChart = combinedChart4;
                        }
                        if (i == 5) {
                            ivBarCart = ivBarCart5;
                            ivLineCart = ivLineCart5;
                            tvLine = tvLine5;
                            tvChartnameL =  tvChartnamel5;
                            tvChartname = tvChartname5;
                            combinedChart = combinedChart5;
                        }
                        if (i == 6) {
                            ivBarCart = ivBarCart6;
                            ivLineCart = ivLineCart6;
                            tvLine = tvLine6;
                            tvChartnameL =  tvChartnamel6;
                            tvChartname = tvChartname6;
                            combinedChart = combinedChart6;
                        }
                        if (i == 7) {
                            ivBarCart = ivBarCart7;
                            ivLineCart = ivLineCart7;
                            tvLine = tvLine7;
                            tvChartnameL =  tvChartnamel7;
                            tvChartname = tvChartname7;
                            combinedChart = combinedChart7;
                        }
                        if (i == 8) {
                            ivBarCart = ivBarCart8;
                            ivLineCart = ivLineCart8;
                            tvLine = tvLine8;
                            tvChartnameL =  tvChartnamel8;
                            tvChartname = tvChartname8;
                            combinedChart = combinedChart8;
                        }
                    }
                    if(list1.size()>0 ){
                        ivBarCart.setVisibility(View.VISIBLE);
                        tvChartname.setText(chatName);//增加单位显示
                        tvChartname.setVisibility(View.VISIBLE);
                    }
                   if(list1.size()>0 && list2.size()>0){
                       ivLineCart.setVisibility(View.VISIBLE);
                       tvLine.setVisibility(View.VISIBLE);
                       tvChartnameL.setText(chatNameL);
                       tvChartnameL.setVisibility(View.VISIBLE);
                   }
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
        }catch(Exception e){
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
        try{
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
                            tempMap.put(count,treeAndCommunityDataItemDetailBean.getAcquisitionTime().substring(5,10));
                            time = count;
                        }else if(days == 15 && count < 15){
                            tempMap.put(count,treeAndCommunityDataItemDetailBean.getAcquisitionTime().substring(5,10));
                            time = count;
                        }if((days == 30 ||days == 90)&& count < 15){
                            treeAndCommunityDataItemDetailBean = (TreeAndCommunityDataItemDetailBean) iter.next();
                            tempMap.put(count,treeAndCommunityDataItemDetailBean.getAcquisitionTime().substring(5,10));
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
        }catch(Exception e){
            e.printStackTrace();
        }
        //Log.d(TAG, "===list不为空====:" + (list!= null)+"===list.size()====:" + (list.size()));
        //Log.d(TAG, "===dataMap====:" + dataMap);
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
        try{
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
                            tempMap.put(count,treeAndCommunityDataItemDetailBean.getAcquisitionTime().substring(5,10));
                            time = count;
                        }else if(days == 15 && count < 15){
                            tempMap.put(count,treeAndCommunityDataItemDetailBean.getAcquisitionTime().substring(5,10));
                            time = count;
                        }if((days == 30 ||days == 90)&& count < 15){
                            treeAndCommunityDataItemDetailBean = (TreeAndCommunityDataItemDetailBean) iter.next();
                            tempMap.put(count,treeAndCommunityDataItemDetailBean.getAcquisitionTime().substring(5,10));
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
        }catch(Exception e){
            e.printStackTrace();
        }
        //Log.d(TAG, "===list不为空====:" + (list!= null)+"===list.size()====:" + (list.size()));
        //Log.d(TAG, "===dataMap====:" + dataMap);
        return list ;
    }
    /*
    画柱形图和曲线图
     */
    private void setChart(float ratio,CombinedChart combinedChart,List<BarEntry> list1,List<Entry> list2,int days) {
        try{
            double maxLeftYaxis = 1f,maxRightYaxis = 1f;
            Log.d(TAG, "=========setChart======list1==:" +list1);
            Log.d(TAG, "=========setChart======list2==:" +list2);
            if(list1.size()>0){
                maxLeftYaxis = getBarMaxValue(list1) * 1.05;
            }
            if(list2.size()>0){
                maxRightYaxis = getMaxValue(list2) * 1.05;
            }
            float maxLeftY   =  new  BigDecimal(maxLeftYaxis).setScale(3,  BigDecimal.ROUND_HALF_UP).floatValue();
            float maxRightY   =  new  BigDecimal(maxRightYaxis).setScale(3,  BigDecimal.ROUND_HALF_UP).floatValue();
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
            xAxis.setAxisMaximum((float) (list1.size() - 0.5));//为了解决柱x状图左右两边只显示了一半的问题 根据实际情况而定
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);//设置X轴标签的位置，BOTTOM在底部显示，TOP在顶部显示
            xAxis.setDrawLabels(true);     //是否显示X坐标轴上的刻度，默认是true
            xAxis.setLabelCount(6,false);   //第一个参数是轴坐标的个数，第二个参数是 是否不均匀分布，true是不均匀分布
            xAxis.setGranularity(1);
            xAxis.setTextSize(10f);//设置X轴刻度字体大小
            xAxis.setLabelRotationAngle(-60);//旋转60度
            xAxis.setValueFormatter(new IAxisValueFormatter() {
                @Override
                public String getFormattedValue(float v, AxisBase axis) {
                    return String.valueOf(dataMap.get((int) v));
                }
            });
            YAxis axisLeft = combinedChart.getAxisLeft();//获取Y轴左边操作类
            axisLeft.setAxisMinimum(0f);//设置最小值
            axisLeft.setAxisMaximum(maxLeftY); //保留四位小数
            //axisLeft.setGranularity(10);//设置label间隔
            axisLeft.setLabelCount(10, true);         //第一个参数是轴坐标的个数，第二个参数是 是否不均匀分布，true是不均匀分布
            axisLeft.setDrawGridLines(true);                      //不要横网格
            //axisLeft.setGridColor(Color.parseColor("#000000"));   //设置横网格颜色
            //axisLeft.setSpaceTop(20f);                            //设置在图表上最高处的值相比轴上最高值的顶端空间（总轴范围的百分比）
            axisLeft.setTextColor(Color.WHITE);
            axisLeft.setDrawAxisLine(true);                      //设置为true,绘制轴线
            axisLeft.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);       //y轴的数值显示在外侧
            axisLeft.setValueFormatter(new IAxisValueFormatter() {
                @Override
                public String getFormattedValue(float value, AxisBase axis) {
                    BigDecimal valueB  =   new  BigDecimal(value);
                    return valueB.setScale(5,  BigDecimal.ROUND_HALF_UP).floatValue()+"";
                }
            });
            YAxis axisRight = combinedChart.getAxisRight();//获取Y轴右边竖线
            if (list2.size()>0) {
                axisRight.setDrawAxisLine(true);
                axisRight.setAxisMinimum(0f);//设置最小值
                axisRight.setAxisMaximum( maxRightY);
                //axisRight.setGranularity(10);//设置label间隔
                axisRight.setLabelCount(10, true);         //第一个参数是轴坐标的个数，第二个参数是 是否强制
                //axisRight.setDrawLabels(true);
                axisRight.setTextColor(Color.BLACK);
                axisRight.setDrawGridLines(true);
                axisRight.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);

                axisRight.setValueFormatter(new IAxisValueFormatter() {
                    @Override
                    public String getFormattedValue(float value, AxisBase axis) {
                        BigDecimal valueB  =   new  BigDecimal(value);
                        return valueB.setScale(5,  BigDecimal.ROUND_HALF_UP).floatValue()+"";
                    }
                });
            }else{
                axisRight.setEnabled(false);//如果只有list1就不显示右侧Y轴
            }
            CombinedData combinedData =  new CombinedData();//创建组合图的数据源
            /**
             * 初始化柱形图的数据
             * 此处用的时list的数量做循环，因为总共所需要的数据数量应该和标签个数一直
             * 其中BarEntry时柱形图数据源的实体类，包装xy坐标数据
             */
            //Log.d(TAG,"*****************BarChart Start*******************");
            BarDataSet barDataSet = new BarDataSet(list1,"LAR");//新建一组柱形图，LAR为本柱形图的label
            barDataSet.setColor(Color.parseColor("#0288d1"));//设置柱形图颜色
            BarData barData = new BarData();
            barData.addDataSet(barDataSet);//添加一组柱形图，如果有多组柱形图数据，则可以多次addDataSet来设置
            barData.setBarWidth(0.5f);   //设置柱子的宽度
            barData.setValueTextSize(11f);
            barData.setValueTextColor(Color.parseColor("#ffffff"));
            //Log.d(TAG,"*****************BarChart End*******************");
            /**
             * 初始化折线图数据
             * 说明同上
             */
            if (list2.size()>0) {
                //Log.d(TAG,"*****************LineChart Start*******************");
                LineDataSet lineDataSet = new LineDataSet(list2,"MAR");
                lineDataSet.setColor(Color.parseColor("#b71c1c"));
                lineDataSet.setCircleColor(Color.parseColor("#b71c1c"));
                lineDataSet.setValueTextColor(Color.parseColor("#f44336"));
                lineDataSet.setLineWidth(3f);
                lineDataSet.setHighlightEnabled(false);
                lineDataSet.setAxisDependency(YAxis.AxisDependency.RIGHT);//默认情况下，添加到图表的所有数据都会绘制在图表左侧YAxis.可以通过更改DataSet对象的AxisDependency来完成
                LineData lineData = new LineData();
                lineData.setValueTextSize(11f);
                lineData.addDataSet(lineDataSet);
                //Log.d(TAG,"*****************LineChart End*******************");
                combinedData.setData(lineData);//添加曲线图数据源
            }
            combinedData.setData(barData);//添加柱形图数据源
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
            //Log.d(TAG, "=====setChart==================days===============:" + days);
            //从Y轴弹出的动画时间
            combinedChart.animateY(1500);
        }catch(Exception e){
           e.printStackTrace();
        }
    }
    private float getBarMaxValue(List<BarEntry> list) {
        float maxValue = 0 ;
        Iterator<BarEntry> it = list.iterator();
        while(it.hasNext()){
            BarEntry be = it.next();
            //Log.d(TAG, "=====getBarMaxValue==========be===========:" + be);
            //Log.d(TAG, "=====getBarMaxValue==========be.getY()===========:" + be.getY());
            if(be.getY()>maxValue){
                maxValue = be.getY();
            }
        }
        //Log.d(TAG, "=====getBarMaxValue=========maxValue==========:" + maxValue);
        return maxValue;
    }
    private float getMaxValue(List<Entry> list) {
        float maxValue = 0;
        Iterator<Entry> it = list.iterator();
        while(it.hasNext()){
            Entry be = it.next();
            //Log.d(TAG, "***********getMaxValue==========be===========:" + be);
            //Log.d(TAG, "***********getMaxValue==========be.getY()===========:" + be.getY());
            if(be.getY()>maxValue){
                maxValue = be.getY();
            }
        }
        //Log.d(TAG, "***********getMaxValue=========maxValue==========:" + maxValue);
        return maxValue;
    }


    private void setBtnEnable(Button btn,String category) {
        List<Button> buttonList = new ArrayList<>();
        if (buttonList.size() == 0){
            if("community".equals(category)){
                buttonList.add(btCommunityName);
            }else{
                buttonList.add(btSevenDay);
                buttonList.add(btFifDay);
                buttonList.add(btThirtyDay);
                buttonList.add(btNintyDay);
            }
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