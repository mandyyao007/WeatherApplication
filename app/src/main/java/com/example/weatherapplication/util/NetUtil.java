package com.example.weatherapplication.util;

import android.util.Log;

import com.example.weatherapplication.bean.CollectorBean;
import com.example.weatherapplication.bean.DaysDataItemBean;
import com.example.weatherapplication.bean.IndexBean;
import com.example.weatherapplication.bean.LoginBean;
import com.example.weatherapplication.bean.ReportBean;
import com.example.weatherapplication.bean.CollectorItemBean;
import com.example.weatherapplication.bean.DaysDataBean;
import com.example.weatherapplication.bean.TreeBean;
import com.example.weatherapplication.bean.WeatherStationBean;
import com.example.weatherapplication.bean.WeatherStationItemBean;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class NetUtil {
    public static final String HTTPHOST = "10.68.201.78:9000";
    // public static final String HTTPHOST = "47.98.106.23:9000";
    //public static final String HTTPHOST = "106.15.5.62:9000";
    public static final String URL_LOGIN =  "http://"+HTTPHOST+"/user/check_login";
    public static final String URL_STATION = "http://"+HTTPHOST+"/basic/show_collector_list";
    public static final String URL_INDEX = "http://"+HTTPHOST+"/basic/show_collector_config_list";
    public static final String URL_WEATHER_STATION = "http://"+HTTPHOST+"/basic/show_weather_station_list";
    public static final String URL_REPORT_DATA = "http://"+HTTPHOST+"/basic/show_report_data_list";
    public static final String URL_TODAY_DATA = "http://"+HTTPHOST+"/basic/show_CDV_data_list";
    public static final String URL_TREE = "http://"+HTTPHOST+"/basic/show_tree_list";
    public static final String URL_TREE_DATA = "http://"+HTTPHOST+"/basic/show_tree_data_list";
    public static final String URL_DAYS_DATA = "http://"+HTTPHOST+"/basic/show_CDV_data_by_type_list";
    public static String service(String urlStr,String method) throws IOException {
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

    public static String getloginInfo(String userName,String password) throws IOException {
        String result ="";
        String status = "";
        String loginUrl = URL_LOGIN+"?password="+password+"&userName="+userName;  //拼接出URL
        Log.d("fan","-----loginUrl======"+loginUrl);
        try{
            result = service(loginUrl,"POST");
            //Log.d("fan","-----result======"+result);
            Gson gson = new Gson();
            LoginBean loginBean = gson.fromJson(result, LoginBean.class);
            //Log.d("fan","====解析后的loginBean==:"+loginBean.toString());
            status =  loginBean.getStatus();
        }catch (IOException e) {
            e.printStackTrace();
        }
        return status;
    }
    public static  List<WeatherStationItemBean>  getWeatherStationItemInfo(String userName) throws IOException {
        List<WeatherStationItemBean> weatherStationItem = null;
        try{
            WeatherStationBean weatherStationBean = getWearherStationInfo(userName);
            weatherStationItem = weatherStationBean.getmItemBeans();
            Log.d("fan","-----weatherStationItem======"+weatherStationItem);
        }catch (IOException e) {
            e.printStackTrace();
        }
        return weatherStationItem;
    }
    public static WeatherStationBean getWearherStationInfo(String userName) throws IOException {
        String result ="";
        //拼接出URL
        String weatherStationUrl = URL_WEATHER_STATION+"?userName="+userName;
        Log.d("fan","-----weatherStationUrl======"+weatherStationUrl);
        WeatherStationBean weatherStationBean = null;
        try{
            result = service(weatherStationUrl,"POST");
            Log.d("fan","-----result======"+result);
            Gson gson = new Gson();
            weatherStationBean = gson.fromJson(result, WeatherStationBean.class);
            Log.d("fan","====weatherStationBean==:"+weatherStationBean.toString());
        }catch (IOException e) {
            e.printStackTrace();
        }
        return weatherStationBean;
    }
    public static CollectorBean getStationInfo(String userName, String weatherStationId) throws IOException {
        String result ="";
        //拼接出URL
        String stationUrl = URL_STATION+"?userName="+userName+"&weatherStationId=" +weatherStationId;
        Log.d("fan","-----stationUrl======"+stationUrl);
        CollectorBean collectorBean = null;
        try{
            result = service(stationUrl,"POST");
            Gson gson = new Gson();
            collectorBean = gson.fromJson(result, CollectorBean.class);
        }catch (IOException e) {
            e.printStackTrace();
        }
        return collectorBean;
    }
    public static  List<CollectorItemBean>  getStationItemInfo(String userName, String weatherStationId) throws IOException {
        List<CollectorItemBean> stationItems = null;
        try{
            CollectorBean collectorBean = getStationInfo(userName,weatherStationId);
            stationItems = collectorBean.getmItemBeans();
            Log.d("fan","-----stationItems======"+stationItems);
        }catch (IOException e) {
            e.printStackTrace();
        }
        return stationItems;
    }

    public static IndexBean getIndexInfo(String collectorId) throws IOException {
        String result ="";
        IndexBean indexBean = null;
        //拼接出URL
        String indexUrl = URL_INDEX+"?collectorId="+collectorId;
        Log.d("fan","-----indexUrl======"+indexUrl);
        try{
            result = service(indexUrl,"POST");
            Gson gson = new Gson();
            indexBean = gson.fromJson(result, IndexBean.class);
        }catch (IOException e) {
            e.printStackTrace();
        }
        return indexBean;
    }
    public static ReportBean getReportInfo(String collectorConfigId, String collectorId,String startDateStr, String endDateStr) throws IOException {
        String result ="";
        ReportBean reportBean = null;
        //拼接出URL
        String reportUrl = URL_REPORT_DATA+"?collectorConfigId="+collectorConfigId+"&collectorId="+collectorId+"&endDate="+endDateStr+"&startDate="+startDateStr;
        Log.d("fan","-----reportUrl======"+reportUrl);
        try{
            result = service(reportUrl,"POST");
            Gson gson = new Gson();
            reportBean = gson.fromJson(result, ReportBean.class);
            Log.d("fan","====解析后的reportBean==:"+reportBean.toString());
        }catch (IOException e) {
            e.printStackTrace();
        }
        return reportBean;
    }

    public static String getReportOfIndex(String selectConfigId, String stationId, String startDateStr, String endDateStr) throws IOException {
        String reportResult ="";
        //拼接出URL
        String reportUrl = URL_REPORT_DATA+"?collectorConfigId="+selectConfigId+"&collectorId="+stationId+"&endDate="+endDateStr+"&startDate="+startDateStr;
        Log.d("fan","-----reportUrl======"+reportUrl);
        try{
            reportResult = service(reportUrl,"POST");
        }catch (IOException e) {
            e.printStackTrace();
        }
        return reportResult;
    }
    public static ReportBean getReportDataOfIndex(String selectConfigId, String stationId, String startDateStr, String endDateStr) throws IOException {
        ReportBean reportBean = null;
        try{
            String reportResult = getReportOfIndex(selectConfigId,stationId,startDateStr,endDateStr);
            Log.d("fan","-----reportResult======"+reportResult);
            Gson gson = new Gson();
            reportBean = gson.fromJson(reportResult, ReportBean.class);
            Log.d("fan","====解析后的reportBean==:"+reportBean.toString());
        }catch (IOException e) {
            e.printStackTrace();
        }
        return reportBean;
    }

    public static ReportBean getNewestData(String selectConfigId, String stationId, int count) throws IOException {
        String dataResult ="";
        ReportBean reportBean = null;
        //拼接出URL
        String newestDataUrl = URL_TODAY_DATA+"?collectorConfigId="+selectConfigId+"&collectorId="+stationId+"&count="+count;
        Log.d("fan","-----newestDataUrl======"+newestDataUrl);
        try{
            dataResult = service(newestDataUrl,"POST");
            Log.d("fan","-----getNewestData  result======"+dataResult);
            Gson gson = new Gson();
            reportBean = gson.fromJson(dataResult, ReportBean.class);
            Log.d("fan","====getNewestData====解析后的reportBean==:"+reportBean.toString());
        }catch (IOException e) {
            e.printStackTrace();
        }
        return reportBean;
    }
    public static TreeBean getTreeBean(String collectorId) throws IOException {
        String dataResult ="";
        //拼接出URL
        String treeUrl = URL_TREE+"?collectorId="+collectorId;
        Log.d("fan","-----getTreeBean======"+treeUrl);
        TreeBean treeBean = null;
        try{
            dataResult = service(treeUrl,"POST");
            Log.d("fan","-----getTreeBean  result======"+dataResult);
            Gson gson = new Gson();
            treeBean = gson.fromJson(dataResult, TreeBean.class);
            Log.d("fan","====getTreeBean====解析后的treeBean==:"+ treeBean.toString());
        }catch (IOException e) {
            e.printStackTrace();
        }
        return treeBean;
    }
    public static CollectorItemBean.TreeDataBean getTreeDataBean(String treeId,int days) throws IOException {
        String dataResult ="";
        //拼接出URL
        String treeDataUrl = URL_TREE_DATA+"?day="+days+"&treeId="+treeId;
        Log.d("fan","-----getTreeDataBean====="+treeDataUrl);
        CollectorItemBean.TreeDataBean treeDataBean = null;
        try{
            dataResult = service(treeDataUrl,"POST");
            Log.d("fan","-----getTreeDataBean  result======"+dataResult);
            Gson gson = new Gson();
            treeDataBean = gson.fromJson(dataResult, CollectorItemBean.TreeDataBean.class);
            Log.d("fan","====getTreeDataBean====解析后的treeDataBean==:"+ treeDataBean.toString());
        }catch (IOException e) {
            e.printStackTrace();
        }
        return treeDataBean;
    }
    public static List<DaysDataItemBean> getDaysData( String stationId,String configType, int day) throws IOException {
        String dataResult ="";
        //拼接出URL
        String daysDataUrl = URL_DAYS_DATA+"?collectorId="+stationId+"&configType="+configType+"&day="+day;;
        Log.d("fan","-----daysDataUrl======"+daysDataUrl);
        List<DaysDataItemBean> daysDataItemBeans = null;
        try{
            dataResult = service(daysDataUrl,"POST");
            Log.d("fan","-----getDaysData  result======"+dataResult);
            Gson gson = new Gson();
            DaysDataBean daysDataBean = gson.fromJson(dataResult, DaysDataBean.class);
            Log.d("fan","====getDaysData====DaysDataBean==:"+daysDataBean.toString());
            if(daysDataBean!= null && daysDataBean.getmItemBeans()!= null){
                daysDataItemBeans =  daysDataBean.getmItemBeans();

            }else{
                return null;
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
        return daysDataItemBeans;
    }
}
