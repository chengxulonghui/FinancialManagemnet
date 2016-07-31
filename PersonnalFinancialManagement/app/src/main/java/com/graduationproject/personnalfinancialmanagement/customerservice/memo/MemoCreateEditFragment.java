package com.graduationproject.personnalfinancialmanagement.customerservice.memo;

import android.content.ContentValues;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.UpdateAppearance;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.graduationproject.personnalfinancialmanagement.R;
import com.graduationproject.personnalfinancialmanagement.config.javabean.MemoBMob;
import com.graduationproject.personnalfinancialmanagement.config.javabean.MemoLitePal;
import com.graduationproject.personnalfinancialmanagement.utils.CommonUtils;
import com.graduationproject.personnalfinancialmanagement.utils.DateUtils;
import com.graduationproject.personnalfinancialmanagement.utils.http.Bmod.MyBMobManager;
import com.graduationproject.personnalfinancialmanagement.utils.layoututils.CustomProgressHUDManager;
import com.graduationproject.personnalfinancialmanagement.utils.layoututils.MaxLengthWatcher;

import org.litepal.crud.DataSupport;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by longhui on 2016/5/21.
 */
public class MemoCreateEditFragment extends Fragment {
    //TitleBar相关
    private ImageView backIv, confirm;
    private TextView titleTv;

    private TextView tipsTv;
    private AppCompatEditText contentEt;

    boolean isEdit = false;
    MemoLitePal currentMemo = null;

    static MemoCreateEditFragment newInstance(MemoLitePal data) {
        MemoCreateEditFragment newFragment = new MemoCreateEditFragment();
        if (data != null) {
            Bundle bundle = new Bundle();
            bundle.putSerializable("memo", (Serializable) data);
            newFragment.setArguments(bundle);
        }
        return newFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle args = getArguments();
        if (args != null) {
            isEdit = true;
            currentMemo = (MemoLitePal) args.getSerializable("memo");
        }
        View rootView = inflater.inflate(R.layout.memo_create_edit_fragment, null);
        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        initView(rootView);
        return rootView;
    }

    private void initView(View v) {
        initTitleBar(v.findViewById(R.id.memo_create_edit_title_bar));
        tipsTv = (TextView) v.findViewById(R.id.memo_create_edit_tips_tv);
        contentEt = (AppCompatEditText) v.findViewById(R.id.memo_create_edit_content_et);
        contentEt.addTextChangedListener(new MaxLengthWatcher(getActivity(), 200, contentEt, "输入文字已达长度上限"));
        contentEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                tipsTv.setText("你还可以输入" + (200 - CommonUtils.calculateLengthWithByte(s.toString())) + "个字");
            }
        });
        if (isEdit) {
            contentEt.setText(currentMemo.getContent());
        }
    }

    private void initTitleBar(View titleBar) {
        backIv = (ImageView) titleBar.findViewById(R.id.title_bar_back_iv);
        confirm = (ImageView) titleBar.findViewById(R.id.title_bar_confirm_tv);
        titleTv = (TextView) titleBar.findViewById(R.id.title_bar_title_tv);
        backIv = (ImageView) titleBar.findViewById(R.id.title_bar_back_iv);
        confirm.setVisibility(View.VISIBLE);
        if (isEdit) {
            titleTv.setText("编辑便签");
        } else {
            titleTv.setText("新增便签");
        }
        backIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                remove();
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEdit) {
                    saveEdit();
                } else {
                    createMemo();
                }
            }
        });
    }

    private boolean check() {
        if (TextUtils.isEmpty(contentEt.getText())) {
            Toast.makeText(getActivity(), "内容不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void saveEdit() {
        if (!check()) {
            return;
        }
        CustomProgressHUDManager.getInstance().show(getActivity(), "保存中", 100);
        ContentValues values = new ContentValues();
        values.put("content", contentEt.getText().toString());
        values.put("updateDate", DateUtils.transCurrentDate2MillisecondStr());
        int index = DataSupport.update(MemoLitePal.class, values, currentMemo.getId());
        Log.e("sdfsdfsdhfsdhfsdhfsd", "" + index);
        if (index > 0) {
            ((MemorandumActivity) getActivity()).refreshAfterEdit();
            FindListener<MemoBMob> findListener = new FindListener<MemoBMob>() {
                @Override
                public void onSuccess(List<MemoBMob> list) {
                    if (list.size() > 0) {
                        UpdateListener updateListener = new UpdateListener() {
                            @Override
                            public void onSuccess() {
                                CustomProgressHUDManager.getInstance().dismiss();
                                Toast.makeText(getActivity(), "保存到云端成功!", Toast.LENGTH_SHORT).show();
                                remove();
                            }

                            @Override
                            public void onFailure(int i, String s) {
                                CustomProgressHUDManager.getInstance().dismiss();
                                Toast.makeText(getActivity(), "保存到云端失败:" + s, Toast.LENGTH_SHORT).show();
                            }
                        };
                        MemoBMob memoBMob = list.get(0);
                        memoBMob.setContent(contentEt.getText().toString());
                        MyBMobManager.getInstance().updateMemo(getActivity(), list.get(0), updateListener);
                    }
                }

                @Override
                public void onError(int i, String s) {
                    CustomProgressHUDManager.getInstance().dismiss();
                }
            };
            MyBMobManager.getInstance().getMemoByMemoId(getActivity(), currentMemo.getMemoId(), findListener);
        } else {
            CustomProgressHUDManager.getInstance().dismiss();
            Toast.makeText(getActivity(), "保存失败", Toast.LENGTH_SHORT).show();
        }
        ;
    }

    public void createMemo() {
        if (!check()) {
            return;
        }
        MemoLitePal memoLitePal = new MemoLitePal();
        String currentDateStr = DateUtils.transCurrentDate2MillisecondStr();
        memoLitePal.setUserId(BmobUser.getCurrentUser(getActivity()).getObjectId());
        memoLitePal.setMemoId("M" + currentDateStr);
        memoLitePal.setContent(contentEt.getText().toString());
        memoLitePal.setCreateDate(currentDateStr);
        memoLitePal.setUpdateDate(currentDateStr);
        CustomProgressHUDManager.getInstance().show(getActivity(), "保存中", 100);
        if (memoLitePal.save()) {
            ((MemorandumActivity) getActivity()).addMemoToList(memoLitePal);
            MemoBMob memoBMob = MyBMobManager.getInstance().tranMemoLitePal2MemoBMob(memoLitePal);
            SaveListener memoSaveListener = new SaveListener() {
                @Override
                public void onSuccess() {
                    CustomProgressHUDManager.getInstance().dismiss();
                    Toast.makeText(getActivity(), "保存到云端成功!", Toast.LENGTH_SHORT).show();
                    remove();
                }

                @Override
                public void onFailure(int i, String s) {
                    CustomProgressHUDManager.getInstance().dismiss();
                    Toast.makeText(getActivity(), "保存到云端失败:" + s, Toast.LENGTH_SHORT).show();
                }
            };
            MyBMobManager.getInstance().saveMemo(getActivity(), memoBMob, memoSaveListener);

        } else {
            CustomProgressHUDManager.getInstance().dismiss();
            Toast.makeText(getActivity(), "保存失败", Toast.LENGTH_SHORT).show();
        }
    }

    public void remove() {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.popBackStack();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.remove(this);
        fragmentTransaction.commit();
    }

}
