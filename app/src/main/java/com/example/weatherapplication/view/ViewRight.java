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
import com.example.weatherapplication.bean.CommunityBean;
import com.example.weatherapplication.bean.CommunityItemBean;
import com.example.weatherapplication.bean.TreeBean;
import com.example.weatherapplication.bean.TreeItemBean;
import com.example.weatherapplication.util.NetUtil;

import java.io.IOException;
import java.util.ArrayList;
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
    private ArrayList<String> groups = new ArrayList<String>();
    private ArrayList<String> itemsGroupVaule = new ArrayList<String>();

    public String getShowText() {
        return showText;
    }

    public ViewRight(Context context) {
        super(context);
        init(context);
    }
    public ViewRight(Context context,String type,String weatherStationId) {
        super(context);
        init(context,type,weatherStationId);
    }

    public ViewRight(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public ViewRight(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }
    private void init(Context context ,String type,String weatherStationId) {
        mContext = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.view_distance, this, true);
        setBackgroundDrawable(getResources().getDrawable(R.drawable.choosearea_bg_right));
        mListView = (ListView) findViewById(R.id.listView);
//        items = new String[]{stationId};
//        itemsVaule = new String[]{stationName};
        adapter = new TextAdapter(context, groups, R.drawable.choose_item_right, R.drawable.choose_area_item_selector);
        adapter.setTextSize(17);
//        for (int i = 0; i < itemsVaule.length; i++) {
//             adapter.setSelectedPositionNoNotify(i);
//             showText = items[i];
//             Log.d(TAG,"=***************==items[i]==:"+ items[i]);
//             Log.d(TAG,"=***************==itemsVaule[i]==:"+itemsVaule[i]);
//             break;
//        }
        Log.d(TAG, "*****ViewRight*********================:" + type);
        if(type!= null && "community".equals(type)){
            Log.d(TAG, "*****ViewRight**********type=============:" + type);
            try {
                CommunityBean communityBean = NetUtil.getCommunityBean(weatherStationId);
                Log.d(TAG, "=====ViewRight==communityBean=============:" + communityBean);
                if(communityBean != null  && communityBean.getmItemBeans()!= null){
                    List<CommunityItemBean> communityItemBeans = communityBean.getmItemBeans();
                    if(communityItemBeans == null){
                        Log.d(TAG, "=====ViewRight==communityItemBeans=============:" + communityItemBeans);
                    }
                    int i = 0;
                    Iterator it = communityItemBeans.iterator();
                    while(it.hasNext()) {
                        CommunityItemBean CommunityItemBean = (CommunityItemBean) it.next();
                        groups.add(CommunityItemBean.getCommunityName());
                        itemsGroupVaule.add(CommunityItemBean.getCommunityId());
                    }
                }
            }catch (IOException e) {
                e.printStackTrace();
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
        Log.d(TAG, "=====ViewRight======groups=============:" + groups);
        Log.d(TAG, "====ViewRight=====itemsGroupVaule=============:" + itemsGroupVaule);
        for (int i = 0; i < groups.size(); i++) {
            Log.d(TAG, "=====ViewRight====itemsGroupVaule.get(i)=============:" + itemsGroupVaule.get(i));
            Log.d(TAG, "=======ViewRight======groups.get(i)=============:" +groups.get(i));
            adapter.setSelectedPositionNoNotify(i);
            showText = groups.get(i);
        }
        mListView.setAdapter(adapter);
        adapter.setOnItemClickListener(new TextAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) throws IOException {

                if (mOnSelectListener != null) {
                    Log.d(TAG, "====ViewRight=====position=============:" +position);
                    Log.d(TAG, "====ViewRight=====itemsGroupVaule.get(position)=============:" +itemsGroupVaule.get(position));
                    Log.d(TAG, "====ViewRight=====groups.get(position)=============:" +groups.get(position));
                    showText = groups.get(position);
                    mOnSelectListener.getValue(itemsGroupVaule.get(position), groups.get(position));
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
