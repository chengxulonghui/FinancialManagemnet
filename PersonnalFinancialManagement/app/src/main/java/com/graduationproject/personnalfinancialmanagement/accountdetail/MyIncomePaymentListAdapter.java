package com.graduationproject.personnalfinancialmanagement.accountdetail;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AlertDialog;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.graduationproject.personnalfinancialmanagement.R;
import com.graduationproject.personnalfinancialmanagement.config.javabean.Income_payment;
import com.graduationproject.personnalfinancialmanagement.utils.CommonUtils;
import com.graduationproject.personnalfinancialmanagement.utils.DateUtils;

import java.util.List;

/**
 * Created by longhui on 2016/5/20.
 */
public class MyIncomePaymentListAdapter extends BaseAdapter {
    Context mContext;
    LayoutInflater inflater;
    List<Income_payment> mIncomePaymentList;

    public MyIncomePaymentListAdapter(Context context, List<Income_payment> incomePaymentList) {
        this.mContext = context;
        this.mIncomePaymentList = incomePaymentList;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return this.mIncomePaymentList.size();
    }

    @Override
    public Object getItem(int position) {
        return this.mIncomePaymentList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Income_payment itemData = this.mIncomePaymentList.get(position);
        IPViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new IPViewHolder();
            convertView = inflater.inflate(R.layout.income_payment_list_item, null);
            viewHolder.mainLl = (LinearLayout) convertView.findViewById(R.id.income_payment_list_item_main_ll);
            viewHolder.moneyTv = (TextView) convertView.findViewById(R.id.income_payment_list_item_money_tv);
            viewHolder.dateTv = (TextView) convertView.findViewById(R.id.income_payment_list_item_date_tv);
            viewHolder.categoryTv = (TextView) convertView.findViewById(R.id.income_payment_list_item_category_tv);
            viewHolder.remarkTv = (TextView) convertView.findViewById(R.id.income_payment_list_item_remark_tv);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (IPViewHolder) convertView.getTag();
        }
        viewHolder.moneyTv.setText(itemData.getMoney() + "");
        if (itemData.getDataType() == 0) {
            viewHolder.categoryTv.setText(itemData.getCategoryName() + "-" + itemData.getSubcategoryName());
            viewHolder.moneyTv.setTextColor(mContext.getResources().getColor(R.color.title_red));
        } else {
            viewHolder.categoryTv.setText(itemData.getCategoryName());
            viewHolder.moneyTv.setTextColor(mContext.getResources().getColor(R.color.font_green));
        }
        viewHolder.dateTv.setText(DateUtils.getDateString_simple(itemData.getDate().getDate()));
        viewHolder.remarkTv.setText(itemData.getRemark());
        MyIncomePaymentOnClickListener myIncomePaymentOnClickListener = new MyIncomePaymentOnClickListener(itemData);
        viewHolder.mainLl.setOnClickListener(myIncomePaymentOnClickListener);
        viewHolder.mainLl.setOnLongClickListener(myIncomePaymentOnClickListener);
        return convertView;
    }


    public void refresh() {
        notifyDataSetChanged();
    }

    public void setDataList(List<Income_payment> dataList) {
        this.mIncomePaymentList.clear();
        this.mIncomePaymentList.addAll(dataList);
        refresh();
    }

    public List<Income_payment> getIncomePaymentList() {
        return mIncomePaymentList;
    }

    //I:income P:payment
    class IPViewHolder {
        LinearLayout mainLl;
        TextView categoryTv;
        TextView moneyTv;
        TextView dateTv;
        TextView remarkTv;
    }

    class MyIncomePaymentOnClickListener implements View.OnClickListener, View.OnLongClickListener {
        Income_payment itemData;

        public MyIncomePaymentOnClickListener(Income_payment incomePayment) {
            this.itemData = incomePayment;
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.income_payment_list_item_main_ll:
                    ((IncomePaymentListActivity) mContext).toIncomePaymentDetail(itemData);
                    break;
            }
        }

        @Override
        public boolean onLongClick(View v) {
            switch (v.getId()) {
                case R.id.income_payment_list_item_main_ll:
                    AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
                    dialog.setTitle("删除收支");
                    dialog.setMessage("确定要删除此项收支记录吗？(删除后将无法恢复)");
                    dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ((IncomePaymentListActivity) mContext).deleteIncomePayment(itemData);
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
            return false;
        }
    }
}
