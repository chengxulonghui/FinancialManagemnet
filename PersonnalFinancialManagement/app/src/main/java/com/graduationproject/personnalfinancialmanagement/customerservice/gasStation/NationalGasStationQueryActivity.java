package com.graduationproject.personnalfinancialmanagement.customerservice.gasStation;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
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
import com.graduationproject.personnalfinancialmanagement.config.javabean.MemoLitePal;
import com.graduationproject.personnalfinancialmanagement.customerservice.gasStation.adapter.MyGasStationListAdapter;
import com.graduationproject.personnalfinancialmanagement.customerservice.gasStation.javabean.GasStationData;
import com.graduationproject.personnalfinancialmanagement.customerservice.gasStation.javabean.GasStationResultSum;
import com.graduationproject.personnalfinancialmanagement.customerservice.memo.MemoCreateEditFragment;
import com.graduationproject.personnalfinancialmanagement.utils.http.volley.VolleyHttpUtils;
import com.graduationproject.personnalfinancialmanagement.utils.layoututils.CustomProgressHUDManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by longhui on 2016/5/24.
 */
public class NationalGasStationQueryActivity extends MyBaseAppCompatActivity {
    public LocationClient mLocationClient = null;
    public BDLocationListener myListener = new MyLocationListener();
    public VolleyHttpUtils volleyHttpUtils;

    //TitleBar相关
    private ImageView backIv, confirm;
    private TextView titleTv;

    private RecyclerView gasStationDataListRv;

    private MyGasStationListAdapter myGasStationListAdapter;
    private String cityName;
    private String keyWord = "";
    private boolean isGetFirstData = false;
    private int queryMode = 0;//查询模式：0，根据城市名字查询；1，根据经纬度查询
    private double lon;
    private double lat;

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_national_gasstation_list);
        initView();
        initData();
        initLocationService();
    }


    public void setQueryMode(int queryMode) {
        this.queryMode = queryMode;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }


    public void setCityName(String cityName) {
        if (cityName.contains("市")) {
            cityName = cityName.replace("市", "");
        }
        if (cityName.contains("自治区")) {
            cityName = cityName.replace("自治区", "");
        }
        this.cityName = cityName;
    }

    private void initView() {
        initTitleBar(findViewById(R.id.national_gas_station_title_bar));
        gasStationDataListRv = (RecyclerView) findViewById(R.id.national_gas_station_rv);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        gasStationDataListRv.setLayoutManager(layoutManager);
        myGasStationListAdapter = new MyGasStationListAdapter(this, new ArrayList<GasStationData>());
        gasStationDataListRv.setAdapter(myGasStationListAdapter);
    }

    private void initTitleBar(View titleBar) {
        backIv = (ImageView) titleBar.findViewById(R.id.title_bar_back_iv);
        confirm = (ImageView) titleBar.findViewById(R.id.title_bar_confirm_tv);
        titleTv = (TextView) titleBar.findViewById(R.id.title_bar_title_tv);
        backIv = (ImageView) titleBar.findViewById(R.id.title_bar_back_iv);
        confirm.setVisibility(View.VISIBLE);
        confirm.setImageResource(R.drawable.icon_search);
        titleTv.setText("全国加油站查询");
        backIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toConditionChoose();
            }
        });
    }

    private void initLocationService() {
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

    private void initData() {
        volleyHttpUtils = new VolleyHttpUtils(this);
        CustomProgressHUDManager.getInstance().show(this, "获取中", 100);
    }

    private void getFirstData() {
        isGetFirstData = true;
        volleyHttpUtils.getGasStationByCityNameAndKeyword(cityName, keyWord, new Response.Listener<GasStationResultSum>() {
            @Override
            public void onResponse(GasStationResultSum gasStationResultSum) {
                CustomProgressHUDManager.getInstance().dismiss();
                if (gasStationResultSum.getResultcode().equals("200")) {
                    Log.e("TAG", gasStationResultSum.getResult().getData().get(0).getGastprice().size() + "");
                    refreshUI(gasStationResultSum.getResult().getData());
                } else {
                    Toast.makeText(NationalGasStationQueryActivity.this, "发生未知错误，获取失败", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                CustomProgressHUDManager.getInstance().dismiss();
                Toast.makeText(NationalGasStationQueryActivity.this, "发生未知错误，获取失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getData() {
        switch (queryMode) {
            case 0:
                getDataByCityKeyWord();
                break;
            case 1:
                getDataByLonLat();
                break;
        }
    }

    private void getDataByCityKeyWord() {
        CustomProgressHUDManager.getInstance().show(this, "获取中", 100);
        volleyHttpUtils.getGasStationByCityNameAndKeyword(cityName, keyWord, new Response.Listener<GasStationResultSum>() {
            @Override
            public void onResponse(GasStationResultSum gasStationResultSum) {
                CustomProgressHUDManager.getInstance().dismiss();
                if (gasStationResultSum.getResultcode().equals("200")) {
                    Log.e("TAG", gasStationResultSum.getResult().getData().get(0).getGastprice().size() + "");
                    refreshUI(gasStationResultSum.getResult().getData());
                } else {
                    Toast.makeText(NationalGasStationQueryActivity.this, "发生未知错误，获取失败", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                CustomProgressHUDManager.getInstance().dismiss();
                Toast.makeText(NationalGasStationQueryActivity.this, "发生未知错误，获取失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getDataByLonLat() {
        CustomProgressHUDManager.getInstance().show(this, "获取中", 100);
        volleyHttpUtils.getGasStationByLonLat(lon, lat, new Response.Listener<GasStationResultSum>() {
            @Override
            public void onResponse(GasStationResultSum gasStationResultSum) {
                CustomProgressHUDManager.getInstance().dismiss();
                if (gasStationResultSum.getResultcode().equals("200")) {
                    Log.e("TAG", gasStationResultSum.getResult().getData().get(0).getGastprice().size() + "");
                    refreshUI(gasStationResultSum.getResult().getData());
                } else {
                    Toast.makeText(NationalGasStationQueryActivity.this, "发生未知错误，获取失败", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                CustomProgressHUDManager.getInstance().dismiss();
                Toast.makeText(NationalGasStationQueryActivity.this, "发生未知错误，获取失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void refreshUI(List<GasStationData> gasStationDataList) {
        myGasStationListAdapter.setDataList(gasStationDataList);
    }

    @Override
    protected void onDestroy() {
        if (mLocationClient.isStarted()) {
            mLocationClient.stop();
        }
        super.onDestroy();
    }

    GasStationConditionFragment gasStationConditionFragment;

    public void toConditionChoose() {
        getSupportFragmentManager().popBackStack();
        gasStationConditionFragment = new GasStationConditionFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.national_gas_station_main_fl, gasStationConditionFragment);
        fragmentTransaction.addToBackStack("add");
        fragmentTransaction.commit();
    }

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
                lon = location.getLongitude();
                lat = location.getAltitude();
                if (!isGetFirstData) {
                    cityName = location.getCity();
                    if (location.getCity().contains("市")) {
                        cityName = location.getCity().replace("市", "");
                    }
                    if (location.getCity().contains("自治区")) {
                        cityName = location.getCity().replace("自治区", "");
                    }
                    getFirstData();
                }
            }
        }
    }
}
