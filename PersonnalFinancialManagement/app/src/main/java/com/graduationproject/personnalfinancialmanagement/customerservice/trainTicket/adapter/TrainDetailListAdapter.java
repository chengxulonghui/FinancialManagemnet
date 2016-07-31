package com.graduationproject.personnalfinancialmanagement.customerservice.trainTicket.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.graduationproject.personnalfinancialmanagement.R;
import com.graduationproject.personnalfinancialmanagement.customerservice.trainTicket.TrainTicketActivity;
import com.graduationproject.personnalfinancialmanagement.customerservice.trainTicket.javabean.TrainDetail;
import com.graduationproject.personnalfinancialmanagement.customerservice.trainTicket.javabean.TrainDetailSum;
import com.graduationproject.personnalfinancialmanagement.customerservice.trainTicket.javabean.TrainStation;
import com.graduationproject.personnalfinancialmanagement.utils.DateUtils;

import java.util.List;

/**
 * Created by longhui on 2016/5/25.
 */
public class TrainDetailListAdapter extends RecyclerView.Adapter<TrainDetailListAdapter.TrainDetailViewHolder> {
    Context mContext;
    List<TrainDetailSum> mTrainDetailSumList;
    LayoutInflater inflater;

    public TrainDetailListAdapter(Context context, List<TrainDetailSum> trainDetailSumList) {
        this.mContext = context;
        this.mTrainDetailSumList = trainDetailSumList;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public TrainDetailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        TrainDetailViewHolder holder = new TrainDetailViewHolder(inflater.inflate(R.layout.train_detail_list_item, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(TrainDetailViewHolder holder, int position) {
        TrainDetailSum itemDataSum = this.mTrainDetailSumList.get(position);
        TrainDetail itemData = itemDataSum.getQueryLeftNewDTO();
        holder.trainCodeTv.setText(itemData.getStation_train_code());
        String dateString = DateUtils.getDateStringYMD(DateUtils.getDateYYYYMMDD(itemData.getStart_train_date()));
        holder.trainTimeTv.setText(dateString + " " + itemData.getStart_time() + " "+"-"
                + " " + itemData.getArrive_time() + " " + "历时:" + itemData.getLishi());
        holder.trainStationTv.setText(itemData.getFrom_station_name() + " " + "-" + " " + itemData.getTo_station_name() +
                " " + "(" + "全程:" + itemData.getStart_station_name() +
                " " + "-" + " " + itemData.getEnd_station_name() + ")");
        fillSitData(holder.sitLl, itemData);
        TrainDetailOnClickListener trainDetailOnClickListener = new TrainDetailOnClickListener(itemData);
        holder.toPriceTr.setOnClickListener(trainDetailOnClickListener);

    }

    private void fillSitData(LinearLayout ll, TrainDetail itemData) {
        if (ll.getChildCount() == 0) {
            for (int i = 0; i < 11; i++) {
                LinearLayout sitLayout = (LinearLayout) inflater.inflate(R.layout.train_sit_layout, null);
                ll.addView(sitLayout);
            }
        }
        for (int i = 0; i < ll.getChildCount(); i++) {
            View sitView = ll.getChildAt(i);
            TextView sitTypeTv = (TextView) sitView.findViewById(R.id.train_sit_type_tv);
            TextView sitHaveTv = (TextView) sitView.findViewById(R.id.train_sit_have_tv);
            String sitType = "";
            String sitHave = "";
            switch (i) {
                case 0:
                    sitType = "高级软卧: ";
                    if (itemData.getGr_num().equals("--")) {
                        sitHave = "不提供该席位";
                    } else {
                        sitHave = itemData.getGr_num();
                    }
                    break;
                case 1:
                    sitType = "其他: ";
                    if (itemData.getQt_num().equals("--")) {
                        sitHave = "不提供该席位";
                    } else {
                        sitHave = itemData.getQt_num();
                    }
                    break;
                case 2:
                    sitType = "软卧: ";
                    if (itemData.getRw_num().equals("--")) {
                        sitHave = "不提供该席位";
                    } else {
                        sitHave = itemData.getRw_num();
                    }
                    break;
                case 3:
                    sitType = "软座: ";
                    if (itemData.getRz_num().equals("--")) {
                        sitHave = "不提供该席位";
                    } else {
                        sitHave = itemData.getRz_num();
                    }
                    break;
                case 4:
                    sitType = "特等座: ";
                    if (itemData.getTz_num().equals("--")) {
                        sitHave = "不提供该席位";
                    } else {
                        sitHave = itemData.getTz_num();
                    }
                    break;
                case 5:
                    sitType = "无座: ";
                    if (itemData.getWz_num().equals("--")) {
                        sitHave = "不提供该席位";
                    } else {
                        sitHave = itemData.getWz_num();
                    }
                    break;
                case 6:
                    sitType = "硬卧: ";
                    if (itemData.getYw_num().equals("--")) {
                        sitHave = "不提供该席位";
                    } else {
                        sitHave = itemData.getYw_num();
                    }
                    break;
                case 7:
                    sitType = "硬座: ";
                    if (itemData.getYz_num().equals("--")) {
                        sitHave = "不提供该席位";
                    } else {
                        sitHave = itemData.getYz_num();
                    }
                    break;
                case 8:
                    sitType = "二等座: ";
                    if (itemData.getZe_num().equals("--")) {
                        sitHave = "不提供该席位";
                    } else {
                        sitHave = itemData.getZe_num();
                    }
                    break;
                case 9:
                    sitType = "一等座: ";
                    if (itemData.getZy_num().equals("--")) {
                        sitHave = "不提供该席位";
                    } else {
                        sitHave = itemData.getZy_num();
                    }
                    break;
                case 10:
                    sitType = "商务座: ";
                    if (itemData.getSwz_num().equals("--")) {
                        sitHave = "不提供该席位";
                    } else {
                        sitHave = itemData.getSwz_num();
                    }
                    break;
            }
            sitTypeTv.setText(sitType);
            sitHaveTv.setText(sitHave);
        }
    }

    @Override
    public int getItemCount() {
        return this.mTrainDetailSumList.size();
    }

    public void refresh() {
        notifyDataSetChanged();
    }

    public List<TrainDetailSum> getDataList() {
        return this.mTrainDetailSumList;
    }

    public void setDataList(List<TrainDetailSum> dataList) {
        this.mTrainDetailSumList.clear();
        this.mTrainDetailSumList.addAll(dataList);
        refresh();
    }

    class TrainDetailViewHolder extends RecyclerView.ViewHolder {
        TextView trainCodeTv;
        TextView trainTimeTv;
        TextView trainStationTv;
        TableRow toPriceTr;
        LinearLayout sitLl;

        public TrainDetailViewHolder(View itemView) {
            super(itemView);
            trainCodeTv = (TextView) itemView.findViewById(R.id.train_item_train_code_tv);
            trainTimeTv = (TextView) itemView.findViewById(R.id.train_item_train_time_tv);
            trainStationTv = (TextView) itemView.findViewById(R.id.train_item_train_station_tv);
            toPriceTr = (TableRow) itemView.findViewById(R.id.train_item_train_to_price_tr);
            sitLl = (LinearLayout) itemView.findViewById(R.id.train_item_train_sit_ll);
        }
    }

    class TrainDetailOnClickListener implements View.OnClickListener {
        TrainDetail itemData;

        public TrainDetailOnClickListener(TrainDetail trainDetail) {
            this.itemData = trainDetail;
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.train_item_train_to_price_tr:
                    ((TrainTicketActivity) mContext).toTrainPrice(itemData);
                    break;
            }
        }
    }
}
