package com.graduationproject.personnalfinancialmanagement.statements.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.graduationproject.personnalfinancialmanagement.R;
import com.graduationproject.personnalfinancialmanagement.accounting.typeconfig.CategoryManager;
import com.graduationproject.personnalfinancialmanagement.config.javabean.FinancialAlarm;
import com.graduationproject.personnalfinancialmanagement.statements.FinancialStatementsActivity;
import com.graduationproject.personnalfinancialmanagement.statements.javabean.IncomePaymentStatementItem;
import com.graduationproject.personnalfinancialmanagement.utils.layoututils.CustomBarView;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by longhui on 2016/5/26.
 */
public class StatementListAdapter extends RecyclerView.Adapter<StatementListAdapter.StatementViewHolder> {
    Context mContext;
    List<IncomePaymentStatementItem> mIncomePaymentStatementItemList;
    LayoutInflater inflater;

    public StatementListAdapter(Context context, List<IncomePaymentStatementItem> incomePaymentStatementItemList) {
        this.mContext = context;
        this.mIncomePaymentStatementItemList = incomePaymentStatementItemList;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public StatementViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        StatementViewHolder holder = new StatementViewHolder(inflater.inflate(R.layout.statement_list_item, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(StatementViewHolder holder, int position) {
        IncomePaymentStatementItem itemData = this.mIncomePaymentStatementItemList.get(position);
        if (itemData.getDataType() == 0) {
            holder.categoryIconIv.setImageResource(CategoryManager.getSingleton().getPaymentCategoryIconIdByCategoryNum(itemData.getCategoryNum() + ""));
            holder.percentTv.setTextColor(mContext.getResources().getColor(R.color.title_red));
            holder.moneyProgressView.setBackgroundColor(mContext.getResources().getColor(R.color.title_red));
            holder.percentTv.setTextColor(mContext.getResources().getColor(R.color.title_red));
        } else {
            holder.categoryIconIv.setImageResource(CategoryManager.getSingleton().getIncomeCategoryIconIdByCategoryNum(itemData.getCategoryNum() + ""));
            holder.percentTv.setTextColor(mContext.getResources().getColor(R.color.font_green));
            holder.moneyProgressView.setBackgroundColor(mContext.getResources().getColor(R.color.font_green));
            holder.percentTv.setTextColor(mContext.getResources().getColor(R.color.font_green));
        }
        int categoryLength = itemData.getCategoryName().length();
        switch (categoryLength) {
            case 0:
                break;
            case 1:
                break;
            case 2:
                break;
            case 3:
                holder.categoryName.setTextSize(12);
                break;
            case 4:
                holder.categoryName.setTextSize(9);
                break;
        }
        DecimalFormat df = new DecimalFormat("0.00");
        holder.moneyProgressView.setBarProgress(itemData.getPercent());
        holder.moneyProgressView.setText(df.format(itemData.getMoneySum()) + "");
        holder.categoryName.setText(itemData.getCategoryName());
        holder.percentTv.setText(df.format(itemData.getPercent()) + "%");
        StatementOnClickListener statementOnClickListener = new StatementOnClickListener(itemData);
        holder.mainLl.setOnClickListener(statementOnClickListener);
    }

    @Override
    public int getItemCount() {
        return this.mIncomePaymentStatementItemList.size();
    }

    public void refresh() {
        notifyDataSetChanged();
    }

    public void setDataList(List<IncomePaymentStatementItem> incomePaymentStatementItemList) {
        this.mIncomePaymentStatementItemList.clear();
        this.mIncomePaymentStatementItemList.addAll(incomePaymentStatementItemList);
        refresh();
    }

    class StatementViewHolder extends RecyclerView.ViewHolder {
        ImageView categoryIconIv;
        TextView categoryName;
        CustomBarView moneyProgressView;
        TextView percentTv;
        LinearLayout mainLl;

        public StatementViewHolder(View itemView) {
            super(itemView);
            categoryIconIv = (ImageView) itemView.findViewById(R.id.statements_list_item_category_iv);
            categoryName = (TextView) itemView.findViewById(R.id.statements_list_item_category_tv);
            moneyProgressView = (CustomBarView) itemView.findViewById(R.id.statements_list_item_money_progress_cbv);
            percentTv = (TextView) itemView.findViewById(R.id.statements_list_item_percent_tv);
            mainLl = (LinearLayout) itemView.findViewById(R.id.statements_list_item_main_ll);
        }
    }


    class StatementOnClickListener implements View.OnClickListener {
        IncomePaymentStatementItem itemData;

        public StatementOnClickListener(IncomePaymentStatementItem itemData) {
            this.itemData = itemData;
        }

        @Override
        public void onClick(View v) {
            if (mContext instanceof FinancialStatementsActivity) {
                ((FinancialStatementsActivity) mContext).toSeeCategoryList(itemData.getDataType(), itemData.getCategoryNum(), itemData.getCategoryName());
            }
        }
    }

}
