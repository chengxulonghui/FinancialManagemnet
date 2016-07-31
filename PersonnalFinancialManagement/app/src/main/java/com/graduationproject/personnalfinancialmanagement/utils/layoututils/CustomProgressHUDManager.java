package com.graduationproject.personnalfinancialmanagement.utils.layoututils;

import android.content.Context;
import android.content.DialogInterface;

import com.kaopiz.kprogresshud.KProgressHUD;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2015/4/6.
 */
public class CustomProgressHUDManager {
    private static CustomProgressHUDManager gCustomProgressHUDManager = null;

    public static CustomProgressHUDManager getInstance() {
        if (null == gCustomProgressHUDManager) {
            gCustomProgressHUDManager = new CustomProgressHUDManager();
        }
        return gCustomProgressHUDManager;
    }

    // ==============================================================
    private KProgressHUD mProgressHUD = null;

    private CustomProgressHUDManager() {

    }

    /**
     * 显示HUD进度，不可手动取消显示
     *
     * @param context context
     * @param message 显示的文字内容
     * @param seconds 定时消失，单位是秒
     */
    private Timer mTimer = null;

    public void show(Context context, String message, int seconds) {
        show(context, message, seconds, false);
    }

    public void show(Context context, String message, int seconds, boolean cancelable) {
        if (null == context || null == message || 0 >= seconds) return;

        try { // TODO 最外层防护
            dismiss();
            mProgressHUD = KProgressHUD.create(context)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel(message)
                    .setCancellable(false)
                    .setAnimationSpeed(2)
                    .setDimAmount(0.5f)
                    .show();


            mTimer = new Timer();
            mTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    dismiss();
                }
            }, seconds * 1000);
        } catch (Exception e) {
            e.printStackTrace();
//            LogConfig.Assert(false);
        }
    }


    public void dismiss() {
        if (null != mTimer) {
            mTimer.cancel();
            mTimer = null;
        }
        if (null != mProgressHUD) {
            mProgressHUD.dismiss();
            mProgressHUD = null;
        }
    }

}
