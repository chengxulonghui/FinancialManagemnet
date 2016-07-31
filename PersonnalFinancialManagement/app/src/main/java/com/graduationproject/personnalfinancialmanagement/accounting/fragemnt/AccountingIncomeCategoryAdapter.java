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
import com.graduationproject.personnalfinancialmanagement.accounting.typeconfig.IncomeCategory;
import com.graduationproject.personnalfinancialmanagement.accounting.typeconfig.PaymentCategory;

/**
 * Created by longhui on 2016/5/13.
 */
public class AccountingIncomeCategoryAdapter extends BaseAdapter {
    Context mContext;
    LayoutInflater inflater;
    int currentCategory;
    IncomeCategoryChooseFragment incomeCategoryChooseFragment;
    public AccountingIncomeCategoryAdapter(Context context, IncomeCategoryChooseFragment paymentTypeChooseFragment) {
        this.mContext = context;
        this.inflater = LayoutInflater.from(context);
        this.currentCategory = 0;
        this.incomeCategoryChooseFragment=paymentTypeChooseFragment;
        CategoryManager.getSingleton();
    }

    @Override
    public int getCount() {
        return CategoryManager.getSingleton().getIncomeCategoryList().size();
    }

    @Override
    public Object getItem(int position) {
        return CategoryManager.getSingleton().getIncomeCategoryList().get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        IncomeCategory categoryData = CategoryManager.getSingleton().getIncomeCategoryList().get(position);
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.accounting_income_category_list_item, null);
            viewHolder.incomeCategoryMain = (LinearLayout) convertView.findViewById(R.id.accounting_income_category_list_item_main_ll);
            viewHolder.incomeCategoryTypeTv = (TextView) convertView.findViewById(R.id.accounting_income_category_list_item_type_tv);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.incomeCategoryTypeTv.setText(categoryData.getCategoryName());
        MyCategoryOncClickListener myCategoryOncClickListener = new MyCategoryOncClickListener(viewHolder, categoryData);
        viewHolder.incomeCategoryMain.setOnClickListener(myCategoryOncClickListener);
        return convertView;
    }

    public void refresh() {
        notifyDataSetChanged();
    }

    class ViewHolder {
        LinearLayout incomeCategoryMain;
        TextView incomeCategoryTypeTv;
    }

    class MyCategoryOncClickListener implements View.OnClickListener {
        ViewHolder itemView;
        IncomeCategory itemData;

        public MyCategoryOncClickListener(ViewHolder itemView, IncomeCategory itemData) {
            this.itemView = itemView;
            this.itemData = itemData;
        }

        @Override
        public void onClick(View v) {
           incomeCategoryChooseFragment.setIncomeCategory(itemData);
        }
    }
}
