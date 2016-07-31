package com.graduationproject.personnalfinancialmanagement.base;

import android.app.Activity;

import java.util.Stack;

/**
 * Created by Administrator on 2015/5/6.
 */
public class MyActivityManager {
    private static MyActivityManager gMyActivityManager = null;
    private MyActivityManager() {

    }
    public static MyActivityManager getInstance() {
        if (null == gMyActivityManager) {
            gMyActivityManager = new MyActivityManager();
        }
        return gMyActivityManager;
    }

    // ==========================内部变量=============================
    private Stack<Activity> mActivityStack = new Stack<Activity>();
    private Stack<Activity> mTempActivityStack = new Stack<Activity>();
    private boolean mAppIsInForeground = false; // 应用是否在前台

    // ==========================外部使用接口=============================
    public synchronized Activity getTop() {
        if (null != mActivityStack && false == mActivityStack.empty())
            return mActivityStack.peek();
        return null;
    }

    public synchronized void push(Activity activity) {
        if (null != mActivityStack && null != activity)
            mActivityStack.push(activity);
    }

    public synchronized Activity pop(Activity activity) {
        if (null == activity) return null;

        mTempActivityStack.clear();
        while (false == mActivityStack.empty()) {
            Activity tempActivity = mActivityStack.pop();
            if (activity.equals(tempActivity)) {
                break;
            } else {
                mTempActivityStack.push(tempActivity);
            }
        }

        // 恢复临时数据到原堆栈
        while (false == mTempActivityStack.empty()) {
            Activity tempActivity = mTempActivityStack.pop();
            mActivityStack.push(tempActivity);
        }

        return activity;
    }

    public synchronized void closeAll() {
        if (null != mActivityStack) {
            while (false == mActivityStack.isEmpty()) {
                Activity activity = mActivityStack.pop();
                activity.finish();
            }
        }
    }

    public synchronized void setAppIsInForeground(boolean isInForeground) {
        mAppIsInForeground = isInForeground;
    }

    public synchronized boolean isAppInForeground() {
        return mAppIsInForeground;
    }
}
