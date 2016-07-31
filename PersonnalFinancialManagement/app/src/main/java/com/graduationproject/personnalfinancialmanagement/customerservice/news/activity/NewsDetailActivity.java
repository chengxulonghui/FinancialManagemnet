package com.graduationproject.personnalfinancialmanagement.customerservice.news.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.graduationproject.personnalfinancialmanagement.R;
import com.graduationproject.personnalfinancialmanagement.base.MyBaseAppCompatActivity;
import com.graduationproject.personnalfinancialmanagement.config.StringConfig;
import com.graduationproject.personnalfinancialmanagement.customerservice.news.config.NewsListItemDataHolder;


import in.srain.cube.util.LocalDisplay;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import in.srain.cube.views.ptr.header.MaterialHeader;

/**
 * Created by longhui on 2016/5/17.
 */
public class NewsDetailActivity extends MyBaseAppCompatActivity {
    //TitleBar相关
    private ImageView backIv, confirm;
    private TextView titleTv;

    private PtrFrameLayout mPtrFrameLayout;
    private WebView newsWv;
    private String url;
    private NewsListItemDataHolder currentNews;

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_news_detail);
        initView();
        initData();
    }

    private void initView() {
        initTitleBar(findViewById(R.id.news_detail_title_bar));
        newsWv = (WebView) findViewById(R.id.news_detail_wv);
        mPtrFrameLayout = (PtrFrameLayout) findViewById(R.id.news_detail_ptr_frame);
        // header
        final MaterialHeader header = new MaterialHeader(this);
        header.setBackgroundColor(getResources().getColor(R.color.title_red));
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
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, newsWv, header);
            }

            @Override
            public void onRefreshBegin(final PtrFrameLayout frame) {
                newsWv.loadUrl(newsWv.getUrl());
            }
        });
        WebSettings settings = newsWv.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        newsWv.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO Auto-generated method stub
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                mPtrFrameLayout.refreshComplete();
            }
        });
        newsWv.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                // TODO Auto-generated method stub
                if (newProgress == 100) {
                    // 网页加载完成

                } else {
                    // 加载中

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

    private void initData() {
        currentNews = (NewsListItemDataHolder) getIntent().getSerializableExtra(StringConfig.NEWS_TAG);
        url = "http://3g.163.com/touch/article.html?channel=money&offset=1&docid=" + currentNews.getDocid() + "&version=B";
        showNews();
    }

    private void showNews() {
        newsWv.loadUrl(url);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (newsWv.canGoBack()) {
                newsWv.goBack();//返回上一页面
                return true;
            } else {
                onBackPressed();
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
