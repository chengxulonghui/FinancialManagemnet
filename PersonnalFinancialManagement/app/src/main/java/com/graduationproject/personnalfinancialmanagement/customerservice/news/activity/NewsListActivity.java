package com.graduationproject.personnalfinancialmanagement.customerservice.news.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.graduationproject.personnalfinancialmanagement.R;
import com.graduationproject.personnalfinancialmanagement.base.MyBaseAppCompatActivity;
import com.graduationproject.personnalfinancialmanagement.config.StringConfig;
import com.graduationproject.personnalfinancialmanagement.customerservice.news.MyNewsListAdapter;
import com.graduationproject.personnalfinancialmanagement.customerservice.news.config.NewsListDataHolder;
import com.graduationproject.personnalfinancialmanagement.customerservice.news.config.NewsListItemDataHolder;
import com.graduationproject.personnalfinancialmanagement.utils.http.volley.VolleyHttpUtils;

import java.util.ArrayList;

import in.srain.cube.util.LocalDisplay;
import in.srain.cube.views.loadmore.LoadMoreContainer;
import in.srain.cube.views.loadmore.LoadMoreHandler;
import in.srain.cube.views.loadmore.LoadMoreListViewContainer;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import in.srain.cube.views.ptr.header.MaterialHeader;


/**
 * Created by longhui on 2016/5/17.
 */
public class NewsListActivity extends MyBaseAppCompatActivity {
    //TitleBar相关
    private ImageView backIv, confirm;
    private TextView titleTv;

    private PtrFrameLayout mPtrFrameLayout;
    private ListView mNewsListView;
    private View mNewsLoadMoreFooterView;
    private MyNewsListAdapter myNewsListAdapter;
    private VolleyHttpUtils volleyHttpUtils;

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_news_list);
        initView();
        initData();
    }

    private void initView() {
        initTitleBar(findViewById(R.id.news_list_title_bar));
        mNewsLoadMoreFooterView = LayoutInflater.from(this).inflate(R.layout.news_list_loadmore_footer_view, null);
        mNewsLoadMoreFooterView.setVisibility(View.GONE);
        mPtrFrameLayout = (PtrFrameLayout) findViewById(R.id.news_list_load_more_list_view_ptr_frame);
        mNewsListView = (ListView) findViewById(R.id.news_list_lv);
        // header
        final MaterialHeader header = new MaterialHeader(this);
        header.setBackgroundColor(Color.parseColor("#ffffff"));
        int[] colors = getResources().getIntArray(R.array.google_colors);
        header.setColorSchemeColors(colors);
        header.setLayoutParams(new PtrFrameLayout.LayoutParams(-1, -2));
        header.setPadding(0, LocalDisplay.dp2px(15), 0, LocalDisplay.dp2px(10));
        header.setPtrFrameLayout(mPtrFrameLayout);

        mPtrFrameLayout.setLoadingMinTime(1000);
        mPtrFrameLayout.setDurationToCloseHeader(1500);
        mPtrFrameLayout.setHeaderView(header);
        mPtrFrameLayout.addPtrUIHandler(header);
        mPtrFrameLayout.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, mNewsListView, header);
            }

            @Override
            public void onRefreshBegin(final PtrFrameLayout frame) {
                getFirstData();
            }
        });
        mNewsListView.addFooterView(mNewsLoadMoreFooterView);
        myNewsListAdapter = new MyNewsListAdapter(this, new ArrayList<NewsListItemDataHolder>());
        mNewsListView.setAdapter(myNewsListAdapter);
        mNewsListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    // 当不滚动时
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                        // 判断滚动到底部
                        if (mNewsListView.getLastVisiblePosition() == (mNewsListView.getCount() - 1)) {
                            if (myNewsListAdapter.getCurrentNewsList().size() > 0) {
                                if (canLoadMore) {
                                    mNewsLoadMoreFooterView.setVisibility(View.VISIBLE);
                                    getMoreData();
                                }
                            }
                        }
                        // 判断滚动到顶部

                        if (mNewsListView.getFirstVisiblePosition() == 0) {

                        }

                        break;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem + visibleItemCount == totalItemCount) {
                    if (myNewsListAdapter.getCurrentNewsList().size() > 0) {
                        if (canLoadMore) {
                            mNewsLoadMoreFooterView.setVisibility(View.VISIBLE);
                            getMoreData();
                        }
                    }
                }

            }
        });
    }

    private void initTitleBar(View titleBar) {
        backIv = (ImageView) titleBar.findViewById(R.id.title_bar_back_iv);
        confirm = (ImageView) titleBar.findViewById(R.id.title_bar_confirm_tv);
        titleTv = (TextView) titleBar.findViewById(R.id.title_bar_title_tv);
        backIv = (ImageView) titleBar.findViewById(R.id.title_bar_back_iv);
        confirm.setVisibility(View.GONE);
        titleTv.setText("财经新闻");
        backIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }


    boolean canLoadMore = true;

    private void initData() {
        volleyHttpUtils = new VolleyHttpUtils(this);
        mNewsListView.postDelayed(new Runnable() {
            @Override
            public void run() {
                mPtrFrameLayout.autoRefresh(true);
            }
        }, 150);
    }

    Response.Listener<NewsListDataHolder> newsListDataHolderListener;
    Response.ErrorListener errorListener;
    Response.Listener<NewsListDataHolder> moreNewsListener;
    Response.ErrorListener moreNewsErrorListener;

    void getFirstData() {
        newsListDataHolderListener = new Response.Listener<NewsListDataHolder>() {
            @Override
            public void onResponse(NewsListDataHolder newsListDataHolder) {
                myNewsListAdapter.setNewsList(newsListDataHolder.getT1348648756099());
                mPtrFrameLayout.refreshComplete();
            }
        };
        errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                mPtrFrameLayout.refreshComplete();
                Toast.makeText(NewsListActivity.this, "加载失败", Toast.LENGTH_SHORT).show();
            }
        };
        volleyHttpUtils.getNewsList(newsListDataHolderListener, errorListener);
    }

    void getMoreData() {
        canLoadMore = false;
        moreNewsListener = new Response.Listener<NewsListDataHolder>() {
            @Override
            public void onResponse(NewsListDataHolder newsListDataHolder) {
                canLoadMore = true;
                mNewsLoadMoreFooterView.setVisibility(View.GONE);
                myNewsListAdapter.addNews(newsListDataHolder.getT1348648756099());
            }
        };
        moreNewsErrorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                canLoadMore = true;
                mNewsLoadMoreFooterView.setVisibility(View.GONE);
                Toast.makeText(NewsListActivity.this, "加载失败", Toast.LENGTH_SHORT).show();
            }
        };
        volleyHttpUtils.getMoreNewsList(myNewsListAdapter.getCurrentNewsList().size(), moreNewsListener, moreNewsErrorListener);
    }

}
