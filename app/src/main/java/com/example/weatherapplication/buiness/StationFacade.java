package com.example.weatherapplication.buiness;

import android.util.Log;

import com.example.weatherapplication.bean.CarbonItemBean;
import com.example.weatherapplication.bean.CarbonItemDataBean;
import com.example.weatherapplication.bean.DayReportBean;
import com.example.weatherapplication.bean.IndexBean;
import com.example.weatherapplication.bean.IndexItemsBean;
import com.example.weatherapplication.bean.ReportBean;
import com.example.weatherapplication.util.NetUtil;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class StationFacade {
    private static StationFacade _facade  = null;
    private static final String TAG = "StationFacade";
    public static StationFacade getInstance() {
        if (_facade == null) {
            _facade = new StationFacade();
        }
        return _facade;
    }
    public Map initIndex(String collectorId) {
        try {
            Map indexMap = new LinkedHashMap();
            if(collectorId!= null){
                IndexBean indexbean = NetUtil.getIndexInfo(collectorId);
                //String[] mIndexs  =  getIndexOfStation(indexbean);
                indexMap =  getIndexOfStationMap(indexbean);
                //Log.d(TAG,"===indexMap==:"+ indexMap.toString());
                return  indexMap;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
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
                //Log.d(TAG,"*********index=====Items==:"+item.toString());
                indexMap.put(item.getDescription().split("-")[0]+","+item.getUnit(),item.getCollectorConfigId());
            }
        }
        //Log.d(TAG,"*********indexMap==:"+indexMap);
        return indexMap;
    }

    private String[] getIndexOfStation(IndexBean indexbean) throws Exception {
        List<IndexItemsBean> indexItems = indexbean.getmIndexItemBeans();
        int count = indexbean.getTotal();
        String[] indexs = new String[count];
        String[] indexsUint = new String[count];
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
                //Log.d(TAG,"======indexs =====:"+indexs[j] +"("+indexsUint[j]+")");
            }
        }
        return indexs;
    }
    public String getNewestData(String selectConfigName, String collectorId, int count) throws Exception {
        Map indexMap = initIndex(collectorId);
        String newestData = "";
        //Log.d(TAG,"======indexMap =====:"+indexMap);
        //Log.d(TAG,"======selectConfigName =====:"+selectConfigName);
        String selectConfigId = (String) indexMap.get(selectConfigName);
        //Log.d(TAG,"======selectConfigId =====:"+selectConfigId);
        ReportBean newestRrpotData = NetUtil.getNewestData(selectConfigId, collectorId,1);
        if(newestRrpotData != null  && newestRrpotData.getmDayReportBeans() != null) {
            List<DayReportBean> dayReports = newestRrpotData.getmDayReportBeans();
            if(dayReports == null){
                return null;
            }
            Iterator iter = dayReports.iterator();
            while(iter.hasNext()){
                DayReportBean dayReportBean = (DayReportBean)iter.next();
                //Log.d(TAG, "===dayReportBean.getCol1()==:" + dayReportBean.getCol1());
                newestData = dayReportBean.getCol1();
                //Log.d(TAG, "===newestData==:" + newestData);
            }
        }
        return newestData;
    }

    public String getCarbonData(String communityId) throws Exception {
        String carbonData = "";
        Log.d(TAG,"======communityId =====:"+communityId);
        List<CarbonItemBean> carbonItemBeans = NetUtil.getCarbonData(communityId);
        Log.d(TAG,"*************8888getCarbonData =====:"+carbonItemBeans);
        if(carbonItemBeans !=null){
            Iterator<CarbonItemBean> iter = carbonItemBeans.iterator();
            if(iter.hasNext()){
              CarbonItemBean carbonItemBean = iter.next();
                Log.d(TAG,"*************8888carbonItemBean=====:"+carbonItemBean);
                List<CarbonItemDataBean> carbonItemDataBeanList = carbonItemBean.getmCarbonItemDataBean();
                Log.d(TAG,"*************8888carbonItemDataBeanList=====:"+carbonItemDataBeanList);
                Iterator<CarbonItemDataBean> iterator = carbonItemDataBeanList.iterator();
                if(iterator.hasNext()){
                    iterator.next();
                    CarbonItemDataBean carbonItemDataBean = iterator.next();
                    Log.d(TAG,"*************8888carbonItemDataBean=====:"+carbonItemDataBean);
                    carbonData = carbonItemDataBean.getValue();
                    Log.d(TAG,"*************8888carbonData=====:"+carbonData);
                }
            }
        }
        return carbonData;
    }
}
