package com.example.weatherapplication.adapter;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.List;

public class simpleArrayAdapter<T> extends ArrayAdapter {
    //构造方法
    public simpleArrayAdapter(Context context, int resource, String[] objects) {
        super(context, resource, objects);
    }
    //复写这个方法，使返回的数据没有最后一项
    @Override
    public int getCount() {
        // 不显示最后一项，这一项是提示语句。
        int count = super.getCount();
        return count > 0 ? count - 1 : count;
    }
}
