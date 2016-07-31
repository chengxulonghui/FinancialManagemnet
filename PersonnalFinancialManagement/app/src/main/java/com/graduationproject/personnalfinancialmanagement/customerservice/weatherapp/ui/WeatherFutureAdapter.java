package com.graduationproject.personnalfinancialmanagement.customerservice.weatherapp.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.graduationproject.personnalfinancialmanagement.R;
import com.graduationproject.personnalfinancialmanagement.customerservice.weatherapp.config.weather.WeatherFutureDetail;
import com.graduationproject.personnalfinancialmanagement.customerservice.weatherapp.utils.WeatherUtils;

import java.util.List;


/**
 * Created by Administrator on 2015/11/15.
 */
public class WeatherFutureAdapter extends BaseAdapter {
    Context mContext;
    List<WeatherFutureDetail> weatherFutureDetails;
    LayoutInflater inflater;

    public WeatherFutureAdapter(Context context, List<WeatherFutureDetail> weatherFutureDetails) {
        this.mContext = context;
        this.weatherFutureDetails = weatherFutureDetails;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return weatherFutureDetails.size();
    }

    @Override
    public Object getItem(int position) {
        return weatherFutureDetails.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        WeatherFutureDetail weatherFutureDetail = weatherFutureDetails.get(position);
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.weather_future_list_item, null);
            viewHolder.weekTv = (TextView) convertView.findViewById(R.id.weather_future_item_week_tv);
            viewHolder.temp = (TextView) convertView.findViewById(R.id.weather_future_item_temp_tv);
            viewHolder.weatherIcon = (ImageView) convertView.findViewById(R.id.weather_future_item_weather_icon_iv);
            convertView.setTag(viewHolder);
        } else {
            viewHolder= (ViewHolder) convertView.getTag();
        }
        viewHolder.temp.setText(weatherFutureDetail.getTemperature());
        viewHolder.weekTv.setText(weatherFutureDetail.getWeek());
        viewHolder.weatherIcon.setImageResource(WeatherUtils.getWeatherBoxIconId(weatherFutureDetail.getWeather_id()));
        return convertView;
    }

    private class ViewHolder {
        TextView weekTv;
        TextView temp;
        ImageView weatherIcon;
    }
}
