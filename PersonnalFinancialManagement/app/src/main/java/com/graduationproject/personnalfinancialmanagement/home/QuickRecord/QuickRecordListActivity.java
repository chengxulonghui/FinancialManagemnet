package com.graduationproject.personnalfinancialmanagement.home.QuickRecord;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.graduationproject.personnalfinancialmanagement.R;
import com.graduationproject.personnalfinancialmanagement.accounting.AccountingActivity;
import com.graduationproject.personnalfinancialmanagement.base.MyBaseAppCompatActivity;
import com.graduationproject.personnalfinancialmanagement.config.StringConfig;
import com.graduationproject.personnalfinancialmanagement.config.javabean.QuickRecordModel;
import com.graduationproject.personnalfinancialmanagement.home.QuickRecord.AddQuickRecord.AddQuickRecordActivity;
import com.graduationproject.personnalfinancialmanagement.utils.database.LitePalManager;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobUser;

/**
 * Created by longhui on 2016/5/27.
 */
public class QuickRecordListActivity extends MyBaseAppCompatActivity {
    //TitleBar相关
    private ImageView backIv, confirm;
    private TextView titleTv;

    private TextView addQrTv;
    private RecyclerView qRListRv;
    private QuickRecordListAdapter mQuickRecordListAdapter;

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_quick_record);
        initView();
        initData();
    }

    private void initView() {
        initTitleBar(findViewById(R.id.quick_record_title_bar));
        addQrTv = (TextView) findViewById(R.id.quick_record_add_tv);
        qRListRv = (RecyclerView) findViewById(R.id.quick_record_list_rv);
        addQrTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(QuickRecordListActivity.this, AddQuickRecordActivity.class);
                startActivityForResult(intent, 100);
            }
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        qRListRv.setLayoutManager(layoutManager);
        mQuickRecordListAdapter = new QuickRecordListAdapter(this, new ArrayList<QuickRecordModel>());
        qRListRv.setAdapter(mQuickRecordListAdapter);
    }

    private void initTitleBar(View titleBar) {
        backIv = (ImageView) titleBar.findViewById(R.id.title_bar_back_iv);
        confirm = (ImageView) titleBar.findViewById(R.id.title_bar_confirm_tv);
        titleTv = (TextView) titleBar.findViewById(R.id.title_bar_title_tv);
        backIv = (ImageView) titleBar.findViewById(R.id.title_bar_back_iv);
        confirm.setVisibility(View.GONE);
        titleTv.setText("快速记账");
        backIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void initData() {
        List<QuickRecordModel> quickRecordModelList = LitePalManager.getInstance().getQuickRecordList(BmobUser.getCurrentUser(this).getObjectId());
        mQuickRecordListAdapter.setDataList(quickRecordModelList);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 100:
                initData();
                break;
        }
    }

    public void toAccounting(QuickRecordModel quickRecordModel) {
        Intent intent = new Intent(this, AccountingActivity.class);
        intent.setAction(StringConfig.ACTION_ACCOUNTING_FROM_QUICK_RECORD);
        intent.putExtra("quickRecordModel", quickRecordModel);
        startActivity(intent);
    }

    public void deleteQr(QuickRecordModel quickRecordModel) {
        DataSupport.delete(QuickRecordModel.class, quickRecordModel.getId());
        mQuickRecordListAdapter.getDataList().remove(quickRecordModel);
        mQuickRecordListAdapter.refresh();
    }
}
