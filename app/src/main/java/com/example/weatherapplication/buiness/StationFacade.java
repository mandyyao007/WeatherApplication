package com.example.weatherapplication.buiness;

import android.util.Log;

import com.example.weatherapplication.bean.DayReportBean;
import com.example.weatherapplication.bean.IndexBean;
import com.example.weatherapplication.bean.IndexItemsBean;
import com.example.weatherapplication.bean.ReportBean;
import com.example.weatherapplication.util.NetUtil;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class StationFacade {
    private static StationFacade _facade  = null;
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
                //Log.d("StationFacade","===indexMap==:"+ indexMap.toString());
                return  indexMap;
            }
        } catch (IOException e) {
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
                //Log.d("StationFacade","*********index=====Items==:"+item.toString());
                indexMap.put(item.getDescription().split("-")[0]+","+item.getUnit(),item.getCollectorConfigId());
            }
        }
        //Log.d("StationFacade","*********indexMap==:"+indexMap);
        return indexMap;
    }

    private String[] getIndexOfStation(IndexBean indexbean) throws IOException {
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
                //Log.d("StationFacade","======indexs =====:"+indexs[j] +"("+indexsUint[j]+")");
            }
        }
        return indexs;
    }
    public String getNewestData(String selectConfigName, String collectorId, int count) throws IOException {
        Map indexMap = initIndex(collectorId);
        String newestData = "";
        Log.d("StationFacade","======indexMap =====:"+indexMap);
        Log.d("StationFacade","======selectConfigName =====:"+selectConfigName);
        String selectConfigId = (String) indexMap.get(selectConfigName);
        //Log.d("StationFacade","======selectConfigId =====:"+selectConfigId);
        ReportBean newestRrpotData = NetUtil.getNewestData(selectConfigId, collectorId,1);
        if(newestRrpotData != null  && newestRrpotData.getmDayReportBeans() != null) {
            List<DayReportBean> dayReports = newestRrpotData.getmDayReportBeans();
            if(dayReports == null){
                return null;
            }
            Iterator iter = dayReports.iterator();
            while(iter.hasNext()){
                DayReportBean dayReportBean = (DayReportBean)iter.next();
                //Log.d("StationFacade", "===dayReportBean.getCol1()==:" + dayReportBean.getCol1());
                newestData = dayReportBean.getCol1();
                //Log.d("StationFacade", "===newestData==:" + newestData);
            }
        }
        return newestData;
    }
}
