package com.example.weatherapplication.view;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.weatherapplication.adapter.TextAdapter;
import com.example.weatherapplication.R;
import com.example.weatherapplication.bean.CollectorItemBean;
import com.example.weatherapplication.bean.TreeBean;
import com.example.weatherapplication.bean.TreeItemBean;
import com.example.weatherapplication.util.NetUtil;

public class ViewMiddle extends LinearLayout implements ViewBaseAction {

    private ListView regionListView;
    private ListView plateListView;
    private ArrayList<String> groups = new ArrayList<String>();
    private LinkedList<String> childrenItem = new LinkedList<String>();
    private SparseArray<LinkedList<String>> children = new SparseArray<LinkedList<String>>();
    private TextAdapter plateListViewAdapter;
    private TextAdapter areaListViewAdapter;
    private OnSelectListener mOnSelectListener;
    private int tAreaPosition = 0;
    private int tBlockPosition = 0;
    private String showString = "不限";
    private static final String TAG = "ViewMiddle";

    public ViewMiddle(Context context) {
        super(context);
        init(context);
    }

    public ViewMiddle(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }
    public ViewMiddle(Context context, String type, String collectorId) {
        super(context);
        init(context,type,collectorId);
    }

    public void updateShowText(String showArea, String showBlock) {
        if (showArea == null || showBlock == null) {
            return;
        }
        for (int i = 0; i < groups.size(); i++) {
            if (groups.get(i).equals(showArea)) {
                areaListViewAdapter.setSelectedPosition(i);
                childrenItem.clear();
                if (i < children.size()) {
                    childrenItem.addAll(children.get(i));
                }
                tAreaPosition = i;
                break;
            }
        }
        for (int j = 0; j < childrenItem.size(); j++) {
            if (childrenItem.get(j).replace("不限", "").equals(showBlock.trim())) {
                plateListViewAdapter.setSelectedPosition(j);
                tBlockPosition = j;
                break;
            }
        }
        setDefaultSelect();
    }

    private void init(Context context,String type, String collectorId) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.view_region, this, true);
        regionListView = (ListView) findViewById(R.id.listView);
        plateListView = (ListView) findViewById(R.id.listView2);
        setBackgroundDrawable(getResources().getDrawable(
                R.drawable.choosearea_bg_left));

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
                    int i = 0 ;
                    Iterator it = treeItemBeans.iterator();
                    while(it.hasNext()) {
                        TreeItemBean treeItemBean = (TreeItemBean) it.next();
                        groups.add(treeItemBean.getTreeName());
                        LinkedList<String> tItem = new LinkedList<String>();
                        for(int j=0;j<15;j++){
                            tItem.add(treeItemBean.getTreeId()+" "+j+"列");
                        }
                        children.put(i, tItem);
                        i++;
                    }
                    /*while(it.hasNext()) {
                        TreeItemBean treeItemBean = (TreeItemBean) it.next();
                        //Log.d("NotificationsFragment", "===treeItemBean" + treeItemBean);
                        Log.d(TAG, "===treeItemBean  getTreeId====:" + treeItemBean.getTreeId() + "getCollectorId====:" + treeItemBean.getCollectorId()
                                + " getTreeName====:" + treeItemBean.getTreeName());
                        if (treeItemBean.getTreeId() != null) {
                            CollectorItemBean.TreeDataBean treeDataBean = null;
                            try {
                                treeDataBean = NetUtil.getTreeDataBean(treeItemBean.getTreeId(), days);
                            }catch (IOException e) {
                                e.printStackTrace();
                            }
                            if (treeDataBean != null && treeDataBean.getmTreeDataItemBeans() != null) {
                                treeDataItemBeans = treeDataBean.getmTreeDataItemBeans();
                                if (treeDataItemBeans == null) {
                                    return null;
                                }
                            }
                        }
                    }*/
                }
            }catch (IOException e) {
                e.printStackTrace();
            }catch (Exception e) {
                e.printStackTrace();
            }
        }

        /*for(int i=0;i<10;i++){
            groups.add(i+"行");
            LinkedList<String> tItem = new LinkedList<String>();
            for(int j=0;j<15;j++){
                tItem.add(i+"行"+j+"列");
            }
            children.put(i, tItem);
        }*/

        areaListViewAdapter = new TextAdapter(context, groups,
                R.drawable.choose_item_selected,
                R.drawable.choose_area_item_selector);
        areaListViewAdapter.setTextSize(17);
        areaListViewAdapter.setSelectedPositionNoNotify(tAreaPosition);
        regionListView.setAdapter(areaListViewAdapter);
        areaListViewAdapter
                .setOnItemClickListener(new TextAdapter.OnItemClickListener() {

                    @Override
                    public void onItemClick(View view, int position) {
                        if (position < children.size()) {
                            childrenItem.clear();
                            childrenItem.addAll(children.get(position));
                            plateListViewAdapter.notifyDataSetChanged();
                        }
                    }
                });
        if (tAreaPosition < children.size())
            childrenItem.addAll(children.get(tAreaPosition));
        plateListViewAdapter = new TextAdapter(context, childrenItem,
                R.drawable.choose_item_right,
                R.drawable.choose_plate_item_selector);
        plateListViewAdapter.setTextSize(15);
        plateListViewAdapter.setSelectedPositionNoNotify(tBlockPosition);
        plateListView.setAdapter(plateListViewAdapter);
        plateListViewAdapter
                .setOnItemClickListener(new TextAdapter.OnItemClickListener() {

                    @Override
                    public void onItemClick(View view, final int position) {

                        showString = childrenItem.get(position);
                        if (mOnSelectListener != null) {

                            mOnSelectListener.getValue(showString);
                        }

                    }
                });
        if (tBlockPosition < childrenItem.size())
            showString = childrenItem.get(tBlockPosition);
        if (showString.contains("不限")) {
            showString = showString.replace("不限", "");
        }
        setDefaultSelect();

    }
    public void setDefaultSelect() {
        regionListView.setSelection(tAreaPosition);
        plateListView.setSelection(tBlockPosition);
    }

    public String getShowText() {
        return showString;
    }

    public void setOnSelectListener(OnSelectListener onSelectListener) {
        mOnSelectListener = onSelectListener;
    }

    public interface OnSelectListener {
        public void getValue(String showText);
    }

    @Override
    public void hide() {
        // TODO Auto-generated method stub

    }

    @Override
    public void show() {
        // TODO Auto-generated method stub

    }
    private void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.view_region, this, true);
        regionListView = (ListView) findViewById(R.id.listView);
        plateListView = (ListView) findViewById(R.id.listView2);
        setBackgroundDrawable(getResources().getDrawable(
                R.drawable.choosearea_bg_left));

        for(int i=0;i<10;i++){
            groups.add(i+"行");
            LinkedList<String> tItem = new LinkedList<String>();
            for(int j=0;j<15;j++){
                tItem.add(i+"行"+j+"列");
            }
            children.put(i, tItem);
        }

        areaListViewAdapter = new TextAdapter(context, groups,
                R.drawable.choose_item_selected,
                R.drawable.choose_area_item_selector);
        areaListViewAdapter.setTextSize(17);
        areaListViewAdapter.setSelectedPositionNoNotify(tAreaPosition);
        regionListView.setAdapter(areaListViewAdapter);
        areaListViewAdapter
                .setOnItemClickListener(new TextAdapter.OnItemClickListener() {

                    @Override
                    public void onItemClick(View view, int position) {
                        if (position < children.size()) {
                            childrenItem.clear();
                            childrenItem.addAll(children.get(position));
                            plateListViewAdapter.notifyDataSetChanged();
                        }
                    }
                });
        if (tAreaPosition < children.size())
            childrenItem.addAll(children.get(tAreaPosition));
        plateListViewAdapter = new TextAdapter(context, childrenItem,
                R.drawable.choose_item_right,
                R.drawable.choose_plate_item_selector);
        plateListViewAdapter.setTextSize(15);
        plateListViewAdapter.setSelectedPositionNoNotify(tBlockPosition);
        plateListView.setAdapter(plateListViewAdapter);
        plateListViewAdapter
                .setOnItemClickListener(new TextAdapter.OnItemClickListener() {

                    @Override
                    public void onItemClick(View view, final int position) {

                        showString = childrenItem.get(position);
                        if (mOnSelectListener != null) {

                            mOnSelectListener.getValue(showString);
                        }

                    }
                });
        if (tBlockPosition < childrenItem.size())
            showString = childrenItem.get(tBlockPosition);
        if (showString.contains("不限")) {
            showString = showString.replace("不限", "");
        }
        setDefaultSelect();

    }
}