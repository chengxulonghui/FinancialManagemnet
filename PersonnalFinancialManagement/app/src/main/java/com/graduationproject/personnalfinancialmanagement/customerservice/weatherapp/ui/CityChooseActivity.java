package com.graduationproject.personnalfinancialmanagement.customerservice.weatherapp.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.graduationproject.personnalfinancialmanagement.R;
import com.graduationproject.personnalfinancialmanagement.customerservice.weatherapp.config.city.CityDetail;
import com.graduationproject.personnalfinancialmanagement.customerservice.weatherapp.config.city.CityResult;
import com.graduationproject.personnalfinancialmanagement.utils.CommonUtils;
import com.graduationproject.personnalfinancialmanagement.utils.SharePreferencesUtils;
import com.graduationproject.personnalfinancialmanagement.utils.http.NetworkUtils;
import com.graduationproject.personnalfinancialmanagement.utils.http.volley.VolleyHttpUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Administrator on 2015/11/14.
 */
public class CityChooseActivity extends FragmentActivity {
    CityChooseAdapter mCityChooseAdapter;
    ListView mCityChooseListView;
    TableRow tipsTr;
    FrameLayout cityChooseContentFl;
    List<CityDetail> sumCityDetails;
    //导航栏相关
    View titleBar;
    ImageView titleBarBack;
    //搜索栏相关
    View searchView;
    EditText searchEditText;
    MyTextWatcher myTextWatcher;
    //Http相关
    private VolleyHttpUtils mHttpUtils;
    private Response.Listener<CityResult> mCityReponse;
    private Response.ErrorListener myCityErrorListener;

    private CityDetail location;
    Handler myHandler;
    private final String SP_TAG_CITY_DATA = "citys";
    private final String SP_TAG_NEVER_SHOW_DIALOG = "neverShowDialog";
    private final String SP_TAG_SHOW_PROVINCE_CITY = "showProvinceCity";

    private boolean isNeverShowDialog = false;
    private boolean isShowProvinceCity = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.city_choose_activity);
        initView();
        initData();
    }

    private void initView() {
        myHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 1:
                        List<CityDetail> cityDetails = (List<CityDetail>) msg.obj;
                        mCityChooseAdapter.setDataList(cityDetails);
                        break;
                }
            }
        };
        titleBar = findViewById(R.id.city_choose_title_bar);
        titleBarBack = (ImageView) titleBar.findViewById(R.id.title_bar_back_iv);
        searchView = findViewById(R.id.city_choose_search_bar);
        searchEditText = (EditText) searchView.findViewById(R.id.city_choose_search_et);
        myTextWatcher = new MyTextWatcher();
        searchEditText.addTextChangedListener(myTextWatcher);
        mCityChooseListView = (ListView) findViewById(R.id.city_choose_list_view);
        tipsTr = (TableRow) findViewById(R.id.city_choose_tip_tr);
        cityChooseContentFl = (FrameLayout) findViewById(R.id.city_choose_content_fl);
        mCityChooseAdapter = new CityChooseAdapter(this, new ArrayList<CityDetail>());
        mCityChooseListView.setAdapter(mCityChooseAdapter);
        mCityChooseListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                String cityName = mCityChooseAdapter.getDataList().get(position).getDistrict();
                intent.putExtra("cityName", cityName);
                setResult(200, intent);
                onBackPressed();
            }
        });
        mCityChooseListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == SCROLL_STATE_TOUCH_SCROLL || scrollState == SCROLL_STATE_FLING) {
                    CommonUtils.hideSoftKeyboard(CityChooseActivity.this, mCityChooseListView);
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
        titleBarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void initData() {
        location = (CityDetail) getIntent().getSerializableExtra("location");
        isNeverShowDialog = SharePreferencesUtils.getSharedPreferences(this).getBoolean(SP_TAG_NEVER_SHOW_DIALOG, false);
        isShowProvinceCity = SharePreferencesUtils.getSharedPreferences(this).getBoolean(SP_TAG_SHOW_PROVINCE_CITY, true);
        mHttpUtils = new VolleyHttpUtils(this);
        mCityReponse = new Response.Listener<CityResult>() {
            @Override
            public void onResponse(CityResult response) {
                sumCityDetails = response.getResult();
                saveCityData2Sp(response.getResult());
                cityChooseContentFl.setVisibility(View.VISIBLE);
                mCityChooseAdapter.getDataList().addAll(response.getResult());
                mCityChooseAdapter.notifyDataSetChanged();
                tipsTr.setVisibility(View.GONE);
                if (location != null) {
                    if (!isNeverShowDialog) {
                        showLocationChooseDialog();
                    } else {
                        if (isShowProvinceCity) {
                            setLocationProvince();
                        }
                    }
                }
            }
        };
        myCityErrorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(CityChooseActivity.this, "获取城市失败。", Toast.LENGTH_SHORT).show();
            }
        };
        sumCityDetails = getCityDataFromSp();
        if (sumCityDetails == null) {
            try {
                if (NetworkUtils.isNetworkAvailable(this)) {
                    mHttpUtils.getCityListHttpRequest(mCityReponse, myCityErrorListener);
                } else {
                    Toast.makeText(this, "网络连接断开,请检查", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            cityChooseContentFl.setVisibility(View.VISIBLE);
            mCityChooseAdapter.getDataList().addAll(sumCityDetails);
            mCityChooseAdapter.notifyDataSetChanged();
            tipsTr.setVisibility(View.GONE);
            if (location != null) {
                if (!isNeverShowDialog) {
                    showLocationChooseDialog();
                } else {
                    if (isShowProvinceCity) {
                        setLocationProvince();
                    }
                }
            }
        }
    }

    boolean isNeverShowCbCheck;

    private void showLocationChooseDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.city_choose_dialog_view, null);
        CheckBox cb = (CheckBox) dialogView.findViewById(R.id.city_choose_dialog_message_cb);
        cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isNeverShowCbCheck = isChecked;
            }
        });
        dialog.setView(dialogView);
        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                setLocationProvince();
                SharePreferencesUtils.saveBoolean2Sp(CityChooseActivity.this, SP_TAG_NEVER_SHOW_DIALOG, isNeverShowCbCheck);
                if (isNeverShowCbCheck) {
                    SharePreferencesUtils.saveBoolean2Sp(CityChooseActivity.this, SP_TAG_SHOW_PROVINCE_CITY, true);
                }
                dialog.dismiss();
            }
        });
        dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SharePreferencesUtils.saveBoolean2Sp(CityChooseActivity.this, SP_TAG_NEVER_SHOW_DIALOG, isNeverShowCbCheck);
                if (isNeverShowCbCheck) {
                    SharePreferencesUtils.saveBoolean2Sp(CityChooseActivity.this, SP_TAG_SHOW_PROVINCE_CITY, false);
                }
                dialog.dismiss();
            }
        });
        dialog.create();
        dialog.show();
    }

    private void setLocationProvince() {
        String provinceString;
        if (location.getProvince().contains("省")) {
            provinceString = location.getProvince().split("省")[0];
        } else if (location.getProvince().contains("自治区")) {
            if (location.getProvince().contains("回族自治区")) {
                provinceString = location.getProvince().split("回族自治区")[0];
            } else {
                provinceString = location.getProvince().split("自治区")[0];
            }
        } else if (location.getProvince().contains("市")) {
            provinceString = location.getProvince().split("市")[0];
        } else {
            provinceString = location.getProvince();
        }
        searchEditText.setText(provinceString);
    }

    //保存天气信息进Sp
    private void saveCityData2Sp(List<CityDetail> cityDetails) {
        String cityDetailsDataStr = null;
        try {
            cityDetailsDataStr = serialize(cityDetails);
        } catch (IOException e) {
            cityDetailsDataStr = null;
            e.printStackTrace();
        }
        if (TextUtils.isEmpty(cityDetailsDataStr)) {
            return;
        }
        SharePreferencesUtils.saveString2Sp(this, SP_TAG_CITY_DATA, cityDetailsDataStr);
    }

    //从Sp中获取WeatherData并显示
    private List<CityDetail> getCityDataFromSp() {
        String weatherDataStr = SharePreferencesUtils.getSharedPreferences(this).getString(SP_TAG_CITY_DATA, null);
        if (TextUtils.isEmpty(weatherDataStr)) {
            return null;
        }
        List<CityDetail> cityDetails = deSerialization(weatherDataStr);
        return cityDetails;
    }

    private class MyTextWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s.length() <= 0) {
                mCityChooseAdapter.setDataList(sumCityDetails);
            } else if (!CommonUtils.isAllChinese(s.toString())) {
                Toast.makeText(CityChooseActivity.this, "请输入中文！", Toast.LENGTH_SHORT).show();
            } else {
                new MySearchThread(s.toString()).start();
            }
            searchEditText.setSelection(searchEditText.getText().length());
        }
    }

    private class MySearchThread extends Thread {
        String toSearch;

        public MySearchThread(String searchStr) {
            this.toSearch = searchStr;
        }

        @Override
        public void run() {
            List<CityDetail> cityDetails = new ArrayList<CityDetail>();
            for (CityDetail cityDetail : sumCityDetails) {
                if (cityDetail.getDistrict().contains(toSearch) || cityDetail.getCity().contains(toSearch) || cityDetail.getProvince().contains(toSearch)) {
                    cityDetails.add(cityDetail);
                }
            }
            Message msg = myHandler.obtainMessage();
            msg.what = 1;
            msg.obj = cityDetails;
            myHandler.sendMessage(msg);
        }
    }

    /**
     * 序列化对象
     *
     * @return
     * @throws IOException
     */
    private String serialize(List<CityDetail> cityDetails) throws IOException {
//        startTime = System.currentTimeMillis();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(
                byteArrayOutputStream);
        objectOutputStream.writeObject(cityDetails);
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
    private List<CityDetail> deSerialization(String str) {
//        startTime = System.currentTimeMillis();
        try {
            String redStr = null;
            redStr = java.net.URLDecoder.decode(str, "UTF-8");
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
                    redStr.getBytes("ISO-8859-1"));
            ObjectInputStream objectInputStream = new ObjectInputStream(
                    byteArrayInputStream);
            List<CityDetail> cityDetails = (List<CityDetail>) objectInputStream.readObject();
            objectInputStream.close();
            byteArrayInputStream.close();
            return cityDetails;
        } catch (Exception e) {
            e.printStackTrace();
        }
//        endTime = System.currentTimeMillis();
//        LogConfig.i("serial 反序列化耗时为:" + (endTime - startTime));
        return null;
    }
}
