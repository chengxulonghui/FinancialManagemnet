package com.graduationproject.personnalfinancialmanagement.customerservice.trainTicket;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.graduationproject.personnalfinancialmanagement.R;
import com.graduationproject.personnalfinancialmanagement.customerservice.trainTicket.adapter.TrainStationListAdapter;
import com.graduationproject.personnalfinancialmanagement.customerservice.trainTicket.javabean.TrainStation;
import com.graduationproject.personnalfinancialmanagement.customerservice.trainTicket.javabean.TrainStationResult;
import com.graduationproject.personnalfinancialmanagement.utils.database.LitePalManager;
import com.graduationproject.personnalfinancialmanagement.utils.http.volley.VolleyHttpUtils;
import com.graduationproject.personnalfinancialmanagement.utils.layoututils.CustomProgressHUDManager;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by longhui on 2016/5/25.
 */
public class TrainTicketToStationChooseFragment extends Fragment {
    //TitleBar相关
    private ImageView backIv, confirm;
    private TextView titleTv;

    private View searchView;
    private EditText searchEt;
    private RecyclerView trainStationListRv;
    private List<TrainStation> originTrainStation;

    private TrainStationListAdapter mTrainStationListAdapter;
    private VolleyHttpUtils volleyHttpUtils;
    private Handler myHandler;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.train_station_choose_fragment, container, false);
        initView(rootView);
        initData();
        return rootView;
    }

    private void initView(View v) {
        myHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 1:
                        List<TrainStation> trainStationList = (List<TrainStation>) msg.obj;
                        mTrainStationListAdapter.setDataList(trainStationList);
                        break;
                    case 2:
                        initFirstTrainDataFromInternet();
                        break;
                    case 3:
                        mTrainStationListAdapter.setDataList(originTrainStation);
                        break;
                }
            }
        };
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        initTitleBar(v.findViewById(R.id.train_station_choose_title_bar));
        searchView = v.findViewById(R.id.train_station_choose_search_layout);
        searchEt = (EditText) searchView.findViewById(R.id.train_station_choose_search_et);
        trainStationListRv = (RecyclerView) v.findViewById(R.id.train_station_choose_rv);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        trainStationListRv.setLayoutManager(layoutManager);
        mTrainStationListAdapter = new TrainStationListAdapter(getActivity(), new ArrayList<TrainStation>(), this);
        trainStationListRv.setAdapter(mTrainStationListAdapter);
        MyTextWatcher myTextWatcher = new MyTextWatcher();
        searchEt.addTextChangedListener(myTextWatcher);
    }

    private void initTitleBar(View v) {
        backIv = (ImageView) v.findViewById(R.id.title_bar_back_iv);
        confirm = (ImageView) v.findViewById(R.id.title_bar_confirm_tv);
        titleTv = (TextView) v.findViewById(R.id.title_bar_title_tv);
        backIv = (ImageView) v.findViewById(R.id.title_bar_back_iv);
        confirm.setVisibility(View.GONE);

        titleTv.setText("选择出发站");
        backIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                remove();
            }
        });
    }

    private void initData() {
        originTrainStation = new ArrayList<TrainStation>();
        CustomProgressHUDManager.getInstance().show(getActivity(), "获取车站信息中", 100);
        TrainTicketActivity activity = (TrainTicketActivity) getActivity();
        this.volleyHttpUtils = activity.getVolleyHttpUtils();
        new Thread() {
            @Override
            public void run() {
                super.run();
                originTrainStation = LitePalManager.getInstance().getTrainStationList();
                if (originTrainStation.size() > 0) {
                    CustomProgressHUDManager.getInstance().dismiss();
                    Message msg = myHandler.obtainMessage();
                    msg.what = 3;
                    myHandler.sendMessage(msg);
                } else {
                    Message msg = myHandler.obtainMessage();
                    msg.what = 2;
                    myHandler.sendMessage(msg);
                }
            }
        }.start();

    }

    private void initFirstTrainDataFromInternet() {
        volleyHttpUtils.getTrainStationList(new Response.Listener<TrainStationResult>() {
            @Override
            public void onResponse(TrainStationResult trainStationResult) {
                CustomProgressHUDManager.getInstance().dismiss();
                if (trainStationResult.getReason().equals("查询成功")) {
                    originTrainStation = trainStationResult.getResult();
                    DataSupport.saveAll(trainStationResult.getResult());
                    mTrainStationListAdapter.setDataList(trainStationResult.getResult());
                } else {
                    Toast.makeText(getActivity(), "发生未知错误，获取失败", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                CustomProgressHUDManager.getInstance().dismiss();
                Toast.makeText(getActivity(), "发生未知错误，获取失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void setToStation(TrainStation station) {
        ((TrainTicketActivity) getActivity()).setToStation(station);
        remove();
    }

    public void remove() {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.popBackStack();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.remove(this);
        fragmentTransaction.commit();
    }

    private class MyTextWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s.length() <= 0) {
                mTrainStationListAdapter.setDataList(originTrainStation);
            } else {
                new MySearchThread(s.toString()).start();
            }
            searchEt.setSelection(searchEt.getText().length());
        }
    }

    private class MySearchThread extends Thread {
        String toSearch;

        public MySearchThread(String searchStr) {
            this.toSearch = searchStr;
        }

        @Override
        public void run() {
            List<TrainStation> trainStationList = new ArrayList<TrainStation>();
            for (TrainStation trainStation : originTrainStation) {
                if (trainStation.getSta_name().contains(toSearch) || trainStation.getSta_ename().contains(toSearch) || trainStation.getSta_code().contains(toSearch)) {
                    trainStationList.add(trainStation);
                }
            }
            Message msg = myHandler.obtainMessage();
            msg.what = 1;
            msg.obj = trainStationList;
            myHandler.sendMessage(msg);
        }
    }
}
