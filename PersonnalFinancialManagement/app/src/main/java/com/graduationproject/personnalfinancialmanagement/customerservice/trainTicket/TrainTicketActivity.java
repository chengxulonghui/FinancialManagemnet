package com.graduationproject.personnalfinancialmanagement.customerservice.trainTicket;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.graduationproject.personnalfinancialmanagement.R;
import com.graduationproject.personnalfinancialmanagement.base.MyBaseAppCompatActivity;
import com.graduationproject.personnalfinancialmanagement.customerservice.gasStation.GasStationConditionFragment;
import com.graduationproject.personnalfinancialmanagement.customerservice.trainTicket.adapter.TrainDetailListAdapter;
import com.graduationproject.personnalfinancialmanagement.customerservice.trainTicket.javabean.TrainDetail;
import com.graduationproject.personnalfinancialmanagement.customerservice.trainTicket.javabean.TrainDetailListDataSum;
import com.graduationproject.personnalfinancialmanagement.customerservice.trainTicket.javabean.TrainDetailSum;
import com.graduationproject.personnalfinancialmanagement.customerservice.trainTicket.javabean.TrainStation;
import com.graduationproject.personnalfinancialmanagement.utils.DateUtils;
import com.graduationproject.personnalfinancialmanagement.utils.http.volley.VolleyHttpUtils;
import com.graduationproject.personnalfinancialmanagement.utils.layoututils.CustomProgressHUDManager;

import java.util.ArrayList;

/**
 * Created by longhui on 2016/5/24.
 */
public class TrainTicketActivity extends MyBaseAppCompatActivity {
    //TitleBar相关
    private ImageView backIv, confirm;
    private TextView titleTv;

    private RecyclerView trainTicketListRv;
    private TableRow fromStationTr;
    private TextView fromStationTv;
    private TableRow toStationTr;
    private TextView toStationTv;
    private TableRow dateTr;
    private TextView dateTv;
    private VolleyHttpUtils volleyHttpUtils;

    private TrainStation fromStation;
    private TrainStation toStation;
    private TrainDetailListAdapter mTrainDetailListAdapter;

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_train_ticket);
        initView();
        initData();
    }

    private void initView() {
        initTitleBar(findViewById(R.id.train_ticket_title_bar));
        trainTicketListRv = (RecyclerView) findViewById(R.id.train_ticket_list_rv);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        trainTicketListRv.setLayoutManager(layoutManager);
        fromStationTr = (TableRow) findViewById(R.id.train_ticket_from_station_tr);
        fromStationTv = (TextView) findViewById(R.id.train_ticket_from_station_tv);
        toStationTr = (TableRow) findViewById(R.id.train_ticket_to_station_tr);
        toStationTv = (TextView) findViewById(R.id.train_ticket_to_station_tv);
        dateTr = (TableRow) findViewById(R.id.train_ticket_date_tr);
        dateTv = (TextView) findViewById(R.id.train_ticket_date_tv);
        fromStationTr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toFromStationChoose();
            }
        });
        toStationTr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toToStationChoose();
            }
        });
        dateTr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });
        mTrainDetailListAdapter = new TrainDetailListAdapter(this, new ArrayList<TrainDetailSum>());
        trainTicketListRv.setAdapter(mTrainDetailListAdapter);
    }

    public void setFromStation(TrainStation fromStation) {
        this.fromStation = fromStation;
        fromStationTv.setText("" + fromStation.getSta_name());
    }

    public void setToStation(TrainStation toStation) {
        this.toStation = toStation;
        toStationTv.setText("" + toStation.getSta_name());
    }

    private void initTitleBar(View v) {
        backIv = (ImageView) v.findViewById(R.id.title_bar_back_iv);
        confirm = (ImageView) v.findViewById(R.id.title_bar_confirm_tv);
        titleTv = (TextView) v.findViewById(R.id.title_bar_title_tv);
        backIv = (ImageView) v.findViewById(R.id.title_bar_back_iv);
        confirm.setVisibility(View.VISIBLE);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getTrainDataFromInternet();
            }
        });
        titleTv.setText("12306火车票查询");
        backIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    int chooseYear, chooseMonth, chooseDay;
    String chooseDateString;

    private void initData() {
        int[] currentDateData = DateUtils.getCurrentDateDataInInt();
        chooseYear = currentDateData[0];
        chooseMonth = currentDateData[1];
        chooseDay = currentDateData[2];
        buildChooseDateStr();
        dateTv.setText(" " + chooseDateString);
        volleyHttpUtils = new VolleyHttpUtils(this);
    }

    private void buildChooseDateStr() {
        String monthStr = "";
        String dayStr;
        if (chooseMonth < 10) {
            monthStr = "0" + chooseMonth;
        } else {
            monthStr = chooseMonth + "";
        }
        if (chooseDay < 10) {
            dayStr = "0" + chooseDay;
        } else {
            dayStr = chooseDay + "";
        }
        chooseDateString = chooseYear + "-" + monthStr + "-" + dayStr;
    }

    private void showDatePicker() {
        DatePickerDialog dpd = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                chooseYear = year;
                chooseMonth = monthOfYear + 1;
                chooseDay = dayOfMonth;
                buildChooseDateStr();
                dateTv.setText(" " + chooseDateString);
            }
        }, chooseYear, chooseMonth - 1, chooseDay);
        dpd.show();//显示DatePickerDialog组件
    }

    public VolleyHttpUtils getVolleyHttpUtils() {
        return this.volleyHttpUtils;
    }

    public boolean check() {
        if (fromStation == null) {
            Toast.makeText(this, "请先选择出发站点", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (fromStation == null) {
            Toast.makeText(this, "请先选择到达站点", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (fromStation.getSta_name().equals(toStation.getSta_name())) {
            Toast.makeText(this, "出发站点与到达站点相同", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public void getTrainDataFromInternet() {
        if (!check()) {
            return;
        }
        CustomProgressHUDManager.getInstance().show(this, "查询中", 100);
        volleyHttpUtils.getTrainDetailList(fromStation.getSta_name(), toStation.getSta_name(), chooseDateString, new Response.Listener<TrainDetailListDataSum>() {
            @Override
            public void onResponse(TrainDetailListDataSum trainDetailListDataSum) {
                CustomProgressHUDManager.getInstance().dismiss();
                if (trainDetailListDataSum.getError_code().equals("0")) {
                    if (trainDetailListDataSum.getResult() == null || trainDetailListDataSum.getResult().size() < 0) {
                        Toast.makeText(TrainTicketActivity.this, "查询不到相关车票信息", Toast.LENGTH_SHORT).show();
                    } else {
                        mTrainDetailListAdapter.setDataList(trainDetailListDataSum.getResult());
                    }
                } else {
                    Toast.makeText(TrainTicketActivity.this, "发生未知错误，查询失败", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                CustomProgressHUDManager.getInstance().dismiss();
                Toast.makeText(TrainTicketActivity.this, "发生未知错误，查询失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    TrainTicketFromStationChooseFragment trainTicketFromStationChooseFragment;

    public void toFromStationChoose() {
        getSupportFragmentManager().popBackStack();
        trainTicketFromStationChooseFragment = new TrainTicketFromStationChooseFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.train_ticket_main_fl, trainTicketFromStationChooseFragment);
        fragmentTransaction.addToBackStack("add");
        fragmentTransaction.commit();
    }

    TrainTicketToStationChooseFragment trainTicketToStationChooseFragment;

    public void toToStationChoose() {
        getSupportFragmentManager().popBackStack();
        trainTicketToStationChooseFragment = new TrainTicketToStationChooseFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.train_ticket_main_fl, trainTicketToStationChooseFragment);
        fragmentTransaction.addToBackStack("add");
        fragmentTransaction.commit();
    }

    TrainPriceFragment trainPriceFragment;

    public void toTrainPrice(TrainDetail trainDetail) {
        getSupportFragmentManager().popBackStack();
        trainPriceFragment = TrainPriceFragment.newInstance(trainDetail);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.train_ticket_main_fl, trainPriceFragment);
        fragmentTransaction.addToBackStack("add");
        fragmentTransaction.commit();
    }
}
