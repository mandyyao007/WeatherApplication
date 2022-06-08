package com.example.weatherapplication.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.example.weatherapplication.adapter.TextAdapter;
import com.example.weatherapplication.R;
import com.example.weatherapplication.bean.TreeBean;
import com.example.weatherapplication.bean.TreeItemBean;
import com.example.weatherapplication.util.NetUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class ViewLeft extends RelativeLayout implements ViewBaseAction{

    private ListView mListView;
    private final String[] items = new String[] { "item1", "item2", "item3", "item4", "item5", "item6" };//显示字段
    private final String[] itemsVaule = new String[] { "1", "2", "3", "4", "5", "6" };//隐藏id
    private OnSelectListener mOnSelectListener;
    private TextAdapter adapter;
    private String mDistance;
    private String showText = "item1";
    private Context mContext;
    private static final String TAG = "ViewLeft";
    private ArrayList<String> groups = new ArrayList<String>();
    private ArrayList<String> itemsGroupVaule = new ArrayList<String>();

    public String getShowText() {
        return showText;
    }

    public ViewLeft(Context context) {
        super(context);
        init(context);
    }
    public ViewLeft(Context context, String type, String collectorId) {
        super(context);
        init(context,type,collectorId);
    }
    public ViewLeft(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public ViewLeft(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }
    private void init(Context context ,String type, String collectorId) {
        mContext = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.view_distance, this, true);
        setBackgroundDrawable(getResources().getDrawable(R.drawable.choosearea_bg_mid));
        mListView = (ListView) findViewById(R.id.listView);
        adapter = new TextAdapter(context, groups, R.drawable.choose_item_right, R.drawable.choose_area_item_selector);
        adapter.setTextSize(15);
        if(type!= null && "tree".equals(type)){
            Log.d(TAG, "===type=============:" + type);
            try {
                TreeBean treeBean = NetUtil.getTreeBean(collectorId);
                Log.d(TAG, "===treeBean=============:" + treeBean);
                if(treeBean != null  && treeBean.getmTreeItemBeans()!= null){
                    List<TreeItemBean> treeItemBeans = treeBean.getmTreeItemBeans();
                    if(treeItemBeans == null){
                        Log.d(TAG, "===treeBean=============:" + treeBean);
                    }
                    int i = 0;
                    Iterator it = treeItemBeans.iterator();
                    while(it.hasNext()) {
                        TreeItemBean treeItemBean = (TreeItemBean) it.next();
                        groups.add(treeItemBean.getTreeName());
                        itemsGroupVaule.add(treeItemBean.getTreeId());
                    }
                }
            }catch (IOException e) {
                e.printStackTrace();
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
        Log.d(TAG, "===groups=============:" + groups);
        Log.d(TAG, "===itemsGroupVaule=============:" + itemsGroupVaule);
       // if (mDistance != null) {
            for (int i = 0; i < groups.size(); i++) {
                //if (itemsVaule[i].equals(mDistance)) {
                    Log.d(TAG, "===itemsGroupVaule.get(i)=============:" + itemsGroupVaule.get(i));
                    Log.d(TAG, "===groups.get(i)=============:" +groups.get(i));
                    adapter.setSelectedPositionNoNotify(i);
                    showText = groups.get(i);
                   // break;
               // }
            }
        //}
        mListView.setAdapter(adapter);
        adapter.setOnItemClickListener(new TextAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(View view, int position) throws IOException {

                if (mOnSelectListener != null) {
                    Log.d(TAG, "==position=============:" +position);
                    Log.d(TAG, "==itemsGroupVaule.get(position)=============:" +itemsGroupVaule.get(position));
                    Log.d(TAG, "==groups.get(position)=============:" +groups.get(position));
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
        setBackgroundDrawable(getResources().getDrawable(R.drawable.choosearea_bg_mid));
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