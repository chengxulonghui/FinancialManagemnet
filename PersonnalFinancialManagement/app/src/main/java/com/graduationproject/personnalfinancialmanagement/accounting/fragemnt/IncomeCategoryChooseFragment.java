package com.graduationproject.personnalfinancialmanagement.accounting.fragemnt;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.graduationproject.personnalfinancialmanagement.R;
import com.graduationproject.personnalfinancialmanagement.accounting.AccountingActivity;
import com.graduationproject.personnalfinancialmanagement.accounting.typeconfig.CategoryManager;
import com.graduationproject.personnalfinancialmanagement.accounting.typeconfig.IncomeCategory;
import com.graduationproject.personnalfinancialmanagement.accounting.typeconfig.PaymentCategory;
import com.graduationproject.personnalfinancialmanagement.accounting.typeconfig.PaymentSubcategory;
import com.graduationproject.personnalfinancialmanagement.home.QuickRecord.AddQuickRecord.AddQuickRecordActivity;

/**
 * Created by longhui on 2016/5/13.
 */
public class IncomeCategoryChooseFragment extends Fragment {
    //TitleBar相关
    private ImageView backIv, confirm;
    private TextView titleTv;

    private ListView categoryLv;
    private AccountingIncomeCategoryAdapter mAccountingIncomeCategoryAdapter;
    private IncomeCategory currentIncomeCategory;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.accounting_income_category_choose_fragment, null);
        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        initView(rootView);
        return rootView;
    }

    private void initView(View v) {
        categoryLv = (ListView) v.findViewById(R.id.accounting_income_category_lv);
        initTitleBar(v);
        mAccountingIncomeCategoryAdapter = new AccountingIncomeCategoryAdapter(getActivity(), this);
        categoryLv.setAdapter(mAccountingIncomeCategoryAdapter);
    }

    private void initTitleBar(View titleBar) {
        backIv = (ImageView) titleBar.findViewById(R.id.title_bar_back_iv);
        confirm = (ImageView) titleBar.findViewById(R.id.title_bar_confirm_tv);
        titleTv = (TextView) titleBar.findViewById(R.id.title_bar_title_tv);
        backIv = (ImageView) titleBar.findViewById(R.id.title_bar_back_iv);
        titleTv.setText("收入类别");
        confirm.setVisibility(View.GONE);
        backIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                remove();
            }
        });
    }


    public void setIncomeCategory(IncomeCategory incomeCategory) {
        currentIncomeCategory = incomeCategory;
        if (getActivity() instanceof AccountingActivity) {
            ((AccountingActivity) getActivity()).setCurrentIncomeCategory(currentIncomeCategory);
        }
        if (getActivity() instanceof AddQuickRecordActivity) {
            ((AddQuickRecordActivity) getActivity()).setCurrentIncomeCategory(currentIncomeCategory);
        }
        remove();
    }

    public void remove() {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.popBackStack();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.remove(this);
        fragmentTransaction.commit();
    }
}
