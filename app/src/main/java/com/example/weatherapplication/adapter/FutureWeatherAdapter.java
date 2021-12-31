package com.example.weatherapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weatherapplication.R;
import com.example.weatherapplication.bean.DayWeatherBean;
import com.example.weatherapplication.ui.home.HomeFragment;

import java.util.List;

public class FutureWeatherAdapter extends RecyclerView.Adapter<FutureWeatherAdapter.WeatherViewHolder>{
    private Context mContext;
    private List<DayWeatherBean> mWeatherBean;

    ///最该温度和最低温度，用于绘制温度曲线
    private int mLowestTem;
    private int mHighestTem;
    ///
    //public FutureWeatherAdapter(Context context,List<DayWeatherBean> weatherBeans,int lowTem ,int highTem){
    public FutureWeatherAdapter(Context context, List<DayWeatherBean> weatherBeans){
        this.mContext = context;
        this.mWeatherBean = weatherBeans;
       // this.mLowestTem = lowTem;
        //this.mHighestTem = highTem;
    }
    @NonNull
    @Override
    public WeatherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.weather_item_layout, parent, false);
        WeatherViewHolder weatherViewHolder = new WeatherViewHolder(view);
        return weatherViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull WeatherViewHolder holder, int position) {
        DayWeatherBean weatherBean = mWeatherBean.get(position);

        holder.tvWeather.setText(weatherBean.getWea());
        holder.tvDay.setText(weatherBean.getDay());
        holder.tvTem.setText(weatherBean.getTem());
        holder.tvTemLowHigh.setText(weatherBean.getTem2() + "~" + weatherBean.getTem1());
        holder.tvWin.setText(weatherBean.getWin()[0] + weatherBean.getWinSpeed());
        holder.tvAir.setText("空气:" + weatherBean.getAir() + weatherBean.getAirLevel());
        holder.ivWeather.setImageResource(getImgResOfWeather(weatherBean.getWeaImg()));
        //holder.weatherLineView.setLowHighestData(mLowestTem,mHighestTem);

    }


    @Override
    public int getItemCount() {
        if(mWeatherBean == null){
            return 0;
        }else{
            return mWeatherBean.size();
        }
    }

    class WeatherViewHolder extends RecyclerView.ViewHolder{

        TextView tvWeather, tvDay,tvTem, tvTemLowHigh, tvWin, tvAir;
        ImageView ivWeather;
       // WeatherLineView weatherLineView;

        public WeatherViewHolder(@NonNull View itemView) {
            super(itemView);

            tvWeather = itemView.findViewById(R.id.tv_weather);
            tvDay = itemView.findViewById(R.id.tv_day);
            tvAir = itemView.findViewById(R.id.tv_air);
            tvTem = itemView.findViewById(R.id.tv_tem);
            tvTemLowHigh = itemView.findViewById(R.id.tv_tem_low_high);
            tvWin = itemView.findViewById(R.id.tv_win);
            ivWeather = itemView.findViewById(R.id.iv_weather);
           // weatherLineView = itemView.findViewById(R.id.wea_line);
        }
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
}
