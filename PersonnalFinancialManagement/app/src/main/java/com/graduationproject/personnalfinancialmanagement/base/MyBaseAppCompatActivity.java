package com.graduationproject.personnalfinancialmanagement.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Administrator on 2015/5/6.
 */
public class MyBaseAppCompatActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);

        MyActivityManager.getInstance().push(this);
    }

    @Override
    protected void onResume() {
//        LogConfig.i("MyActivityManager onResume " + this.getClass().getCanonicalName() + " " + PiggyDate.getDateInMillisecond());
        MyActivityManager.getInstance().setAppIsInForeground(true);

        super.onResume();
    }

    @Override
    protected void onPause() {
//        LogConfig.i("MyActivityManager onPause " + this.getClass().getCanonicalName() + " " + PiggyDate.getDateInMillisecond());
        MyActivityManager.getInstance().setAppIsInForeground(false);

        super.onPause();
    }

    @Override
    protected void onDestroy() {
        MyActivityManager.getInstance().pop(this);

        super.onDestroy();
    }
}
