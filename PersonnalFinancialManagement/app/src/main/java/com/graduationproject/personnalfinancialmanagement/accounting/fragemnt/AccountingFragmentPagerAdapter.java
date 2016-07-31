package com.graduationproject.personnalfinancialmanagement.accounting.fragemnt;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by longhui on 2016/5/8.
 */
public class AccountingFragmentPagerAdapter extends FragmentPagerAdapter {
    List<Fragment> fragmentList;
    String[] titles={"支出","收入"};
    public AccountingFragmentPagerAdapter(FragmentManager fm,List<Fragment> fragments) {
        super(fm);
        this.fragmentList=fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}
