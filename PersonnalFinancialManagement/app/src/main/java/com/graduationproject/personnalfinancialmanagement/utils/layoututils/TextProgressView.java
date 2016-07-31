package com.graduationproject.personnalfinancialmanagement.utils.layoututils;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.graduationproject.personnalfinancialmanagement.R;

/**
 * Created by longhui on 2016/5/19.
 */
public class TextProgressView extends LinearLayout {
    private TextView textView;
    private ProgressBar progressBar;
    private Context mContext;

    public TextProgressView(Context context) {
        this(context, null);
    }

    public TextProgressView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TextProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        initView();
    }

    private void initView() {
        this.textView = (TextView) this.findViewById(R.id.view_progress_bar_tv);
        this.progressBar = (ProgressBar) this.findViewById(R.id.view_progress_bar_pb);
    }

    public void setProgress(int progress) {
        this.progressBar.setProgress(progress);
    }

    public void setText(String text) {
        this.textView.setText(text);
    }

    public void setTextColor(String colorString) {
        this.textView.setTextColor(Color.parseColor(colorString));
    }

    public void setTextColor(int colorId) {
        this.textView.setTextColor(getResources().getColor(colorId));
    }

    public int getProgress() {
        return this.progressBar.getProgress();
    }
}
