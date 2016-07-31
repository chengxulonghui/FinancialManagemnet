package com.graduationproject.personnalfinancialmanagement.accounting.fragemnt;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatEditText;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.graduationproject.personnalfinancialmanagement.R;
import com.graduationproject.personnalfinancialmanagement.accounting.AccountingActivity;
import com.graduationproject.personnalfinancialmanagement.accounting.typeconfig.CategoryManager;
import com.graduationproject.personnalfinancialmanagement.config.StringConfig;
import com.graduationproject.personnalfinancialmanagement.config.javabean.FinancialAlarm;
import com.graduationproject.personnalfinancialmanagement.config.javabean.Income_payment;
import com.graduationproject.personnalfinancialmanagement.accounting.typeconfig.IncomeCategory;
import com.graduationproject.personnalfinancialmanagement.config.javabean.QuickRecordModel;
import com.graduationproject.personnalfinancialmanagement.utils.DateUtils;
import com.graduationproject.personnalfinancialmanagement.utils.http.Bmod.MyBMobManager;
import com.graduationproject.personnalfinancialmanagement.utils.layoututils.CustomProgressHUDManager;
import com.graduationproject.personnalfinancialmanagement.utils.layoututils.MaxLengthWatcher;

import java.util.Date;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by longhui on 2016/5/8.
 */
public class IncomeFragment extends Fragment {
    IncomeCategory currentIncomeCategory;
    private ImageView categoryIv;
    private TextView categoryTv;
    private TableRow incomeTypeTr;
    private TableRow incomeDateTr;
    private TextView incomeDateTv;
    private EditText incomeMoneyEt;
    private AppCompatEditText remarkEt;
    private BmobDate chooseDate;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.accounting_income_fragment, null);
        initView(rootView);
        initData();
        return rootView;
    }

    private void initView(View v) {
        incomeMoneyEt = (EditText) v.findViewById(R.id.accounting_income_money_et);
        incomeDateTr = (TableRow) v.findViewById(R.id.accounting_income_date_tr);
        incomeDateTv = (TextView) v.findViewById(R.id.accounting_income_date_tv);
        categoryIv = (ImageView) v.findViewById(R.id.accounting_income_category_iv);
        categoryTv = (TextView) v.findViewById(R.id.accounting_income_category_tv);
        incomeTypeTr = (TableRow) v.findViewById(R.id.accounting_income_type_tr);
        remarkEt = (AppCompatEditText) v.findViewById(R.id.accounting_income_remark_et);
        incomeTypeTr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((AccountingActivity) getActivity()).toChooseIncomeCategory();
            }
        });
        incomeDateTr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });
        remarkEt.addTextChangedListener(new MaxLengthWatcher(getActivity(), 200, remarkEt, "文字数量超过上限"));
    }

    public void setCurrentIncomeCategory(IncomeCategory currentIncomeCategory) {
        if (categoryTv == null || categoryIv == null) {
            return;
        }
        this.currentIncomeCategory = currentIncomeCategory;
        categoryIv.setImageResource(currentIncomeCategory.getCategoryIconId());
        categoryTv.setText(currentIncomeCategory.getCategoryName());
    }

    int chooseYear, chooseMonth, chooseDay, chooseHour, chooseMinute;
    String chooseDateString;

    private void initData() {
        int[] currentDateData = DateUtils.getCurrentDateDataInInt();
        int[] currentTimeData = DateUtils.getCurrentTimeDataInInt();
        chooseYear = currentDateData[0];
        chooseMonth = currentDateData[1];
        chooseDay = currentDateData[2];
        chooseHour = currentTimeData[0];
        chooseMinute = currentTimeData[1];
        chooseDate = new BmobDate(new Date());
        chooseDateString = DateUtils.transCurrentDate2MillisecondStr();
        if (getActivity().getIntent().getAction() != null) {
            String action = getActivity().getIntent().getAction();
            if (action.equals(StringConfig.ACTION_ACCOUNTING_FROM_QUICK_RECORD)) {
                QuickRecordModel quickRecordModel = (QuickRecordModel) getActivity().getIntent().getSerializableExtra("quickRecordModel");
                if (quickRecordModel.getDataType() == 1) {
                    remarkEt.setText(quickRecordModel.getName());
                    ((AccountingActivity) getActivity()).setCurrentIncomeCategory(CategoryManager.getSingleton().getIncomeCategoryByCategoryNum(quickRecordModel.getCategoryNum() + ""));
                }
            }
        }
    }

    private void showDatePicker() {
        DatePickerDialog dpd = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                chooseYear = year;
                chooseMonth = monthOfYear + 1;
                chooseDay = dayOfMonth;
                showTimePicker();
            }
        }, chooseYear, chooseMonth - 1, chooseDay);
//        DatePicker datePicker = dpd.getDatePicker();
//        if (datePicker != null) {
//            ((ViewGroup) ((ViewGroup) datePicker.getChildAt(0)).getChildAt(0)).getChildAt(2).setVisibility(View.GONE);
//        }
        dpd.show();//显示DatePickerDialog组件
    }

    private void showTimePicker() {
        TimePickerDialog tpd = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                chooseHour = hourOfDay;
                chooseMinute = minute;
                incomeDateTv.setText(" " + DateUtils.getDateString_simple(chooseYear, chooseMonth, chooseDay, hourOfDay, minute, 0));
                chooseDate = new BmobDate(DateUtils.getDate(chooseYear, chooseMonth, chooseDay, hourOfDay, minute, 0));
                chooseDateString = chooseYear + "" + chooseMonth + "" + chooseDay + "" + hourOfDay + "" + minute + "00" + "00";
            }
        }, chooseHour, chooseMinute, true);
        tpd.show();
    }

    private boolean checkBeforeSave() {
        if (TextUtils.isEmpty(incomeMoneyEt.getText()) || Float.valueOf(incomeMoneyEt.getText().toString()) < 0) {
            Toast.makeText(getActivity(), "请输入有效金额!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public void saveIncome() {
        if (!checkBeforeSave()) {
            return;
        }
        Income_payment income = new Income_payment();
        BmobUser bmobUser = BmobUser.getCurrentUser(getActivity());
        income.setUserId(bmobUser.getObjectId());
        income.setDataType(1);
        income.setDataTypeName("收入");
        income.setCategoryNum(Integer.valueOf(currentIncomeCategory.getCategoryNum()));
        income.setCategoryName(currentIncomeCategory.getCategoryName());
        income.setDate(chooseDate);
        income.setMoney(Float.valueOf(incomeMoneyEt.getText().toString()));
        income.setRemark(remarkEt.getText().toString());
        SaveListener incomeSaveListener = new SaveListener() {
            @Override
            public void onSuccess() {
                CustomProgressHUDManager.getInstance().dismiss();
                Toast.makeText(getActivity(), "保存成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int i, String s) {
                CustomProgressHUDManager.getInstance().dismiss();
                Toast.makeText(getActivity(), "保存失败：" + s, Toast.LENGTH_SHORT).show();
            }
        };
        CustomProgressHUDManager.getInstance().show(getActivity(), "保存中", 100);
        MyBMobManager.getInstance().saveIncome(getActivity(), income, incomeSaveListener);
    }

    public void saveFinancialAlarmIncome() {
        if (!checkBeforeSave()) {
            return;
        }
        FinancialAlarm income = new FinancialAlarm();
        BmobUser bmobUser = BmobUser.getCurrentUser(getActivity());
        income.setUserId(bmobUser.getObjectId());
        income.setDataType(1);
        income.setDataTypeName("收入");
        income.setCategoryNum(Integer.valueOf(currentIncomeCategory.getCategoryNum()));
        income.setCategoryName(currentIncomeCategory.getCategoryName());
        income.setDate(DateUtils.transNormalDate2MillisecondStr(chooseDate.getDate()));
        income.setMoney(Float.valueOf(incomeMoneyEt.getText().toString()));
        income.setRemark(remarkEt.getText().toString());
        income.setIsAlarm(0);
        income.setIsHandle(0);
        if (DateUtils.calculateDayMsDate(DateUtils.transCurrentDate2MillisecondStr(), chooseDateString) >= 0) {
            income.setIsObsolete(0);
        } else {
            income.setIsObsolete(1);
        }
        if (income.save()) {
            Toast.makeText(getActivity(), "保存成功", Toast.LENGTH_SHORT).show();
        }
    }
}
