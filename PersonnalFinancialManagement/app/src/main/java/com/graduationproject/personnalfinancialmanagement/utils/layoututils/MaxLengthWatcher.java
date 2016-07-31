package com.graduationproject.personnalfinancialmanagement.utils.layoututils;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.Toast;

import com.graduationproject.personnalfinancialmanagement.utils.CommonUtils;


/**
 * Created by Administrator on 2014/12/11.
 */

/*
 * 监听输入内容是否超出最大长度，并设置光标位置
 * */
public class MaxLengthWatcher implements TextWatcher {
    public static final int MAX_CHINESE_CHARACTER_LENGTH = 16;

    private int mMaxLen = 0;
    private EditText mEditText = null;
    private String mToastString = null; // 超出限定的字符后提示的内容
    private Context mContext = null; //

    public MaxLengthWatcher(int maxLen, EditText editText) {
        this.mMaxLen = maxLen;
        this.mEditText = editText;
    }

    public MaxLengthWatcher(Context context, int mMaxLen, EditText editText, String toastString)
    {
        this.mMaxLen = mMaxLen;
        this.mEditText = editText;
        this.mToastString = toastString;
        this.mContext = context;
    }

    public void afterTextChanged(Editable arg0) {
    }

    public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
    }

    public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
        int selectStart = mEditText.getSelectionStart();
        int selectEnd = mEditText.getSelectionEnd();
        Editable editable = mEditText.getText();
        mEditText.removeTextChangedListener(this); //先去掉监听器，否则会出现栈溢出
        try { // TODO 最外层防护
            if (CommonUtils.calculateLengthWithByte(editable.toString()) > this.mMaxLen) {
                editable.delete(selectStart - arg3, selectEnd);
                mEditText.setTextKeepState(editable);
                //请读者注意这一行，保持光标原先的位置，而 mEditText.setText(s)会让光标跑到最前面，
                //就算是再加mEditText.setSelection(nSelStart) 也不起作用
                if (null != mToastString && null != mContext)
                    Toast.makeText(mContext,mToastString,Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        mEditText.addTextChangedListener(this); //恢复监听器
    }

}