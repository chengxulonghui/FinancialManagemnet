package com.graduationproject.personnalfinancialmanagement.customerservice.memo;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.graduationproject.personnalfinancialmanagement.R;
import com.graduationproject.personnalfinancialmanagement.accounting.fragemnt.PaymentTypeChooseFragment;
import com.graduationproject.personnalfinancialmanagement.base.MyBaseAppCompatActivity;
import com.graduationproject.personnalfinancialmanagement.config.javabean.MemoBMob;
import com.graduationproject.personnalfinancialmanagement.config.javabean.MemoLitePal;
import com.graduationproject.personnalfinancialmanagement.utils.CommonUtils;
import com.graduationproject.personnalfinancialmanagement.utils.database.LitePalManager;
import com.graduationproject.personnalfinancialmanagement.utils.http.Bmod.MyBMobManager;
import com.graduationproject.personnalfinancialmanagement.utils.layoututils.CustomProgressHUDManager;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by longhui on 2016/5/21.
 */
public class MemorandumActivity extends MyBaseAppCompatActivity {
    //TitleBar相关
    private ImageView backIv, confirm;
    private TextView titleTv;


    private RecyclerView memoListRv;
    private MyMemoListAdapter myMemoListAdapter;
    private MemoCreateEditFragment memoCreateEditFragment;

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_memorandum);
        initView();
        initData();
    }

    private void initView() {
        initTitleBar(findViewById(R.id.memorandum_title_bar));
        memoListRv = (RecyclerView) findViewById(R.id.memorandum_list_rv);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        memoListRv.setLayoutManager(layoutManager);
        memoListRv.setItemAnimator(new DefaultItemAnimator());
        myMemoListAdapter = new MyMemoListAdapter(this, new ArrayList<MemoLitePal>());
        memoListRv.setAdapter(myMemoListAdapter);
    }

    private void initTitleBar(View titleBar) {
        backIv = (ImageView) titleBar.findViewById(R.id.title_bar_back_iv);
        confirm = (ImageView) titleBar.findViewById(R.id.title_bar_confirm_tv);
        titleTv = (TextView) titleBar.findViewById(R.id.title_bar_title_tv);
        backIv = (ImageView) titleBar.findViewById(R.id.title_bar_back_iv);
        confirm.setVisibility(View.VISIBLE);
        confirm.setImageResource(R.drawable.icon_add);
        titleTv.setText("事务备忘");
        backIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toCreateMemo();
            }
        });
    }

    private void initData() {
        CustomProgressHUDManager.getInstance().show(MemorandumActivity.this, "同步中", 100);
        List<MemoLitePal> memoList = DataSupport
                .where("userId = ? ", BmobUser.getCurrentUser(this).getObjectId())
                .order("createDate desc").find(MemoLitePal.class);
        myMemoListAdapter.setDataList(memoList);
        asyncData();
    }

    //获取云端数据保存到本地
    private void asyncData() {
        FindListener<MemoBMob> memoBMobFindListener = new FindListener<MemoBMob>() {
            @Override
            public void onSuccess(List<MemoBMob> list) {
                MergeAsyncTask mergeAsyncTask = new MergeAsyncTask(myMemoListAdapter.getDataList(), list);
                mergeAsyncTask.execute(myMemoListAdapter.getDataList());
            }

            @Override
            public void onError(int i, String s) {
                Toast.makeText(MemorandumActivity.this, "同步云端失败:" + s, Toast.LENGTH_SHORT).show();
            }
        };
        MyBMobManager.getInstance().getMemoListByUserId(this, BmobUser.getCurrentUser(this).getObjectId(), memoBMobFindListener);
    }

    public void toCreateMemo() {
        getSupportFragmentManager().popBackStack();
        memoCreateEditFragment = MemoCreateEditFragment.newInstance(null);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.memorandum_main_fl, memoCreateEditFragment);
        fragmentTransaction.addToBackStack("add");
        fragmentTransaction.commit();
    }

    public void toEditMemo(MemoLitePal memoLitePal) {
        getSupportFragmentManager().popBackStack();
        memoCreateEditFragment = MemoCreateEditFragment.newInstance(memoLitePal);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.memorandum_main_fl, memoCreateEditFragment);
        fragmentTransaction.addToBackStack("add");
        fragmentTransaction.commit();
    }

    public void refreshAfterEdit() {
        myMemoListAdapter.setDataList(LitePalManager.getInstance().getMemoList(BmobUser.getCurrentUser(this).getObjectId()));
    }

    public void addMemoToList(MemoLitePal memoLitePal) {
        myMemoListAdapter.getDataList().add(0, memoLitePal);
        myMemoListAdapter.refresh();
    }

    //合并网络与本地数据
    class MergeAsyncTask extends AsyncTask<List<MemoLitePal>, List<MemoBMob>, List<MemoLitePal>> {
        List<MemoLitePal> memoLitePalList;
        List<MemoBMob> memoBMobList;

        public MergeAsyncTask(List<MemoLitePal> memoLitePalList, List<MemoBMob> memoBMobList) {
            this.memoLitePalList = memoLitePalList;
            this.memoBMobList = memoBMobList;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected List<MemoLitePal> doInBackground(List<MemoLitePal>... params) {
            List<MemoLitePal> tempList = new ArrayList<MemoLitePal>();
            if (memoLitePalList.size() < memoBMobList.size()) {
                for (int i = memoLitePalList.size(); i < memoBMobList.size(); i++) {
                    MemoBMob memoBMob = memoBMobList.get(i);
                    MemoLitePal memoLitePal = LitePalManager.getInstance().tranMemoBMob2MemoLitePal(memoBMob);
                    if (memoLitePal.save()) {
                        tempList.add(memoLitePal);
                    }
                }
            }
            return tempList;
        }

        @Override
        protected void onPostExecute(List<MemoLitePal> memoLitePals) {
            super.onPostExecute(memoLitePals);
            CustomProgressHUDManager.getInstance().dismiss();
            myMemoListAdapter.getDataList().addAll(0, memoLitePals);
            myMemoListAdapter.refresh();
        }
    }


}
