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
import com.graduationproject.personnalfinancialmanagement.accounting.typeconfig.PaymentCategory;
import com.graduationproject.personnalfinancialmanagement.accounting.typeconfig.PaymentSubcategory;
import com.graduationproject.personnalfinancialmanagement.home.QuickRecord.AddQuickRecord.AddQuickRecordActivity;

/**
 * Created by longhui on 2016/5/13.
 */
public class PaymentTypeChooseFragment extends Fragment {
    //TitleBar相关
    private ImageView backIv, confirm;
    private TextView titleTv;

    private ListView categoryLv, subCategoryLv;
    private AccountingPaymentCategoryAdapter mAccountingPaymentCategoryAdapter;
    private AccountingPaymentSubcategoryAdapter mAccountingPaymentSubcategoryAdapter;
    private PaymentCategory currentPaymentCategory;
    private PaymentSubcategory currentPaymentSubcategory;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.accounting_payment_type_choose_fragment, null);
        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        initView(rootView);
        return rootView;
    }

    private void initView(View v) {
        categoryLv = (ListView) v.findViewById(R.id.accounting_payment_type_choose_category_lv);
        subCategoryLv = (ListView) v.findViewById(R.id.accounting_payment_type_choose_subcategory_lv);
        initTitleBar(v);
        mAccountingPaymentCategoryAdapter = new AccountingPaymentCategoryAdapter(getActivity(), this);
        categoryLv.setAdapter(mAccountingPaymentCategoryAdapter);
        mAccountingPaymentSubcategoryAdapter = new AccountingPaymentSubcategoryAdapter(getActivity(), CategoryManager.getSingleton().getPaymentCategoryList().get(0), this);
        subCategoryLv.setAdapter(mAccountingPaymentSubcategoryAdapter);
        currentPaymentCategory = CategoryManager.getSingleton().getPaymentCategoryList().get(0);
        currentPaymentSubcategory = currentPaymentCategory.getSubcategories().get(0);
    }

    private void initTitleBar(View titleBar) {
        backIv = (ImageView) titleBar.findViewById(R.id.title_bar_back_iv);
        confirm = (ImageView) titleBar.findViewById(R.id.title_bar_confirm_tv);
        titleTv = (TextView) titleBar.findViewById(R.id.title_bar_title_tv);
        backIv = (ImageView) titleBar.findViewById(R.id.title_bar_back_iv);
        titleTv.setText("类别");
        confirm.setVisibility(View.GONE);
        backIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                remove();
            }
        });
    }

    public void setCurrentCategory(PaymentCategory categoryData) {
        currentPaymentCategory = categoryData;
        mAccountingPaymentSubcategoryAdapter.resetData(categoryData);
    }

    public void setCurrentPaymentSubcategory(PaymentSubcategory paymentSubcategory) {
        currentPaymentSubcategory = paymentSubcategory;
    }

    public void setPaymentType() {
        if (getActivity() instanceof AccountingActivity) {
            ((AccountingActivity) getActivity()).setPaymentCategory(currentPaymentCategory);
            ((AccountingActivity) getActivity()).setPaymentSubcategory(currentPaymentSubcategory);
        }
        if (getActivity() instanceof AddQuickRecordActivity) {
            ((AddQuickRecordActivity) getActivity()).setPaymentCategory(currentPaymentCategory);
            ((AddQuickRecordActivity) getActivity()).setPaymentSubcategory(currentPaymentSubcategory);
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
