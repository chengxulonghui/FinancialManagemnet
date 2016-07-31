package com.graduationproject.personnalfinancialmanagement.customerservice.memo;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.graduationproject.personnalfinancialmanagement.R;
import com.graduationproject.personnalfinancialmanagement.config.javabean.MemoLitePal;
import com.graduationproject.personnalfinancialmanagement.utils.DateUtils;

import java.util.List;

/**
 * Created by longhui on 2016/5/21.
 */
public class MyMemoListAdapter extends RecyclerView.Adapter<MyMemoListAdapter.MyViewHolder> {
    Context mContext;
    List<MemoLitePal> mMemoList;
    LayoutInflater inflater;

    public MyMemoListAdapter(Context context, List<MemoLitePal> memoList) {
        this.mContext = context;
        this.mMemoList = memoList;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(inflater.inflate(R.layout.memorandum_list_item, parent,
                false));
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        MemoLitePal itemData = this.mMemoList.get(position);
        holder.contentTv.setText(itemData.getContent());
        holder.dateTv.setText(DateUtils.transMsString2NormalDateStr(itemData.getCreateDate()));
        MyMemoOnClickListener myMemoOnClickListener = new MyMemoOnClickListener(itemData);
        holder.contentCv.setOnClickListener(myMemoOnClickListener);
    }

    @Override
    public int getItemCount() {
        return mMemoList.size();
    }

    public void refresh() {
        notifyDataSetChanged();
    }

    public void setDataList(List<MemoLitePal> dataList) {
        this.mMemoList.clear();
        this.mMemoList.addAll(dataList);
        refresh();
    }

    public List<MemoLitePal> getDataList() {
        return this.mMemoList;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        CardView contentCv;
        TextView contentTv;
        TextView dateTv;

        public MyViewHolder(View view) {
            super(view);
            contentCv = (CardView) view.findViewById(R.id.memorandum_list_item_content_cv);
            contentTv = (TextView) view.findViewById(R.id.memorandum_list_item_content_tv);
            dateTv = (TextView) view.findViewById(R.id.memorandum_list_item_date_tv);
        }
    }

    class MyMemoOnClickListener implements View.OnClickListener {
        MemoLitePal itemData;

        public MyMemoOnClickListener(MemoLitePal memoLitePal) {
            this.itemData = memoLitePal;
        }

        @Override
        public void onClick(View v) {
            if (mContext instanceof MemorandumActivity) {
                ((MemorandumActivity) mContext).toEditMemo(itemData);
            }
        }
    }
}
