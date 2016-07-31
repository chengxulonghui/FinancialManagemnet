package com.graduationproject.personnalfinancialmanagement.financialalarm;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.graduationproject.personnalfinancialmanagement.R;
import com.graduationproject.personnalfinancialmanagement.config.StringConfig;
import com.graduationproject.personnalfinancialmanagement.config.javabean.FinancialAlarm;
import com.graduationproject.personnalfinancialmanagement.utils.DateUtils;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.Date;

/**
 * Created by longhui on 2016/5/24.
 */
public class FinancialAlarmDetailFragment extends Fragment {
    //TitleBar相关
    private ImageView backIv, confirm;
    private TextView titleTv;

    private TextView dateTv;
    private TextView categoryTv;
    private TextView moneyTv;
    private TextView timeTv;
    private TextView remarkTv;

    private FinancialAlarm financialAlarm;

    static FinancialAlarmDetailFragment newInstance(FinancialAlarm data) {
        FinancialAlarmDetailFragment newFragment = new FinancialAlarmDetailFragment();
        if (data != null) {
            Bundle bundle = new Bundle();
            bundle.putSerializable(StringConfig.FINANCIAL_ALARM_TAG, (Serializable) data);
            newFragment.setArguments(bundle);
        }
        return newFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle args = getArguments();
        if (args != null) {
            financialAlarm = (FinancialAlarm) args.getSerializable(StringConfig.FINANCIAL_ALARM_TAG);
        }
        View rootView = inflater.inflate(R.layout.financial_alarm_detail_fragment, null);
        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        initView(rootView);
        initData();
        return rootView;
    }

    private void initView(View v) {
        initTitleBar(v.findViewById(R.id.financial_alarm_detail_title_bar));
        dateTv = (TextView) v.findViewById(R.id.financial_alarm_detail_date_tv);
        categoryTv = (TextView) v.findViewById(R.id.financial_alarm_detail_category_tv);
        moneyTv = (TextView) v.findViewById(R.id.financial_alarm_detail_money_tv);
        timeTv = (TextView) v.findViewById(R.id.financial_alarm_detail_time_tv);
        remarkTv = (TextView) v.findViewById(R.id.financial_alarm_detail_remark_tv);
    }

    private void initData() {
        if (financialAlarm == null) {
            return;
        }
        Date date = DateUtils.transMsString2NormalDate(financialAlarm.getDate());
        dateTv.setText(DateUtils.getDateStringYMD(date));
        if (financialAlarm.getDataType() == 0) {
            categoryTv.setText(financialAlarm.getCategoryName() + " " + "-" + " " + financialAlarm.getSubcategoryName());
            moneyTv.setTextColor(getActivity().getResources().getColor(R.color.title_red));
        } else {
            categoryTv.setText(financialAlarm.getCategoryName());
            moneyTv.setTextColor(getActivity().getResources().getColor(R.color.font_green));
        }
        DecimalFormat df = new DecimalFormat("0.00");
        moneyTv.setText(df.format(financialAlarm.getMoney()) + "");
        timeTv.setText(DateUtils.getTimeStringHM(date));
        remarkTv.setText(financialAlarm.getRemark());
    }

    private void initTitleBar(View titleBar) {
        backIv = (ImageView) titleBar.findViewById(R.id.title_bar_back_iv);
        confirm = (ImageView) titleBar.findViewById(R.id.title_bar_confirm_tv);
        titleTv = (TextView) titleBar.findViewById(R.id.title_bar_title_tv);
        backIv = (ImageView) titleBar.findViewById(R.id.title_bar_back_iv);
        titleTv.setText("财务日历-资金详情");
        confirm.setVisibility(View.GONE);
        backIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                remove();
            }
        });
    }

    public void remove() {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.popBackStack();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.remove(this);
        fragmentTransaction.commit();
    }
}
