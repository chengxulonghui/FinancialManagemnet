package com.graduationproject.personnalfinancialmanagement.enter;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.graduationproject.personnalfinancialmanagement.R;
import com.graduationproject.personnalfinancialmanagement.base.MyBaseAppCompatActivity;
import com.graduationproject.personnalfinancialmanagement.config.CodeConfig;
import com.graduationproject.personnalfinancialmanagement.config.StringConfig;
import com.graduationproject.personnalfinancialmanagement.home.HomeActivity;
import com.graduationproject.personnalfinancialmanagement.utils.MD5Util;
import com.graduationproject.personnalfinancialmanagement.utils.SharePreferencesUtils;
import com.graduationproject.personnalfinancialmanagement.utils.http.Bmod.MyBMobManager;
import com.graduationproject.personnalfinancialmanagement.utils.layoututils.CustomProgressHUDManager;

import cn.bmob.v3.listener.SaveListener;

import static com.graduationproject.personnalfinancialmanagement.config.CodeConfig.TO_REGISTER_CODE;

/**
 * Created by longhui on 2016/5/11.
 */
public class LoginActivity extends MyBaseAppCompatActivity implements View.OnClickListener {
    //TitleBar相关
    private ImageView backIv, confirm;
    private TextView titleTv;

    private EditText userNameEt, userPasswordEt;
    private TextView loginTv,toRegisterTv;

    //Bmod相关
    private SaveListener loginSaveListener;
    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_login);
        initView();
    }

    private void initView() {
        initTitleBar(findViewById(R.id.login_title_bar));
        userNameEt = (EditText) findViewById(R.id.login_user_name_et);
        userPasswordEt = (EditText) findViewById(R.id.login_user_password_et);
        loginTv = (TextView) findViewById(R.id.login_tv);
        toRegisterTv=(TextView)findViewById(R.id.login_to_register_tv);
        toRegisterTv.setOnClickListener(this);
        loginTv.setOnClickListener(this);
    }

    private void initTitleBar(View titleBar) {
        backIv = (ImageView) titleBar.findViewById(R.id.title_bar_back_iv);
        confirm = (ImageView) titleBar.findViewById(R.id.title_bar_confirm_tv);
        titleTv=(TextView)titleBar.findViewById(R.id.title_bar_title_tv);
        backIv=(ImageView)titleBar.findViewById(R.id.title_bar_back_iv);
        titleTv.setText("登录");
        confirm.setVisibility(View.GONE);
        backIv.setVisibility(View.GONE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==CodeConfig.REGISTER_SUCCESS_RESULT_CODE){
            switch(requestCode){
                case CodeConfig.TO_REGISTER_CODE:
                    String userName=data.getStringExtra(StringConfig.USER_NAME_TAG);
                    String userPassword=data.getStringExtra(StringConfig.USER_PASSWORD);
                    userNameEt.setText(userName);
                    userPasswordEt.setText(userPassword);
                    try {
                        login();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.login_tv:
                try {
                    login();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.login_to_register_tv:
                Intent intent=new Intent(this,RegisterActivity.class);
                startActivityForResult(intent, TO_REGISTER_CODE);
                break;
        }
    }
    private void login() throws Exception {
        if(!checkBeforeLogin()){
            return;
        }
        loginSaveListener=new SaveListener() {
            @Override
            public void onSuccess() {
                CustomProgressHUDManager.getInstance().dismiss();
                Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                saveToSp();
                Intent intent=new Intent(LoginActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onFailure(int i, String s) {
                CustomProgressHUDManager.getInstance().dismiss();
                Toast.makeText(LoginActivity.this, "登录失败:" + s, Toast.LENGTH_SHORT).show();
            }
        };
        MyBMobManager.getInstance().login(this,userNameEt.getText().toString().trim(),
                MD5Util.md5Encode(userPasswordEt.getText().toString().trim()),
                loginSaveListener);
        CustomProgressHUDManager.getInstance().show(this, "登录中", 50);
    }
    private boolean checkBeforeLogin() {
        if (TextUtils.isEmpty(userNameEt.getText().toString().trim())) {
            Toast.makeText(this, "用户名不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(userPasswordEt.getText().toString().trim())) {
            Toast.makeText(this, "密码不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
    private void saveToSp(){
        SharePreferencesUtils.saveString2Sp(this,StringConfig.USER_NAME_TAG,userNameEt.getText().toString().trim());
        SharePreferencesUtils.saveString2Sp(this,StringConfig.USER_PASSWORD,userPasswordEt.getText().toString().trim());
    }
}
