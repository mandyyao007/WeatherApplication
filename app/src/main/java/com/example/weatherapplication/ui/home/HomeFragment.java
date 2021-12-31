package com.example.weatherapplication.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weatherapplication.R;
import com.example.weatherapplication.adapter.FutureWeatherAdapter;
import com.example.weatherapplication.bean.DayWeatherBean;
import com.example.weatherapplication.bean.WeatherBean;
import com.example.weatherapplication.databinding.FragmentHomeBinding;
import com.example.weatherapplication.util.NetUtil;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;
    ////
    private AppCompatSpinner mSpinner;
    private ArrayAdapter<String> mSpAdapter;
    private String[] mCities;
    private TextView tvWeather,tvTem,tvTemLowHigh,tvWin,tvAir;
    private ImageView ivWeather;
    private RecyclerView rlvFutureWeather;
    private FutureWeatherAdapter mWeatherAdapter;
    private DayWeatherBean todayWeather;
    String  userName;

    private Handler mHandler = new Handler(Looper.myLooper()){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if(msg.what == 0){
                String weather = (String) msg.obj;
                Log.d("fan","====weather==:"+weather);
                Gson gson = new Gson();
                WeatherBean weatherBean = gson.fromJson(weather, WeatherBean.class);
                Log.d("fan","====解析后的weatherBean==:"+weatherBean.toString());
                updateUiOfWeather(weatherBean);
            }
        }
    };
    ///
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View fragmentHomeView = binding.getRoot();
        //fragmentHomeView.findViewById(R.id.rlv_future_weather);
        //rlvFutureWeather= container.findViewById(R.id.rlv_future_weather);
        userName = getActivity().getIntent().getStringExtra("userName");
        System.out.println("=============userName=====:"+userName);
        try {
            initView(fragmentHomeView,userName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fragmentHomeView;
    }
    /*@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initView();
    }*/
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void updateUiOfWeather(WeatherBean weatherBean) {
        if(weatherBean == null){
            return ;
        }
        List<DayWeatherBean> dayWeathers = weatherBean.getDayWeatherBeans();
        if(dayWeathers == null){
            return ;
        }
        DayWeatherBean todayWeather = dayWeathers.get(0);
        if(todayWeather == null){
            return;
        }
        tvTem.setText(todayWeather.getTem());
        tvWeather.setText(todayWeather.getWea()+"("+todayWeather.getDay()+")");
        tvTemLowHigh.setText(todayWeather.getTem2()+"~"+todayWeather.getTem1());
        tvWin.setText(todayWeather.getWin()[0]+todayWeather.getWinSpeed());
        tvAir.setText("空气："+todayWeather.getAir()+todayWeather.getAirLevel()+"\n"+todayWeather.getAirTips());
        ivWeather.setImageResource(getImgResOfWeather(todayWeather.getWeaImg()));

        dayWeathers.remove(0); // 去掉当天的天气

        // 未来天气的展示
        //int low = Integer.valueOf(todayWeather.getTem2());
        //int high = Integer.valueOf(todayWeather.getTem1());
        //mWeatherAdapter = new FutureWeatherAdapter(this, dayWeathers,low,high);
        mWeatherAdapter = new FutureWeatherAdapter(this.getActivity(), dayWeathers);
        rlvFutureWeather.setAdapter(mWeatherAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getActivity(),LinearLayoutManager.HORIZONTAL,false);
        rlvFutureWeather.setLayoutManager(layoutManager);
    }
    private int getImgResOfWeather(String weaStr){
        int result = 0;
        switch(weaStr){
            case "xue":
                result = R.drawable.icon_weather_daxue;
                break;
            case "lei":
                result = R.drawable.icon_weather_leizhenyu;
                break;
            case "shachen":
                result = R.drawable.icon_weather_mai;
                break;
            case "wu":
                result = R.drawable.icon_weather_wu;
                break;
            case "bingbo":
                result = R.drawable.icon_weather_bingbao;
                break;
            case "yun":
                result = R.drawable.icon_weather_duoyun_day;
                break;
            case "yu":
                result = R.drawable.icon_weather_dayu;
                break;
            case "yin":
                result = R.drawable.icon_weather_yin;
                break;
            case "qing":
                result = R.drawable.icon_weather_qing;
                break;
            default:
                result = R.drawable.icon_weather_dafeng;
        }
        return  result;
    }


    private void initView(View fragmentHomeView ,String userName) throws IOException {
        mSpinner = fragmentHomeView.findViewById(R.id.sp_station);
        mCities = getResources().getStringArray(R.array.cities);
        mSpAdapter = new ArrayAdapter<String>(this.getActivity(),R.layout.sp_item_layout,mCities);
        mSpinner.setAdapter(mSpAdapter);
        Log.d("fan","====STATION info==:"+NetUtil.getStationInfo(userName));
        mSpinner.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedCity = mCities[position];
                getWeatherOfCity(selectedCity);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        tvWeather =fragmentHomeView.findViewById(R.id.tv_weather);
        tvTem =fragmentHomeView.findViewById(R.id.tv_tem);
        tvTemLowHigh = fragmentHomeView.findViewById(R.id.tv_tem_low_high);
        tvWin = fragmentHomeView.findViewById(R.id.tv_win);
        tvAir =fragmentHomeView.findViewById(R.id.tv_air);
        ivWeather =(ImageView)fragmentHomeView.findViewById(R.id.iv_weather);
        rlvFutureWeather = fragmentHomeView.findViewById(R.id.rlv_future_weather);
    }

    private void getWeatherOfCity(String selectedCity) {
        //开启子线程，请求网络
        new Thread(new Runnable() {
            @Override
            public void run() {
                //请求网络
                try {
                    String weatherOfCity =  NetUtil.getWeatherOfCity(selectedCity);
                    //使用Handler将数据传送给主线程
                    Message message = new Message();
                    message.what = 0;
                    message.obj = weatherOfCity;
                    mHandler.sendMessage(message);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            //  case R.id.main_iv_add:
            //    intent.setClass(this,CityManagerActivity.class);
            //    break;
        }
        startActivity(intent);
    }
}