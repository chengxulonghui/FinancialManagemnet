package com.graduationproject.personnalfinancialmanagement.customerservice.gasStation;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.AppCompatEditText;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.graduationproject.personnalfinancialmanagement.R;

/**
 * Created by longhui on 2016/5/25.
 */
public class GasStationConditionFragment extends Fragment {
    //TitleBar相关
    private ImageView backIv, confirm;
    private TextView titleTv;

    private TableRow lonLatQueryTr;
    private AppCompatEditText cityNameEt;
    private AppCompatEditText keyWordEt;
    private TextView cityNameKeyWordQueryTv;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.gas_station_condition_fragment, container, false);
        initView(rootView);
        return rootView;
    }

    private void initView(View v) {
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        initTitleBar(v.findViewById(R.id.gas_station_condition_title_bar));
        lonLatQueryTr = (TableRow) v.findViewById(R.id.gas_station_condition_lon_lat_query_tr);
        cityNameEt = (AppCompatEditText) v.findViewById(R.id.gas_station_condition_city_name_et);
        keyWordEt = (AppCompatEditText) v.findViewById(R.id.gas_station_condition_keyword_et);
        cityNameKeyWordQueryTv = (TextView) v.findViewById(R.id.gas_station_condition_query_tv);
        lonLatQueryTr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lonLatQuery();
            }
        });
        cityNameKeyWordQueryTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cityNameKeyWordQuery();
            }
        });
    }

    private void initTitleBar(View v) {
        backIv = (ImageView) v.findViewById(R.id.title_bar_back_iv);
        confirm = (ImageView) v.findViewById(R.id.title_bar_confirm_tv);
        titleTv = (TextView) v.findViewById(R.id.title_bar_title_tv);
        backIv = (ImageView) v.findViewById(R.id.title_bar_back_iv);
        confirm.setVisibility(View.GONE);

        titleTv.setText("加油站搜索");
        backIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                remove();
            }
        });
    }

    private void lonLatQuery() {
        ((NationalGasStationQueryActivity) getActivity()).setQueryMode(1);
        ((NationalGasStationQueryActivity) getActivity()).getData();
        remove();
    }

    private void cityNameKeyWordQuery() {
        if (!checkBeforeQuery()) {
            return;
        }
        NationalGasStationQueryActivity activity = (NationalGasStationQueryActivity) getActivity();
        activity.setCityName(cityNameEt.getText().toString().trim());
        activity.setKeyWord(keyWordEt.getText().toString().trim());
        activity.setQueryMode(0);
        activity.getData();
        remove();
    }

    private boolean checkBeforeQuery() {
        if (TextUtils.isEmpty(cityNameEt.getText())) {
            Toast.makeText(getActivity(), "城市名不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public void remove() {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.popBackStack();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.remove(this);
        fragmentTransaction.commit();
    }
}
