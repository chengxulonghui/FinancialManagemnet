package com.graduationproject.personnalfinancialmanagement.enter;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.widget.TextView;

import com.graduationproject.personnalfinancialmanagement.R;
import com.graduationproject.personnalfinancialmanagement.base.MyBaseAppCompatActivity;
import com.graduationproject.personnalfinancialmanagement.config.StringConfig;
import com.graduationproject.personnalfinancialmanagement.home.HomeActivity;
import com.graduationproject.personnalfinancialmanagement.utils.CommonUtils;
import com.graduationproject.personnalfinancialmanagement.utils.MD5Util;
import com.graduationproject.personnalfinancialmanagement.utils.SharePreferencesUtils;
import com.graduationproject.personnalfinancialmanagement.utils.http.Bmod.MyBMobManager;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.listener.SaveListener;


/**
 * Created by longhui on 2016/5/11.
 */
public class WelcomeActivity extends MyBaseAppCompatActivity {
    private TextView bannerTv;
    private Handler mHandler;
    private Intent nextIntent;
    private SaveListener loginSaveListener;
    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_welcome);
        initBmod();
        initView();
        toNext();

    }
    private void initBmod(){
        Bmob.initialize(this, "17a7dbc8d478d2d4212a5e1f91b438c5");
    }
    private void initView(){
        bannerTv=(TextView)findViewById(R.id.welcome_banner_tv);
        CommonUtils.changeFonts(this, bannerTv, "for_banner.TTF");
    }
    private void toNext(){
        if(TextUtils.isEmpty(SharePreferencesUtils.getStringFromSp(this, StringConfig.USER_NAME_TAG))
                ||TextUtils.isEmpty(SharePreferencesUtils.getStringFromSp(this,StringConfig.USER_PASSWORD))){
            nextIntent=new Intent(this,LoginActivity.class);
            initHandler();
        }
        else{
            try {
                login();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    private void login() throws Exception {
        String userName=SharePreferencesUtils.getStringFromSp(this, StringConfig.USER_NAME_TAG);
        String userPassword=SharePreferencesUtils.getStringFromSp(this,StringConfig.USER_PASSWORD);
        loginSaveListener=new SaveListener() {
            @Override
            public void onSuccess() {
                nextIntent=new Intent(WelcomeActivity.this,HomeActivity.class);
                initHandler();
            }

            @Override
            public void onFailure(int i, String s) {
                nextIntent=new Intent(WelcomeActivity.this,LoginActivity.class);
                initHandler();
            }
        };
        MyBMobManager.getInstance().login(this,userName, MD5Util.md5Encode(userPassword),loginSaveListener);
    }
    private void initHandler(){
        mHandler=new Handler();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(nextIntent);
                finish();
            }
        },1500);
    }
}
