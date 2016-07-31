package com.graduationproject.personnalfinancialmanagement.utils.http;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

/**
 * Created by Administrator on 2014/12/17.
 */
public class NetworkUtils
{
    /**
     * 判断是否有网络连接
     * @param context
     * @return
     */
    public static boolean isNetworkAvailable(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    /**
     * 判断WIFI网络是否可用
     * @param context
     * @return
     */
    public static boolean isWifiConnected(Context context) {
        if (context != null) {
            NetworkInfo mWiFiNetworkInfo = getWifiNetworkInfo(context);
            if (mWiFiNetworkInfo != null) {
                return mWiFiNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    /**
     * 判断MOBILE网络是否可用
     * @param context
     * @return
     */
    public static boolean isMobileConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mMobileNetworkInfo = mConnectivityManager
                .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (mMobileNetworkInfo != null) {
                return mMobileNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    /**
     * 获取当前网络连接的类型信息
     * @param context
     * @return
     */
    public static int getConnectedType(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null && mNetworkInfo.isAvailable()) {
                return mNetworkInfo.getType();
            }
        }
        return -1;
    }

    /***
     * 获取当前连接上的Wifi ssid名字（Id）
     * @param mContext
     * @return
     */
    public static String getConnectedWifiSsid(Context mContext)
    {
        WifiInfo mWifiInfo = getWifiConnectionInfo(mContext);
        String ssid = null;
        if (null != mWifiInfo && isWifiConnected(mContext)) {
            int len = mWifiInfo.getSSID().length();
            if (mWifiInfo.getSSID().startsWith("\"") && mWifiInfo.getSSID().endsWith("\"")) {
                ssid = mWifiInfo.getSSID().substring(1, len -1);
            } else {
                ssid = mWifiInfo.getSSID();
            }
        }
        return ssid;
    }

    /**
     * 获取当前连接上的wifi bssid
     */
    public static String getConnectedWifiBssid(Context context) {
        WifiInfo mWifiInfo = getWifiConnectionInfo(context);
        String bssid = null;
        if (null != mWifiInfo && isWifiConnected(context)) {
            bssid = mWifiInfo.getBSSID();
        }
        return bssid;
    }

    /**
     * 获取wifi信息
     */
    private static WifiInfo getWifiConnectionInfo(Context context) {
        WifiManager mWifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        return mWifiManager.getConnectionInfo();
    }

    /**
     * 获取当前wifi网络信息
     */
    private static NetworkInfo getWifiNetworkInfo(Context context) {
        ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifiNetworkInfo = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        return mWifiNetworkInfo;
    }
}
