package com.example.weatherapplication.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.example.weatherapplication.R;
import com.example.weatherapplication.adapter.TextAdapter;
import com.example.weatherapplication.bean.TreeBean;
import com.example.weatherapplication.bean.TreeItemBean;
import com.example.weatherapplication.util.NetUtil;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

public class ViewRight extends RelativeLayout implements ViewBaseAction{

    private ListView mListView;
    //private final String[] items = new String[] { "item1", "item2", "item3", "item4", "item5", "item6" };//显示字段
    //private final String[] itemsVaule = new String[] { "1", "2", "3", "4", "5", "6" };//隐藏id
    private String[] items ;//显示字段
    private String[] itemsVaule ;//隐藏id
    private OnSelectListener mOnSelectListener;
    private TextAdapter adapter;
    private String mDistance;
    private String showText = "item1";
    private Context mContext;
    private static final String TAG = "ViewRight";

    public String getShowText() {
        return showText;
    }

    public ViewRight(Context context) {
        super(context);
        init(context);
    }
    public ViewRight(Context context,String stationName,String stationNameId) {
        super(context);
        init(context,stationName,stationNameId);
    }

    public ViewRight(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public ViewRight(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }
    private void init(Context context ,String stationName,String stationId) {
        mContext = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.view_distance, this, true);
        setBackgroundDrawable(getResources().getDrawable(R.drawable.choosearea_bg_right));
        mListView = (ListView) findViewById(R.id.listView);
        items = new String[]{stationId};
        itemsVaule = new String[]{stationName};
        adapter = new TextAdapter(context, items, R.drawable.choose_item_right, R.drawable.choose_area_item_selector);
        adapter.setTextSize(17);
        for (int i = 0; i < itemsVaule.length; i++) {
             adapter.setSelectedPositionNoNotify(i);
             showText = items[i];
             Log.d(TAG,"=***************==items[i]==:"+ items[i]);
             Log.d(TAG,"=***************==itemsVaule[i]==:"+itemsVaule[i]);
             break;
        }
        mListView.setAdapter(adapter);
        adapter.setOnItemClickListener(new TextAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(View view, int position) throws IOException {

                if (mOnSelectListener != null) {
                    showText = items[position];
                    mOnSelectListener.getValue(itemsVaule[position], items[position]);
                }
            }
        });
    }
    private void init(Context context) {
        mContext = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.view_distance, this, true);
        setBackgroundDrawable(getResources().getDrawable(R.drawable.choosearea_bg_right));
        mListView = (ListView) findViewById(R.id.listView);
        adapter = new TextAdapter(context, items, R.drawable.choose_item_right, R.drawable.choose_area_item_selector);
        adapter.setTextSize(17);
        if (mDistance != null) {
            for (int i = 0; i < itemsVaule.length; i++) {
                if (itemsVaule[i].equals(mDistance)) {
                    adapter.setSelectedPositionNoNotify(i);
                    showText = items[i];
                    break;
                }
            }
        }
        mListView.setAdapter(adapter);
        adapter.setOnItemClickListener(new TextAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(View view, int position) throws IOException {

                if (mOnSelectListener != null) {
                    showText = items[position];
                    mOnSelectListener.getValue(itemsVaule[position], items[position]);
                }
            }
        });
    }

    public void setOnSelectListener(OnSelectListener onSelectListener) {
        mOnSelectListener = onSelectListener;
    }

    public interface OnSelectListener {
        public void getValue(String distance, String showText) throws IOException;
    }

    @Override
    public void hide() {

    }

    @Override
    public void show() {

    }

}
