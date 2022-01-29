package com.example.weatherapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.weatherapplication.R;
import com.example.weatherapplication.bean.StationItemBean;

import java.util.List;

public class StationAdapter extends BaseAdapter {
    Context context;
    List<StationItemBean> mDatas;

    public StationAdapter(Context context, List<StationItemBean> mDatas) {
        this.context = context;
        this.mDatas = mDatas;
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_station_layout,null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        StationItemBean bean = mDatas.get(position);
        holder.stationNameTv.setText(bean.getCollectorName());
        holder.latitudeTv.setText(bean.getLatitude());
        holder.longitudeTv.setText(bean.getLongitude());
        return convertView;
    }

    class ViewHolder{
        TextView stationNameTv,longitudeTv,latitudeTv;
        public ViewHolder(View itemView){
            stationNameTv = itemView.findViewById(R.id.item_station_name);
            longitudeTv = itemView.findViewById(R.id.item_station_longitude);
            latitudeTv = itemView.findViewById(R.id.item_station_latitude);

        }
    }
}