package com.graduationproject.personnalfinancialmanagement.customerservice.trainTicket.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.graduationproject.personnalfinancialmanagement.R;
import com.graduationproject.personnalfinancialmanagement.customerservice.trainTicket.TrainTicketFromStationChooseFragment;
import com.graduationproject.personnalfinancialmanagement.customerservice.trainTicket.TrainTicketToStationChooseFragment;
import com.graduationproject.personnalfinancialmanagement.customerservice.trainTicket.javabean.TrainStation;

import java.util.List;

/**
 * Created by longhui on 2016/5/25.
 */
public class TrainStationListAdapter extends RecyclerView.Adapter<TrainStationListAdapter.TrainStationViewHolder> {
    Context mContext;
    List<TrainStation> mTrainStationList;
    LayoutInflater inflater;
    Fragment mFragment;

    public TrainStationListAdapter(Context context, List<TrainStation> trainStationList, Fragment fragment) {
        this.mContext = context;
        this.mTrainStationList = trainStationList;
        this.mFragment = fragment;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public TrainStationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        TrainStationViewHolder holder = new TrainStationViewHolder(inflater.inflate(R.layout.train_station_choose_list_item, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(TrainStationViewHolder holder, int position) {
        TrainStation itemData = this.mTrainStationList.get(position);
        holder.stationName.setText(itemData.getSta_name() + "");
        holder.stationCode.setText(itemData.getSta_code() + "");
        TrainStationOnClickListener trainStationOnClickListener = new TrainStationOnClickListener(itemData);
        holder.stationMainLl.setOnClickListener(trainStationOnClickListener);
    }

    @Override
    public int getItemCount() {
        return this.mTrainStationList.size();
    }

    public void refresh() {
        notifyDataSetChanged();
    }

    public List<TrainStation> getDataList() {
        return this.mTrainStationList;
    }

    public void setDataList(List<TrainStation> dataList) {
        this.mTrainStationList.clear();
        this.mTrainStationList.addAll(dataList);
        refresh();
    }

    class TrainStationViewHolder extends RecyclerView.ViewHolder {
        LinearLayout stationMainLl;
        TextView stationName;
        TextView stationCode;

        public TrainStationViewHolder(View itemView) {
            super(itemView);
            stationName = (TextView) itemView.findViewById(R.id.train_station_choose_item_name_tv);
            stationCode = (TextView) itemView.findViewById(R.id.train_station_choose_item_code_tv);
            stationMainLl = (LinearLayout) itemView.findViewById(R.id.train_station_choose_item_main);
        }
    }

    class TrainStationOnClickListener implements View.OnClickListener {
        TrainStation itemData;

        public TrainStationOnClickListener(TrainStation trainStation) {
            this.itemData = trainStation;
        }

        @Override
        public void onClick(View v) {
            if (mFragment instanceof TrainTicketFromStationChooseFragment) {
                ((TrainTicketFromStationChooseFragment) mFragment).setFromStation(itemData);
            }
            if (mFragment instanceof TrainTicketToStationChooseFragment) {
                ((TrainTicketToStationChooseFragment) mFragment).setToStation(itemData);
            }
        }
    }
}
