package com.graduationproject.personnalfinancialmanagement.home.QuickRecord.AddQuickRecord;

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
import com.graduationproject.personnalfinancialmanagement.accounting.typeconfig.IncomeCategory;
import com.graduationproject.personnalfinancialmanagement.config.javabean.FinancialAlarm;
import com.graduationproject.personnalfinancialmanagement.config.javabean.Income_payment;
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
public class AddQuickRecordIncomeFragment extends Fragment {
    IncomeCategory currentIncomeCategory;
    private EditText nameEt;
    private TextView categoryTv;
    private TableRow incomeTypeTr;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.add_quick_record_income_fragment, null);
        initView(rootView);
        initData();
        return rootView;
    }

    private void initView(View v) {
        nameEt = (EditText) v.findViewById(R.id.add_income_quick_record_name_et);
        categoryTv = (TextView) v.findViewById(R.id.add_income_quick_record_category_tv);
        incomeTypeTr = (TableRow) v.findViewById(R.id.add_income_quick_record_category_tr);
        incomeTypeTr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((AddQuickRecordActivity) getActivity()).toChooseIncomeCategory();
            }
        });
        nameEt.addTextChangedListener(new MaxLengthWatcher(getActivity(),20,nameEt,"文字长度超过上限"));
    }

    private void initData() {

    }

    public void setCurrentIncomeCategory(IncomeCategory currentIncomeCategory) {
        this.currentIncomeCategory = currentIncomeCategory;
        categoryTv.setText(currentIncomeCategory.getCategoryName());
    }


    private boolean checkBeforeSave() {
        if (TextUtils.isEmpty(nameEt.getText())) {
            Toast.makeText(getActivity(), "请输入名字", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public void saveIncomeRecord() {
        if (!checkBeforeSave()) {
            return;
        }
        QuickRecordModel quickRecordModel = new QuickRecordModel();
        BmobUser bmobUser = BmobUser.getCurrentUser(getActivity());
        quickRecordModel.setName(nameEt.getText().toString());
        quickRecordModel.setUserId(bmobUser.getObjectId());
        quickRecordModel.setDataType(1);
        quickRecordModel.setCategoryNum(Integer.valueOf(currentIncomeCategory.getCategoryNum()));
        CustomProgressHUDManager.getInstance().show(getActivity(), "保存中", 100);
        if (quickRecordModel.save()) {
            CustomProgressHUDManager.getInstance().dismiss();
            Toast.makeText(getActivity(), "保存成功", Toast.LENGTH_SHORT).show();
        } else {
            CustomProgressHUDManager.getInstance().dismiss();
            Toast.makeText(getActivity(), "发生未知原因,保存失败", Toast.LENGTH_SHORT).show();
        }

    }

}
