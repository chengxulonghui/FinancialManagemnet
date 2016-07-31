package com.graduationproject.personnalfinancialmanagement.utils.layoututils;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.graduationproject.personnalfinancialmanagement.R;


public class CustomBarView extends View {

    /**
     * 显示的文本
     */
    private String mText = "";
    /**
     * 显示的文本的字体颜色
     */
    private int mTextColor;
    /**
     * 显示文本的字体大小
     */
    private int mTextSize;
    /**
     * 背景颜色
     */
    private int mBackgroundColor;
    /**
     * 最大值
     */
    private float mBarMax;
    /**
     * 当前值
     */
    private float mBarProgress;
    /**
     * 绘制文本的画笔
     */
    private Paint mTextPaint;
    /**
     * 绘制文本的区域
     */
    private Rect mTextBound;
    /**
     * 绘制背景画笔
     */
    private Paint mBackgroundPaint;
    /**
     * 绘制第二层背景画笔
     */
    private Paint mSecondaryBackgroundPaint;
    /**
     * 文本与左边屏幕的边距
     */
    private int textPadding = 10;

    public CustomBarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        // 获取自定义属性
        TypedArray ta = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.customBar, defStyleAttr, 0);
        mBackgroundColor = ta.getColor(R.styleable.customBar_barBackground,
                0x00FFFFFF);
        mBarMax = ta.getFloat(R.styleable.customBar_barMax, 0);
        mBarProgress = ta.getFloat(R.styleable.customBar_barProgress, 0);
        mText = ta.getString(R.styleable.customBar_barText);
        mTextColor = ta.getColor(R.styleable.customBar_barTextColor,
                mBackgroundColor);
        mTextSize = ta.getDimensionPixelSize(R.styleable.customBar_barTextSize,
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 14,
                        getResources().getDisplayMetrics()));
        ta.recycle();
        /**
         * 初始化文本画笔
         */
        mTextPaint = new Paint();
        mTextBound = new Rect();
        /**
         * 初始化背景画笔
         */
        mBackgroundPaint = new Paint();
        mSecondaryBackgroundPaint = new Paint();
    }

    public CustomBarView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomBarView(Context context) {
        this(context, null);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        // 设置底色，与字体颜色一致
        mBackgroundPaint.setColor(mTextColor);
        canvas.drawRect(0, 0, getMeasuredWidth(), getMeasuredHeight(),
                mBackgroundPaint);

        // 设置文本画笔的属性
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setStyle(Style.FILL);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTypeface(Typeface.MONOSPACE);
        // 获取Text的大小
        mTextPaint.getTextBounds(mText, 0, mText.length(), mTextBound);

        // 绘制底层文本
        mTextPaint.setColor(mBackgroundColor);
        canvas.drawText(mText, textPadding,
                getHeight() / 2 + mTextBound.height() / 2, mTextPaint);

        // 设置背景色
        mSecondaryBackgroundPaint.setColor(mBackgroundColor);
        canvas.drawRect(0, 0, getMeasuredWidth() * (mBarProgress / mBarMax),
                getMeasuredHeight(), mSecondaryBackgroundPaint);

        // 切割图层，大小为 mBarProgress / mBarMax
        canvas.clipRect(0, 0, getMeasuredWidth() * (mBarProgress / mBarMax),
                getMeasuredHeight());

        // 绘制顶层文本
        mTextPaint.setColor(mTextColor);
        canvas.drawText(mText, textPadding,
                getHeight() / 2 + mTextBound.height() / 2, mTextPaint);
        super.onDraw(canvas);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(measureWidth(widthMeasureSpec),
                measureHeight(heightMeasureSpec));
    }

    private int measureWidth(int widthMeasureSpec) {
        int result = 0;
        int specMode = MeasureSpec.getMode(widthMeasureSpec);
        int specSize = MeasureSpec.getSize(widthMeasureSpec);

        if (specMode == MeasureSpec.EXACTLY) { // 精准值模式，指定为特定大小如100dp或match_parent
            result = specSize;
        } else {
            result = 200;
            if (specMode == MeasureSpec.AT_MOST) { // 最大值模式，指定为wrap_content
                result = Math.min(result, specSize);
            }
        }
        return result;
    }

    private int measureHeight(int heightMeasureSpec) {
        int result = 0;
        int specMode = MeasureSpec.getMode(heightMeasureSpec);
        int specSize = MeasureSpec.getSize(heightMeasureSpec);

        if (specMode == MeasureSpec.EXACTLY) { // 精准值模式，指定为特定大小如100dp或match_parent
            result = specSize;
        } else {
            result = 200;
            if (specMode == MeasureSpec.AT_MOST) { // 最大值模式，指定为wrap_content
                result = Math.min(result, specSize);
            }
        }
        return result;
    }

    /**
     * 得到文本
     *
     * @return
     */
    public String getText() {
        return mText;
    }

    /**
     * 设置文本
     *
     * @param mText
     */
    public void setText(String mText) {
        this.mText = mText;
        requestLayout();
    }

    /**
     * 得到文本的字体颜色
     *
     * @return
     */
    public int getTextColor() {
        return mTextColor;
    }

    /**
     * 设置文本颜色
     *
     * @param mTextColor
     */
    public void setTextColor(int mTextColor) {
        this.mTextColor = mTextColor;
    }

    /**
     * 得到文本的字体大小
     *
     * @return
     */
    public int getTextSize() {
        return mTextSize;
    }

    /**
     * 设置文本的字体大小
     *
     * @param mTextSize
     */
    public void setTextSize(int mTextSize) {
        this.mTextSize = mTextSize;
    }

    /**
     * 得到背景颜色
     *
     * @return
     */
    public int getBackgroundColor() {
        return mBackgroundColor;
    }

    /**
     * 设置背景颜色
     */
    public void setBackgroundColor(int mBackgroundColor) {
        this.mBackgroundColor = mBackgroundColor;
        requestLayout();
    }

    /**
     * 得到view的最大值
     *
     * @return
     */
    public float getBarMax() {
        return mBarMax;
    }

    /**
     * 设置最大值
     *
     * @param mBarMax
     */
    public void setBarMax(float mBarMax) {
        this.mBarMax = mBarMax;
    }

    /**
     * 得到当前view的值
     *
     * @return
     */
    public float getBarProgress() {
        return mBarProgress;
    }

    /**
     * 设置当前view的值
     *
     * @param mBarProgress
     */
    public void setBarProgress(float mBarProgress) {
        this.mBarProgress = mBarProgress;
        requestLayout();
    }

    /**
     * 得到当前文本与左边的边距
     *
     * @return
     */
    public int getTextPadding() {
        return textPadding;
    }

    /**
     * 设置当前文本与左边的间距
     *
     * @param textPadding
     */
    public void setTextPadding(int textPadding) {
        this.textPadding = textPadding;
    }

}
