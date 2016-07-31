package com.graduationproject.personnalfinancialmanagement.accounting.fragemnt;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.graduationproject.personnalfinancialmanagement.R;
import com.graduationproject.personnalfinancialmanagement.accounting.typeconfig.PaymentCategory;
import com.graduationproject.personnalfinancialmanagement.accounting.typeconfig.PaymentSubcategory;

/**
 * Created by longhui on 2016/5/13.
 */
public class AccountingPaymentSubcategoryAdapter extends BaseAdapter {
    Context mContext;
    LayoutInflater inflater;
    PaymentCategory currentCategory;
    PaymentTypeChooseFragment paymentTypeChooseFragment;
    public AccountingPaymentSubcategoryAdapter(Context context,PaymentCategory paymentCategory,PaymentTypeChooseFragment paymentTypeChooseFragment) {
        this.mContext = context;
        this.inflater = LayoutInflater.from(context);
        this.currentCategory = paymentCategory;
        this.paymentTypeChooseFragment=paymentTypeChooseFragment;
    }

    @Override
    public int getCount() {
        return currentCategory.getSubcategories().size();
    }

    @Override
    public Object getItem(int position) {
        return currentCategory.getSubcategories().get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        PaymentSubcategory subcategoryData = this.currentCategory.getSubcategories().get(position);
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.accounting_payment_subcategory_list_item, null);
            viewHolder.paymentSubcategoryMain = (LinearLayout) convertView.findViewById(R.id.accounting_payment_subcategory_list_item_main_ll);
            viewHolder.paymentSubcategoryTypeTv = (TextView) convertView.findViewById(R.id.accounting_payment_subcategory_list_item_type_tv);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.paymentSubcategoryTypeTv.setText(subcategoryData.getSubcategoryName());
        MySubcategoryOncClickListener myCategoryOncClickListener = new MySubcategoryOncClickListener(viewHolder, subcategoryData);
        viewHolder.paymentSubcategoryMain.setOnClickListener(myCategoryOncClickListener);
        return convertView;
    }
    public void resetData(PaymentCategory paymentCategory){
        this.currentCategory=paymentCategory;
        refresh();
    }
    public void refresh() {
        notifyDataSetChanged();
    }

    class ViewHolder {
        LinearLayout paymentSubcategoryMain;
        TextView paymentSubcategoryTypeTv;
    }

    class MySubcategoryOncClickListener implements View.OnClickListener {
        ViewHolder itemView;
        PaymentSubcategory itemData;

        public MySubcategoryOncClickListener(ViewHolder itemView, PaymentSubcategory itemData) {
            this.itemView = itemView;
            this.itemData = itemData;
        }

        @Override
        public void onClick(View v) {
            paymentTypeChooseFragment.setCurrentPaymentSubcategory(itemData);
            paymentTypeChooseFragment.setPaymentType();
        }
    }
}
