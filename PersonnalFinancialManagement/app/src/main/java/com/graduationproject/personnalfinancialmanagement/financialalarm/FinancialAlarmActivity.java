package com.graduationproject.personnalfinancialmanagement.financialalarm;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.graduationproject.personnalfinancialmanagement.R;
import com.graduationproject.personnalfinancialmanagement.accounting.AccountingActivity;
import com.graduationproject.personnalfinancialmanagement.base.MyBaseAppCompatActivity;
import com.graduationproject.personnalfinancialmanagement.config.StringConfig;
import com.graduationproject.personnalfinancialmanagement.config.javabean.FinancialAlarm;
import com.graduationproject.personnalfinancialmanagement.config.javabean.Income_payment;
import com.graduationproject.personnalfinancialmanagement.financialalarm.alarm.AlarmPollingService;
import com.graduationproject.personnalfinancialmanagement.financialalarm.alarm.AlarmPollingUtils;
import com.graduationproject.personnalfinancialmanagement.utils.DateUtils;
import com.graduationproject.personnalfinancialmanagement.utils.database.LitePalManager;
import com.graduationproject.personnalfinancialmanagement.utils.http.Bmod.MyBMobManager;
import com.graduationproject.personnalfinancialmanagement.utils.layoututils.CustomProgressHUDManager;

import org.litepal.crud.DataSupport;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by longhui on 2016/5/23.
 */
public class FinancialAlarmActivity extends MyBaseAppCompatActivity {
    //TitleBar相关
    private ImageView backIv, confirm;
    private TextView titleTv;

    private TextView monthWaitingPayTv;
    private TextView monthWaitingIncomeTv;
    private TableRow toAllTr;

    private RecyclerView financialAlarmListRv;
    private MyFinancialAlarmListAdapter myFinancialAlarmListAdapter;

    @Override

    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_financial_alarm);
        initView();
        initData();
        initAlarmService();
    }

    private void initView() {
        initTitleBar(findViewById(R.id.financial_alarm_title_bar));

        toAllTr = (TableRow) findViewById(R.id.financial_alarm_list_rv_to_all_ll).findViewById(R.id.financial_alarm_list_item_content_tr);
        monthWaitingPayTv = (TextView) findViewById(R.id.financial_alarm_month_waiting_pay_tv);
        monthWaitingIncomeTv = (TextView) findViewById(R.id.financial_alarm_month_waiting_income_tv);
        financialAlarmListRv = (RecyclerView) findViewById(R.id.financial_alarm_list_rv);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        financialAlarmListRv.setLayoutManager(layoutManager);
        myFinancialAlarmListAdapter = new MyFinancialAlarmListAdapter(this, new ArrayList<FinancialAlarm>());
        financialAlarmListRv.setAdapter(myFinancialAlarmListAdapter);
        financialAlarmListRv.setItemAnimator(new DefaultItemAnimator());
        toAllTr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FinancialAlarmActivity.this, AllFinancialAlarmActivity.class);
                startActivityForResult(intent, 100);
            }
        });
    }

    private void initTitleBar(View titleBar) {
        backIv = (ImageView) titleBar.findViewById(R.id.title_bar_back_iv);
        confirm = (ImageView) titleBar.findViewById(R.id.title_bar_confirm_tv);
        titleTv = (TextView) titleBar.findViewById(R.id.title_bar_title_tv);
        backIv = (ImageView) titleBar.findViewById(R.id.title_bar_back_iv);
        confirm.setVisibility(View.VISIBLE);
        confirm.setImageResource(R.drawable.icon_add);
        titleTv.setText("本月资金提醒");
        backIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toCreateFinancialAlarm();
            }
        });
    }

    private void initData() {
        List<FinancialAlarm> financialAlarmList = new ArrayList<FinancialAlarm>();
        CustomProgressHUDManager.getInstance().show(this, "获取中", 100);
        financialAlarmList = LitePalManager.getInstance().getFinancialAlarmList(BmobUser.getCurrentUser(this).getObjectId());
        refreshUI(financialAlarmList);
    }

    private void refreshUI(List<FinancialAlarm> financialAlarmList) {
        calculateMoney(financialAlarmList);
        myFinancialAlarmListAdapter.setDataList(financialAlarmList);
        CustomProgressHUDManager.getInstance().dismiss();
    }

    private void calculateMoney(List<FinancialAlarm> financialAlarmList) {
        float waitingPayMoney = 0.00f;
        float waitingIncomeMoney = 0.00f;
        DecimalFormat df = new DecimalFormat("0.00");
        for (FinancialAlarm financialAlarm : financialAlarmList) {
            if (DateUtils.calculateDayMsDate(DateUtils.transCurrentDate2MillisecondStr(), financialAlarm.getDate()) < 0) {
                if (financialAlarm.getIsObsolete() != 1) {
                    financialAlarm.setIsObsolete(1);
                    financialAlarm.update(financialAlarm.getId());
                }
            }
            if (financialAlarm.getDataType() == 0) {
                if (financialAlarm.getIsHandle() == 0 && financialAlarm.getIsObsolete() == 0) {
                    waitingPayMoney = waitingPayMoney + financialAlarm.getMoney();
                }
            } else {
                if (financialAlarm.getIsHandle() == 0 && financialAlarm.getIsObsolete() == 0) {
                    waitingIncomeMoney = waitingIncomeMoney + financialAlarm.getMoney();
                }
            }
        }
        monthWaitingPayTv.setText(df.format(waitingPayMoney) + "");
        monthWaitingIncomeTv.setText(df.format(waitingIncomeMoney) + "");
    }

    private void initAlarmService() {
        if (!isServiceWork(this, "com.graduationproject.personnalfinancialmanagement.financialalarm.alarm.AlarmPollingService")) {
            AlarmPollingUtils.startPollingService(this, 30, AlarmPollingService.class, AlarmPollingService.ACTION);
        }
    }

    private void toCreateFinancialAlarm() {
        Intent intent = new Intent(this, AccountingActivity.class);
        intent.setAction(StringConfig.ACTION_SAVE_FINANCIAL_ALARM);
        startActivityForResult(intent, 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            initData();
        }
    }

    /**
     * 判断某个服务是否正在运行的方法
     *
     * @param mContext
     * @param serviceName 是包名+服务的类名（例如：net.loonggg.testbackstage.TestService）
     * @return true代表正在运行，false代表服务没有正在运行
     */
    public boolean isServiceWork(Context mContext, String serviceName) {
        boolean isWork = false;
        ActivityManager myAM = (ActivityManager) mContext
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> myList = myAM.getRunningServices(40);
        if (myList.size() <= 0) {
            return false;
        }
        for (int i = 0; i < myList.size(); i++) {
            String mName = myList.get(i).service.getClassName().toString();
            if (mName.equals(serviceName)) {
                isWork = true;
                break;
            }
        }
        return isWork;
    }

    public void saveFinancialAlarm(final FinancialAlarm financialAlarm) {
        final Income_payment payment = new Income_payment();
        payment.setUserId(financialAlarm.getUserId());
        payment.setDataType(financialAlarm.getDataType());
        payment.setDataTypeName(financialAlarm.getDataTypeName());
        payment.setCategoryNum(Integer.valueOf(financialAlarm.getCategoryNum()));
        payment.setCategoryName(financialAlarm.getCategoryName());
        payment.setSubcategoryNum(Integer.valueOf(financialAlarm.getSubcategoryNum()));
        payment.setSubcategoryName(financialAlarm.getSubcategoryName());
        payment.setMoney(Float.valueOf(financialAlarm.getMoney()));
        payment.setDate(new BmobDate(DateUtils.transMsString2NormalDate(financialAlarm.getDate())));
        payment.setRemark(financialAlarm.getRemark());
        payment.setReimbursed(financialAlarm.isReimbursed());
        SaveListener paymentSaveListener = new SaveListener() {
            @Override
            public void onSuccess() {
                CustomProgressHUDManager.getInstance().dismiss();
                financialAlarm.setIsHandle(1);
                financialAlarm.update(financialAlarm.getId());
                Toast.makeText(FinancialAlarmActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
                calculateMoney(myFinancialAlarmListAdapter.getDataList());
                myFinancialAlarmListAdapter.refresh();
            }

            @Override
            public void onFailure(int i, String s) {
                CustomProgressHUDManager.getInstance().dismiss();
                Toast.makeText(FinancialAlarmActivity.this, "保存失败：" + s, Toast.LENGTH_SHORT).show();
            }
        };
        CustomProgressHUDManager.getInstance().show(this, "保存中", 100);
        MyBMobManager.getInstance().savePayment(this, payment, paymentSaveListener);
    }

    public void deleteFinancialAlarm(FinancialAlarm financialAlarm) {
        DataSupport.delete(FinancialAlarm.class, financialAlarm.getId());
        myFinancialAlarmListAdapter.getDataList().remove(financialAlarm);
        myFinancialAlarmListAdapter.refresh();
        calculateMoney(myFinancialAlarmListAdapter.getDataList());
    }

    FinancialAlarmDetailFragment financialAlarmDetailFragment;

    public void toFinancialAlarmDetail(FinancialAlarm financialAlarm) {
        getSupportFragmentManager().popBackStack();
        financialAlarmDetailFragment = financialAlarmDetailFragment.newInstance(financialAlarm);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.financial_alarm_main_fl, financialAlarmDetailFragment);
        fragmentTransaction.addToBackStack("add");
        fragmentTransaction.commit();
    }
}
