package com.graduationproject.personnalfinancialmanagement.statements;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.graduationproject.personnalfinancialmanagement.R;
import com.graduationproject.personnalfinancialmanagement.statements.adapter.StatementListAdapter;
import com.graduationproject.personnalfinancialmanagement.statements.javabean.IncomePaymentStatementItem;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by longhui on 2016/5/26.
 */
public class StatementPieFragment extends Fragment {
    PieChart pieChart;
    List<IncomePaymentStatementItem> incomePaymentStatementItemList;

    static StatementPieFragment newInstance(List<IncomePaymentStatementItem> incomePaymentStatementItemList) {
        StatementPieFragment newFragment = new StatementPieFragment();
        if (incomePaymentStatementItemList != null) {
            Bundle bundle = new Bundle();
            bundle.putSerializable("data_list", (Serializable) incomePaymentStatementItemList);
            newFragment.setArguments(bundle);
        }
        return newFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle args = getArguments();
        if (args != null) {
            incomePaymentStatementItemList = (List<IncomePaymentStatementItem>) args.getSerializable("data_list");
        }
        View rootView = inflater.inflate(R.layout.statement_pie_fragment, container, false);
        pieChart = (PieChart) rootView.findViewById(R.id.statement_pie_chart);
        PieData pieData = getPieData(incomePaymentStatementItemList, "支出");
        pieData.setValueTextColor(Color.parseColor("#ffffff"));
        pieData.setValueTextSize(14);
        showChart(pieChart, pieData, "支出");
        pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
                int index=pieChart.getData().getDataSet().getEntryIndex(e);
                Log.e("StatementPieFragment", index + "");
                IncomePaymentStatementItem itemData = incomePaymentStatementItemList.get(index);
                if (getActivity() instanceof FinancialStatementsActivity) {
                    ((FinancialStatementsActivity) getActivity()).toSeeCategoryList(itemData.getDataType(), itemData.getCategoryNum(), itemData.getCategoryName());
                }

            }

            @Override
            public void onNothingSelected() {

            }
        });
        if (incomePaymentStatementItemList.size() == 0) {
            pieChart.setCenterText("暂无数据");
        }
        return rootView;
    }

    public void setIncomePaymentStatementItemList(int dataType, List<IncomePaymentStatementItem> incomePaymentStatementItemList) {
        this.incomePaymentStatementItemList = incomePaymentStatementItemList;
        String typeString = null;
        if (dataType == 0) {
            typeString = "支出";
        } else {
            typeString = "收入";
        }
        PieData pieData = getPieData(incomePaymentStatementItemList, typeString);
        pieData.setValueTextColor(Color.parseColor("#ffffff"));
        pieData.setValueTextSize(14);
        showChart(pieChart, pieData, typeString);
        if (incomePaymentStatementItemList.size() == 0) {
            pieChart.setCenterText("暂无数据");
        }
    }

    private void showChart(PieChart pieChart, PieData pieData, String typeStr) {
        pieChart.setHoleColorTransparent(true);

        pieChart.setHoleRadius(60f);  //半径
        pieChart.setTransparentCircleRadius(64f); // 半透明圈
        //pieChart.setHoleRadius(0)  //实心圆

        pieChart.setDescription(typeStr + "饼状图");

        // mChart.setDrawYValues(true);
        pieChart.setDrawCenterText(true);  //饼状图中间可以添加文字

        pieChart.setDrawHoleEnabled(true);

        pieChart.setRotationAngle(90); // 初始旋转角度

        // draws the corresponding description value into the slice
        // mChart.setDrawXValues(true);

        // enable rotation of the chart by touch
        pieChart.setRotationEnabled(true); // 可以手动旋转

        // display percentage values
        pieChart.setUsePercentValues(true);  //显示成百分比
        // mChart.setUnit(" €");
        // mChart.setDrawUnitsInChart(true);

        // add a selection listener
//      mChart.setOnChartValueSelectedListener(this);
        // mChart.setTouchEnabled(false);

//      mChart.setOnAnimationListener(this);

        pieChart.setCenterText(typeStr);  //饼状图中间的文字

        //设置数据
        pieChart.setData(pieData);

        // undo all highlights
//      pieChart.highlightValues(null);
//      pieChart.invalidate();

        Legend mLegend = pieChart.getLegend();  //设置比例图
        mLegend.setPosition(Legend.LegendPosition.RIGHT_OF_CHART);  //最右边显示
//      mLegend.setForm(LegendForm.LINE);  //设置比例图的形状，默认是方形
        mLegend.setXEntrySpace(7f);
        mLegend.setYEntrySpace(5f);

        pieChart.animateXY(1000, 1000);  //设置动画
        // mChart.spin(2000, 0, 360);
    }

    /**
     * @param
     * @param
     */
    private PieData getPieData(List<IncomePaymentStatementItem> incomePaymentStatementItemList, String type) {

        ArrayList<String> xValues = new ArrayList<String>();  //xVals用来表示每个饼块上的内容

        for (int i = 0; i < incomePaymentStatementItemList.size(); i++) {
            IncomePaymentStatementItem incomePaymentStatementItem = incomePaymentStatementItemList.get(i);
            xValues.add(incomePaymentStatementItem.getCategoryName());  //饼块上显示成Quarterly1, Quarterly2, Quarterly3, Quarterly4
        }

        ArrayList<Entry> yValues = new ArrayList<Entry>();  //yVals用来表示封装每个饼块的实际数据

        // 饼图数据
        /**
         * 将一个饼形图分成四部分， 四部分的数值比例为14:14:34:38
         * 所以 14代表的百分比就是14%
         */
        ArrayList<Integer> colors = new ArrayList<Integer>();

        // 饼图颜色
        for (int i = 0; i < incomePaymentStatementItemList.size(); i++) {
            IncomePaymentStatementItem incomePaymentStatementItem = incomePaymentStatementItemList.get(i);
            yValues.add(new Entry(incomePaymentStatementItem.getPercent(), i));
            colors.add((Color.rgb((int) (Math.random() * 255), (int) (Math.random() * 255), (int) (Math.random() * 255))));
        }

        //y轴的集合
        PieDataSet pieDataSet = new PieDataSet(yValues, type/*显示在比例图上*/);
        pieDataSet.setSliceSpace(0f); //设置个饼状图之间的距离
        pieDataSet.setColors(colors);

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        float px = 5 * (metrics.densityDpi / 160f);
        pieDataSet.setSelectionShift(px); // 选中态多出的长度

        PieData pieData = new PieData(xValues, pieDataSet);

        return pieData;
    }
}
