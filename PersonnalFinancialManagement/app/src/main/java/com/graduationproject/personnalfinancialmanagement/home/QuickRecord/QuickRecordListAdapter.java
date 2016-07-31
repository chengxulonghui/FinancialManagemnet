package com.graduationproject.personnalfinancialmanagement.home.QuickRecord;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.graduationproject.personnalfinancialmanagement.R;
import com.graduationproject.personnalfinancialmanagement.accounting.typeconfig.CategoryManager;
import com.graduationproject.personnalfinancialmanagement.accounting.typeconfig.IncomeCategory;
import com.graduationproject.personnalfinancialmanagement.accounting.typeconfig.PaymentCategory;
import com.graduationproject.personnalfinancialmanagement.accounting.typeconfig.PaymentSubcategory;
import com.graduationproject.personnalfinancialmanagement.config.javabean.QuickRecordModel;

import java.util.List;

/**
 * Created by longhui on 2016/5/27.
 */
public class QuickRecordListAdapter extends RecyclerView.Adapter<QuickRecordListAdapter.QuickRecordViewHolder> {
    Context mContext;
    List<QuickRecordModel> mQuickRecordModelList;
    LayoutInflater inflater;

    public QuickRecordListAdapter(Context mContext, List<QuickRecordModel> mQuickRecordModelList) {
        this.mContext = mContext;
        this.mQuickRecordModelList = mQuickRecordModelList;
        this.inflater = LayoutInflater.from(mContext);
    }

    @Override
    public QuickRecordViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        QuickRecordViewHolder holder = new QuickRecordViewHolder(inflater.inflate(R.layout.quick_record_list_item, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(QuickRecordViewHolder holder, int position) {
        QuickRecordModel itemData = this.mQuickRecordModelList.get(position);
        holder.nameTv.setText(itemData.getName());
        if (itemData.getDataType() == 0) {
            PaymentCategory paymentCategory = CategoryManager.getSingleton().getPaymentCategoryByCategoryNum(itemData.getCategoryNum() + "");
            PaymentSubcategory paymentSubcategory = CategoryManager.getSingleton().getPaymentSubCategoryByCategoryNum(paymentCategory, itemData.getSubcategoryNum() + "");
            holder.categoryTv.setText(paymentCategory.getCategoryName() + " " + "-" + " " + paymentSubcategory.getSubcategoryName());
            holder.typeTv.setText("支出");
        } else {
            IncomeCategory incomeCategory = CategoryManager.getSingleton().getIncomeCategoryByCategoryNum(itemData.getCategoryNum() + "");
            holder.categoryTv.setText(incomeCategory.getCategoryName());
            holder.typeTv.setText("收入");
        }
        QuickRecordOnClickListener quickRecordOnClickListener = new QuickRecordOnClickListener(itemData);
        holder.mainLl.setOnClickListener(quickRecordOnClickListener);
        holder.mainLl.setOnLongClickListener(quickRecordOnClickListener);
    }

    public void refresh() {
        notifyDataSetChanged();
    }

    public List<QuickRecordModel> getDataList() {
        return this.mQuickRecordModelList;
    }

    public void setDataList(List<QuickRecordModel> quickRecordModelList) {
        this.mQuickRecordModelList.clear();
        this.mQuickRecordModelList.addAll(quickRecordModelList);
        refresh();
    }

    @Override
    public int getItemCount() {
        return this.mQuickRecordModelList.size();
    }

    class QuickRecordViewHolder extends RecyclerView.ViewHolder {
        LinearLayout mainLl;
        TextView nameTv;
        TextView typeTv;
        TextView categoryTv;

        public QuickRecordViewHolder(View itemView) {
            super(itemView);
            mainLl = (LinearLayout) itemView.findViewById(R.id.quick_record_list_main_ll);
            nameTv = (TextView) itemView.findViewById(R.id.quick_record_list_name_tv);
            typeTv = (TextView) itemView.findViewById(R.id.quick_record_list_type_tv);
            categoryTv = (TextView) itemView.findViewById(R.id.quick_record_list_category_tv);
        }
    }

    class QuickRecordOnClickListener implements View.OnClickListener, View.OnLongClickListener {
        QuickRecordModel itemData;

        public QuickRecordOnClickListener(QuickRecordModel itemData) {
            this.itemData = itemData;
        }

        @Override
        public void onClick(View v) {
            ((QuickRecordListActivity) mContext).toAccounting(itemData);
        }

        @Override
        public boolean onLongClick(View v) {
            switch (v.getId()) {
                case R.id.quick_record_list_main_ll:
                    if (mContext instanceof QuickRecordListActivity) {
                        AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
                        dialog.setTitle("删除速记模板");
                        dialog.setMessage("确定要删除此速记模板吗？");
                        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ((QuickRecordListActivity) mContext).deleteQr(itemData);
                            }
                        });
                        dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        dialog.create();
                        dialog.show();
                    }
                    break;
            }
            return false;
        }
    }
}
