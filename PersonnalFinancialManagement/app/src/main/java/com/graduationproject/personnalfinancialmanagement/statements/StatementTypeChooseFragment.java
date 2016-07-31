package com.graduationproject.personnalfinancialmanagement.statements;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;

import com.graduationproject.personnalfinancialmanagement.R;

/**
 * Created by longhui on 2016/5/26.
 */
public class StatementTypeChooseFragment extends Fragment {
    //TitleBar相关
    private ImageView backIv, confirm;
    private TextView titleTv;

    private TableRow paymentTableRow;
    private TableRow incomeTableRow;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.statement_type_choose_fragment, container, false);
        initView(rootView);
        return rootView;
    }

    private void initView(View v) {
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        initTitleBar(v.findViewById(R.id.statement_type_choose_title_bar));
        paymentTableRow = (TableRow) v.findViewById(R.id.statement_type_choose_payment_tr);
        incomeTableRow = (TableRow) v.findViewById(R.id.statement_type_choose_income_tr);
        paymentTableRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((FinancialStatementsActivity) getActivity()).setDataType(0);
                remove();
            }
        });
        incomeTableRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((FinancialStatementsActivity) getActivity()).setDataType(1);
                remove();
            }
        });
    }

    private void initTitleBar(View titleBar) {
        backIv = (ImageView) titleBar.findViewById(R.id.title_bar_back_iv);
        confirm = (ImageView) titleBar.findViewById(R.id.title_bar_confirm_tv);
        titleTv = (TextView) titleBar.findViewById(R.id.title_bar_title_tv);
        backIv = (ImageView) titleBar.findViewById(R.id.title_bar_back_iv);
        confirm.setVisibility(View.GONE);
        titleTv.setText("选择报表类型");
        backIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                remove();
            }
        });
    }

    public void remove() {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.popBackStack();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.remove(this);
        fragmentTransaction.commit();
    }
}
