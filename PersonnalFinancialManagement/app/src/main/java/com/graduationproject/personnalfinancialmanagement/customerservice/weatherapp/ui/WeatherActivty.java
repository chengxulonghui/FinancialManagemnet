package com.graduationproject.personnalfinancialmanagement.customerservice.weatherapp.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;
import com.graduationproject.personnalfinancialmanagement.R;
import com.graduationproject.personnalfinancialmanagement.base.MyBaseAppCompatActivity;
import com.graduationproject.personnalfinancialmanagement.customerservice.weatherapp.config.WeatherStringConfig;
import com.graduationproject.personnalfinancialmanagement.customerservice.weatherapp.config.city.CityDetail;
import com.graduationproject.personnalfinancialmanagement.customerservice.weatherapp.config.weather.WeatherDataSum;
import com.graduationproject.personnalfinancialmanagement.customerservice.weatherapp.config.weather.WeatherFutureDetail;
import com.graduationproject.personnalfinancialmanagement.customerservice.weatherapp.config.weather.WeatherTodayDetail;
import com.graduationproject.personnalfinancialmanagement.customerservice.weatherapp.utils.WeatherUtils;
import com.graduationproject.personnalfinancialmanagement.utils.CommonUtils;
import com.graduationproject.personnalfinancialmanagement.utils.SharePreferencesUtils;
import com.graduationproject.personnalfinancialmanagement.utils.http.NetworkUtils;
import com.graduationproject.personnalfinancialmanagement.utils.http.volley.VolleyHttpUtils;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;


import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;


public class WeatherActivty extends MyBaseAppCompatActivity {
    private PtrClassicFrameLayout refreshLayout;
    private Handler mHandler;
    private VolleyHttpUtils mHttpUtils;
    private Response.Listener<WeatherDataSum> mFirstWeatherReponse;
    private Response.ErrorListener myFirstWeatherErrorListener;
    private Response.Listener<WeatherDataSum> mRefreshWeatherReponse;
    private Response.ErrorListener myRefreshWeatherErrorListener;
    private FrameLayout mMainFrameLayout;
    //天气导航栏相关
    private View weatherNavigatorBarView;
    private TextView weatherNavigatorCityTv;
    private TextView weatherNavigatorDateTv;
    private ImageView weatherNavigatorCityChooseIv;
    private ImageView weatherNavigatorShareIv;
    private ImageView weatherNavigatorProgressIv;
    //当前天气相关
    private TextView weatherTempBigTv;
    private TextView weatherNameTv;
    private ImageView weatherAirConditionIv;
    private TextView weatherAirConditionTv;
    private TextView weatherPublishTimeTv;
    private ImageView weatherWindIconIv;
    private TextView weatherWindLevelTv;
    private TextView weatherWindName;
    private LinearLayout weatherView;
    //底部今天天气Box相关
    private View weatherTodayBoxView;
    private TextView weatherTodayBoxDateTv;
    private TextView weatherTodayBoxWindTv;
    private TextView weatherTodayBoxTempTv;
    private TextView weatherTodayBoxWeatherTv;
    private ImageView weatherTodayBoxWeatherIconTv;
    //底部明天天气Box相关
    private View weatherTomorrowBoxView;
    private TextView weatherTomorrowBoxDateTv;
    private TextView weatherTomorrowBoxWindTv;
    private TextView weatherTomorrowBoxTempTv;
    private TextView weatherTomorrowBoxWeatherTv;
    private ImageView weatherTomorrowBoxWeatherIconTv;

    private List<WeatherFutureDetail> futureWeatherList;

    private final String SP_TAG_CITY_NAME = "cityName";
    private final String SP_TAG_WEATHER_DATA = "weatherData";

    private String cityName = "";

    private FragmentManager fragmentManager;
    private WeatherMoreDetailFragment weatherMoreDetailFragment;
    private WeatherFutureFragment weatherFutureFragment;
    private WeatherDataSum currentWeatherDataSum;

    private CityDetail locationCityDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_activty);
        initView();
        initData();
    }

    private void initView() {
        mHandler = new Handler();
        initTodayBoxView();
        initTomorrowBoxView();
        mMainFrameLayout = (FrameLayout) findViewById(R.id.weather_frame);
        weatherNavigatorBarView = findViewById(R.id.weather_action_bar);
        weatherView = (LinearLayout) findViewById(R.id.weather_parent_ll);
        weatherNavigatorCityTv = (TextView) weatherNavigatorBarView.findViewById(R.id.common_title_bar_title_above);
        weatherNavigatorCityChooseIv = (ImageView) weatherNavigatorBarView.findViewById(R.id.common_title_bar_left_iv);
        weatherNavigatorShareIv = (ImageView) weatherNavigatorBarView.findViewById(R.id.common_title_bar_right_iv);
        weatherNavigatorProgressIv = (ImageView) weatherNavigatorBarView.findViewById(R.id.common_title_bar_title_progress);
        AnimationDrawable animationDrawable = (AnimationDrawable) weatherNavigatorProgressIv.getDrawable();
        if (!animationDrawable.isRunning()) {
            animationDrawable.start();
        }
        weatherNavigatorShareIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showShare();
            }
        });
        weatherNavigatorCityChooseIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WeatherActivty.this, CityChooseActivity.class);
                if (locationCityDetail != null) {
                    intent.putExtra("location", locationCityDetail);
                }
                startActivityForResult(intent, 100);
            }
        });
        weatherNavigatorDateTv = (TextView) weatherNavigatorBarView.findViewById(R.id.common_title_bar_title_bottom);
        weatherNavigatorCityTv.setText("广州");
        weatherNavigatorDateTv.setText(CommonUtils.getTitleDate());
        weatherTempBigTv = (TextView) findViewById(R.id.weather_temp_big_tv);
        weatherNameTv = (TextView) findViewById(R.id.weather_weather_name_tv);
        weatherAirConditionIv = (ImageView) findViewById(R.id.weather_air_condition_iv);
        weatherAirConditionTv = (TextView) findViewById(R.id.weather_air_condition_tv);
        weatherPublishTimeTv = (TextView) findViewById(R.id.weather_publish_time);
        weatherWindIconIv = (ImageView) findViewById(R.id.weather_wind_icon_iv);
        weatherWindLevelTv = (TextView) findViewById(R.id.weather_wind_level_tv);
        weatherWindName = (TextView) findViewById(R.id.weather_wind_name_tv);
        refreshLayout = (PtrClassicFrameLayout) findViewById(R.id.weather_refresh_ptr_layout);
        CommonUtils.changeFonts(this, weatherTempBigTv, "Roboto-Thin_3.ttf");
        CommonUtils.changeFonts(this, weatherNameTv, "Roboto-Thin_3.ttf");
        refreshLayout.setLastUpdateTimeKey("2015-01-01");
        refreshLayout.setPtrHandler(new PtrHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                refreshWeather();
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                // 默认实现，根据实际情况做改动
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
            }
        });
        weatherTempBigTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentWeatherDataSum != null) {
                    trans2MoreWeatherDetail(currentWeatherDataSum);
                }
            }
        });
        weatherTodayBoxView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (futureWeatherList == null) {
                    return;
                }
                if (futureWeatherList.size() <= 0) {
                    return;
                }
                List<WeatherFutureDetail> fiveDetails = new ArrayList<WeatherFutureDetail>();
                for (WeatherFutureDetail weatherFutureDetail : futureWeatherList) {
                    if (fiveDetails.size() < 5) {
                        fiveDetails.add(weatherFutureDetail);
                    } else {
                        break;
                    }
                }
                trans2FutureWeatherDetail(fiveDetails);
            }
        });
        weatherTomorrowBoxView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                weatherTodayBoxView.performClick();
            }
        });
    }

    private void initTodayBoxView() {
        weatherTodayBoxView = findViewById(R.id.weather_today_box);
        weatherTodayBoxDateTv = (TextView) weatherTodayBoxView.findViewById(R.id.weather_info_box_date_tv);
        weatherTodayBoxWindTv = (TextView) weatherTodayBoxView.findViewById(R.id.weather_info_box_wind_tv);
        weatherTodayBoxTempTv = (TextView) weatherTodayBoxView.findViewById(R.id.weather_info_box_temp_tv);
        weatherTodayBoxWeatherTv = (TextView) weatherTodayBoxView.findViewById(R.id.weather_info_box_weather_tv);
        weatherTodayBoxWeatherIconTv = (ImageView) weatherTodayBoxView.findViewById(R.id.weather_info_box_weather_icon_iv);
    }

    private void initTomorrowBoxView() {
        weatherTomorrowBoxView = findViewById(R.id.weather_tomorrow_box);
        weatherTomorrowBoxDateTv = (TextView) weatherTomorrowBoxView.findViewById(R.id.weather_info_box_date_tv);
        weatherTomorrowBoxWindTv = (TextView) weatherTomorrowBoxView.findViewById(R.id.weather_info_box_wind_tv);
        weatherTomorrowBoxTempTv = (TextView) weatherTomorrowBoxView.findViewById(R.id.weather_info_box_temp_tv);
        weatherTomorrowBoxWeatherTv = (TextView) weatherTomorrowBoxView.findViewById(R.id.weather_info_box_weather_tv);
        weatherTomorrowBoxWeatherIconTv = (ImageView) weatherTomorrowBoxView.findViewById(R.id.weather_info_box_weather_icon_iv);
    }

    private void initData() {
        fragmentManager = getSupportFragmentManager();
        futureWeatherList = new ArrayList<WeatherFutureDetail>();
        getWeatherDataFromSp();
        cityName = SharePreferencesUtils.getSharedPreferences(this).getString(SP_TAG_CITY_NAME, null);
        mHttpUtils = new VolleyHttpUtils(this);
        mFirstWeatherReponse = new Response.Listener<WeatherDataSum>() {
            @Override
            public void onResponse(WeatherDataSum response) {
                weatherNavigatorProgressIv.setVisibility(View.GONE);
                showWeatherData(response);
            }
        };
        mRefreshWeatherReponse = new Response.Listener<WeatherDataSum>() {
            @Override
            public void onResponse(WeatherDataSum response) {
                if (weatherNavigatorProgressIv.getVisibility() == View.VISIBLE) {
                    weatherNavigatorProgressIv.setVisibility(View.GONE);
                }
                refreshLayout.refreshComplete();
                showWeatherData(response);
            }
        };
        myFirstWeatherErrorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (weatherNavigatorProgressIv.getVisibility() == View.VISIBLE) {
                    weatherNavigatorProgressIv.setVisibility(View.GONE);
                }
                Toast.makeText(WeatherActivty.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("TAG", error.getMessage() + "");
            }
        };
        myRefreshWeatherErrorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (weatherNavigatorProgressIv.getVisibility() == View.VISIBLE) {
                    weatherNavigatorProgressIv.setVisibility(View.GONE);
                }
                refreshLayout.refreshComplete();
                Toast.makeText(WeatherActivty.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        };
        if (TextUtils.isEmpty(cityName)) {
            weatherNavigatorCityTv.setText("定位中");
            return;
        } else {
            IS_LOCATION_SUCCESS = true;
            weatherNavigatorCityTv.setText(cityName);
            try {
                if (NetworkUtils.isNetworkAvailable(this)) {
                    mHttpUtils.getWeatherInfoHttpRequest(cityName, mFirstWeatherReponse, myFirstWeatherErrorListener);
                } else {
                    Toast.makeText(this, "网络连接断开,请检查", Toast.LENGTH_SHORT).show();
                    weatherNavigatorProgressIv.setVisibility(View.GONE);
                }
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "发生未知错误，请稍后刷新一下", Toast.LENGTH_SHORT).show();
            }
        }
        initLocation();
    }

    private void refreshWeather() {
        try {
            if (NetworkUtils.isNetworkAvailable(this)) {
                mHttpUtils.getWeatherInfoHttpRequest(cityName, mRefreshWeatherReponse, myRefreshWeatherErrorListener);
            } else {
                refreshLayout.refreshComplete();
                Toast.makeText(this, "网络连接断开,请检查", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "发生未知错误，刷新失败，请稍后重试", Toast.LENGTH_SHORT).show();
        }
    }

    public LocationClient mLocationClient = null;
    public BDLocationListener myListener = new MyLocationListener();

    private void initLocation() {
        mLocationClient = new LocationClient(getApplicationContext());     //声明LocationClient类
        mLocationClient.registerLocationListener(myListener);    //注册监听函数
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy
        );//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        int span = 1000;
        option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(true);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤gps仿真结果，默认需要
        mLocationClient.setLocOption(option);
        mLocationClient.start();
    }


    boolean IS_LOCATION_SUCCESS = false;

    class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            //Receive Location
            StringBuffer sb = new StringBuffer(256);
            sb.append("time : ");
            sb.append(location.getTime());
            sb.append("\nerror code : ");
            sb.append(location.getLocType());
            sb.append("\nlatitude : ");
            sb.append(location.getLatitude());
            sb.append("\nlontitude : ");
            sb.append(location.getLongitude());
            sb.append("\nradius : ");
            sb.append(location.getRadius());
            if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果
                sb.append("\nspeed : ");
                sb.append(location.getSpeed());// 单位：公里每小时
                sb.append("\nsatellite : ");
                sb.append(location.getSatelliteNumber());
                sb.append("\nheight : ");
                sb.append(location.getAltitude());// 单位：米
                sb.append("\ndirection : ");
                sb.append(location.getDirection());// 单位度
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());
                sb.append("\ndescribe : ");
                sb.append("gps定位成功");

            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());
                //运营商信息
                sb.append("\noperationers : ");
                sb.append(location.getOperators());
                sb.append("\ndescribe : ");
                sb.append("网络定位成功");
            } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
                sb.append("\ndescribe : ");
                sb.append("离线定位成功，离线定位结果也是有效的");
            } else if (location.getLocType() == BDLocation.TypeServerError) {
                sb.append("\ndescribe : ");
                sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
            } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                sb.append("\ndescribe : ");
                sb.append("网络不同导致定位失败，请检查网络是否通畅");
            } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                sb.append("\ndescribe : ");
                sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
            }
            sb.append("\nlocationdescribe : ");
            sb.append(location.getLocationDescribe());// 位置语义化信息
            List<Poi> list = location.getPoiList();// POI数据
            if (list != null) {
                sb.append("\npoilist size = : ");
                sb.append(list.size());
                for (Poi p : list) {
                    sb.append("\npoi= : ");
                    sb.append(p.getId() + " " + p.getName() + " " + p.getRank());
                }
            }
            Log.i("BaiduLocationApiDem", sb.toString());
            Log.e("BaiduLocationApiDem", location.getCity());
            if (!TextUtils.isEmpty(location.getCity())) {
                cityName = location.getCity();
                if (location.getCity().contains("市")) {
                    cityName = location.getCity().replace("市", "");
                }
                if (location.getCity().contains("自治区")) {
                    cityName = location.getCity().replace("自治区", "");
                }
                locationCityDetail = new CityDetail();
                locationCityDetail.setProvince(location.getProvince());
                locationCityDetail.setCity(cityName);
                locationCityDetail.setDistrict(location.getDistrict().split("区")[0]);
                locationCityDetail.setId(location.getCityCode());
                Log.e("TAG", location.getCity() + " " + location.getDistrict().split("区")[0]);
                if (!IS_LOCATION_SUCCESS) {
                    setWeatherDataAfterLocation();
                    IS_LOCATION_SUCCESS = true;
                }
            }
        }
    }

    private void setWeatherDataAfterLocation() {
        if (TextUtils.isEmpty(locationCityDetail.getDistrict())) {
            cityName = locationCityDetail.getCity();
        } else {
            cityName = locationCityDetail.getDistrict();
        }
        SharePreferencesUtils.saveString2Sp(this, SP_TAG_CITY_NAME, cityName);
        weatherNavigatorCityTv.setText(cityName);
        refreshWeather();
    }

    int currentBgId = -1;

    private void showWeatherData(WeatherDataSum weatherDataSum) {
        if (!weatherDataSum.getResultcode().equals("200")) {
            Toast.makeText(this, weatherDataSum.getReason(), Toast.LENGTH_SHORT).show();
            return;
        }
        currentWeatherDataSum = weatherDataSum;
        currentBgId = WeatherUtils.getWeatherBgId(weatherDataSum.getResult().getToday().getWeather_id());
        mMainFrameLayout.setBackgroundResource(currentBgId);
        weatherTempBigTv.setText("" + weatherDataSum.getResult().getSk().getTemp() + WeatherStringConfig.TEMP_CHAR_2);
        String humidityStr = weatherDataSum.getResult().getSk().getHumidity();
        int humidity = Integer.parseInt((humidityStr.split("%"))[0]);
        if (humidity > 30 && humidity < 80) {
            weatherAirConditionIv.setImageResource(R.drawable.notif_level1);
        } else {
            weatherAirConditionIv.setImageResource(R.drawable.notif_level2);
        }
        weatherAirConditionTv.setText("湿度:" + humidity + "%");
        weatherPublishTimeTv.setText(weatherDataSum.getResult().getSk().getTime() + "发布");
        String weatherNameStr = weatherDataSum.getResult().getToday().getWeather();
        weatherNameTv.setText(weatherNameStr);
        if (weatherNameStr.length() > 2) {
            weatherNameTv.setTextSize(20);
        } else {
            weatherNameTv.setTextSize(30);
        }
        weatherWindLevelTv.setText(weatherDataSum.getResult().getSk().getWind_strength());
        String windStr = weatherDataSum.getResult().getSk().getWind_direction();
        weatherWindName.setText(windStr);
        if (windStr.equals("北风")) {
            weatherWindIconIv.setImageResource(R.drawable.trend_wind_1);
        } else if (windStr.equals("东北风")) {
            weatherWindIconIv.setImageResource(R.drawable.trend_wind_2);
        } else if (windStr.equals("东风")) {
            weatherWindIconIv.setImageResource(R.drawable.trend_wind_3);
        } else if (windStr.equals("东南风")) {
            weatherWindIconIv.setImageResource(R.drawable.trend_wind_4);
        } else if (windStr.equals("南风")) {
            weatherWindIconIv.setImageResource(R.drawable.trend_wind_5);
        } else if (windStr.equals("西南风")) {
            weatherWindIconIv.setImageResource(R.drawable.trend_wind_6);
        } else if (windStr.equals("西风")) {
            weatherWindIconIv.setImageResource(R.drawable.trend_wind_7);
        } else if (windStr.equals("西北风")) {
            weatherWindIconIv.setImageResource(R.drawable.trend_wind_8);
        }
        setWeatherTodayBoxData(weatherDataSum);
        setWeatherTomorrowBoxData(weatherDataSum);
        saveWeatherData2Sp(weatherDataSum);
    }

    private void setWeatherTodayBoxData(WeatherDataSum dataSum) {
        WeatherTodayDetail todayDetail = dataSum.getResult().getToday();
        weatherTodayBoxDateTv.setText("今天");
        weatherTodayBoxWindTv.setText(todayDetail.getWind().split("风")[0] + "风");
        weatherTodayBoxTempTv.setText(todayDetail.getTemperature());
        weatherTodayBoxWeatherTv.setText(todayDetail.getWeather());
        weatherTodayBoxWeatherIconTv.setImageResource(WeatherUtils.getWeatherBoxIconId(todayDetail.getWeather_id()));
    }

    private void setWeatherTomorrowBoxData(WeatherDataSum dataSum) {

        Map<String, WeatherFutureDetail> future = dataSum.getResult().getFuture();
        for (Map.Entry<String, WeatherFutureDetail> entry : future.entrySet()) {
            futureWeatherList.add(entry.getValue());
        }
        WeatherFutureDetail tomorrowDetail = futureWeatherList.get(1);
        weatherTomorrowBoxDateTv.setText("明天");
        weatherTomorrowBoxWindTv.setText(tomorrowDetail.getWind().split("风")[0] + "风");
        weatherTomorrowBoxTempTv.setText(tomorrowDetail.getTemperature());
        weatherTomorrowBoxWeatherTv.setText(tomorrowDetail.getWeather());
        weatherTomorrowBoxWeatherIconTv.setImageResource(WeatherUtils.getWeatherBoxIconId(tomorrowDetail.getWeather_id()));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 200) {
            switch (requestCode) {
                case 100:
                    cityName = data.getStringExtra(SP_TAG_CITY_NAME);
                    SharePreferencesUtils.saveString2Sp(WeatherActivty.this, SP_TAG_CITY_NAME, cityName);
                    refreshWeather();
                    weatherNavigatorCityTv.setText(cityName);
                    break;
            }
        }
    }

    private void trans2MoreWeatherDetail(WeatherDataSum weatherDataSum) {
        fragmentManager.popBackStack();
        weatherMoreDetailFragment = WeatherMoreDetailFragment.newInstance(weatherDataSum);
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.weather_frame, weatherMoreDetailFragment);
        fragmentTransaction.addToBackStack("add");
        fragmentTransaction.commit();
    }

    private void trans2FutureWeatherDetail(List<WeatherFutureDetail> futureDetails) {
        fragmentManager.popBackStack();
        weatherFutureFragment = WeatherFutureFragment.newInstance(futureDetails, currentBgId);
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.weather_frame, weatherFutureFragment);
        fragmentTransaction.addToBackStack("add");
        fragmentTransaction.commit();
    }

    //保存天气信息进Sp
    private void saveWeatherData2Sp(WeatherDataSum weatherDataSum) {
        String weatherDataStr = null;
        try {
            weatherDataStr = serialize(weatherDataSum);
        } catch (IOException e) {
            weatherDataStr = null;
            e.printStackTrace();
        }
        if (TextUtils.isEmpty(weatherDataStr)) {
            return;
        }
        SharePreferencesUtils.saveString2Sp(this, SP_TAG_WEATHER_DATA, weatherDataStr);
    }

    //从Sp中获取WeatherData并显示
    private void getWeatherDataFromSp() {
        String weatherDataStr = SharePreferencesUtils.getSharedPreferences(this).getString(SP_TAG_WEATHER_DATA, null);
        if (TextUtils.isEmpty(weatherDataStr)) {
            return;
        }
        WeatherDataSum weatherDataSum = deSerialization(weatherDataStr);
        if (weatherDataSum != null) {
            showWeatherData(weatherDataSum);
        }
    }

    /**
     * 序列化对象
     *
     * @return
     * @throws IOException
     */
    private String serialize(WeatherDataSum weatherDataSum) throws IOException {
//        startTime = System.currentTimeMillis();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(
                byteArrayOutputStream);
        objectOutputStream.writeObject(weatherDataSum);
        String serStr = byteArrayOutputStream.toString("ISO-8859-1");
        serStr = java.net.URLEncoder.encode(serStr, "UTF-8");
        objectOutputStream.close();
        byteArrayOutputStream.close();
//        LogConfig.i("serial serialize str =" + serStr);
//        endTime = System.currentTimeMillis();
//        LogConfig.i("serial 序列化耗时为:" + (endTime - startTime));
        return serStr;
    }

    /**
     * 反序列化对象
     *
     * @param str
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private WeatherDataSum deSerialization(String str) {
//        startTime = System.currentTimeMillis();
        try {
            String redStr = null;
            redStr = java.net.URLDecoder.decode(str, "UTF-8");
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
                    redStr.getBytes("ISO-8859-1"));
            ObjectInputStream objectInputStream = new ObjectInputStream(
                    byteArrayInputStream);
            WeatherDataSum weatherData = (WeatherDataSum) objectInputStream.readObject();
            objectInputStream.close();
            byteArrayInputStream.close();
            return weatherData;
        } catch (Exception e) {
            e.printStackTrace();
        }
//        endTime = System.currentTimeMillis();
//        LogConfig.i("serial 反序列化耗时为:" + (endTime - startTime));
        return null;
    }

    //分享
    private void showShare() {
        if (currentWeatherDataSum == null) {
            Toast.makeText(this, "暂无天气资料，无法分享", Toast.LENGTH_SHORT).show();
            return;
        }
        ShareSDK.initSDK(this);
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();
        // 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
        //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        // text是分享文本，所有平台都需要这个字段
        oks.setText(buildShareString());
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.show(this);
    }
//
//    private void showShares() {
//        ShareSDK.initSDK(this);
//        OnekeyShare oks = new OnekeyShare();
//        //关闭sso授权
//        oks.disableSSOWhenAuthorize();
//// 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
//        //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
//        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
//        oks.setTitle("dd");
//        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
//        oks.setTitleUrl("http://sharesdk.cn");
//        // text是分享文本，所有平台都需要这个字段
//        oks.setText("我是分享文本，啦啦啦~");
//        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
////        oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
//        // url仅在微信（包括好友和朋友圈）中使用
//        oks.setUrl("http://sharesdk.cn");
//        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
//        oks.setComment("我是测试评论文本");
//        // site是分享此内容的网站名称，仅在QQ空间使用
//        oks.setSite(getString(R.string.app_name));
//        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
//        oks.setSiteUrl("http://sharesdk.cn");
//// 启动分享GUI
//        oks.show(this);
//    }

    private String buildShareString() {
        StringBuilder sb = new StringBuilder();
        sb.append("天气小预报-" + cityName);
        sb.append("\n");
        sb.append("今天天气:" + currentWeatherDataSum.getResult().getToday().getWeather());
        sb.append("\n");
        sb.append("当前气温:" + currentWeatherDataSum.getResult().getSk().getTemp() + WeatherStringConfig.TEMP_CHAR);
        sb.append("\n");
        sb.append("明天天气:" + futureWeatherList.get(1).getWeather());
        sb.append("\n");
        sb.append("明天气温:" + futureWeatherList.get(1).getTemperature());
        sb.append("\n");
        sb.append("穿着建议：" + currentWeatherDataSum.getResult().getToday().getDressing_advice());
        sb.append("\n");
        sb.append("--个人财务管理 " + currentWeatherDataSum.getResult().getSk().getTime() + "发布");
        return sb.toString();
    }

    private String saveScreenShot() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss", Locale.US);
        String picName = sdf.format(new Date()) + ".png";
        String fName = Environment.getExternalStorageDirectory() + File.separator + "WeatherShare";
        String picAsName = fName + File.separator + picName;
        File file = new File(fName);
        if (!file.exists()) {
            file.mkdirs();
        }
        new MySavePicThread(picAsName).start();
        return picAsName;
    }


    private class MySavePicThread extends Thread {
        String fileName;

        private MySavePicThread(String file) {
            this.fileName = file;
        }

        @Override
        public void run() {
            mMainFrameLayout.setDrawingCacheEnabled(true);
            mMainFrameLayout.buildDrawingCache();
            Bitmap bitmap = mMainFrameLayout.getDrawingCache();
            if (bitmap != null) {
                FileOutputStream out = null;
                try {
                    out = new FileOutputStream(fileName);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 50,
                            out);
                    out.flush();
                    out.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Log.e("TAG", fileName);
            }
        }
    }


    @Override
    protected void onDestroy() {
        if (mLocationClient.isStarted()) {
            mLocationClient.stop();
        }
        super.onDestroy();
    }
}
