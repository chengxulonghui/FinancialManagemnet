package com.graduationproject.personnalfinancialmanagement.customerservice.trainTicket;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.graduationproject.personnalfinancialmanagement.R;
import com.graduationproject.personnalfinancialmanagement.config.javabean.MemoLitePal;
import com.graduationproject.personnalfinancialmanagement.customerservice.trainTicket.javabean.TrainDetail;
import com.graduationproject.personnalfinancialmanagement.customerservice.trainTicket.javabean.TrainPrice;
import com.graduationproject.personnalfinancialmanagement.customerservice.trainTicket.javabean.TrainPriceSum;
import com.graduationproject.personnalfinancialmanagement.utils.DateUtils;
import com.graduationproject.personnalfinancialmanagement.utils.http.volley.VolleyHttpUtils;
import com.graduationproject.personnalfinancialmanagement.utils.layoututils.CustomProgressHUDManager;

import java.io.Serializable;

/**
 * Created by longhui on 2016/5/25.
 */
public class TrainPriceFragment extends Fragment {
    //TitleBar相关
    private ImageView backIv, confirm;
    private TextView titleTv;

    TrainDetail trainDetail;
    TextView trainCodeTv;
    TextView trainTimeTv;
    TextView trainStationTv;
    TableRow toPriceTr;
    LinearLayout sitLl;
    VolleyHttpUtils volleyHttpUtils;

    static TrainPriceFragment newInstance(TrainDetail data) {
        TrainPriceFragment newFragment = new TrainPriceFragment();
        if (data != null) {
            Bundle bundle = new Bundle();
            bundle.putSerializable("train_detail", (Serializable) data);
            newFragment.setArguments(bundle);
        }
        return newFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle args = getArguments();
        if (args != null) {
            trainDetail = (TrainDetail) args.getSerializable("train_detail");
        }
        View rootView = inflater.inflate(R.layout.train_price_fragment, container, false);
        initView(rootView);
        initData();
        return rootView;
    }

    private void initView(View itemView) {
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        initTitleBar(itemView.findViewById(R.id.train_price_title_bar));
        trainCodeTv = (TextView) itemView.findViewById(R.id.train_item_train_code_tv);
        trainTimeTv = (TextView) itemView.findViewById(R.id.train_item_train_time_tv);
        trainStationTv = (TextView) itemView.findViewById(R.id.train_item_train_station_tv);
        toPriceTr = (TableRow) itemView.findViewById(R.id.train_item_train_to_price_tr);
        sitLl = (LinearLayout) itemView.findViewById(R.id.train_item_train_sit_ll);
        trainCodeTv.setText(trainDetail.getStation_train_code());
        String dateString = DateUtils.getDateStringYMD(DateUtils.getDateYYYYMMDD(trainDetail.getStart_train_date()));
        trainTimeTv.setText(dateString + " " + trainDetail.getStart_time() + " " + "-"
                + " " + trainDetail.getArrive_time() + " " + "历时:" + trainDetail.getLishi());
        trainStationTv.setText(trainDetail.getFrom_station_name() + " " + "-" + " " + trainDetail.getTo_station_name() +
                " " + "(" + "全程:" + trainDetail.getStart_station_name() +
                " " + "-" + " " + trainDetail.getEnd_station_name() + ")");
    }

    private void initTitleBar(View titleBar) {
        backIv = (ImageView) titleBar.findViewById(R.id.title_bar_back_iv);
        confirm = (ImageView) titleBar.findViewById(R.id.title_bar_confirm_tv);
        titleTv = (TextView) titleBar.findViewById(R.id.title_bar_title_tv);
        backIv = (ImageView) titleBar.findViewById(R.id.title_bar_back_iv);
        confirm.setVisibility(View.GONE);
        titleTv.setText(trainDetail.getStation_train_code());
        backIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                remove();
            }
        });
    }

    private void initData() {
        volleyHttpUtils = ((TrainTicketActivity) getActivity()).getVolleyHttpUtils();
        CustomProgressHUDManager.getInstance().show(getActivity(), "", 100);
        String dateString = DateUtils.getDateStringYMD(DateUtils.getDateYYYYMMDD(trainDetail.getStart_train_date()));
        volleyHttpUtils.getTrainPrice(trainDetail.getTrain_no(), trainDetail.getFrom_station_no(), trainDetail.getTo_station_no(), dateString, new Response.Listener<TrainPriceSum>() {
            @Override
            public void onResponse(TrainPriceSum trainPriceSum) {
                CustomProgressHUDManager.getInstance().dismiss();
                if (trainPriceSum.getError_code().equals("0")) {
                    fillSitData(sitLl, trainDetail, trainPriceSum.getResult());
                } else {
                    Toast.makeText(getActivity(), "发生未知错误，获取价格失败", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                CustomProgressHUDManager.getInstance().dismiss();
                Toast.makeText(getActivity(), "发生未知错误，获取价格失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fillSitData(LinearLayout ll, TrainDetail itemData, TrainPrice trainPrice) {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        if (ll.getChildCount() == 0) {
            for (int i = 0; i < 11; i++) {
                LinearLayout sitLayout = (LinearLayout) inflater.inflate(R.layout.train_sit_price_layout, null);
                ll.addView(sitLayout);
            }
        }
        for (int i = 0; i < ll.getChildCount(); i++) {
            View sitView = ll.getChildAt(i);
            TextView sitTypeTv = (TextView) sitView.findViewById(R.id.train_sit_type_tv);
            TextView sitHaveTv = (TextView) sitView.findViewById(R.id.train_sit_have_tv);
            TextView priceTv = (TextView) sitView.findViewById(R.id.train_sit_price_tv);
            String sitType = "";
            String sitHave = "";
            String price = "";
            switch (i) {
                case 0:
                    sitType = "高级软卧: ";
                    if (itemData.getGr_num().equals("--")) {
                        sitHave = "不提供该席位";
                        price = "";
                    } else {
                        sitHave = itemData.getGr_num();
                        price = trainPrice.getGr() + "/张";
                    }
                    break;
                case 1:
                    sitType = "其他: ";
                    if (itemData.getQt_num().equals("--")) {
                        sitHave = "不提供该席位";
                        price = "";
                    } else {
                        sitHave = itemData.getQt_num();
                        price = trainPrice.getQt() + "/张";
                    }
                    break;
                case 2:
                    sitType = "软卧: ";
                    if (itemData.getRw_num().equals("--")) {
                        sitHave = "不提供该席位";
                        price = "";
                    } else {
                        sitHave = itemData.getRw_num();
                        price = trainPrice.getRw() + "/张";
                    }
                    break;
                case 3:
                    sitType = "软座: ";
                    if (itemData.getRz_num().equals("--")) {
                        sitHave = "不提供该席位";
                        price = "";
                    } else {
                        sitHave = itemData.getRz_num();
                        price = trainPrice.getRz() + "/张";
                    }
                    break;
                case 4:
                    sitType = "特等座: ";
                    if (itemData.getTz_num().equals("--")) {
                        sitHave = "不提供该席位";
                        price = "";
                    } else {
                        sitHave = itemData.getTz_num();
                        price = trainPrice.getTz() + "/张";
                    }
                    break;
                case 5:
                    sitType = "无座: ";
                    if (itemData.getWz_num().equals("--")) {
                        sitHave = "不提供该席位";
                        price = "";
                    } else {
                        sitHave = itemData.getWz_num();
                        price = trainPrice.getWz() + "/张";
                    }
                    break;
                case 6:
                    sitType = "硬卧: ";
                    if (itemData.getYw_num().equals("--")) {
                        sitHave = "不提供该席位";
                        price = "";
                    } else {
                        sitHave = itemData.getYw_num();
                        price = trainPrice.getYw() + "/张";
                    }
                    break;
                case 7:
                    sitType = "硬座: ";
                    if (itemData.getYz_num().equals("--")) {
                        sitHave = "不提供该席位";
                        price = "";
                    } else {
                        sitHave = itemData.getYz_num();
                        price = trainPrice.getYz() + "/张";
                    }
                    break;
                case 8:
                    sitType = "二等座: ";
                    if (itemData.getZe_num().equals("--")) {
                        sitHave = "不提供该席位";
                        price = "";
                    } else {
                        sitHave = itemData.getZe_num();
                        price = trainPrice.getZe() + "/张";
                    }
                    break;
                case 9:
                    sitType = "一等座: ";
                    if (itemData.getZy_num().equals("--")) {
                        sitHave = "不提供该席位";
                        price = "";
                    } else {
                        sitHave = itemData.getZy_num();
                        price = trainPrice.getZy() + "/张";
                    }
                    break;
                case 10:
                    sitType = "商务座: ";
                    if (itemData.getSwz_num().equals("--")) {
                        sitHave = "不提供该席位";
                        price = "";
                    } else {
                        sitHave = itemData.getSwz_num();
                        price = trainPrice.getSwz() + "/张";
                    }
                    break;
            }
            sitTypeTv.setText(sitType);
            sitHaveTv.setText(sitHave);
            priceTv.setText(price);
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
