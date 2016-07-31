package com.graduationproject.personnalfinancialmanagement.customerservice.enter;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.graduationproject.personnalfinancialmanagement.R;
import com.graduationproject.personnalfinancialmanagement.base.MyBaseAppCompatActivity;

/**
 * Created by longhui on 2016/5/27.
 */
public class CustomEnterActivity extends MyBaseAppCompatActivity {
    RecyclerView functionRv;
    EnterListAdapter enterListAdapter;

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_custom_service_enter);
        functionRv = (RecyclerView) findViewById(R.id.custom_enter_rv);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        functionRv.setLayoutManager(gridLayoutManager);
        enterListAdapter = new EnterListAdapter(this);
        functionRv.setAdapter(enterListAdapter);
    }
}
