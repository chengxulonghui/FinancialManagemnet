package com.graduationproject.personnalfinancialmanagement.customerservice.news;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.graduationproject.personnalfinancialmanagement.R;
import com.graduationproject.personnalfinancialmanagement.config.StringConfig;
import com.graduationproject.personnalfinancialmanagement.customerservice.news.activity.NewsDetailActivity;
import com.graduationproject.personnalfinancialmanagement.customerservice.news.config.NewsListItemDataHolder;

import java.util.List;

/**
 * Created by longhui on 2016/5/17.
 */
public class MyNewsListAdapter extends BaseAdapter {
    Context mContext;
    List<NewsListItemDataHolder> mNewsList;
    LayoutInflater inflater;

    public MyNewsListAdapter(Context context, List<NewsListItemDataHolder> newsList) {
        this.mContext = context;
        this.mNewsList = newsList;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return this.mNewsList.size();
    }

    @Override
    public Object getItem(int position) {
        return this.mNewsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        NewsListItemDataHolder itemData = this.mNewsList.get(position);
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.news_list_list_item, null);
            viewHolder.newsItemMainLl = (LinearLayout) convertView.findViewById(R.id.news_list_item_main_ll);
            viewHolder.newsItemTitleTv = (TextView) convertView.findViewById(R.id.news_list_item_title_tv);
            viewHolder.newsItemDigestTv = (TextView) convertView.findViewById(R.id.news_list_item_digest_tv);
            viewHolder.newsItemReplyCountTv = (TextView) convertView.findViewById(R.id.news_list_item_reply_num_tv);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.newsItemTitleTv.setText(itemData.getTitle());
        viewHolder.newsItemDigestTv.setText(itemData.getLtitle());
        if(!TextUtils.isEmpty(itemData.getReplyCount())) {
            viewHolder.newsItemReplyCountTv.setText(itemData.getReplyCount() + "跟帖数");
        }
        else{
            viewHolder.newsItemReplyCountTv.setVisibility(View.INVISIBLE);
        }
        MyNewsListOnClickListener myNewsListOnClickListener=new MyNewsListOnClickListener(itemData);
        viewHolder.newsItemMainLl.setOnClickListener(myNewsListOnClickListener);
        return convertView;
    }

    public List<NewsListItemDataHolder> getCurrentNewsList() {
        return this.mNewsList;
    }

    public void setNewsList(List<NewsListItemDataHolder> newsListItemDataHolders) {
        this.mNewsList.clear();
        this.mNewsList.addAll(newsListItemDataHolders);
        refresh();
    }

    public void addNews(List<NewsListItemDataHolder> newsListItemDataHolders) {
        getCurrentNewsList().addAll(newsListItemDataHolders);
        refresh();
    }

    public void refresh() {
        notifyDataSetChanged();
    }

    class ViewHolder {
        LinearLayout newsItemMainLl;
        TextView newsItemTitleTv;
        TextView newsItemDigestTv;
        TextView newsItemReplyCountTv;
    }
    class MyNewsListOnClickListener implements View.OnClickListener{
        NewsListItemDataHolder itemData;
        public MyNewsListOnClickListener(NewsListItemDataHolder itemData){
            this.itemData=itemData;
        }
        @Override
        public void onClick(View v) {
            Intent intent=new Intent(mContext, NewsDetailActivity.class);
            intent.putExtra(StringConfig.NEWS_TAG,itemData);
            mContext.startActivity(intent);
        }
    }
}
