package com.graduationproject.personnalfinancialmanagement.utils.database;

import android.util.Log;

import com.graduationproject.personnalfinancialmanagement.config.javabean.FinancialAlarm;
import com.graduationproject.personnalfinancialmanagement.config.javabean.MemoBMob;
import com.graduationproject.personnalfinancialmanagement.config.javabean.MemoLitePal;
import com.graduationproject.personnalfinancialmanagement.config.javabean.QuickRecordModel;
import com.graduationproject.personnalfinancialmanagement.customerservice.trainTicket.javabean.TrainStation;
import com.graduationproject.personnalfinancialmanagement.utils.DateUtils;

import org.litepal.crud.DataSupport;

import java.text.DecimalFormat;
import java.util.List;

import cn.bmob.v3.BmobUser;

/**
 * Created by longhui on 2016/5/21.
 */
public class LitePalManager {
    private static LitePalManager gLitePalManager = null;

    public static LitePalManager getInstance() {
        if (null == gLitePalManager) {
            gLitePalManager = new LitePalManager();
        }
        return gLitePalManager;
    }

    private LitePalManager() {
    }

    public List<MemoLitePal> getMemoList(String userId) {
        List<MemoLitePal> memoList = DataSupport.where("userId = ?", userId).find(MemoLitePal.class);
        return memoList;
    }

    public List<FinancialAlarm> getFinancialAlarmList(String userId) {
        int[] currentDate = DateUtils.getCurrentDateDataInInt();
        String startDate = null;
        int monthDaysNum = DateUtils.getCurrentMonthDaysNum();
        String endDate = null;
        if (currentDate[1] < 10) {
            startDate = currentDate[0] + "" + "0" + currentDate[1] + "01" + "00" + "00" + "00" + "00";
            endDate = currentDate[0] + "" + "0" + currentDate[1] + monthDaysNum + "23" + "59" + "59" + "59";
        } else {
            startDate = currentDate[0] + "" + currentDate[1] + "01" + "00" + "00" + "00" + "00";
            endDate = currentDate[0] + "" + currentDate[1] + monthDaysNum + "23" + "59" + "59" + "59";
        }
        Log.e("LitePalManager", startDate + "    " + endDate);
        List<FinancialAlarm> financialAlarmList = DataSupport.where("userId = ? and date>? and date<?", userId, startDate, endDate)
                .order("date desc").find(FinancialAlarm.class);
        return financialAlarmList;
    }

    public List<FinancialAlarm> getAllFinancialAlarmList(String userId) {
        List<FinancialAlarm> financialAlarmList = DataSupport.where("userId = ? ", userId)
                .order("date desc").find(FinancialAlarm.class);
        return financialAlarmList;
    }

    public List<FinancialAlarm> getTodayFinancialAlarmList(String userId) {
        int[] currentDate = DateUtils.getCurrentDateDataInInt();
        String startDate = null;
        int monthDaysNum = DateUtils.getCurrentMonthDaysNum();
        String endDate = null;
        DecimalFormat df = new DecimalFormat("00");
        startDate = currentDate[0] + "" + df.format(currentDate[1]) + df.format(currentDate[2]) + "00" + "00" + "00" + "00";
        endDate = currentDate[0] + "" + df.format(currentDate[1]) + df.format(currentDate[2]) + "23" + "59" + "59" + "59";
        Log.e("LitePalManager", startDate + "    " + endDate);
        List<FinancialAlarm> financialAlarmList = DataSupport.where("userId = ? and date>? and date<?", userId, startDate, endDate)
                .order("date desc").find(FinancialAlarm.class);
        return financialAlarmList;
    }

    public MemoLitePal tranMemoBMob2MemoLitePal(MemoBMob memoBMob) {
        MemoLitePal memoLitePal = new MemoLitePal();
        memoLitePal.setUserId(memoBMob.getUserId());
        memoLitePal.setMemoId(memoBMob.getMemoId());
        memoLitePal.setContent(memoBMob.getContent());
        memoLitePal.setCreateDate(memoBMob.getCreateDate());
        memoLitePal.setUpdateDate(memoBMob.getUpdateDate());
        return memoLitePal;
    }

    public List<TrainStation> getTrainStationList() {
        List<TrainStation> trainStationList = DataSupport.findAll(TrainStation.class);
        return trainStationList;
    }

    public List<QuickRecordModel> getQuickRecordList(String userId) {
        List<QuickRecordModel> quickRecordModelList = DataSupport.where("userId = ?", userId).find(QuickRecordModel.class);
        return quickRecordModelList;
    }

}
