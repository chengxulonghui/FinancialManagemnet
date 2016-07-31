package com.graduationproject.personnalfinancialmanagement.financialalarm;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;

import com.graduationproject.personnalfinancialmanagement.R;
import com.graduationproject.personnalfinancialmanagement.config.javabean.FinancialAlarm;
import com.graduationproject.personnalfinancialmanagement.utils.DateUtils;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by longhui on 2016/5/23.
 */

public class MyFinancialAlarmListAdapter extends RecyclerView.Adapter<MyFinancialAlarmListAdapter.MyViewHolder> {
    Context mContext;
    List<FinancialAlarm> mFinancialAlarmList;
    LayoutInflater inflater;

    public MyFinancialAlarmListAdapter(Context context, List<FinancialAlarm> financialAlarmList) {
        this.mContext = context;
        this.mFinancialAlarmList = financialAlarmList;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(inflater.inflate(R.layout.financial_alarm_list_item, parent,
                false));
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        FinancialAlarm itemData = this.mFinancialAlarmList.get(position);
        holder.dateTv.setText(DateUtils.transMsString2Month_Day_DateStr(itemData.getDate()));
        DecimalFormat df = new DecimalFormat("0.00");
        if (itemData.getIsHandle() == 0) {
            holder.moneyTv.setText(df.format(itemData.getMoney()));
        } else {
            holder.moneyTv.setText(df.format(itemData.getMoney()) + " " + "(已处理)");
        }
        if (itemData.getDataType() == 0) {
            holder.moneyTv.setTextColor(mContext.getResources().getColor(R.color.title_red));
            holder.categoryTv.setText(itemData.getCategoryName() + " " + "-" + " " + itemData.getSubcategoryName());
            holder.dotIv.setImageResource(R.drawable.icon_red_dot);
        } else {
            holder.moneyTv.setTextColor(mContext.getResources().getColor(R.color.font_green));
            holder.categoryTv.setText(itemData.getCategoryName());
            holder.dotIv.setImageResource(R.drawable.icon_green_dot);
        }
        if (DateUtils.calculateDayMsDate(itemData.getDate(), DateUtils.transCurrentDate2MillisecondStr()) == 0
                && itemData.getIsObsolete() == 0 && itemData.getIsHandle() == 0 &&
                DateUtils.transMsString2Month_Day_DateStr(itemData.getDate()).equals(DateUtils.transMsString2Month_Day_DateStr(DateUtils.transCurrentDate2MillisecondStr()))) {
            holder.saveTv.setVisibility(View.VISIBLE);
        } else {
            holder.saveTv.setVisibility(View.GONE);
        }
        MyFinancialAlarmOnClickListener myFinancialAlarmOnClickListener = new MyFinancialAlarmOnClickListener(itemData);
        holder.saveTv.setOnClickListener(myFinancialAlarmOnClickListener);
        holder.contentTr.setOnLongClickListener(myFinancialAlarmOnClickListener);
        holder.contentTr.setOnClickListener(myFinancialAlarmOnClickListener);
    }

    @Override
    public int getItemCount() {
        return this.mFinancialAlarmList.size();
    }

    public void refresh() {
        notifyDataSetChanged();
    }

    public void setDataList(List<FinancialAlarm> financialAlarmList) {
        this.mFinancialAlarmList.clear();
        this.mFinancialAlarmList.addAll(financialAlarmList);
        refresh();
    }

    public List<FinancialAlarm> getDataList() {
        return this.mFinancialAlarmList;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        View line;
        TextView dateTv;
        TextView categoryTv;
        TextView moneyTv;
        TextView saveTv;
        TableRow contentTr;
        ImageView dotIv;

        public MyViewHolder(View view) {
            super(view);
            line = view.findViewById(R.id.financial_alarm_list_item_line_v);
            dateTv = (TextView) view.findViewById(R.id.financial_alarm_list_item_date_tv);
            categoryTv = (TextView) view.findViewById(R.id.financial_alarm_list_item_category_tv);
            moneyTv = (TextView) view.findViewById(R.id.financial_alarm_list_item_money_tv);
            saveTv = (TextView) view.findViewById(R.id.financial_alarm_list_item_save_tv);
            contentTr = (TableRow) view.findViewById(R.id.financial_alarm_list_item_content_tr);
            dotIv = (ImageView) view.findViewById(R.id.financial_alarm_list_item_dot_iv);
        }
    }

    class MyFinancialAlarmOnClickListener implements View.OnClickListener, View.OnLongClickListener {
        FinancialAlarm itemData;

        public MyFinancialAlarmOnClickListener(FinancialAlarm financialAlarm) {
            this.itemData = financialAlarm;
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.financial_alarm_list_item_save_tv:
                    if (mContext instanceof FinancialAlarmActivity) {
                        ((FinancialAlarmActivity) mContext).saveFinancialAlarm(itemData);
                    }
                    if (mContext instanceof AllFinancialAlarmActivity) {
                        ((AllFinancialAlarmActivity) mContext).saveFinancialAlarm(itemData);
                    }
                    break;
                case R.id.financial_alarm_list_item_content_tr:
                    if (mContext instanceof FinancialAlarmActivity) {
                        ((FinancialAlarmActivity) mContext).toFinancialAlarmDetail(itemData);
                    }
                    if (mContext instanceof AllFinancialAlarmActivity) {
                        ((AllFinancialAlarmActivity) mContext).toFinancialAlarmDetail(itemData);
                    }
                    break;
            }
        }

        @Override
        public boolean onLongClick(View v) {
            if (itemData.getIsHandle() != 0) {
                return false;
            }
            AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
            dialog.setTitle("删除日历提醒");
            dialog.setTitle("确定删除此条日历提醒？");
            dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (mContext instanceof FinancialAlarmActivity) {
                        ((FinancialAlarmActivity) mContext).deleteFinancialAlarm(itemData);
                    }
                    if (mContext instanceof AllFinancialAlarmActivity) {
                        ((AllFinancialAlarmActivity) mContext).deleteFinancialAlarm(itemData);
                    }
                }
            });
            dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            dialog.create();
            dialog.show();
            return false;
        }
    }
}
