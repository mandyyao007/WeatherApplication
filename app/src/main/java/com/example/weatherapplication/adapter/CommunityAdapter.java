package com.example.weatherapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.weatherapplication.R;
import com.example.weatherapplication.bean.CommunityItemBean;

import java.util.List;

public class CommunityAdapter extends BaseAdapter {
    Context context;
    List<CommunityItemBean> mDatas;

    public CommunityAdapter(Context context, List<CommunityItemBean> mDatas) {
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
        CommunityAdapter.ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_community_layout, null);
            holder = new CommunityAdapter.ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (CommunityAdapter.ViewHolder) convertView.getTag();
        }
        CommunityItemBean bean = mDatas.get(position);
        holder.communityNameTv.setText(bean.getCommunityName());
        holder.communityIdTv.setText(bean.getCommunityId());
        return convertView;
    }

    class ViewHolder {
        TextView communityNameTv, communityIdTv;

        public ViewHolder(View itemView) {
            communityNameTv = itemView.findViewById(R.id.tv_community_name);
            communityIdTv = itemView.findViewById(R.id.tv_community_id);
        }
    }
}
