package com.graduationproject.personnalfinancialmanagement.customerservice.gasStation.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.graduationproject.personnalfinancialmanagement.R;
import com.graduationproject.personnalfinancialmanagement.customerservice.gasStation.javabean.GasNameAndPrice;
import com.graduationproject.personnalfinancialmanagement.customerservice.gasStation.javabean.GasStationData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by longhui on 2016/5/24.
 */
public class MyGasStationListAdapter extends RecyclerView.Adapter<MyGasStationListAdapter.MyViewHolder> {
    Context mContext;
    List<GasStationData> mGasStationDataList;
    LayoutInflater inflater;

    public MyGasStationListAdapter(Context context, List<GasStationData> gasStationDataList) {
        this.mContext = context;
        this.mGasStationDataList = gasStationDataList;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public MyGasStationListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(inflater.inflate(R.layout.gas_station_list_item, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        GasStationData itemData = this.mGasStationDataList.get(position);
        holder.stationNameTv.setText(itemData.getName());
        holder.stationAddressTv.setText(itemData.getAddress());
        if (TextUtils.isEmpty(itemData.getFwlsmc())) {
            holder.stationServiceTv.setText("未能查询到该加油站可提供的服务");
        } else {
            holder.stationServiceTv.setText(itemData.getFwlsmc());
        }
        addPrice(holder.stationPriceLl, itemData.getGastprice());
    }

    public void addPrice(LinearLayout ll, List<GasNameAndPrice> gasNameAndPriceList) {
        int childCount = ll.getChildCount();
        if (gasNameAndPriceList == null) {
            gasNameAndPriceList = new ArrayList<GasNameAndPrice>();
        }
        if (childCount > gasNameAndPriceList.size()) {
            int i = 0;
            for (; i < gasNameAndPriceList.size(); i++) {
                GasNameAndPrice gasNameAndPrice = gasNameAndPriceList.get(i);
                LinearLayout priceLayout = (LinearLayout) ll.getChildAt(i);
                TextView nameTextView = (TextView) priceLayout.findViewById(R.id.gas_station_gas_name_tv);
                TextView priceTextView = (TextView) priceLayout.findViewById(R.id.gas_station_gas_price_tv);
                nameTextView.setText(gasNameAndPrice.getName());
                priceTextView.setText(gasNameAndPrice.getPrice() + "");
                priceLayout.setVisibility(View.VISIBLE);
            }
            for (; i < childCount; i++) {
                LinearLayout priceLayout = (LinearLayout) ll.getChildAt(i);
                priceLayout.setVisibility(View.GONE);
            }
        } else {
            int i = 0;
            for (; i < childCount; i++) {
                GasNameAndPrice gasNameAndPrice = gasNameAndPriceList.get(i);
                LinearLayout priceLayout = (LinearLayout) ll.getChildAt(i);
                TextView nameTextView = (TextView) priceLayout.findViewById(R.id.gas_station_gas_name_tv);
                TextView priceTextView = (TextView) priceLayout.findViewById(R.id.gas_station_gas_price_tv);
                nameTextView.setText(gasNameAndPrice.getName());
                priceTextView.setText(gasNameAndPrice.getPrice() + "");
                priceLayout.setVisibility(View.VISIBLE);
            }
            for (; i < gasNameAndPriceList.size(); i++) {
                Log.e("MyGasStationListAdapter", "addView");
                GasNameAndPrice gasNameAndPrice = gasNameAndPriceList.get(i);
                LinearLayout priceLayout = (LinearLayout) inflater.inflate(R.layout.gas_station_name_price_list_item, null);
                TextView nameTextView = (TextView) priceLayout.findViewById(R.id.gas_station_gas_name_tv);
                TextView priceTextView = (TextView) priceLayout.findViewById(R.id.gas_station_gas_price_tv);
                nameTextView.setText(gasNameAndPrice.getName());
                priceTextView.setText(gasNameAndPrice.getPrice() + "");
                priceLayout.setVisibility(View.VISIBLE);
                ll.addView(priceLayout);
            }
        }
//        ll.removeAllViews();
//        for (GasNameAndPrice gasNameAndPrice : gasNameAndPriceList) {
//            LinearLayout priceLayout = (LinearLayout) inflater.inflate(R.layout.gas_station_name_price_list_item, null);
//            TextView nameTextView = (TextView) priceLayout.findViewById(R.id.gas_station_gas_name_tv);
//            TextView priceTextView = (TextView) priceLayout.findViewById(R.id.gas_station_gas_price_tv);
//            nameTextView.setText(gasNameAndPrice.getName());
//            priceTextView.setText(gasNameAndPrice.getPrice() + "");
//            ll.addView(priceLayout);
//        }
    }

    @Override
    public int getItemCount() {
        return this.mGasStationDataList.size();
    }

    public void refresh() {
        notifyDataSetChanged();
    }

    public void setDataList(List<GasStationData> gasStationDataList) {
        this.mGasStationDataList.clear();
        this.mGasStationDataList.addAll(gasStationDataList);
        refresh();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView stationNameTv;
        TextView stationAddressTv;
        TextView stationServiceTv;
        LinearLayout stationPriceLl;

        public MyViewHolder(View view) {
            super(view);
            stationNameTv = (TextView) view.findViewById(R.id.gas_station_name_tv);
            stationAddressTv = (TextView) view.findViewById(R.id.gas_station_address_tv);
            stationServiceTv = (TextView) view.findViewById(R.id.gas_station_service_tv);
            stationPriceLl = (LinearLayout) view.findViewById(R.id.gas_station_gas_price_ll);

        }
    }
}
