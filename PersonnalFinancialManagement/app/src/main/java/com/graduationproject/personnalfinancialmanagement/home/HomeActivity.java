package com.graduationproject.personnalfinancialmanagement.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.graduationproject.personnalfinancialmanagement.R;
import com.graduationproject.personnalfinancialmanagement.base.MyBaseAppCompatActivity;
import com.graduationproject.personnalfinancialmanagement.config.javabean.FinancialAlarm;
import com.graduationproject.personnalfinancialmanagement.config.javabean.Income_payment;
import com.graduationproject.personnalfinancialmanagement.customerservice.enter.CustomEnterActivity;
import com.graduationproject.personnalfinancialmanagement.customerservice.gasStation.NationalGasStationQueryActivity;
import com.graduationproject.personnalfinancialmanagement.customerservice.news.activity.NewsListActivity;
import com.graduationproject.personnalfinancialmanagement.customerservice.trainTicket.TrainTicketActivity;
import com.graduationproject.personnalfinancialmanagement.home.DailyAccount.DailyAccountBookFragment;
import com.graduationproject.personnalfinancialmanagement.statements.FinancialStatementsActivity;
import com.graduationproject.personnalfinancialmanagement.utils.database.LitePalManager;
import com.graduationproject.personnalfinancialmanagement.utils.http.Bmod.MyBMobManager;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;


public class HomeActivity extends MyBaseAppCompatActivity {
    private LinearLayout bottomNewsLl;
    private LinearLayout bottomStateLl;
    private LinearLayout bottomCustomService;
    private RecyclerView homePageRv;
    private ViewPager mHomeViewPager;
    private MyFragmentPagerAdapter myFragmentPagerAdapter;
    private List<Fragment> myHomeFragmentList;
    private FragmentManager fragmentManager;

    private ImageView titleRightIv;
    private TextView titleTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initView();
        initData();
    }

    private void initView() {
        initTitleBar(findViewById(R.id.home_activity_title_bar));
        fragmentManager = getSupportFragmentManager();
        homePageRv = (RecyclerView) findViewById(R.id.home_title_bar_page_rv);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        homePageRv.setLayoutManager(layoutManager);
        mHomeViewPager = (ViewPager) findViewById(R.id.home_activity_vp);
        bottomNewsLl = (LinearLayout) findViewById(R.id.home_activity_bottom_news);
        bottomStateLl = (LinearLayout) findViewById(R.id.home_activity_bottom_statement);
        bottomCustomService = (LinearLayout) findViewById(R.id.home_activity_bottom_custom_service_ll);
        bottomNewsLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, NewsListActivity.class);
                startActivity(intent);
            }
        });
        bottomStateLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, FinancialStatementsActivity.class);
                startActivity(intent);
            }
        });
        bottomCustomService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                titleRightIv.performClick();
            }
        });
    }

    private void initTitleBar(View v) {
        titleRightIv = (ImageView) v.findViewById(R.id.home_title_bar_right_iv);
        titleTv = (TextView) v.findViewById(R.id.home_title_bar_title_tv);
        titleTv.setText("财务汇总");
        titleRightIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, CustomEnterActivity.class);
                startActivity(intent);
            }
        });
    }

    DailyAccountBookFragment dailyAccountBookFragment;

    private void initData() {
        myHomeFragmentList = new ArrayList<Fragment>();
        dailyAccountBookFragment = new DailyAccountBookFragment();
        myHomeFragmentList.add(dailyAccountBookFragment);
        myFragmentPagerAdapter = new MyFragmentPagerAdapter(fragmentManager, myHomeFragmentList);
        mHomeViewPager.setAdapter(myFragmentPagerAdapter);
    }

    public void getData() {
        getFinancialAlarm();
        getTodayPayment();
        getMonthPayment();
    }

    private void getTodayPayment() {
        FindListener<Income_payment> findListener = new FindListener<Income_payment>() {
            @Override
            public void onSuccess(List<Income_payment> list) {
                if (list.size() == 0) {
                    dailyAccountBookFragment.setTodayPayment("未消费");
                } else {
                    dailyAccountBookFragment.setTodayPayment("今日支出" + list.size() + "笔");
                }
            }

            @Override
            public void onError(int i, String s) {

            }
        };
        MyBMobManager.getInstance().getTodayPayment(this, findListener);
    }

    private void getMonthPayment() {
        FindListener<Income_payment> findListener = new FindListener<Income_payment>() {
            @Override
            public void onSuccess(List<Income_payment> list) {
                calculateData(list);
            }

            @Override
            public void onError(int i, String s) {

            }
        };
        MyBMobManager.getInstance().getCurrentMonthIncomeAndPayment(this, findListener);
    }

    private void calculateData(List<Income_payment> incomePaymentList) {
        float incomeNum = 0;
        float paymentNum = 0;
        for (Income_payment incomePayment : incomePaymentList) {
            if (incomePayment.getDataType() == 0) {
                paymentNum = paymentNum + incomePayment.getMoney();
            } else {
                incomeNum = incomeNum + incomePayment.getMoney();
            }
        }
        float balance = incomeNum - paymentNum;
        DecimalFormat df = new DecimalFormat("0.00");
        dailyAccountBookFragment.setMonthData(df.format(incomeNum), df.format(paymentNum), df.format(balance));
    }

    private void getFinancialAlarm() {
        List<FinancialAlarm> financialAlarmList = LitePalManager.getInstance().getFinancialAlarmList(BmobUser.getCurrentUser(this).getObjectId());
        if (financialAlarmList.size() == 0) {
            dailyAccountBookFragment.setTodayAlarm("未添加提醒");
        } else {
            dailyAccountBookFragment.setTodayAlarm("本月有" + financialAlarmList.size() + "条提醒");
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        getFinancialAlarm();
        getTodayPayment();
        getMonthPayment();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e("TAG_HOME", "onStart");

    }
}
