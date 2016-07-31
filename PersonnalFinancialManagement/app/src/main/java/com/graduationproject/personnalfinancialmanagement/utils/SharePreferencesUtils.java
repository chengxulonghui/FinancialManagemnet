package com.graduationproject.personnalfinancialmanagement.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Administrator on 2015/11/14.
 */
public class SharePreferencesUtils {
    private static final String spName="financial_Sp";
    public static SharedPreferences getSharedPreferences(Context context){
        return context.getSharedPreferences(spName,Context.MODE_PRIVATE);
    }
    public static void saveString2Sp(Context context,String key,String value){
        SharedPreferences sp=getSharedPreferences(context);
        SharedPreferences.Editor editor=sp.edit();
        editor.putString(key,value);
        editor.commit();
    }
    public static void saveBoolean2Sp(Context context,String key,boolean value){
        SharedPreferences sp=getSharedPreferences(context);
        SharedPreferences.Editor editor=sp.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }
    public static String getStringFromSp(Context context,String tag){
        SharedPreferences sp=getSharedPreferences(context);
        return sp.getString(tag,"");
    }
}
