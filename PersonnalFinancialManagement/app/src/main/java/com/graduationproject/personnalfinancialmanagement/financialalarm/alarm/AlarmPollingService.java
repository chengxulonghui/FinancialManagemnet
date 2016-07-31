package com.graduationproject.personnalfinancialmanagement.financialalarm.alarm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.drawable.Icon;
import android.os.IBinder;

import com.graduationproject.personnalfinancialmanagement.R;
import com.graduationproject.personnalfinancialmanagement.config.javabean.FinancialAlarm;
import com.graduationproject.personnalfinancialmanagement.financialalarm.FinancialAlarmActivity;
import com.graduationproject.personnalfinancialmanagement.utils.DateUtils;
import com.graduationproject.personnalfinancialmanagement.utils.database.LitePalManager;

import java.util.List;

import cn.bmob.v3.BmobUser;

public class AlarmPollingService extends Service {

    public static final String ACTION = "com.personnalfinancialmanagement.service.AlarmPollingService";
    private static final int NOTIFICATION_FLAG = 1;
    private Notification mNotification;
    private NotificationManager mManager;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        initNotificationManager();
    }

    @Override
    public void onStart(Intent intent, int startId) {
        new PollingThread().start();
    }

    private void initNotificationManager() {
        mManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }

    private void showNotification(String tickerStr, String titleStr, String contentStr) {
        PendingIntent pendingIntent2 = PendingIntent.getActivity(this, 0,
                new Intent(this, FinancialAlarmActivity.class).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        // 通过Notification.Builder来创建通知，注意API Level
        // API11之后才支持
        Notification notify2 = new Notification.Builder(this)
                .setSmallIcon(R.drawable.icon_app_small) // 设置状态栏中的小图片，尺寸一般建议在24×24，这个图片同样也是在下拉状态栏中所显示，如果在那里需要更换更大的图片，可以使用setLargeIcon(Bitmap
                        // icon)
                .setTicker(tickerStr)// 设置在status
                        // bar上显示的提示文字
                .setContentTitle(titleStr)// 设置在下拉status
                        // bar后Activity，本例子中的NotififyMessage的TextView中显示的标题
                .setContentText(contentStr)// TextView中显示的详细内容
                .setContentIntent(pendingIntent2) // 关联PendingIntent
                .setNumber(1) // 在TextView的右方显示的数字，可放大图片看，在最右侧。这个number同时也起到一个序列号的左右，如果多个触发多个通知（同一ID），可以指定显示哪一个。
                .setAutoCancel(true)
                .getNotification(); // 需要注意build()是在API level
        // 16及之后增加的，在API11中可以使用getNotificatin()来代替
        notify2.flags |= Notification.FLAG_AUTO_CANCEL;
        mManager.notify(NOTIFICATION_FLAG, notify2);
    }

    /**
     * Polling thread
     * 模拟向Server轮询的异步线程
     *
     * @Author Ryan
     * @Create 2013-7-13 上午10:18:34
     */

    class PollingThread extends Thread {
        @Override
        public void run() {
            System.out.println("Polling...");
            List<FinancialAlarm> financialAlarmList =
                    LitePalManager.getInstance().getFinancialAlarmList(BmobUser.getCurrentUser(AlarmPollingService.this).getObjectId());
            int alarmCount = 0;
            for (FinancialAlarm financialAlarm : financialAlarmList) {
                if (DateUtils.calculateDayMsDate(financialAlarm.getDate(), DateUtils.transCurrentDate2MillisecondStr()) == 0
                        && financialAlarm.getIsObsolete() == 0 && financialAlarm.getIsAlarm() == 0 && financialAlarm.getIsHandle() == 0 &&
                        DateUtils.transMsString2Month_Day_DateStr(financialAlarm.getDate()).equals(DateUtils.transMsString2Month_Day_DateStr(DateUtils.transCurrentDate2MillisecondStr()))) {
                    alarmCount++;
                }
                financialAlarm.setIsAlarm(1);
                financialAlarm.update(financialAlarm.getId());
            }
            if (alarmCount > 0) {
                showNotification("财务日历-您有一条新的资金提醒消息", "财务日历-资金提醒", "你有可记录资金信息，共" + alarmCount + "条");
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        System.out.println("Service:onDestroy");
    }

}

