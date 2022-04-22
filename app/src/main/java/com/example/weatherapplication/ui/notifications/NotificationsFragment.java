package com.example.weatherapplication.ui.notifications;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
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

import com.example.weatherapplication.R;
import com.example.weatherapplication.StationActivity;
import com.example.weatherapplication.bean.CollectorItemBean;
import com.example.weatherapplication.bean.TreeBean;
import com.example.weatherapplication.bean.TreeDataItemBean;
import com.example.weatherapplication.bean.TreeDataItemDetailBean;
import com.example.weatherapplication.bean.TreeItemBean;
import com.example.weatherapplication.databinding.FragmentNotificationsBinding;
import com.example.weatherapplication.util.NetUtil;
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
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class NotificationsFragment extends Fragment implements View.OnClickListener{

    private NotificationsViewModel notificationsViewModel;
    private FragmentNotificationsBinding binding;
    private ImageView ivAdd;
    private TextView tvStation,tvChartname1,tvChartname2,tvChartname3,tvChartname4,tvChartname5;
    String  userName,collectorId,collectorName,weatherStationId;
    private BarChart barChart1,barChart2,barChart3,barChart4,barChart5;
    private Button btnTree,btCommunity;
    private ScrollView scrollView;
    String type = "";//tree or commnuity

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel =
                new ViewModelProvider(this).get(NotificationsViewModel.class);

        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View notificationFragmentView = binding.getRoot();
        initView(notificationFragmentView);
        try{
            if(collectorId!= null) {
                btnTree.setOnClickListener(this);
                btCommunity.setOnClickListener(this);
            }else{
                btnTree.setVisibility(View.INVISIBLE);//如果没有选中station直接打开数据，就不显示按钮
                btCommunity.setVisibility(View.INVISIBLE);
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
        Log.d("NotificationsFragment","=***************==userName==:"+ userName);
        Log.d("NotificationsFragment","=***************==collectorId==:"+ collectorId);
        Log.d("NotificationsFragment","=***************==stationName==:"+ collectorName);
        Log.d("NotificationsFragment","=***************==weatherStationId==:"+ weatherStationId);
        tvStation = notificationFragmentView.findViewById(R.id.tv_station);
        tvStation.setText(collectorName);
        ivAdd = (ImageView) notificationFragmentView.findViewById(R.id.iv_station);
        barChart1 = (BarChart) notificationFragmentView.findViewById(R.id.barchart_1);
        barChart2 = (BarChart) notificationFragmentView.findViewById(R.id.barchart_2);
        barChart3 = (BarChart) notificationFragmentView.findViewById(R.id.barchart_3);
        barChart4 = (BarChart) notificationFragmentView.findViewById(R.id.barchart_4);
        barChart5 = (BarChart) notificationFragmentView.findViewById(R.id.barchart_5);
        btnTree = notificationFragmentView.findViewById(R.id.btn_tree);
        btCommunity = notificationFragmentView.findViewById(R.id.btn_community);
        tvChartname1 = notificationFragmentView.findViewById(R.id.chartname_tv_1);
        tvChartname2 = notificationFragmentView.findViewById(R.id.chartname_tv_2);
        tvChartname3 = notificationFragmentView.findViewById(R.id.chartname_tv_3);
        tvChartname4 = notificationFragmentView.findViewById(R.id.chartname_tv_4);
        tvChartname5 = notificationFragmentView.findViewById(R.id.chartname_tv_5);
        scrollView = notificationFragmentView.findViewById(R.id.scrollview_eva);
    }
    @Override
    public void onClick(View v) {
        Intent intent = null;
        String  category= "";
        switch (v.getId()) {
            case R.id.iv_station:
                intent = new Intent(getActivity(), StationActivity.class);
                intent.putExtra("userName",userName);
                intent.putExtra("weatherStationId",weatherStationId);
                intent.putExtra("page","notification");
                startActivity(intent);
                break;
            case R.id.btn_tree:
                setBtnEnable(btnTree,"type");
                type = "tree" ;
                Log.d("NotificationsFragment","=***************=tree=========");
               //
                break;
            case R.id.btn_community:
                setBtnEnable(btCommunity,"type");
                type = "community" ;
                Log.d("NotificationsFragment","=***************=community=========");
                break;
        }
        setScrollInvisiable();
        try {
            drawChart(type);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void drawChart(String type) throws IOException {
        if(type!= null && "tree".equals(type)){
            Log.d("NotificationsFragment", "===type=============:" + type);
            try {
                TreeBean treeBean =  NetUtil.getTreeBean(collectorId);
                Log.d("NotificationsFragment", "===treeBean=============:" + treeBean);
                if(treeBean != null  && treeBean.getmTreeItemBeans()!= null){
                    List<TreeItemBean> treeItemBeans = treeBean.getmTreeItemBeans();
                    if(treeItemBeans == null){
                           return ;
                    }
                    Iterator it = treeItemBeans.iterator();
                    while(it.hasNext()){
                        TreeItemBean treeItemBean = (TreeItemBean)it.next();
                        Log.d("NotificationsFragment", "===treeItemBean" + treeItemBean);
                        Log.d("NotificationsFragment", "===treeItemBean  getTreeId====:" + treeItemBean.getTreeId());
                        Log.d("NotificationsFragment", "===treeItemBean  getCollectorId====:" + treeItemBean.getCollectorId());
                        Log.d("NotificationsFragment", "===treeItemBean  getTreeName====:" + treeItemBean.getTreeName());
                        if(treeItemBean.getTreeId()!= null){
                            CollectorItemBean.TreeDataBean treeDataBean = NetUtil.getTreeDataBean(treeItemBean.getTreeId());
                            Log.d("NotificationsFragment", "===treeDataBean====:" + treeDataBean);
                            if(treeDataBean!= null && treeDataBean.getmTreeDataItemBeans()!=null){
                                List<TreeDataItemBean> treeDataItemBeans = treeDataBean.getmTreeDataItemBeans();
                                if(treeDataItemBeans == null){
                                    return ;
                                }
                                Log.d("NotificationsFragment", "===treeDataItemBeans====:" + treeDataItemBeans);
                                Iterator its = treeDataItemBeans.iterator();
                                int i  = 1;
                                while(its.hasNext()){
                                    TreeDataItemBean treeDataItemBean = (TreeDataItemBean) its.next();
                                    if(!"test".equals(treeDataItemBean.getConfigName())){
                                        Log.d("NotificationsFragment", "===name====:" + treeDataItemBean.getConfigName().toString());
                                        List<TreeDataItemDetailBean>  treeDataItemDetailBeans = treeDataItemBean.getmTreeDataItemDetailBean();
                                        List<BarEntry>list = new ArrayList<>();//实例化一个List用来存储数据
                                        //list.add(new BarEntry(1.0f ,60.0f));
                                        /*for(int j=0;j<25;j++){
                                            Random e = new Random();
                                            list.add(new BarEntry(j ,e.nextFloat()*100));
                                        }*/
                                        if(treeDataItemDetailBeans != null){
                                            Iterator iter = treeDataItemDetailBeans.iterator();
                                            while (iter.hasNext()){
                                                TreeDataItemDetailBean treeDataItemDetailBean = (TreeDataItemDetailBean) iter.next();
                                                Log.d("NotificationsFragment", "===treeDataItemDetailBean====:" + treeDataItemDetailBean);
                                                if(treeDataItemDetailBean!=null ){
                                                    float time = Float.parseFloat(treeDataItemDetailBean.getAcquisitionTime().substring(11,13));
                                                    float value = 0.0f;
                                                    if(!"".equals(treeDataItemDetailBean.getValue())){
                                                        if(!"-".equals(treeDataItemDetailBean.getValue())) {
                                                            value = Float.parseFloat(treeDataItemDetailBean.getValue());
                                                        }
                                                        Log.d("NotificationsFragment", "===time====:" + time);
                                                        Log.d("NotificationsFragment", "===value====:" + value);
                                                        list.add(new BarEntry(time,value));
                                                    }
                                                }
                                            }
                                        }
                                        Log.d("NotificationsFragment", "===list====:" + list);
                                        Collections.reverse(list);
                                        Log.d("NotificationsFragment", "===list 倒序====:" + list);
                                        Log.d("NotificationsFragment", "===list是否为空====:" + (list!= null));
                                        //手机屏幕上显示6剩下的滑动直方图然后显示
                                        if(list!= null){
                                            float ratio = (float) list.size()/(float) 6;
                                            BarDataSet barDataSet=new BarDataSet(list,treeDataItemBean.getConfigName());
                                            barDataSet.setValueTextColor(Color.parseColor("#FFFFFFFF"));//设置显示值的文字颜色
                                            barDataSet.setValueTextSize(13f);//设置显示值的文字大小
                                            BarData barData=new BarData(barDataSet);
                                            if(i==1){
                                                tvChartname1.setText(treeDataItemBean.getConfigName());
                                                tvChartname1.setVisibility(View.VISIBLE);
                                                barChart1.setData(barData);
                                                barChart1.zoom(0,1f,0,0);
                                                //显示的时候是按照多大的比率缩放显示，1f表示不放大缩小
                                                barChart1.zoom(1/ratio,1f,0,0);
                                                setChart(ratio,barChart1,list);
                                                barChart1.setVisibility(View.VISIBLE);
                                            }
                                            if(i==2){
                                                tvChartname2.setText(treeDataItemBean.getConfigName());
                                                tvChartname2.setVisibility(View.VISIBLE);
                                                barChart2.setData(barData);
                                                barChart2.zoom(0,1f,0,0);
                                                //显示的时候是按照多大的比率缩放显示，1f表示不放大缩小
                                                barChart2.zoom(0.25f,1f,0,0);
                                                setChart(ratio,barChart2,list);
                                                barChart2.setVisibility(View.VISIBLE);
                                            }
                                            if(i==3){
                                                tvChartname3.setText(treeDataItemBean.getConfigName());
                                                tvChartname3.setVisibility(View.VISIBLE);
                                                barChart3.setData(barData);
                                                barChart3.zoom(0,1f,0,0);
                                                //显示的时候是按照多大的比率缩放显示，1f表示不放大缩小
                                                barChart3.zoom(0.25f,1f,0,0);
                                                setChart(ratio,barChart3,list);
                                                barChart3.setVisibility(View.VISIBLE);
                                            }
                                            if(i==4){
                                                tvChartname4.setText(treeDataItemBean.getConfigName());
                                                tvChartname4.setVisibility(View.VISIBLE);
                                                barChart4.setData(barData);
                                                barChart4.zoom(0,1f,0,0);
                                                //显示的时候是按照多大的比率缩放显示，1f表示不放大缩小
                                                barChart4.zoom(0.25f,1f,0,0);
                                                setChart(ratio,barChart4,list);
                                                barChart4.setVisibility(View.VISIBLE);
                                            }
                                            if(i==5){
                                                tvChartname5.setText(treeDataItemBean.getConfigName());
                                                tvChartname5.setVisibility(View.VISIBLE);
                                                barChart5.setData(barData);
                                                barChart5.zoom(0,1f,0,0);
                                                //显示的时候是按照多大的比率缩放显示，1f表示不放大缩小
                                                barChart5.zoom(0.25f,1f,0,0);
                                                setChart(ratio,barChart5,list);
                                                barChart5.setVisibility(View.VISIBLE);
                                            }
                                        }
                                        i++;
                                    }
                                }

                            }
                        }
                    }

                }
            } catch (Exception e) {
                    e.printStackTrace();
            }
        }else{
            String message = "请先选择类别";
            Toast toastCenter = Toast.makeText(getActivity().getApplicationContext(), message,Toast.LENGTH_SHORT);
            toastCenter.setGravity(Gravity.CENTER,0,0);
            toastCenter.show();
        }

    }
    /*
     * 设置BarChart的数据
     * */
    private void setChart(float ratio,BarChart barChart,List<BarEntry> list) {
        barChart.setDescription(null);                             //设置描述文字为null
        barChart.setBackgroundColor(Color.parseColor("#00000000"));  //设置背景颜色
        barChart.setDrawBarShadow(false);                          //绘制当前展示的内容顶部阴影
        barChart.setPinchZoom(false);                              //设置x轴和y轴能否同时缩放。默认否
        barChart.setMaxVisibleValueCount(10);                       //设置图表能显示的最大值，仅当setDrawValues()属性值为true时有用
        barChart.setFitBars(true);                                 //设置X轴范围两侧柱形条是否显示一半
        XAxis xAxis = barChart.getXAxis();                         //x轴
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);             //设置label在底下
        xAxis.setDrawGridLines(false);                             //不设置竖型网格线
        xAxis.setTextColor(Color.parseColor("#ffffff"));
        xAxis.setTextSize(11);
        xAxis.setDrawLabels(true);                                 //是否显示X坐标轴上的刻度，默认是true
        xAxis.setLabelCount(6,false);                //第一个参数是轴坐标的个数，第二个参数是 是否不均匀分布，true是不均匀分布
        xAxis.setAxisMinimum(0);   //X轴最小数值

        YAxis leftAxis = barChart.getAxisLeft();              //获取到y轴，分左右
        leftAxis.setLabelCount(3, true);                      //第一个参数是轴坐标的个数，第二个参数是 是否不均匀分布，true是不均匀分布
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
        barChart.zoom(4,1f,0,0);
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float v, AxisBase axis) {
                return String.valueOf((int) v+":00");
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
            buttonList.add(btnTree);
            buttonList.add(btCommunity);
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