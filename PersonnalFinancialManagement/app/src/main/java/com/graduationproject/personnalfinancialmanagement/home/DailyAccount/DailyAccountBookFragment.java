package com.graduationproject.personnalfinancialmanagement.home.DailyAccount;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.graduationproject.personnalfinancialmanagement.R;
import com.graduationproject.personnalfinancialmanagement.accountdetail.IncomePaymentListActivity;
import com.graduationproject.personnalfinancialmanagement.accounting.AccountingActivity;
import com.graduationproject.personnalfinancialmanagement.customerservice.memo.MemorandumActivity;
import com.graduationproject.personnalfinancialmanagement.financialalarm.FinancialAlarmActivity;
import com.graduationproject.personnalfinancialmanagement.home.HomeActivity;
import com.graduationproject.personnalfinancialmanagement.home.QuickRecord.QuickRecordListActivity;

/**
 * Created by Administrator on 2015/12/24.
 */
public class DailyAccountBookFragment extends Fragment {
    LinearLayout normalAccountingLL;
    LinearLayout todayPaymentLl;
    LinearLayout memoLl;
    LinearLayout quickRecordLl;
    LinearLayout financialAlarmLl;
    TextView todayPaymentTv;
    TextView todayAlarm;
    TextView monthIncomeTv;
    TextView monthPaymentTv;
    TextView monthBalanceTv;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.home_daily_account_daily_fragment, null);
        initView(rootView);
        ((HomeActivity) getActivity()).getData();
        return rootView;
    }

    private void initView(View rootView) {
        quickRecordLl = (LinearLayout) rootView.findViewById(R.id.home_daily_account_quick_accounting_ll);
        normalAccountingLL = (LinearLayout) rootView.findViewById(R.id.home_daily_account_normal_accounting_ll);
        todayPaymentLl = (LinearLayout) rootView.findViewById(R.id.home_daily_account_today_payment_ll);
        memoLl = (LinearLayout) rootView.findViewById(R.id.home_daily_account_memo_ll);
        financialAlarmLl = (LinearLayout) rootView.findViewById(R.id.home_daily_account_financial_alarm_ll);
        todayPaymentTv = (TextView) rootView.findViewById(R.id.home_daily_account_today_payment_tv);
        todayAlarm = (TextView) rootView.findViewById(R.id.home_daily_account_today_alarm_tv);
        monthIncomeTv = (TextView) rootView.findViewById(R.id.home_daily_account_month_income_tv);
        monthPaymentTv = (TextView) rootView.findViewById(R.id.home_daily_account_month_payment_tv);
        monthBalanceTv = (TextView) rootView.findViewById(R.id.home_daily_account_month_balance_tv);
        memoLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MemorandumActivity.class);
                getActivity().startActivity(intent);
            }
        });
        normalAccountingLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AccountingActivity.class);
                getActivity().startActivity(intent);
            }
        });
        todayPaymentLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), IncomePaymentListActivity.class);
                getActivity().startActivity(intent);
            }
        });
        financialAlarmLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), FinancialAlarmActivity.class);
                getActivity().startActivity(intent);
            }
        });
        quickRecordLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), QuickRecordListActivity.class);
                getActivity().startActivity(intent);
            }
        });
    }

    public void setTodayPayment(String str) {
        if (todayPaymentTv == null) {
            return;
        }
        todayPaymentTv.setText(str);
    }

    public void setTodayAlarm(String str) {
        if (todayAlarm == null) {
            return;
        }
        todayAlarm.setText(str);
    }

    public void setMonthData(String income, String payment, String balance) {
        if (monthIncomeTv == null || monthPaymentTv == null || monthBalanceTv == null) {
            return;
        }
        monthIncomeTv.setText(income + "");
        monthPaymentTv.setText(payment + "");
        monthBalanceTv.setText(balance + "");
    }
}
