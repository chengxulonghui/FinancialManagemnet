package com.graduationproject.personnalfinancialmanagement.accounting.fragemnt;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.graduationproject.personnalfinancialmanagement.R;
import com.graduationproject.personnalfinancialmanagement.accounting.typeconfig.CategoryManager;
import com.graduationproject.personnalfinancialmanagement.accounting.typeconfig.PaymentCategory;

/**
 * Created by longhui on 2016/5/13.
 */
public class AccountingPaymentCategoryAdapter extends BaseAdapter {
    Context mContext;
    LayoutInflater inflater;
    int currentCategory;
    PaymentTypeChooseFragment paymentTypeChooseFragment;
    public AccountingPaymentCategoryAdapter(Context context,PaymentTypeChooseFragment paymentTypeChooseFragment) {
        this.mContext = context;
        this.inflater = LayoutInflater.from(context);
        this.currentCategory = 0;
        this.paymentTypeChooseFragment=paymentTypeChooseFragment;
        CategoryManager.getSingleton();
    }

    @Override
    public int getCount() {
        return CategoryManager.getSingleton().getPaymentCategoryList().size();
    }

    @Override
    public Object getItem(int position) {
        return CategoryManager.getSingleton().getPaymentCategoryList().get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        PaymentCategory categoryData = CategoryManager.getSingleton().getPaymentCategoryList().get(position);
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.accounting_payment_category_list_item, null);
            viewHolder.paymentCategoryMain = (LinearLayout) convertView.findViewById(R.id.accounting_payment_category_list_item_main_ll);
            viewHolder.paymentCategoryTypeTv = (TextView) convertView.findViewById(R.id.accounting_payment_category_list_item_type_tv);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.paymentCategoryTypeTv.setText(categoryData.getCategoryName());
        if (position != currentCategory) {
            viewHolder.paymentCategoryMain.setBackgroundColor(mContext.getResources().getColor(R.color.light_gray));
        } else {
            viewHolder.paymentCategoryMain.setBackgroundColor(mContext.getResources().getColor(R.color.white));
        }
        MyCategoryOncClickListener myCategoryOncClickListener = new MyCategoryOncClickListener(viewHolder, categoryData);
        viewHolder.paymentCategoryMain.setOnClickListener(myCategoryOncClickListener);
        return convertView;
    }

    public void refresh() {
        notifyDataSetChanged();
    }

    class ViewHolder {
        LinearLayout paymentCategoryMain;
        TextView paymentCategoryTypeTv;
    }

    class MyCategoryOncClickListener implements View.OnClickListener {
        ViewHolder itemView;
        PaymentCategory itemData;

        public MyCategoryOncClickListener(ViewHolder itemView, PaymentCategory itemData) {
            this.itemView = itemView;
            this.itemData = itemData;
        }

        @Override
        public void onClick(View v) {
            int index = CategoryManager.getSingleton().getPaymentCategoryList().indexOf(itemData);
            if (index != currentCategory) {
                currentCategory = index;
                paymentTypeChooseFragment.setCurrentCategory(itemData);
                refresh();
            }
        }
    }
}
