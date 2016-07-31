package com.graduationproject.personnalfinancialmanagement.accounting.fragemnt;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatCheckBox;
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

import com.baidu.location.BDLocation;
import com.graduationproject.personnalfinancialmanagement.R;
import com.graduationproject.personnalfinancialmanagement.accounting.AccountingActivity;
import com.graduationproject.personnalfinancialmanagement.config.StringConfig;
import com.graduationproject.personnalfinancialmanagement.config.javabean.FinancialAlarm;
import com.graduationproject.personnalfinancialmanagement.config.javabean.Income_payment;
import com.graduationproject.personnalfinancialmanagement.accounting.typeconfig.CategoryManager;
import com.graduationproject.personnalfinancialmanagement.accounting.typeconfig.PaymentCategory;
import com.graduationproject.personnalfinancialmanagement.accounting.typeconfig.PaymentSubcategory;
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
public class PaymentFragment extends Fragment {
    private ImageView categoryIv;
    private TextView subcategoryTv;
    private TableRow paymentTypeTr;
    private TableRow paymentDateTr;
    private TextView paymentDateTv;
    private EditText moneyEt;
    private AppCompatEditText remarkEt;
    private AppCompatCheckBox reimbursedCb;
    private PaymentCategory currentPaymentCategory;
    private PaymentSubcategory currentPaymentSubcategory;
    private BmobDate chooseDate;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.accounting_payment_fragment, null);
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
        moneyEt = (EditText) v.findViewById(R.id.accounting_payment_money_et);
        remarkEt = (AppCompatEditText) v.findViewById(R.id.accounting_payment_remark_et);
        reimbursedCb = (AppCompatCheckBox) v.findViewById(R.id.accounting_payment_reimbursed_cb);
        paymentDateTr = (TableRow) v.findViewById(R.id.accounting_payment_date_tr);
        paymentDateTv = (TextView) v.findViewById(R.id.accounting_payment_date_tv);
        paymentDateTv.setText(" " + DateUtils.getCurrentDateString_simple());
        categoryIv = (ImageView) v.findViewById(R.id.accounting_payment_category_iv);
        subcategoryTv = (TextView) v.findViewById(R.id.accounting_payment_subcategory_tv);
        paymentTypeTr = (TableRow) v.findViewById(R.id.accounting_payment_type_tr);
        paymentTypeTr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((AccountingActivity) getActivity()).toChoosePaymentType();
            }
        });
        paymentDateTr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });
        remarkEt.addTextChangedListener(new MaxLengthWatcher(getActivity(), 200, remarkEt, "文字数量超过上限"));
    }

    int chooseYear, chooseMonth, chooseDay, chooseHour, chooseMinute;
    String chooseDateString;

    private void initData() {
        currentPaymentCategory = CategoryManager.getSingleton().getPaymentCategoryList().get(0);
        currentPaymentSubcategory = currentPaymentCategory.getSubcategories().get(0);
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
                if (quickRecordModel.getDataType() == 0) {
                    remarkEt.setText(quickRecordModel.getName());
                    reimbursedCb.setChecked(quickRecordModel.getReimbursed() == 0 ? false : true);
                    ((AccountingActivity) getActivity()).setPaymentCategory(CategoryManager.getSingleton().getPaymentCategoryByCategoryNum(quickRecordModel.getCategoryNum() + ""));
                    ((AccountingActivity) getActivity()).setPaymentSubcategory(CategoryManager.getSingleton().getPaymentSubCategoryByCategoryNum(currentPaymentCategory, quickRecordModel.getSubcategoryNum() + ""));
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
        dpd.show();//显示DatePickerDialog组件
    }

    private void showTimePicker() {
        TimePickerDialog tpd = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                chooseHour = hourOfDay;
                chooseMinute = minute;
                paymentDateTv.setText(" " + DateUtils.getDateString_simple(chooseYear, chooseMonth, chooseDay, hourOfDay, minute, 0));
                chooseDate = new BmobDate(DateUtils.getDate(chooseYear, chooseMonth, chooseDay, hourOfDay, minute, 0));
                chooseDateString = chooseYear + "" + chooseMonth + "" + chooseDay + "" + hourOfDay + "" + minute + "00" + "00";
            }
        }, chooseHour, chooseMinute, true);
        tpd.show();
    }

    public void setCurrentPaymentCategory(PaymentCategory currentPaymentCategory) {
        this.currentPaymentCategory = currentPaymentCategory;
        categoryIv.setImageResource(currentPaymentCategory.getIconId());
    }

    public void setCurrentPaymentSubcategory(PaymentSubcategory currentPaymentSubcategory) {
        this.currentPaymentSubcategory = currentPaymentSubcategory;
        subcategoryTv.setText(this.currentPaymentSubcategory.getSubcategoryName());

    }

    private boolean checkBeforeSave() {
        if (TextUtils.isEmpty(moneyEt.getText()) || Float.valueOf(moneyEt.getText().toString()) < 0) {
            Toast.makeText(getActivity(), "请输入有效金额!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public void savePayment() {
        if (!checkBeforeSave()) {
            return;
        }
        Income_payment payment = new Income_payment();
        BmobUser bmobUser = BmobUser.getCurrentUser(getActivity());
        payment.setUserId(bmobUser.getObjectId());
        payment.setDataType(0);
        payment.setDataTypeName("支出");
        payment.setCategoryNum(Integer.valueOf(currentPaymentCategory.getCategoryNum()));
        payment.setCategoryName(currentPaymentCategory.getCategoryName());
        payment.setSubcategoryNum(Integer.valueOf(currentPaymentSubcategory.getSubcategoryNum()));
        payment.setSubcategoryName(currentPaymentSubcategory.getSubcategoryName());
        payment.setMoney(Float.valueOf(moneyEt.getText().toString()));
        payment.setDate(chooseDate);
        payment.setRemark(remarkEt.getText().toString());
        payment.setReimbursed(reimbursedCb.isChecked());
        SaveListener paymentSaveListener = new SaveListener() {
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
        MyBMobManager.getInstance().savePayment(getActivity(), payment, paymentSaveListener);
    }


    public void saveFinancialAlarmPayment() {
        if (!checkBeforeSave()) {
            return;
        }
        FinancialAlarm payment = new FinancialAlarm();
        BmobUser bmobUser = BmobUser.getCurrentUser(getActivity());
        payment.setUserId(bmobUser.getObjectId());
        payment.setDataType(0);
        payment.setDataTypeName("支出");
        payment.setCategoryNum(Integer.valueOf(currentPaymentCategory.getCategoryNum()));
        payment.setCategoryName(currentPaymentCategory.getCategoryName());
        payment.setSubcategoryNum(Integer.valueOf(currentPaymentSubcategory.getSubcategoryNum()));
        payment.setSubcategoryName(currentPaymentSubcategory.getSubcategoryName());
        payment.setMoney(Float.valueOf(moneyEt.getText().toString()));
        payment.setDate(DateUtils.transNormalDate2MillisecondStr(chooseDate.getDate()));
        payment.setRemark(remarkEt.getText().toString());
        payment.setReimbursed(reimbursedCb.isChecked());
        payment.setIsAlarm(0);
        payment.setIsHandle(0);
        if (DateUtils.calculateDayMsDate(DateUtils.transCurrentDate2MillisecondStr(), chooseDateString) >= 0) {
            payment.setIsObsolete(0);
        } else {
            payment.setIsObsolete(1);
        }
        if (payment.save()) {
            Toast.makeText(getActivity(), "保存成功", Toast.LENGTH_SHORT).show();
        }

    }
}
