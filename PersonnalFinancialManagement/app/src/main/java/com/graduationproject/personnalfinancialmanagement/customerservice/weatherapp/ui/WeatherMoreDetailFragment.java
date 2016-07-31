package com.graduationproject.personnalfinancialmanagement.customerservice.weatherapp.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.graduationproject.personnalfinancialmanagement.R;
import com.graduationproject.personnalfinancialmanagement.customerservice.weatherapp.config.WeatherStringConfig;
import com.graduationproject.personnalfinancialmanagement.customerservice.weatherapp.config.weather.WeatherDataSum;
import com.graduationproject.personnalfinancialmanagement.utils.CommonUtils;


/**
 * Created by Administrator on 2015/11/14.
 */
public class WeatherMoreDetailFragment extends Fragment {
    WeatherDataSum weatherDataSum;
    //UI相关
    LinearLayout parentLayout;
    TextView weatherTempBigTv;
    TextView weatherNameTv;
    TextView weatherDateTv;
    TextView weatherHumidity;
    TextView weatherWind;
    TextView weatherWear;
    TextView weatherWash;
    TextView weatherTravel;
    TextView weatherExe;
    TextView weatherUv;

    static WeatherMoreDetailFragment newInstance(WeatherDataSum weatherDataSum) {
        WeatherMoreDetailFragment newFragment = new WeatherMoreDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("weatherData", weatherDataSum);
        newFragment.setArguments(bundle);
        return newFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle args = getArguments();
        weatherDataSum = (WeatherDataSum) (args != null ? args.getSerializable("weatherData") : "");
        View rootView = inflater.inflate(R.layout.weather_more_detail_fragment_layout, null);
        weatherTempBigTv = (TextView) rootView.findViewById(R.id.weather_more_detail_temp_big_tv);
        weatherNameTv = (TextView) rootView.findViewById(R.id.weather_more_detail_weather_name_tv);
        CommonUtils.changeFonts(getActivity(), weatherTempBigTv, "Roboto-Thin_3.ttf");
        CommonUtils.changeFonts(getActivity(), weatherNameTv, "Roboto-Thin_3.ttf");
        weatherTempBigTv.setText(weatherDataSum.getResult().getSk().getTemp() + WeatherStringConfig.TEMP_CHAR_2);
        weatherNameTv.setText(weatherDataSum.getResult().getToday().getWeather());
        weatherDateTv = (TextView) rootView.findViewById(R.id.weather_more_detail_date_tv);
        weatherDateTv.setText(CommonUtils.getTitleDate());
        parentLayout = (LinearLayout) rootView.findViewById(R.id.weather_more_detail_ll);
        parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                remove();
            }
        });
        weatherHumidity = (TextView) rootView.findViewById(R.id.weather_more_detail_humidity_tv);
        weatherHumidity.setText(weatherDataSum.getResult().getSk().getHumidity());
        weatherWind = (TextView) rootView.findViewById(R.id.weather_more_detail_wind_tv);
        String windStr = weatherDataSum.getResult().getSk().getWind_direction() + " "
                + weatherDataSum.getResult().getSk().getWind_strength();
        weatherWind.setText(windStr);
        weatherWear = (TextView) rootView.findViewById(R.id.weather_more_detail_wear_tv);
        weatherWear.setText(weatherDataSum.getResult().getToday().getDressing_index());
        weatherWash = (TextView) rootView.findViewById(R.id.weather_more_detail_wash_tv);
        weatherWash.setText(weatherDataSum.getResult().getToday().getWash_index());
        weatherTravel = (TextView) rootView.findViewById(R.id.weather_more_detail_travel_tv);
        weatherTravel.setText(weatherDataSum.getResult().getToday().getTravel_index());
        weatherExe = (TextView) rootView.findViewById(R.id.weather_more_detail_exe_tv);
        weatherExe.setText(weatherDataSum.getResult().getToday().getExercise_index());
        weatherUv = (TextView) rootView.findViewById(R.id.weather_more_detail_uv_tv);
        weatherUv.setText(weatherDataSum.getResult().getToday().getUv_index());
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
