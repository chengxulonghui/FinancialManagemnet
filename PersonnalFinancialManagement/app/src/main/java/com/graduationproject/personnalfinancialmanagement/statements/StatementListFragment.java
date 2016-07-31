package com.graduationproject.personnalfinancialmanagement.statements;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.graduationproject.personnalfinancialmanagement.R;
import com.graduationproject.personnalfinancialmanagement.statements.adapter.StatementListAdapter;
import com.graduationproject.personnalfinancialmanagement.statements.javabean.IncomePaymentStatementItem;

import java.io.Serializable;
import java.util.List;

/**
 * Created by longhui on 2016/5/26.
 */
public class StatementListFragment extends Fragment {
    RecyclerView listRv;
    StatementListAdapter mStatementListAdapter;
    List<IncomePaymentStatementItem> incomePaymentStatementItemList;
    TextView tipsTv;

    static StatementListFragment newInstance(List<IncomePaymentStatementItem> incomePaymentStatementItemList) {
        StatementListFragment newFragment = new StatementListFragment();
        if (incomePaymentStatementItemList != null) {
            Bundle bundle = new Bundle();
            bundle.putSerializable("data_list", (Serializable) incomePaymentStatementItemList);
            newFragment.setArguments(bundle);
        }
        return newFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle args = getArguments();
        if (args != null) {
            incomePaymentStatementItemList = (List<IncomePaymentStatementItem>) args.getSerializable("data_list");
        }
        View rootView = inflater.inflate(R.layout.statement_list_fragment, container, false);
        listRv = (RecyclerView) rootView.findViewById(R.id.statements_list_statement_rv);
        tipsTv = (TextView) rootView.findViewById(R.id.statements_list_tips_tv);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        listRv.setLayoutManager(linearLayoutManager);
        mStatementListAdapter = new StatementListAdapter(getActivity(), incomePaymentStatementItemList);
        listRv.setAdapter(mStatementListAdapter);
        if (incomePaymentStatementItemList.size() == 0) {
            tipsTv.setVisibility(View.VISIBLE);
        } else {
            tipsTv.setVisibility(View.GONE);
        }
        return rootView;
    }

    public void setIncomePaymentStatementItemList(List<IncomePaymentStatementItem> incomePaymentStatementItemList) {
        mStatementListAdapter.setDataList(incomePaymentStatementItemList);
        if (incomePaymentStatementItemList.size() == 0) {
            tipsTv.setVisibility(View.VISIBLE);
        } else {
            tipsTv.setVisibility(View.GONE);
        }
    }
}
