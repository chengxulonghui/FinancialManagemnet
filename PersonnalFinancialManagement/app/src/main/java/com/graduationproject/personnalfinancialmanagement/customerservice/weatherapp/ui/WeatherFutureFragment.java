package com.graduationproject.personnalfinancialmanagement.customerservice.weatherapp.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.graduationproject.personnalfinancialmanagement.R;
import com.graduationproject.personnalfinancialmanagement.customerservice.weatherapp.config.weather.WeatherFutureDetail;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2015/11/15.
 */
public class WeatherFutureFragment extends Fragment {
    List<WeatherFutureDetail> weatherFutureDetails;
    ListView mWeatherFutureListView;
    WeatherFutureAdapter mWeatherFutureAdapter;
    static WeatherFutureFragment newInstance(List<WeatherFutureDetail> weatherFutureDetails,int bgId) {
        WeatherFutureFragment newFragment = new WeatherFutureFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("weatherFutureData", (Serializable) weatherFutureDetails);
        bundle.putInt("bgId",bgId);
        newFragment.setArguments(bundle);
        return newFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle args = getArguments();
        weatherFutureDetails = (List<WeatherFutureDetail>) (args != null ? args.getSerializable("weatherFutureData") : null);
        int bgId=args.getInt("bgId",-1);
        if(bgId==-1){
            bgId=R.drawable.weather_bg_null;
        }
        View rootView = inflater.inflate(R.layout.weather_future_fragment_layout, null);
//        rootView.setBackgroundResource(bgId);
        mWeatherFutureListView= (ListView) rootView.findViewById(R.id.weather_future_lv);
        mWeatherFutureAdapter=new WeatherFutureAdapter(getActivity(),weatherFutureDetails);
        mWeatherFutureListView.setAdapter(mWeatherFutureAdapter);
        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                remove();
            }
        });
        return rootView;
    }

    public void remove() {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.popBackStack();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.remove(this);
        fragmentTransaction.commit();
    }
}
