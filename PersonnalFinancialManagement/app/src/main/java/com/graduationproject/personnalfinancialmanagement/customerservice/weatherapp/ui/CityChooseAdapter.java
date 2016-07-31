package com.graduationproject.personnalfinancialmanagement.customerservice.weatherapp.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.graduationproject.personnalfinancialmanagement.R;
import com.graduationproject.personnalfinancialmanagement.customerservice.weatherapp.config.city.CityDetail;

import java.util.List;

/**
 * Created by Administrator on 2015/11/14.
 */
public class CityChooseAdapter extends BaseAdapter {
    LayoutInflater inflater;
    Context mContext;
    List<CityDetail> cityDetailList;
    public CityChooseAdapter(Context context,List<CityDetail> cityDetails) {
        this.mContext = context;
        this.cityDetailList=cityDetails;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return cityDetailList.size();
    }

    @Override
    public Object getItem(int position) {
        return cityDetailList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CityChooseViewHolder viewHolder;
        CityDetail cityDetail=cityDetailList.get(position);
        if (convertView == null) {
            viewHolder = new CityChooseViewHolder();
            convertView = inflater.inflate(R.layout.city_choose_item, null);
            viewHolder.district = (TextView) convertView.findViewById(R.id.city_choose_item_district);
            viewHolder.address = (TextView) convertView.findViewById(R.id.city_choose_item_address);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (CityChooseViewHolder) convertView.getTag();
        }
        viewHolder.district.setText(cityDetail.getDistrict());
        viewHolder.address.setText(cityDetail.getProvince()+"-"+cityDetail.getCity());
        return convertView;
    }
    public void setDataList(List<CityDetail> cityDetails){
        this.cityDetailList=cityDetails;
        refresh();
    }
    public List<CityDetail> getDataList() {
        return this.cityDetailList;
    }
    public void refresh(){
        notifyDataSetChanged();
    }
}
