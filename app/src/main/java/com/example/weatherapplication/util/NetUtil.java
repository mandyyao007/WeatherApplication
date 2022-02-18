package com.example.weatherapplication.util;

import android.util.Log;

import com.example.weatherapplication.bean.IndexBean;
import com.example.weatherapplication.bean.LoginBean;
import com.example.weatherapplication.bean.ReportBean;
import com.example.weatherapplication.bean.StationBean;
import com.example.weatherapplication.bean.StationItemBean;
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
    //public static final String HTTPHOST = "10.68.201.78:9000";
    public static final String HTTPHOST = "47.98.106.23:9000";
    public static final String URL_LOGIN =  "http://"+HTTPHOST+"/user/check_login";//"http://47.98.106.23:9000/user/check_login";
    public static final String URL_STATION = "http://"+HTTPHOST+"/basic/show_collector_list";//"http://47.98.106.23:9000/basic/show_collector_list";
    public static final String URL_INDEX = "http://"+HTTPHOST+"/basic/show_collector_config_list";;//"http://47.98.106.23:9000/basic/show_collector_config_list";
    public static final String URL_WEATHER_STATION = "http://"+HTTPHOST+"/basic/show_weather_station_list";//"http://47.98.106.23:9000/basic/show_collector_list";
    public static final String URL_REPORT_DATA = "http://"+HTTPHOST+"/basic/show_report_data_list"; //"http://47.98.106.23:9000/basic/show_report_data_list";
    public static final String URL_TODAY_DATA = "http://"+HTTPHOST+"/basic/show_CDV_data_list"; //"http://47.98.106.23:9000/basic/show_report_data_list";
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
        //拼接出URL
        String loginUrl = URL_LOGIN+"?password="+password+"&userName="+userName;
        Log.d("fan","-----loginUrl======"+loginUrl);
        result = service(loginUrl,"POST");
        //Log.d("fan","-----result======"+result);
        Gson gson = new Gson();
        LoginBean loginBean = gson.fromJson(result, LoginBean.class);
        //Log.d("fan","====解析后的loginBean==:"+loginBean.toString());
        String status =  loginBean.getStatus();
        return status;

    }
    public static  List<WeatherStationItemBean>  getWeatherStationItemInfo(String userName) throws IOException {
        WeatherStationBean weatherStationBean = getWearherStationInfo(userName);
        List<WeatherStationItemBean> weatherStationItem = weatherStationBean.getmItemBeans();
        Log.d("fan","-----weatherStationItem======"+weatherStationItem);
        return weatherStationItem;
    }
    public static WeatherStationBean getWearherStationInfo(String userName) throws IOException {
        String result ="";
        //拼接出URL
        String weatherStationUrl = URL_WEATHER_STATION+"?userName="+userName;
        Log.d("fan","-----weatherStationUrl======"+weatherStationUrl);
        result = service(weatherStationUrl,"POST");
        Log.d("fan","-----result======"+result);
        Gson gson = new Gson();
        WeatherStationBean weatherStationBean = gson.fromJson(result, WeatherStationBean.class);
        Log.d("fan","====weatherStationBean==:"+weatherStationBean.toString());
        return weatherStationBean;
    }
    public static StationBean getStationInfo(String userName,String weatherStationId) throws IOException {
        String result ="";
        //拼接出URL
        String stationUrl = URL_STATION+"?userName="+userName+"&weatherStationId=" +weatherStationId;
        Log.d("fan","-----stationUrl======"+stationUrl);
        result = service(stationUrl,"POST");
        //Log.d("fan","-----result======"+result);
        Gson gson = new Gson();
        StationBean stationBean = gson.fromJson(result, StationBean.class);
        //Log.d("fan","====解析后的stationBean==:"+stationBean.toString());
        return stationBean;
    }
    public static  List<StationItemBean>  getStationItemInfo(String userName,String weatherStationId) throws IOException {
        StationBean stationBean = getStationInfo(userName,weatherStationId);
        List<StationItemBean> stationItems = stationBean.getmItemBeans();
        Log.d("fan","-----stationItems======"+stationItems);
        return stationItems;
    }

    public static IndexBean getIndexInfo(String collectorId) throws IOException {
        String result ="";
        //拼接出URL
        String indexUrl = URL_INDEX+"?collectorId="+collectorId;
        //Log.d("fan","-----indexUrl======"+indexUrl);
        result = service(indexUrl,"POST");
       // Log.d("fan","-----indexBean  result======"+result);
        Gson gson = new Gson();
        IndexBean indexBean = gson.fromJson(result, IndexBean.class);
        //Log.d("fan","====解析后的indexBean==:"+indexBean.toString());
        return indexBean;

    }
    public static ReportBean getReportInfo(String collectorConfigId, String collectorId,String startDateStr, String endDateStr) throws IOException {
        String result ="";
        //拼接出URL
        String reportUrl = URL_REPORT_DATA+"?collectorConfigId="+collectorConfigId+"&collectorId="+collectorId+"&endDate="+endDateStr+"&startDate="+startDateStr;
        Log.d("fan","-----reportUrl======"+reportUrl);
        result = service(reportUrl,"POST");
        //Log.d("fan","-----reportBean  result======"+result);
        Gson gson = new Gson();
        ReportBean reportBean = gson.fromJson(result, ReportBean.class);
        Log.d("fan","====解析后的reportBean==:"+reportBean.toString());
        return reportBean;

    }

    public static String getReportOfIndex(String selectConfigId, String stationId, String startDateStr, String endDateStr) throws IOException {
        String reportResult ="";
        //拼接出URL
        String reportUrl = URL_REPORT_DATA+"?collectorConfigId="+selectConfigId+"&collectorId="+stationId+"&endDate="+endDateStr+"&startDate="+startDateStr;
        Log.d("fan","-----reportUrl======"+reportUrl);
        reportResult = service(reportUrl,"POST");
//        Log.d("fan","-----reportBean  result======"+result);
//        Gson gson = new Gson();
//        ReportBean reportBean = gson.fromJson(result, ReportBean.class);
//        Log.d("fan","====解析后的reportBean==:"+reportBean.toString());
        return reportResult;
    }
    public static ReportBean getReportDataOfIndex(String selectConfigId, String stationId, String startDateStr, String endDateStr) throws IOException {
        String reportResult = getReportOfIndex(selectConfigId,stationId,startDateStr,endDateStr);
        Log.d("fan","-----reportResult======"+reportResult);
        Gson gson = new Gson();
        ReportBean reportBean = gson.fromJson(reportResult, ReportBean.class);
        Log.d("fan","====解析后的reportBean==:"+reportBean.toString());
        return reportBean;
    }

    public static ReportBean getNewestData(String selectConfigId, String stationId, int count) throws IOException {
        String dataResult ="";
        //拼接出URL
        String newestDataUrl = URL_TODAY_DATA+"?collectorConfigId="+selectConfigId+"&collectorId="+stationId+"&count="+count;
        Log.d("fan","-----newestDataUrl======"+newestDataUrl);
        dataResult = service(newestDataUrl,"POST");
        Log.d("fan","-----getNewestData  result======"+dataResult);
        Gson gson = new Gson();
        ReportBean reportBean = gson.fromJson(dataResult, ReportBean.class);
        Log.d("fan","====getNewestData====解析后的reportBean==:"+reportBean.toString());
        return reportBean;
    }

}
