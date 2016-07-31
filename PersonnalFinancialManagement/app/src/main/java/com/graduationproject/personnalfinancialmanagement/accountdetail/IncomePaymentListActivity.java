package com.graduationproject.personnalfinancialmanagement.accountdetail;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.graduationproject.personnalfinancialmanagement.R;
import com.graduationproject.personnalfinancialmanagement.base.MyBaseAppCompatActivity;
import com.graduationproject.personnalfinancialmanagement.config.StringConfig;
import com.graduationproject.personnalfinancialmanagement.config.javabean.FinancialAlarm;
import com.graduationproject.personnalfinancialmanagement.config.javabean.Income_payment;
import com.graduationproject.personnalfinancialmanagement.financialalarm.FinancialAlarmDetailFragment;
import com.graduationproject.personnalfinancialmanagement.utils.DateUtils;
import com.graduationproject.personnalfinancialmanagement.utils.http.Bmod.MyBMobManager;
import com.graduationproject.personnalfinancialmanagement.utils.layoututils.CustomBarView;
import com.graduationproject.personnalfinancialmanagement.utils.layoututils.CustomProgressHUDManager;
import com.graduationproject.personnalfinancialmanagement.utils.layoututils.TextProgressView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.listener.DeleteListener;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by longhui on 2016/5/19.
 */
public class IncomePaymentListActivity extends MyBaseAppCompatActivity {
    //TitleBar相关
    private ImageView backIv, confirm;
    private TextView titleTv;

    private TextView currentMonthTv;
    private TextView currentYearTv;
    private CustomBarView incomeTextProgressView;
    private CustomBarView paymentTextProgressView;
    private TextView balanceTv;
    private ListView incomePaymentListLv;
    private View incomePaymentListHeaderView;
    private MyIncomePaymentListAdapter myIncomePaymentListAdapter;

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_income_payment_list);
        initView();
        initData();
    }

    private void initView() {
        initTitleBar(findViewById(R.id.income_payment_list_title_bar));
        incomePaymentListLv = (ListView) findViewById(R.id.income_payment_list_lv);
        incomePaymentListHeaderView = LayoutInflater.from(this).inflate(R.layout.income_payment_list_header, null);
        incomePaymentListLv.addHeaderView(incomePaymentListHeaderView);
        myIncomePaymentListAdapter = new MyIncomePaymentListAdapter(this, new ArrayList<Income_payment>());
        incomePaymentListLv.setAdapter(myIncomePaymentListAdapter);
        balanceTv = (TextView) findViewById(R.id.income_payment_list_balance_tv);
        int[] currentDateData = DateUtils.getCurrentDateDataInInt();
        currentMonthTv = (TextView) findViewById(R.id.income_payment_list_current_month_tv);
        if (currentDateData[1] > 9) {
            currentMonthTv.setText(currentDateData[1] + "");
        } else {
            currentMonthTv.setText("0" + currentDateData[1]);
        }
        currentYearTv = (TextView) findViewById(R.id.income_payment_list_current_year_tv);
        currentYearTv.setText(currentDateData[0] + "");
        incomeTextProgressView = (CustomBarView) findViewById(R.id.income_payment_list_income_progress);
        paymentTextProgressView = (CustomBarView) findViewById(R.id.income_payment_list_payment_progress);
        if (getIntent().getAction() != null && getIntent().getAction().equals(StringConfig.ACTION_ACCOUNT_DETAIL_BY_CATEGORY)) {
            confirm.setVisibility(View.GONE);
            String typeString = null;
            if (getIntent().getIntExtra("dataType", 0) == 0) {
                typeString = "支出";
            } else {
                typeString = "收入";
            }
            int year = getIntent().getIntExtra("year", 0);
            int month = getIntent().getIntExtra("month", 0);
            DecimalFormat df = new DecimalFormat("00");
            currentMonthTv.setText(df.format(month) + "");
            currentYearTv.setText(year + "");
            titleTv.setText(typeString + "-" + getIntent().getStringExtra("categoryName"));
            incomeTextProgressView.setVisibility(View.INVISIBLE);
            paymentTextProgressView.setVisibility(View.INVISIBLE);
            findViewById(R.id.income_payment_list_balance_ll).setVisibility(View.INVISIBLE);
            incomePaymentListLv.removeHeaderView(incomePaymentListHeaderView);
        }
    }

    private void initTitleBar(View titleBar) {
        backIv = (ImageView) titleBar.findViewById(R.id.title_bar_back_iv);
        confirm = (ImageView) titleBar.findViewById(R.id.title_bar_confirm_tv);
        titleTv = (TextView) titleBar.findViewById(R.id.title_bar_title_tv);
        backIv = (ImageView) titleBar.findViewById(R.id.title_bar_back_iv);
        confirm.setVisibility(View.VISIBLE);
        confirm.setImageResource(R.drawable.icon_search);
        titleTv.setText("收支详情");
        backIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });
    }

    private void showDatePicker() {
        lastChooseYear = chooseYear;
        lastChooseMonth = chooseMonth;
        DatePickerDialog dpd = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                chooseYear = year;
                chooseMonth = monthOfYear + 1;
                DecimalFormat df = new DecimalFormat("00");
                if (lastChooseMonth != chooseMonth || lastChooseYear != chooseYear) {
                    getData(chooseYear, chooseMonth);
                    currentMonthTv.setText(df.format(chooseMonth) + "");
                    currentYearTv.setText(chooseYear + "");
                }
                lastChooseYear = chooseYear;
                lastChooseMonth = chooseMonth;
            }
        }, chooseYear, chooseMonth - 1, 1);
        DatePicker datePicker = dpd.getDatePicker();
        if (datePicker != null) {
            ((ViewGroup) ((ViewGroup) datePicker.getChildAt(0)).getChildAt(0)).getChildAt(2).setVisibility(View.GONE);
        }
        dpd.show();//显示DatePickerDialog组件
    }

    int chooseYear, chooseMonth;
    int lastChooseYear, lastChooseMonth;

    private void initData() {
        int[] currentDateData = DateUtils.getCurrentDateDataInInt();
        chooseYear = currentDateData[0];
        chooseMonth = currentDateData[1];
        lastChooseYear = chooseYear;
        lastChooseMonth = chooseMonth;
        CustomProgressHUDManager.getInstance().show(this, "拼命获取中", 100);
        if (getIntent().getAction() != null && getIntent().getAction().equals(StringConfig.ACTION_ACCOUNT_DETAIL_BY_CATEGORY)) {
            Intent intent = getIntent();
            int year = intent.getIntExtra("year", 0);
            int month = intent.getIntExtra("month", 0);
            Integer dataType = intent.getIntExtra("dataType", 0);
            Integer categoryNum = intent.getIntExtra("categoryNum", 0);
            getDataByCategoryNum(year, month, categoryNum, dataType);
        } else {
            getFirstData();
        }
    }

    private void getFirstData() {
        FindListener<Income_payment> findListener = new FindListener<Income_payment>() {
            @Override
            public void onSuccess(List<Income_payment> list) {
                CustomProgressHUDManager.getInstance().dismiss();
                refreshUI(list);
            }

            @Override
            public void onError(int i, String s) {
                CustomProgressHUDManager.getInstance().dismiss();
                Toast.makeText(IncomePaymentListActivity.this, "获取失败：", Toast.LENGTH_SHORT).show();
            }
        };
        MyBMobManager.getInstance().getCurrentMonthIncomeAndPayment(this, findListener);
    }

    private void getData(int year, int month) {
        CustomProgressHUDManager.getInstance().show(this, "拼命获取中", 100);
        FindListener<Income_payment> findListener = new FindListener<Income_payment>() {
            @Override
            public void onSuccess(List<Income_payment> list) {
                CustomProgressHUDManager.getInstance().dismiss();
                refreshUI(list);
            }

            @Override
            public void onError(int i, String s) {
                CustomProgressHUDManager.getInstance().dismiss();
                Toast.makeText(IncomePaymentListActivity.this, "获取失败：", Toast.LENGTH_SHORT).show();
            }
        };
        MyBMobManager.getInstance().getMonthIncomeAndPayment(this, year, month, findListener);
    }

    private void getDataByCategoryNum(int year, int month, Integer categoryNum, Integer dataType) {
        FindListener<Income_payment> findListener = new FindListener<Income_payment>() {
            @Override
            public void onSuccess(List<Income_payment> list) {
                CustomProgressHUDManager.getInstance().dismiss();
                myIncomePaymentListAdapter.setDataList(list);
            }

            @Override
            public void onError(int i, String s) {
                CustomProgressHUDManager.getInstance().dismiss();
                Toast.makeText(IncomePaymentListActivity.this, "获取失败：", Toast.LENGTH_SHORT).show();
            }
        };
        MyBMobManager.getInstance().getMonthIncomeAndPaymentByCategoryNum(this, year, month, categoryNum, dataType, findListener);
    }

    private void refreshUI(List<Income_payment> incomePaymentList) {
        calculateData(incomePaymentList);
        myIncomePaymentListAdapter.setDataList(incomePaymentList);
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
        if (balance > 0) {
            balanceTv.setTextColor(getResources().getColor(R.color.font_green));
        } else {
            balanceTv.setTextColor(getResources().getColor(R.color.title_red));
        }
        balanceTv.setText("" + balance);
        int paymentProgress = (int) ((paymentNum / (incomeNum + paymentNum)) * 100);
        int incomeProgress = (int) ((incomeNum / (incomeNum + paymentNum)) * 100);
        paymentTextProgressView.setText("支:" + paymentNum);
        paymentTextProgressView.setBarProgress(paymentProgress);
        incomeTextProgressView.setText("收:" + incomeNum);
        incomeTextProgressView.setBarProgress(incomeProgress);
    }

    AccountDetailFragment accountDetailFragment;

    public void toIncomePaymentDetail(Income_payment incomePayment) {
        getSupportFragmentManager().popBackStack();
        accountDetailFragment = accountDetailFragment.newInstance(incomePayment);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.income_payment_list_main_fl, accountDetailFragment);
        fragmentTransaction.addToBackStack("add");
        fragmentTransaction.commit();
    }

    public void deleteIncomePayment(final Income_payment incomePayment) {
        DeleteListener deleteListener = new DeleteListener() {
            @Override
            public void onSuccess() {
                CustomProgressHUDManager.getInstance().dismiss();
                List<Income_payment> incomePaymentList = myIncomePaymentListAdapter.getIncomePaymentList();
                incomePaymentList.remove(incomePayment);
                myIncomePaymentListAdapter.refresh();
                if (getIntent().getAction() != null && getIntent().getAction().equals(StringConfig.ACTION_ACCOUNT_DETAIL_BY_CATEGORY)) {
                    setResult(201, new Intent());
                } else {
                    calculateData(myIncomePaymentListAdapter.getIncomePaymentList());
                }
            }

            @Override
            public void onFailure(int i, String s) {
                CustomProgressHUDManager.getInstance().dismiss();
                Toast.makeText(IncomePaymentListActivity.this, "删除失败:" + s, Toast.LENGTH_SHORT).show();
            }
        };
        CustomProgressHUDManager.getInstance().show(this, "删除中", 100);
        MyBMobManager.getInstance().deleteIncomePayment(this, incomePayment, deleteListener);
    }
}
