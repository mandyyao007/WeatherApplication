package com.example.weatherapplication.util;

import android.util.Log;

import com.example.weatherapplication.bean.CarbonBean;
import com.example.weatherapplication.bean.CarbonItemBean;
import com.example.weatherapplication.bean.CarbonItemDataBean;
import com.example.weatherapplication.bean.CollectorBean;
import com.example.weatherapplication.bean.CommunityBean;
import com.example.weatherapplication.bean.DaysDataItemBean;
import com.example.weatherapplication.bean.IndexBean;
import com.example.weatherapplication.bean.LoginBean;
import com.example.weatherapplication.bean.ReportBean;
import com.example.weatherapplication.bean.CollectorItemBean;
import com.example.weatherapplication.bean.DaysDataBean;
import com.example.weatherapplication.bean.TreeAndCommunityDataBean;
import com.example.weatherapplication.bean.TreeBean;
import com.example.weatherapplication.bean.WeatherStationBean;
import com.example.weatherapplication.bean.WeatherStationItemBean;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;

public class NetUtil {
    private static final String TAG = "NetUtil";
    //public static final String HTTPHOST = "10.68.201.78:9000";
    // public static final String HTTPHOST = "47.98.106.23:9000";
    public static final String HTTPHOST = "106.15.5.62:9000";
    public static final String URL_LOGIN =  "http://"+HTTPHOST+"/user/check_login";
    public static final String URL_STATION = "http://"+HTTPHOST+"/basic/show_collector_list";
    public static final String URL_INDEX = "http://"+HTTPHOST+"/basic/show_collector_config_list";
    public static final String URL_WEATHER_STATION = "http://"+HTTPHOST+"/basic/show_weather_station_list";
    public static final String URL_REPORT_DATA = "http://"+HTTPHOST+"/basic/show_report_data_list";
    public static final String URL_TODAY_DATA = "http://"+HTTPHOST+"/basic/show_CDV_data_list";
    public static final String URL_TREE = "http://"+HTTPHOST+"/basic/show_tree_list";
    public static final String URL_TREE_DATA = "http://"+HTTPHOST+"/basic/show_tree_data_list_by_group";
    public static final String URL_DAYS_DATA = "http://"+HTTPHOST+"/basic/show_CDV_data_by_type_list";
    public static final String URL_COMMNUITY = "http://"+HTTPHOST+"/basic/show_community_list";
    public static final String URL_COMMNUITY_DATA = "http://"+HTTPHOST+"/basic/show_community_data_list";
    public static final String URL_CARBON_DATA = "http://"+HTTPHOST+"/basic/show_community_c_data_list";
    public static String service(String urlStr,String method) throws  Exception {
        String result = "";
        HttpURLConnection connection = null;
        InputStreamReader inputStreamReader = null;

        BufferedReader bufferedReader = null;
        //连接网络
        try {
            URL url = new URL(urlStr);
            connection =(HttpURLConnection) url.openConnection();
            connection.setRequestMethod(method);
            connection.setConnectTimeout(5000);
            //从连接中读取数据(二进制数据)
            InputStream inputStream = connection.getInputStream();
            inputStreamReader = new InputStreamReader(inputStream);
            //二进制流送入缓冲区
            bufferedReader = new BufferedReader(inputStreamReader);
            //从缓冲区一行行读取字符串
            StringBuilder stringBuilder = new StringBuilder();
            String line = "";
            while( (line = bufferedReader.readLine())!= null ){
              stringBuilder.append(line);
            }
         result = stringBuilder.toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            if(connection != null){
                connection.disconnect();
            }
            if(inputStreamReader != null){
                inputStreamReader.close();
            }
            if(bufferedReader!= null){
                bufferedReader.close();            }
        }

        return result;
    }

    public static String getloginInfo(String userName,String password) throws  Exception{
        String result ="";
        String status = "";
        String loginUrl = URL_LOGIN+"?password="+password+"&userName="+userName;  //拼接出URL
        //Log.d(TAG,"-----loginUrl======"+loginUrl);
        try{
            result = service(loginUrl,"POST");
            Log.d(TAG,"-----result======"+result);
            Gson gson = new Gson();
            Log.d(TAG,"-----result  null======"+(result == null));
            Log.d(TAG,"-----result======"+(!"".equals(result)));
            if(result == null || !"".equals(result)){
                LoginBean loginBean = gson.fromJson(result, LoginBean.class);
                Log.d(TAG,"====解析后的loginBean==:"+loginBean.toString());
                status =  loginBean.getStatus();
            }
        }catch(Exception e){
            Log.d(TAG,"====Exception==:"+e.getMessage());
            e.printStackTrace();
        }
        return status;
    }
    public static  List<WeatherStationItemBean>  getWeatherStationItemInfo(String userName) throws  Exception{
        List<WeatherStationItemBean> weatherStationItem = null;
        try{
            WeatherStationBean weatherStationBean = getWearherStationInfo(userName);
            weatherStationItem = weatherStationBean.getmItemBeans();
           // Log.d(TAG,"-----weatherStationItem======"+weatherStationItem);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return weatherStationItem;
    }
    public static WeatherStationBean getWearherStationInfo(String userName) throws  Exception{
        String result ="";
        //拼接出URL
        String weatherStationUrl = URL_WEATHER_STATION+"?userName="+userName;
       // Log.d(TAG,"-----weatherStationUrl======"+weatherStationUrl);
        WeatherStationBean weatherStationBean = null;
        try{
            result = service(weatherStationUrl,"POST");
           // Log.d(TAG,"-----result======"+result);
            Gson gson = new Gson();
            weatherStationBean = gson.fromJson(result, WeatherStationBean.class);
           // Log.d(TAG,"====weatherStationBean==:"+weatherStationBean.toString());
        }catch (Exception e) {
            e.printStackTrace();
        }
        return weatherStationBean;
    }
    public static CollectorBean getStationInfo(String userName, String weatherStationId) throws  Exception{
        String result ="";
        //拼接出URL
        String stationUrl = URL_STATION+"?userName="+userName+"&weatherStationId=" +weatherStationId;
       // Log.d(TAG,"-----stationUrl======"+stationUrl);
        CollectorBean collectorBean = null;
        try{
            result = service(stationUrl,"POST");
            Gson gson = new Gson();
            collectorBean = gson.fromJson(result, CollectorBean.class);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return collectorBean;
    }
    public static  List<CollectorItemBean>  getStationItemInfo(String userName, String weatherStationId) throws  Exception{
        List<CollectorItemBean> stationItems = null;
        try{
            CollectorBean collectorBean = getStationInfo(userName,weatherStationId);
            stationItems = collectorBean.getmItemBeans();
            //Log.d(TAG,"-----stationItems======"+stationItems);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return stationItems;
    }

    public static IndexBean getIndexInfo(String collectorId) throws  Exception {
        String result ="";
        IndexBean indexBean = null;
        //拼接出URL
        String indexUrl = URL_INDEX+"?collectorId="+collectorId;
        //Log.d(TAG,"-----indexUrl======"+indexUrl);
        try{
            result = service(indexUrl,"POST");
            //Log.d(TAG,"-----indexUrl===result==="+result);
            Gson gson = new Gson();
            indexBean = gson.fromJson(result, IndexBean.class);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return indexBean;
    }
    public static ReportBean getReportInfo(String collectorConfigId, String collectorId,String startDateStr, String endDateStr) throws  Exception {
        String result ="";
        ReportBean reportBean = null;
        //拼接出URL
        String reportUrl = URL_REPORT_DATA+"?collectorConfigId="+collectorConfigId+"&collectorId="+collectorId+"&endDate="+endDateStr+"&startDate="+startDateStr;
        //Log.d(TAG,"-----reportUrl======"+reportUrl);
        try{
            result = service(reportUrl,"POST");
            Gson gson = new Gson();
            reportBean = gson.fromJson(result, ReportBean.class);
           // Log.d(TAG,"====解析后的reportBean==:"+reportBean.toString());
        }catch (Exception e) {
            e.printStackTrace();
        }
        return reportBean;
    }

    public static String getReportOfIndex(String selectConfigId, String stationId, String startDateStr, String endDateStr) throws  Exception {
        String reportResult ="";
        //拼接出URL
        String reportUrl = URL_REPORT_DATA+"?collectorConfigId="+selectConfigId+"&collectorId="+stationId+"&endDate="+endDateStr+"&startDate="+startDateStr;
       // Log.d(TAG,"-----reportUrl======"+reportUrl);
        try{
            reportResult = service(reportUrl,"POST");
        }catch (Exception e) {
            e.printStackTrace();
        }
        return reportResult;
    }
    public static ReportBean getReportDataOfIndex(String selectConfigId, String stationId, String startDateStr, String endDateStr) throws  Exception {
        ReportBean reportBean = null;
        try{
            String reportResult = getReportOfIndex(selectConfigId,stationId,startDateStr,endDateStr);
            //Log.d(TAG,"-----reportResult======"+reportResult);
            Gson gson = new Gson();
            reportBean = gson.fromJson(reportResult, ReportBean.class);
            //Log.d(TAG,"====解析后的reportBean==:"+reportBean.toString());
        }catch (Exception e) {
            e.printStackTrace();
        }
        return reportBean;
    }

    public static ReportBean getNewestData(String selectConfigId, String stationId, int count) throws  Exception {
        String dataResult ="";
        ReportBean reportBean = null;
        //拼接出URL
        String newestDataUrl = URL_TODAY_DATA+"?collectorConfigId="+selectConfigId+"&collectorId="+stationId+"&count="+count;
        Log.d(TAG,"-----newestDataUrl======"+newestDataUrl);
        try{
            dataResult = service(newestDataUrl,"POST");
            Log.d(TAG,"-----getNewestData  result======"+dataResult);
            Gson gson = new Gson();
            reportBean = gson.fromJson(dataResult, ReportBean.class);
           // Log.d(TAG,"====getNewestData====解析后的reportBean==:"+reportBean.toString());
        }catch (Exception e) {
            e.printStackTrace();
        }
        return reportBean;
    }
    public static TreeBean getTreeBean(String collectorId) throws  Exception{
        String dataResult ="";
        //拼接出URL
        String treeUrl = URL_TREE+"?weatherStationId="+collectorId;
        Log.d(TAG,"-----getTreeBean======"+treeUrl);
        TreeBean treeBean = null;
        try{
            dataResult = service(treeUrl,"POST");
            Log.d(TAG,"-----getTreeBean  result======"+dataResult);
            Gson gson = new Gson();
            treeBean = gson.fromJson(dataResult, TreeBean.class);
            Log.d(TAG,"====getTreeBean====解析后的treeBean==:"+ treeBean.toString());
        }catch (Exception e) {
            e.printStackTrace();
        }
        return treeBean;
    }
    public static TreeAndCommunityDataBean getTreeDataBean(String treeId, int days) throws Exception {
        String dataResult ="";
        //拼接出URL
        String treeDataUrl = URL_TREE_DATA+"?day="+days+"&treeId="+treeId;
        Log.d(TAG,"-----getTreeDataBean====="+treeDataUrl);
        TreeAndCommunityDataBean treeDataBean = null;
        try{
             dataResult = service(treeDataUrl,"POST");
//            dataResult = "{\"total\":3,\"rows\":[{\"mainConfigId\":\"61\",\"treeDataList\":[{\"configName\":\"树株1水文调节\",\"dataList\":[{\"acquisitionTime\":\"2022-10-06 00:00:00\",\"val\":\"21\"},{\"acquisitionTime\":\"2022-10-05 00:00:00\",\"val\":\"24\"},{\"acquisitionTime\":\"2022-10-04 00:00:00\",\"val\":\"38\"},{\"acquisitionTime\":\"2022-10-03 00:00:00\",\"val\":\"45\"},{\"acquisitionTime\":\"2022-10-02 00:00:00\",\"val\":\"26\"},{\"acquisitionTime\":\"2022-10-01 00:00:00\",\"val\":\"32\"}],\"groupId\":\"62\",\"mainConfigId\":\"61\",\"treeConfigId\":\"61\",\"unit\":\"ml\"},\n" +
//                    "{\"configName\":\"树株1水文调节价值\",\"dataList\":[{\"acquisitionTime\":\"2022-10-06 00:00:00\",\"val\":\"111\"},{\"acquisitionTime\":\"2022-10-05 00:00:00\",\"val\":\"213\"},{\"acquisitionTime\":\"2022-10-04 00:00:00\",\"val\":\"341\"},{\"acquisitionTime\":\"2022-10-03 00:00:00\",\"val\":\"452\"},{\"acquisitionTime\":\"2022-10-02 00:00:00\",\"val\":\"432\"},{\"acquisitionTime\":\"2022-10-01 00:00:00\",\"val\":\"145\"}],\"groupId\":\"61\",\"mainConfigId\":\"61\",\"treeConfigId\":\"62\",\"unit\":\"yuan\"}\n" +
//                    "]},{\"mainConfigId\":\"67\",\"treeDataList\":[{\"configName\":\"树株1滞纳PM10\",\"dataList\":[{\"acquisitionTime\":\"2022-10-06 00:00:00\",\"val\":\"3\"},{\"acquisitionTime\":\"2022-10-05 00:00:00\",\"val\":\"14\"},{\"acquisitionTime\":\"2022-10-04 00:00:00\",\"val\":\"25\"},{\"acquisitionTime\":\"2022-10-03 00:00:00\",\"val\":\"17\"},{\"acquisitionTime\":\"2022-10-02 00:00:00\",\"val\":\"14\"},{\"acquisitionTime\":\"2022-10-01 00:00:00\",\"val\":\"54\"}],\"groupId\":\"71\",\"mainConfigId\":\"67\",\"treeConfigId\":\"67\",\"unit\":\"g-m-2\"},\n"+
//                    "{\"configName\":\"树株1滞纳PM10价值\",\"dataList\":[{\"acquisitionTime\":\"2022-10-06 00:00:00\",\"val\":\"100\"},{\"acquisitionTime\":\"2022-10-05 00:00:00\",\"val\":\"156\"},{\"acquisitionTime\":\"2022-10-04 00:00:00\",\"val\":\"321\"},{\"acquisitionTime\":\"2022-10-03 00:00:00\",\"val\":\"134\"},{\"acquisitionTime\":\"2022-10-02 00:00:00\",\"val\":\"236\"},{\"acquisitionTime\":\"2022-10-01 00:00:00\",\"val\":\"287\"}],\"groupId\":\"67\",\"mainConfigId\":\"67\",\"treeConfigId\":\"71\",\"unit\":\"yuan\"}]}]}";
            Log.d(TAG,"-----getTreeDataBean  result======"+dataResult);
            Gson gson = new Gson();
            treeDataBean = gson.fromJson(dataResult, TreeAndCommunityDataBean.class);
            Log.d(TAG,"====getTreeDataBean====解析后的treeDataBean==:"+ treeDataBean.toString());
        }catch (Exception e) {
            e.printStackTrace();
        }
        return treeDataBean;
    }
    public static List<DaysDataItemBean> getDaysData( String stationId,String configType, int day) throws  Exception {
        String dataResult ="";
        //拼接出URL
        String daysDataUrl = URL_DAYS_DATA+"?collectorId="+stationId+"&configType="+configType+"&day="+day;;
        Log.d(TAG,"-----daysDataUrl======"+daysDataUrl);
        List<DaysDataItemBean> daysDataItemBeans = null;
        try{
            dataResult = service(daysDataUrl,"POST");
            Log.d(TAG,"-----getDaysData  result======"+dataResult);
            Gson gson = new Gson();
            DaysDataBean daysDataBean = gson.fromJson(dataResult, DaysDataBean.class);
            Log.d(TAG,"====getDaysData====DaysDataBean==:"+daysDataBean.toString());
            if(daysDataBean!= null && daysDataBean.getmItemBeans()!= null){
                daysDataItemBeans =  daysDataBean.getmItemBeans();

            }else{
                return null;
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        Log.d(TAG,"====getDaysData====daysDataItemBeans==:"+daysDataItemBeans);
        return daysDataItemBeans;
    }

    public static CommunityBean getCommunityBean(String weatherStationId) throws Exception {
        String dataResult ="";
        //拼接出URL
        String communityUrl = URL_COMMNUITY+"?weatherStationId="+weatherStationId;
        Log.d(TAG,"-----getCommunityBean======"+communityUrl);
        CommunityBean communityBean = null;
        try{
            dataResult = service(communityUrl,"POST");
            Log.d(TAG,"-----getCommunityBean  result======"+dataResult);
            Gson gson = new Gson();
            communityBean = gson.fromJson(dataResult, CommunityBean.class);
            Log.d(TAG,"====getCommunityBean====communityBean==:"+ communityBean.toString());
        }catch (Exception e) {
            e.printStackTrace();
        }
        return communityBean;
    }

    public static TreeAndCommunityDataBean getCommunityDataBean(String communityId, int day) throws Exception {
        String dataResult ="";
        //拼接出URL
        String communityDataUrl = URL_COMMNUITY_DATA+"?communityId="+communityId+"&day="+day;
        Log.d(TAG,"-----getCommunityDataBean====="+communityDataUrl);
        TreeAndCommunityDataBean communityDataBean = null;
        try{
            dataResult = service(communityDataUrl,"POST");
            Log.d(TAG,"-----getCommunityDataBean  result======"+dataResult);
            Gson gson = new Gson();
            communityDataBean = gson.fromJson(dataResult, TreeAndCommunityDataBean.class);
            Log.d(TAG,"====getCommunityDataBean====解析后的communityDataBean==:"+ communityDataBean.toString());
        }catch (Exception e) {
            e.printStackTrace();
        }
        return communityDataBean;
    }

    public static List<CarbonItemBean> getCarbonData(String communityId) throws Exception {
        String dataResult = "";
        List<CarbonItemBean> carbonItemBeans = null;
        //拼接出URL
        String newestDataUrl = URL_CARBON_DATA;
        try {
            dataResult = service(newestDataUrl, "POST");
            Log.d(TAG, "-----getCarbonData  result======" + dataResult);
            Gson gson = new Gson();
            CarbonBean carbonBean = gson.fromJson(dataResult, CarbonBean.class);
            Log.d(TAG, "====getCarbonData====解析后的carbonBean==:" + carbonBean.toString());
            if (carbonBean != null && carbonBean.getmCarbonItemBeans() != null) {
                carbonItemBeans = carbonBean.getmCarbonItemBeans();
                Log.d(TAG, "====getCarbonData====carbonItemBeans==:" + carbonItemBeans);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return carbonItemBeans;
    }
}
