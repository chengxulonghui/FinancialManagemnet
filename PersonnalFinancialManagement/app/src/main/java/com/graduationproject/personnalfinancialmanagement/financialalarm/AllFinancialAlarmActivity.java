package com.graduationproject.personnalfinancialmanagement.financialalarm;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.graduationproject.personnalfinancialmanagement.R;
import com.graduationproject.personnalfinancialmanagement.base.MyBaseAppCompatActivity;
import com.graduationproject.personnalfinancialmanagement.config.javabean.FinancialAlarm;
import com.graduationproject.personnalfinancialmanagement.config.javabean.Income_payment;
import com.graduationproject.personnalfinancialmanagement.financialalarm.FinancialAlarmDetailFragment;
import com.graduationproject.personnalfinancialmanagement.financialalarm.MyFinancialAlarmListAdapter;
import com.graduationproject.personnalfinancialmanagement.utils.DateUtils;
import com.graduationproject.personnalfinancialmanagement.utils.database.LitePalManager;
import com.graduationproject.personnalfinancialmanagement.utils.http.Bmod.MyBMobManager;
import com.graduationproject.personnalfinancialmanagement.utils.layoututils.CustomProgressHUDManager;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by longhui on 2016/5/29.
 */
public class AllFinancialAlarmActivity extends MyBaseAppCompatActivity {
    //TitleBar相关
    private ImageView backIv, confirm;
    private TextView titleTv;

    private RecyclerView financialAlarmListRv;
    private MyFinancialAlarmListAdapter myFinancialAlarmListAdapter;

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_all_financial_alarm_list);
        initView();
        initData();
    }

    private void initView() {
        initTitleBar(findViewById(R.id.financial_alarm_title_bar));
        financialAlarmListRv = (RecyclerView) findViewById(R.id.financial_alarm_list_rv);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        financialAlarmListRv.setLayoutManager(layoutManager);
        myFinancialAlarmListAdapter = new MyFinancialAlarmListAdapter(this, new ArrayList<FinancialAlarm>());
        financialAlarmListRv.setAdapter(myFinancialAlarmListAdapter);
        financialAlarmListRv.setItemAnimator(new DefaultItemAnimator());
    }

    private void initTitleBar(View titleBar) {
        backIv = (ImageView) titleBar.findViewById(R.id.title_bar_back_iv);
        confirm = (ImageView) titleBar.findViewById(R.id.title_bar_confirm_tv);
        titleTv = (TextView) titleBar.findViewById(R.id.title_bar_title_tv);
        backIv = (ImageView) titleBar.findViewById(R.id.title_bar_back_iv);
        confirm.setVisibility(View.GONE);
        confirm.setImageResource(R.drawable.icon_add);
        titleTv.setText("所有资金提醒");
        backIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void initData() {
        List<FinancialAlarm> financialAlarmList = new ArrayList<FinancialAlarm>();
        CustomProgressHUDManager.getInstance().show(this, "获取中", 100);
        financialAlarmList = LitePalManager.getInstance().getAllFinancialAlarmList(BmobUser.getCurrentUser(this).getObjectId());
        myFinancialAlarmListAdapter.setDataList(financialAlarmList);
        CustomProgressHUDManager.getInstance().dismiss();
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
                Toast.makeText(AllFinancialAlarmActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
                myFinancialAlarmListAdapter.refresh();
            }

            @Override
            public void onFailure(int i, String s) {
                CustomProgressHUDManager.getInstance().dismiss();
                Toast.makeText(AllFinancialAlarmActivity.this, "保存失败：" + s, Toast.LENGTH_SHORT).show();
            }
        };
        CustomProgressHUDManager.getInstance().show(this, "保存中", 100);
        MyBMobManager.getInstance().savePayment(this, payment, paymentSaveListener);
    }

    public void deleteFinancialAlarm(FinancialAlarm financialAlarm) {
        DataSupport.delete(FinancialAlarm.class, financialAlarm.getId());
        myFinancialAlarmListAdapter.getDataList().remove(financialAlarm);
        myFinancialAlarmListAdapter.refresh();
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
