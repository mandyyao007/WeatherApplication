package com.example.weatherapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weatherapplication.R;
import com.example.weatherapplication.bean.DayReportBean;

import java.util.List;

public class BeforeReportAdapter extends RecyclerView.Adapter<BeforeReportAdapter.ReportViewHolder>{
    private Context mContext;
    private List<DayReportBean> mReportBean;

    public BeforeReportAdapter(Context context, List<DayReportBean> reportBeans){
        this.mContext = context;
        this.mReportBean = reportBeans;
    }
    @NonNull
    @Override
    public BeforeReportAdapter.ReportViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.report_item_layout, parent, false);
        BeforeReportAdapter.ReportViewHolder reportViewHolder = new BeforeReportAdapter.ReportViewHolder(view);
        return reportViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull BeforeReportAdapter.ReportViewHolder holder, int position) {
        DayReportBean reportBean = mReportBean.get(position);

        holder.tvTem.setText(reportBean.getCol1());
        holder.tvTime.setText(reportBean.getAcquisitionTime());

    }
    @Override
    public int getItemCount() {
        if(mReportBean == null){
            return 0;
        }else{
            return mReportBean.size();
        }
    }

    class ReportViewHolder extends RecyclerView.ViewHolder{

        TextView tvTem,tvTime;
        // WeatherLineView weatherLineView;

        public ReportViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTem = itemView.findViewById(R.id.tv_tem);
            tvTime = itemView.findViewById(R.id.tv_time);
        }
    }


}

