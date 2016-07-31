package com.graduationproject.personnalfinancialmanagement.home.QuickRecord.AddQuickRecord;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.astuetz.PagerSlidingTabStrip;
import com.graduationproject.personnalfinancialmanagement.R;
import com.graduationproject.personnalfinancialmanagement.accounting.fragemnt.AccountingFragmentPagerAdapter;
import com.graduationproject.personnalfinancialmanagement.accounting.fragemnt.IncomeCategoryChooseFragment;
import com.graduationproject.personnalfinancialmanagement.accounting.fragemnt.IncomeFragment;
import com.graduationproject.personnalfinancialmanagement.accounting.fragemnt.PaymentFragment;
import com.graduationproject.personnalfinancialmanagement.accounting.fragemnt.PaymentTypeChooseFragment;
import com.graduationproject.personnalfinancialmanagement.accounting.typeconfig.CategoryManager;
import com.graduationproject.personnalfinancialmanagement.accounting.typeconfig.IncomeCategory;
import com.graduationproject.personnalfinancialmanagement.accounting.typeconfig.PaymentCategory;
import com.graduationproject.personnalfinancialmanagement.accounting.typeconfig.PaymentSubcategory;
import com.graduationproject.personnalfinancialmanagement.base.MyBaseAppCompatActivity;
import com.graduationproject.personnalfinancialmanagement.config.StringConfig;
import com.graduationproject.personnalfinancialmanagement.utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by longhui on 2016/5/7.
 */
public class AddQuickRecordActivity extends MyBaseAppCompatActivity {
    //TitleBar相关
    private ImageView backIv, confirm;
    private TextView titleTv;

    PagerSlidingTabStrip accountingTabs;
    ViewPager accountingPager;
    TextView saveTv;
    List<Fragment> fragments = new ArrayList<Fragment>();
    AccountingFragmentPagerAdapter accountingFragmentPagerAdapter;
    AddQuickRecordPaymentFragment mPaymentFragment;
    AddQuickRecordIncomeFragment mIncomeFragment;
    PaymentTypeChooseFragment mPaymentTypeChooseFragment;
    IncomeCategoryChooseFragment mIncomeCategoryChooseFragment;
    private PaymentCategory currentPaymentCategory;
    private PaymentSubcategory currentPaymentSubcategory;
    private IncomeCategory currentIncomeCategory;
    boolean isFinancialAlarm = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accounting);
        initType();
        initView();
    }

    //判断是记账还是记录财务日历
    private void initType() {
        String action = getIntent().getAction();
        if (action != null && action.equals(StringConfig.ACTION_SAVE_FINANCIAL_ALARM)) {
            isFinancialAlarm = true;
        }
    }

    private void initView() {
        initTitleBar(findViewById(R.id.accounting_title_bar));
        accountingTabs = (PagerSlidingTabStrip) findViewById(R.id.accounting_tabs);
        accountingPager = (ViewPager) findViewById(R.id.accounting_pager);
        saveTv = (TextView) findViewById(R.id.accounting_save_tv);
        accountingTabs.setIndicatorColorResource(R.color.white);
        accountingTabs.setTextColorResource(R.color.white);
        currentPaymentCategory = CategoryManager.getSingleton().getPaymentCategoryList().get(0);
        currentPaymentSubcategory = currentPaymentCategory.getSubcategories().get(0);
        currentIncomeCategory = CategoryManager.getSingleton().getIncomeCategoryList().get(0);
        mPaymentFragment = new AddQuickRecordPaymentFragment();
        mIncomeFragment = new AddQuickRecordIncomeFragment();
        fragments.add(mPaymentFragment);
        fragments.add(mIncomeFragment);
        accountingFragmentPagerAdapter = new AccountingFragmentPagerAdapter(getSupportFragmentManager(), fragments);
        accountingPager.setOffscreenPageLimit(3);
        accountingPager.setAdapter(accountingFragmentPagerAdapter);
        accountingTabs.setViewPager(accountingPager);
        accountingTabs.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                CommonUtils.hideSoftKeyboard(AddQuickRecordActivity.this, accountingTabs);

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        setPaymentCategory(currentPaymentCategory);
                        setPaymentSubcategory(currentPaymentSubcategory);
                        break;
                    case 1:
                        setCurrentIncomeCategory(currentIncomeCategory);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        saveTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (accountingPager.getCurrentItem()) {
                    case 0:
                        if (isFinancialAlarm) {

                        } else {
                            mPaymentFragment.savePaymentRecord();
                        }
                        break;
                    case 1:
                        if (isFinancialAlarm) {
                        } else {
                            mIncomeFragment.saveIncomeRecord();
                        }
                        break;
                }
            }
        });
        accountingPager.setCurrentItem(0);
    }

    private void initTitleBar(View titleBar) {
        backIv = (ImageView) titleBar.findViewById(R.id.title_bar_back_iv);
        confirm = (ImageView) titleBar.findViewById(R.id.title_bar_confirm_tv);
        titleTv = (TextView) titleBar.findViewById(R.id.title_bar_title_tv);
        backIv = (ImageView) titleBar.findViewById(R.id.title_bar_back_iv);
        confirm.setVisibility(View.VISIBLE);
        titleTv.setText("添加速记模板");
        backIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveTv.performClick();
            }
        });
    }

    public void toChoosePaymentType() {
        CommonUtils.hideSoftKeyboard(this, accountingPager);
        getSupportFragmentManager().popBackStack();
        mPaymentTypeChooseFragment = new PaymentTypeChooseFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.accounting_main_fl, mPaymentTypeChooseFragment);
        fragmentTransaction.addToBackStack("add");
        fragmentTransaction.commit();
    }

    public void toChooseIncomeCategory() {
        CommonUtils.hideSoftKeyboard(this, accountingPager);
        getSupportFragmentManager().popBackStack();
        mIncomeCategoryChooseFragment = new IncomeCategoryChooseFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.accounting_main_fl, mIncomeCategoryChooseFragment);
        fragmentTransaction.addToBackStack("add");
        fragmentTransaction.commit();
    }

    public void setPaymentCategory(PaymentCategory paymentCategory) {
        currentPaymentCategory = paymentCategory;
        mPaymentFragment.setCurrentPaymentCategory(paymentCategory);
    }

    public void setPaymentSubcategory(PaymentSubcategory paymentSubcategory) {
        currentPaymentSubcategory = paymentSubcategory;
        mPaymentFragment.setCurrentPaymentSubcategory(paymentSubcategory);
    }

    public void setCurrentIncomeCategory(IncomeCategory incomeCategory) {
        currentIncomeCategory = incomeCategory;
        mIncomeFragment.setCurrentIncomeCategory(incomeCategory);
    }
}
