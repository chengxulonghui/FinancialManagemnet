package com.graduationproject.personnalfinancialmanagement.utils.http.Bmod;

import android.content.Context;
import android.util.Log;

import com.graduationproject.personnalfinancialmanagement.config.javabean.Income_payment;
import com.graduationproject.personnalfinancialmanagement.config.javabean.MemoBMob;
import com.graduationproject.personnalfinancialmanagement.config.javabean.MemoLitePal;
import com.graduationproject.personnalfinancialmanagement.utils.DateUtils;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.listener.DeleteListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by longhui on 2016/5/11.
 */
public class MyBMobManager {
    private static MyBMobManager gBmodManager = null;

    public static MyBMobManager getInstance() {
        if (null == gBmodManager) {
            gBmodManager = new MyBMobManager();
        }
        return gBmodManager;
    }


    private MyBMobManager() {

    }

    public void register(Context context, String userName, String password, String email, SaveListener registerSaveListener) {
        BmobUser bu = new BmobUser();
        bu.setUsername(userName);
        bu.setPassword(password);
        bu.setEmail(email);
        //注意：不能用save方法进行注册
        bu.signUp(context, registerSaveListener);
    }

    public void login(Context context, String userName, String password, SaveListener loginSaveListener) {
        BmobUser bu = new BmobUser();
        bu.setUsername(userName);
        bu.setPassword(password);
        //注意：不能用save方法进行注册
        bu.login(context, loginSaveListener);
    }

    public void saveIncome(Context context, Income_payment income, SaveListener incomeSaveListener) {
        income.save(context, incomeSaveListener);
    }

    public void savePayment(Context context, Income_payment payment, SaveListener paymentSaveListener) {
        payment.save(context, paymentSaveListener);
    }

    public void getTodayPayment(Context context, FindListener<Income_payment> findListener) {
        BmobQuery<Income_payment> query = new BmobQuery<Income_payment>();
        List<BmobQuery<Income_payment>> and = new ArrayList<BmobQuery<Income_payment>>();
        query.addWhereEqualTo("userId", BmobUser.getCurrentUser(context).getObjectId());
        query.addWhereEqualTo("dataType", 0);
        query.setLimit(500);
        //大于
        BmobQuery<Income_payment> q1 = new BmobQuery<Income_payment>();
        int[] currentDateData = DateUtils.getCurrentDateDataInInt();
        int currentYear = currentDateData[0];
        int currentMonth = currentDateData[1];
        int currentDay = currentDateData[2];
        DecimalFormat df = new DecimalFormat("00");
        String start = currentYear + "-" + currentMonth + "-" + df.format(currentDay);
        Log.e("TAG_START", start);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = sdf.parse(start);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        q1.addWhereGreaterThanOrEqualTo("date", new BmobDate(date));
        and.add(q1);
        //小于
        BmobQuery<Income_payment> q2 = new BmobQuery<Income_payment>();
        int monthDaysNum = DateUtils.getCurrentMonthDaysNum();
        String end = currentYear + "-" + currentMonth + "-" + df.format(currentDay) + " 23:59:59";
        Log.e("TAG_END", end);
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date1 = null;
        try {
            date1 = sdf1.parse(end);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        q2.addWhereLessThanOrEqualTo("date", new BmobDate(date1));
        and.add(q2);
        //添加复合与查询
        query.and(and);
        query.order("-date");
        query.findObjects(context, findListener);
    }

    public void getCurrentMonthIncomeAndPayment(Context context, FindListener<Income_payment> findListener) {
        BmobQuery<Income_payment> query = new BmobQuery<Income_payment>();
        List<BmobQuery<Income_payment>> and = new ArrayList<BmobQuery<Income_payment>>();
        query.addWhereEqualTo("userId", BmobUser.getCurrentUser(context).getObjectId());
        query.setLimit(500);
        //大于
        BmobQuery<Income_payment> q1 = new BmobQuery<Income_payment>();
        int[] currentDateData = DateUtils.getCurrentDateDataInInt();
        int currentYear = currentDateData[0];
        int currentMonth = currentDateData[1];
        String start = currentYear + "-" + currentMonth + "-" + "01";
        Log.e("TAG_START", start);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = sdf.parse(start);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        q1.addWhereGreaterThanOrEqualTo("date", new BmobDate(date));
        and.add(q1);
        //小于
        BmobQuery<Income_payment> q2 = new BmobQuery<Income_payment>();
        int monthDaysNum = DateUtils.getCurrentMonthDaysNum();
        String end = currentYear + "-" + currentMonth + "-" + monthDaysNum + " 23:59:59";
        Log.e("TAG_END", end);
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date1 = null;
        try {
            date1 = sdf1.parse(end);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        q2.addWhereLessThanOrEqualTo("date", new BmobDate(date1));
        and.add(q2);
        //添加复合与查询
        query.and(and);
        query.order("-date");
        query.findObjects(context, findListener);
    }

    public void getMonthIncomeAndPayment(Context context, int year, int month, FindListener<Income_payment> findListener) {
        BmobQuery<Income_payment> query = new BmobQuery<Income_payment>();
        List<BmobQuery<Income_payment>> and = new ArrayList<BmobQuery<Income_payment>>();
        query.addWhereEqualTo("userId", BmobUser.getCurrentUser(context).getObjectId());
        query.setLimit(500);
        //大于
        BmobQuery<Income_payment> q1 = new BmobQuery<Income_payment>();
        String start = year + "-" + month + "-" + "01";
        Log.e("TAG_START", start);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = sdf.parse(start);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        q1.addWhereGreaterThanOrEqualTo("date", new BmobDate(date));
        and.add(q1);
        //小于
        BmobQuery<Income_payment> q2 = new BmobQuery<Income_payment>();
        int monthDaysNum = DateUtils.getMonthDaysNum(year, month);
        String end = year + "-" + month + "-" + monthDaysNum + " 23:59:59";
        Log.e("TAG_END", end);
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date1 = null;
        try {
            date1 = sdf1.parse(end);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        q2.addWhereLessThanOrEqualTo("date", new BmobDate(date1));
        and.add(q2);
        //添加复合与查询
        query.and(and);
        query.order("-date");
        query.findObjects(context, findListener);
    }

    public void getMonthIncomeAndPaymentByCategoryNum(Context context, int year, int month, Integer categoryNum, Integer dataType, FindListener<Income_payment> findListener) {
        BmobQuery<Income_payment> query = new BmobQuery<Income_payment>();
        List<BmobQuery<Income_payment>> and = new ArrayList<BmobQuery<Income_payment>>();
        query.addWhereEqualTo("userId", BmobUser.getCurrentUser(context).getObjectId());
        query.addWhereEqualTo("categoryNum", categoryNum);
        query.addWhereEqualTo("dataType", dataType);
        query.setLimit(500);
        //大于
        BmobQuery<Income_payment> q1 = new BmobQuery<Income_payment>();
        String start = year + "-" + month + "-" + "01";
        Log.e("TAG_START", start);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = sdf.parse(start);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        q1.addWhereGreaterThanOrEqualTo("date", new BmobDate(date));
        and.add(q1);
        //小于
        BmobQuery<Income_payment> q2 = new BmobQuery<Income_payment>();
        int monthDaysNum = DateUtils.getMonthDaysNum(year, month);
        String end = year + "-" + month + "-" + monthDaysNum + " 23:59:59";
        Log.e("TAG_END", end);
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date1 = null;
        try {
            date1 = sdf1.parse(end);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        q2.addWhereLessThanOrEqualTo("date", new BmobDate(date1));
        and.add(q2);
        //添加复合与查询
        query.and(and);
        query.order("-date");
        query.findObjects(context, findListener);
    }

    public void saveMemo(Context context, MemoBMob memo, SaveListener memoSaveListener) {
        memo.save(context, memoSaveListener);
    }

    public void getMemoListByUserId(Context context, String userId, FindListener<MemoBMob> findListener) {
        BmobQuery<MemoBMob> query = new BmobQuery<MemoBMob>();
        query.addWhereEqualTo("userId", userId);
        query.order("-createDate");
        query.findObjects(context, findListener);
    }

    public void getMemoByMemoId(Context context, String memoId, FindListener<MemoBMob> findListener) {
        BmobQuery<MemoBMob> query = new BmobQuery<MemoBMob>();
        query.addWhereEqualTo("userId", BmobUser.getCurrentUser(context).getObjectId());
        query.addWhereEqualTo("memoId", memoId);
        query.findObjects(context, findListener);
    }

    public void updateMemo(Context context, MemoBMob memoBMob, UpdateListener updateListener) {
        memoBMob.update(context, updateListener);
    }

    public MemoBMob tranMemoLitePal2MemoBMob(MemoLitePal memoLitePal) {
        MemoBMob memoBMob = new MemoBMob();
        memoBMob.setUserId(memoLitePal.getUserId());
        memoBMob.setContent(memoLitePal.getContent());
        memoBMob.setMemoId(memoLitePal.getMemoId());
        memoBMob.setCreateDate(memoLitePal.getCreateDate());
        memoBMob.setUpdateDate(memoLitePal.getUpdateDate());
        return memoBMob;
    }

    public void deleteIncomePayment(Context context, Income_payment incomePayment, DeleteListener deleteListener) {
        incomePayment.delete(context, deleteListener);
    }
}
