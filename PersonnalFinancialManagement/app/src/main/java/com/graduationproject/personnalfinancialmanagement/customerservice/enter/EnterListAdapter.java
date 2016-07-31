package com.graduationproject.personnalfinancialmanagement.customerservice.enter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.graduationproject.personnalfinancialmanagement.R;
import com.graduationproject.personnalfinancialmanagement.customerservice.gasStation.NationalGasStationQueryActivity;
import com.graduationproject.personnalfinancialmanagement.customerservice.news.activity.NewsListActivity;
import com.graduationproject.personnalfinancialmanagement.customerservice.trainTicket.TrainTicketActivity;
import com.graduationproject.personnalfinancialmanagement.customerservice.weatherapp.ui.WeatherActivty;

/**
 * Created by longhui on 2016/5/27.
 */
public class EnterListAdapter extends RecyclerView.Adapter<EnterListAdapter.EnterViewHolder> {
    Context mContext;
    LayoutInflater inflater;
    String[] titles = {"财经新闻", "12306火车票查询", "全国加油站查询", "全国天气预报"};
    int[] ids = {R.drawable.icon_news, R.drawable.icon_train, R.drawable.icon_gas_station, R.drawable.icon_weather};
    Class<?>[] cls = {NewsListActivity.class, TrainTicketActivity.class, NationalGasStationQueryActivity.class, WeatherActivty.class};

    public EnterListAdapter(Context mContext) {
        this.mContext = mContext;
        this.inflater = LayoutInflater.from(mContext);
    }

    @Override
    public EnterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        EnterViewHolder viewHolder = new EnterViewHolder(inflater.inflate(R.layout.custom_grid_item, parent, false));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(EnterViewHolder holder, int position) {
        final int pos = position;
        holder.iconIv.setImageResource(ids[position]);
        holder.nameTv.setText(titles[position]);
        holder.mainLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cls[pos] == null) {
                    return;
                }
                Intent intent = new Intent(mContext, cls[pos]);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return 4;
    }

    class EnterViewHolder extends RecyclerView.ViewHolder {
        LinearLayout mainLl;
        TextView nameTv;
        ImageView iconIv;

        public EnterViewHolder(View itemView) {
            super(itemView);
            mainLl = (LinearLayout) itemView.findViewById(R.id.custom_grid_ll);
            nameTv = (TextView) itemView.findViewById(R.id.custom_grid_tv);
            iconIv = (ImageView) itemView.findViewById(R.id.custom_grid_iv);
        }
    }
}
