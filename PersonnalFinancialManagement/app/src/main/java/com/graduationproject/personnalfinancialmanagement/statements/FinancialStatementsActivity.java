package com.graduationproject.personnalfinancialmanagement.statements;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.graduationproject.personnalfinancialmanagement.R;
import com.graduationproject.personnalfinancialmanagement.accountdetail.IncomePaymentListActivity;
import com.graduationproject.personnalfinancialmanagement.base.MyBaseAppCompatActivity;
import com.graduationproject.personnalfinancialmanagement.config.StringConfig;
import com.graduationproject.personnalfinancialmanagement.config.javabean.Income_payment;
import com.graduationproject.personnalfinancialmanagement.config.javabean.MemoLitePal;
import com.graduationproject.personnalfinancialmanagement.customerservice.memo.MemoCreateEditFragment;
import com.graduationproject.personnalfinancialmanagement.statements.adapter.StatementFragmentPagerAdapter;
import com.graduationproject.personnalfinancialmanagement.statements.javabean.IncomePaymentStatementItem;
import com.graduationproject.personnalfinancialmanagement.statements.javabean.IncomeStatementItem;
import com.graduationproject.personnalfinancialmanagement.statements.javabean.PaymentStatementItem;
import com.graduationproject.personnalfinancialmanagement.utils.DateUtils;
import com.graduationproject.personnalfinancialmanagement.utils.http.Bmod.MyBMobManager;
import com.graduationproject.personnalfinancialmanagement.utils.layoututils.CustomProgressHUDManager;
import com.graduationproject.personnalfinancialmanagement.utils.layoututils.NoScrollViewPager;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bmob.v3.listener.FindListener;

/**
 * Created by longhui on 2016/5/26.
 */
public class FinancialStatementsActivity extends MyBaseAppCompatActivity {
    //TitleBar相关
    private ImageView backIv, confirm;
    private TextView titleTv;

    TableRow dateChooseTr;
    TextView dateChooseTv;
    NoScrollViewPager pager;
    ImageView toListChartIv;
    ImageView toPieIv;
    StatementListFragment stateListFragment;
    StatementPieFragment statementPieFragment;
    List<Fragment> fragmentList;
    StatementFragmentPagerAdapter statementFragmentPagerAdapter;

    int dataType = 0;

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_financial_statements);
        initView();
        initData();
    }

    private void initView() {
        initTitleBar(findViewById(R.id.financial_statements_title_bar));
        dateChooseTr = (TableRow) findViewById(R.id.financial_statements_date_tr);
        dateChooseTv = (TextView) findViewById(R.id.financial_statements_date_tv);
        pager = (NoScrollViewPager) findViewById(R.id.financial_statements_vp);
        toListChartIv = (ImageView) findViewById(R.id.financial_statements_list_iv);
        toPieIv = (ImageView) findViewById(R.id.financial_statements_pie_iv);
        dateChooseTr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });
    }

    private void initTitleBar(View titleBar) {
        backIv = (ImageView) titleBar.findViewById(R.id.title_bar_back_iv);
        confirm = (ImageView) titleBar.findViewById(R.id.title_bar_confirm_tv);
        titleTv = (TextView) titleBar.findViewById(R.id.title_bar_title_tv);
        backIv = (ImageView) titleBar.findViewById(R.id.title_bar_back_iv);
        confirm.setVisibility(View.VISIBLE);
        confirm.setImageResource(R.drawable.icon_menu);
        titleTv.setText("财务报表-支出");
        backIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toStatementTypeChooseFragment();
            }
        });
    }

    int chooseYear, chooseMonth;
    int lastChooseYear, lastChooseMonth;

    private void initData() {
        int[] currentDateData = DateUtils.getCurrentDateDataInInt();
        chooseYear = currentDateData[0];
        chooseMonth = currentDateData[1];
        lastChooseYear = chooseYear;
        lastChooseMonth = chooseMonth;
        initFirstData();
    }

    private void initFirstData() {
        CustomProgressHUDManager.getInstance().show(this, "获取中", 100);
        FindListener<Income_payment> findListener = new FindListener<Income_payment>() {
            @Override
            public void onSuccess(List<Income_payment> list) {
                handleFirstData(list);
            }

            @Override
            public void onError(int i, String s) {
                CustomProgressHUDManager.getInstance().dismiss();
                Toast.makeText(FinancialStatementsActivity.this, "查询失败", Toast.LENGTH_SHORT).show();
            }
        };
        MyBMobManager.getInstance().getCurrentMonthIncomeAndPayment(this, findListener);
    }

    private void getData(int year, int month) {
        CustomProgressHUDManager.getInstance().show(this, "获取中", 100);
        FindListener<Income_payment> findListener = new FindListener<Income_payment>() {
            @Override
            public void onSuccess(List<Income_payment> list) {
                handleData(list);
            }

            @Override
            public void onError(int i, String s) {
                CustomProgressHUDManager.getInstance().dismiss();
                Toast.makeText(FinancialStatementsActivity.this, "查询失败", Toast.LENGTH_SHORT).show();
            }
        };
        MyBMobManager.getInstance().getMonthIncomeAndPayment(this, year, month, findListener);
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
                dateChooseTv.setText(" " + chooseYear + "-" + df.format(chooseMonth));
                if (lastChooseMonth != chooseMonth || lastChooseYear != chooseYear) {
                    getData(chooseYear, chooseMonth);
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

    private void handleFirstData(List<Income_payment> incomePaymentList) {
        List<IncomePaymentStatementItem> incomeStatementItemList = new ArrayList<IncomePaymentStatementItem>();
        List<IncomePaymentStatementItem> paymentStatementItemList = new ArrayList<IncomePaymentStatementItem>();
        Map<Integer, IncomeStatementItem> incomeStatementItemMap = new HashMap<Integer, IncomeStatementItem>();
        Map<Integer, PaymentStatementItem> paymentStatementItemMap = new HashMap<Integer, PaymentStatementItem>();
        float paymentSum = 0;
        float incomeSum = 0;
        for (Income_payment incomePayment : incomePaymentList) {
            if (incomePayment.getDataType() == 0) {
                paymentSum = paymentSum + incomePayment.getMoney();
                PaymentStatementItem paymentStatementItem = paymentStatementItemMap.get(incomePayment.getCategoryNum());
                if (paymentStatementItem == null) {
                    paymentStatementItem = new PaymentStatementItem();
                    paymentStatementItem.setDataType(incomePayment.getDataType());
                    paymentStatementItem.setCategoryNum(incomePayment.getCategoryNum());
                    paymentStatementItem.setCategoryName(incomePayment.getCategoryName());
                    paymentStatementItem.setMoneySum(incomePayment.getMoney());
                    paymentStatementItemList.add(paymentStatementItem);
                    paymentStatementItemMap.put(paymentStatementItem.getCategoryNum(), paymentStatementItem);
                } else {
                    paymentStatementItem.setMoneySum(paymentStatementItem.getMoneySum() + incomePayment.getMoney());
                }
            } else {
                incomeSum = incomeSum + incomePayment.getMoney();
                IncomeStatementItem incomeStatementItem = incomeStatementItemMap.get(incomePayment.getCategoryNum());
                if (incomeStatementItem == null) {
                    incomeStatementItem = new IncomeStatementItem();
                    incomeStatementItem.setDataType(incomePayment.getDataType());
                    incomeStatementItem.setCategoryNum(incomePayment.getCategoryNum());
                    incomeStatementItem.setCategoryName(incomePayment.getCategoryName());
                    incomeStatementItem.setMoneySum(incomePayment.getMoney());
                    incomeStatementItemList.add(incomeStatementItem);
                    incomeStatementItemMap.put(incomeStatementItem.getCategoryNum(), incomeStatementItem);
                } else {
                    incomeStatementItem.setMoneySum(incomeStatementItem.getMoneySum() + incomePayment.getMoney());
                }
            }
        }
        for (IncomePaymentStatementItem paymentStatementItem : paymentStatementItemList) {
            paymentStatementItem.setPercent(paymentStatementItem.getMoneySum() / paymentSum * 100.0f);
        }
        for (IncomePaymentStatementItem incomeStatementItem : incomeStatementItemList) {
            incomeStatementItem.setPercent(incomeStatementItem.getMoneySum() / incomeSum * 100.0f);
        }
        CustomProgressHUDManager.getInstance().dismiss();
        stateListFragment = StatementListFragment.newInstance(paymentStatementItemList);
        statementPieFragment = StatementPieFragment.newInstance(paymentStatementItemList);
        fragmentList = new ArrayList<Fragment>();
        fragmentList.add(stateListFragment);
        fragmentList.add(statementPieFragment);
        statementFragmentPagerAdapter = new StatementFragmentPagerAdapter(getSupportFragmentManager(), fragmentList);
        pager.setAdapter(statementFragmentPagerAdapter);
        pager.setCurrentItem(0);
        toListChartIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toListChartIv.setImageResource(R.drawable.chart_list_p);
                toPieIv.setImageResource(R.drawable.chart_pie_n);
                pager.setCurrentItem(0);
            }
        });
        toPieIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toListChartIv.setImageResource(R.drawable.chart_list_n);
                toPieIv.setImageResource(R.drawable.chart_pie_p);
                pager.setCurrentItem(1);
            }
        });
    }

    private void handleData(List<Income_payment> incomePaymentList) {
        List<IncomePaymentStatementItem> incomeStatementItemList = new ArrayList<IncomePaymentStatementItem>();
        List<IncomePaymentStatementItem> paymentStatementItemList = new ArrayList<IncomePaymentStatementItem>();
        Map<Integer, IncomeStatementItem> incomeStatementItemMap = new HashMap<Integer, IncomeStatementItem>();
        Map<Integer, PaymentStatementItem> paymentStatementItemMap = new HashMap<Integer, PaymentStatementItem>();
        float paymentSum = 0;
        float incomeSum = 0;
        for (Income_payment incomePayment : incomePaymentList) {
            if (incomePayment.getDataType() == 0) {
                paymentSum = paymentSum + incomePayment.getMoney();
                PaymentStatementItem paymentStatementItem = paymentStatementItemMap.get(incomePayment.getCategoryNum());
                if (paymentStatementItem == null) {
                    paymentStatementItem = new PaymentStatementItem();
                    paymentStatementItem.setDataType(incomePayment.getDataType());
                    paymentStatementItem.setCategoryNum(incomePayment.getCategoryNum());
                    paymentStatementItem.setCategoryName(incomePayment.getCategoryName());
                    paymentStatementItem.setMoneySum(incomePayment.getMoney());
                    paymentStatementItemList.add(paymentStatementItem);
                    paymentStatementItemMap.put(paymentStatementItem.getCategoryNum(), paymentStatementItem);
                } else {
                    paymentStatementItem.setMoneySum(paymentStatementItem.getMoneySum() + incomePayment.getMoney());
                }
            } else {
                incomeSum = incomeSum + incomePayment.getMoney();
                IncomeStatementItem incomeStatementItem = incomeStatementItemMap.get(incomePayment.getCategoryNum());
                if (incomeStatementItem == null) {
                    incomeStatementItem = new IncomeStatementItem();
                    incomeStatementItem.setDataType(incomePayment.getDataType());
                    incomeStatementItem.setCategoryNum(incomePayment.getCategoryNum());
                    incomeStatementItem.setCategoryName(incomePayment.getCategoryName());
                    incomeStatementItem.setMoneySum(incomePayment.getMoney());
                    incomeStatementItemList.add(incomeStatementItem);
                    incomeStatementItemMap.put(incomeStatementItem.getCategoryNum(), incomeStatementItem);
                } else {
                    incomeStatementItem.setMoneySum(incomeStatementItem.getMoneySum() + incomePayment.getMoney());
                }
            }
        }
        for (IncomePaymentStatementItem paymentStatementItem : paymentStatementItemList) {
            paymentStatementItem.setPercent(paymentStatementItem.getMoneySum() / paymentSum * 100.0f);
        }
        for (IncomePaymentStatementItem incomeStatementItem : incomeStatementItemList) {
            incomeStatementItem.setPercent(incomeStatementItem.getMoneySum() / incomeSum * 100.0f);
        }
        CustomProgressHUDManager.getInstance().dismiss();
        if (dataType == 0) {
            statementPieFragment.setIncomePaymentStatementItemList(0, paymentStatementItemList);
            stateListFragment.setIncomePaymentStatementItemList(paymentStatementItemList);
        } else {
            statementPieFragment.setIncomePaymentStatementItemList(1, incomeStatementItemList);
            stateListFragment.setIncomePaymentStatementItemList(incomeStatementItemList);
        }
    }

    public void toSeeCategoryList(int dataType, int categoryNum, String categoryName) {
        Intent intent = new Intent(this, IncomePaymentListActivity.class);
        intent.setAction(StringConfig.ACTION_ACCOUNT_DETAIL_BY_CATEGORY);
        intent.putExtra("year", chooseYear);
        intent.putExtra("month", chooseMonth);
        intent.putExtra("dataType", dataType);
        intent.putExtra("categoryNum", categoryNum);
        intent.putExtra("categoryName", categoryName);
        startActivityForResult(intent, 200);
    }

    public void setDataType(int dataType) {
        if (this.dataType != dataType) {
            if (dataType == 0) {
                titleTv.setText("财务报表-支出");
            } else {
                titleTv.setText("财务报表-收入");
            }
            this.dataType = dataType;
            getData(chooseYear, chooseMonth);
        }
    }

    StatementTypeChooseFragment statementTypeChooseFragment;

    public void toStatementTypeChooseFragment() {
        getSupportFragmentManager().popBackStack();
        statementTypeChooseFragment = new StatementTypeChooseFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.financial_statements_main_fl, statementTypeChooseFragment);
        fragmentTransaction.addToBackStack("add");
        fragmentTransaction.commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 200) {
            if (resultCode == 201) {
                getData(chooseYear, chooseMonth);
            }
        }
    }
}
