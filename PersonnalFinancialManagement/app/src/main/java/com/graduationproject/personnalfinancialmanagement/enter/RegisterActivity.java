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
import com.graduationproject.personnalfinancialmanagement.utils.CommonUtils;
import com.graduationproject.personnalfinancialmanagement.utils.MD5Util;
import com.graduationproject.personnalfinancialmanagement.utils.http.Bmod.MyBMobManager;
import com.graduationproject.personnalfinancialmanagement.utils.layoututils.CustomProgressHUDManager;

import cn.bmob.v3.listener.SaveListener;

/**
 * Created by longhui on 2016/5/11.
 */
public class RegisterActivity extends MyBaseAppCompatActivity implements View.OnClickListener {
    //TitleBar相关
    private ImageView backIv, confirm;
    private TextView titleTv;
    private EditText userNameEt, userPasswordEt, repeatUserPasswordEt, emailEt;
    private TextView registerTv;

    //Bmod注册相关
    SaveListener registerSaveListener;

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_register);
        initView();
    }

    private void initView() {
        initTitleBar(findViewById(R.id.register_title_bar));
        userNameEt = (EditText) findViewById(R.id.register_user_name_et);
        userPasswordEt = (EditText) findViewById(R.id.register_user_password_et);
        repeatUserPasswordEt = (EditText) findViewById(R.id.register_repeat_user_password_et);
        registerTv = (TextView) findViewById(R.id.register_tv);
        emailEt = (EditText) findViewById(R.id.register_repeat_user_email_et);
        registerTv.setOnClickListener(this);
    }

    private void initTitleBar(View titleBar) {
        backIv = (ImageView) titleBar.findViewById(R.id.title_bar_back_iv);
        confirm = (ImageView) titleBar.findViewById(R.id.title_bar_confirm_tv);
        titleTv = (TextView) titleBar.findViewById(R.id.title_bar_title_tv);
        backIv = (ImageView) titleBar.findViewById(R.id.title_bar_back_iv);
        confirm.setVisibility(View.GONE);
        titleTv.setText("注册");
        backIv.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.register_tv:
                register();
                break;
            case R.id.title_bar_back_iv:
                finish();
                break;
        }
    }

    private void register() {
        if (!checkBeforeRegister()) {
            return;
        }
        registerSaveListener = new SaveListener() {
            @Override
            public void onSuccess() {
                CustomProgressHUDManager.getInstance().dismiss();
                Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                try {
                    returnToLogin();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int i, String s) {
                CustomProgressHUDManager.getInstance().dismiss();
                Toast.makeText(RegisterActivity.this, "注册失败:" + s, Toast.LENGTH_SHORT).show();
            }
        };
        try {
            MyBMobManager.getInstance().register(this,
                    userNameEt.getText().toString().trim(),
                    MD5Util.md5Encode(userPasswordEt.getText().toString().trim()), emailEt.getText().toString(),
                    registerSaveListener);
        } catch (Exception e) {
            e.printStackTrace();
        }
        CustomProgressHUDManager.getInstance().show(this, "正在注册", 50);
    }

    private boolean checkBeforeRegister() {
        if (TextUtils.isEmpty(userNameEt.getText())) {
            Toast.makeText(this, "用户名不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(userPasswordEt.getText())) {
            Toast.makeText(this, "密码不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!TextUtils.equals(userPasswordEt.getText(), repeatUserPasswordEt.getText())) {
            Toast.makeText(this, "两次密码不一致", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(emailEt.getText().toString().trim())) {
            Toast.makeText(this, "邮箱不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!CommonUtils.isEmail(emailEt.getText().toString().trim())) {
            Toast.makeText(this, "邮箱格式不符", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void returnToLogin() throws Exception {
        Intent intent = new Intent();
        intent.putExtra(StringConfig.USER_NAME_TAG, userNameEt.getText().toString().trim());
        intent.putExtra(StringConfig.USER_PASSWORD,
                userPasswordEt.getText().toString().trim());
        setResult(CodeConfig.REGISTER_SUCCESS_RESULT_CODE, intent);
        finish();
    }
}
