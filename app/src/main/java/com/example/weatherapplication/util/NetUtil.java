package com.example.weatherapplication.util;

import android.util.Log;

import com.example.weatherapplication.bean.LoginBean;
import com.example.weatherapplication.bean.StationBean;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class NetUtil {
    public static final String URL_WEATHER_WITH_FUTURE = "https://tianqiapi.com/api?unescape=1&version=v1&appid=17358133&appsecret=A2V2yvEE";
    public static final String URL_LOGIN = "http://10.68.200.109:9000/user/check_login";
    public static final String URL_STATION = "http://10.68.200.109:9000/basic/show_collector_list";
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

    public static String getWeatherOfCity(String city) throws IOException {
        String weatherResult ="";
        //拼接出URL
        String weatherUrl = URL_WEATHER_WITH_FUTURE+"&city="+city;
        Log.d("fan","-----weatherUrl======"+weatherUrl);
        weatherResult = service(weatherUrl,"GET");
        Log.d("fan","-----weatherResult======"+weatherResult);
        return weatherResult;

    }
    public static String getloginInfo(String userName,String password) throws IOException {
        String result ="";
        //拼接出URL
        String loginUrl = URL_LOGIN+"?password="+password+"&userName="+userName;
        Log.d("fan","-----loginUrl======"+loginUrl);
        result = service(loginUrl,"POST");
        Log.d("fan","-----result======"+result);
        Gson gson = new Gson();
        LoginBean loginBean = gson.fromJson(result, LoginBean.class);
        Log.d("fan","====解析后的loginBean==:"+loginBean.toString());
        String status =  loginBean.getStatus();
        return status;

    }

    public static String getStationInfo(String userName) throws IOException {
        String result ="";
        //拼接出URL
        String stationUrl = URL_STATION+"?userName="+userName+"&weatherStationId=1";
        Log.d("fan","-----stationUrl======"+stationUrl);
        result = service(stationUrl,"POST");
        Log.d("fan","-----result======"+result);
        Gson gson = new Gson();
        StationBean stationBean = gson.fromJson(result, StationBean.class);
        Log.d("fan","====解析后的stationBean==:"+stationBean.toString());
        return stationBean.toString();
    }
}
