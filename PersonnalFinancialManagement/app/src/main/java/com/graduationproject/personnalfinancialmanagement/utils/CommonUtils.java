package com.graduationproject.personnalfinancialmanagement.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2015/11/9.
 */
public class CommonUtils {
    public static void changeFonts(Context context, TextView tv, String fontName) {
        AssetManager mgr = context.getAssets();//得到AssetManager
        Typeface tf = Typeface.createFromAsset(mgr, "fonts/" + fontName);//根据路径得到Typeface
        tv.setTypeface(tf);//设置字体
    }

    /**
     * 强制隐藏输入法键盘
     */
    public static void hideSoftKeyboard(Context context, View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static String getTitleDate() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM月dd日");
        Date date = new Date();
        String dateStr = simpleDateFormat.format(date);
        String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;
        return dateStr + " " + weekDays[w];
    }

    /***
     * 判断是否是晚上
     *
     * @return true:夜晚 false:白天
     */
    public static boolean isNight() {
        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        if (hour > 17 || hour < 6) {
            return true;
        } else {
            return false;
        }
    }

    /***
     * 判断是否过了中午
     *
     * @return true:过了 false:没过
     */
    public static boolean isPassNoon() {
        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        if (hour > 12) {
            return true;
        } else {
            return false;
        }
    }

    /***
     * 在当前手机上面，将dp转换为px
     *
     * @param context
     * @param dpValue
     * @return
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = getScreenScale(context);
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从px(像素)的单位转成为dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /***
     * 获取屏幕密度
     *
     * @param context
     * @return 屏幕密度
     */
    public static float getScreenScale(Context context) {
        return context.getResources().getDisplayMetrics().density;
    }

    //判断是否全是中文
    public static boolean isAllChinese(String str) {
        String regEx = "[\\u4e00-\\u9fa5]"; //表示中文
        Pattern pat = Pattern.compile(regEx);
        Matcher mat = pat.matcher(str);
        return mat.find();
    }

    public static String standardizeDate(String date) {
        String[] date_parts = date.split("-");
        int year = Integer.valueOf(date_parts[0]);
        int month = Integer.valueOf(date_parts[1]);
        int day = Integer.valueOf(date_parts[2]);
        String monthStr = String.valueOf(month);
        if (month < 10)
            monthStr = "0" + monthStr;
        String dayStr = String.valueOf(day);
        if (day < 10)
            dayStr = "0" + dayStr;
        return year + "-" + monthStr + "-" + dayStr;
    }

    public static int compareTime(String left, String right) {
        long ret = 0;
        try {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");
            Date leftTime = df.parse(left);
            Date rightTime = df.parse(right);
            ret = leftTime.getTime() - rightTime.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (ret < 0) return -1;
        else if (ret > 0) return 1;
        else return 0;
    }

    //计算中英文混合长度，一个中文字符=两个英文字符
    public static int calculateLengthWithByte(String str) {
        int length = 0;
        for (int i = 0; i < str.length(); i++) {
            char tmp = str.charAt(i);
            if (tmp > 0 && tmp < 127) {
                length += 1;
            } else {
                length += 2;
            }
        }
        return length;
    }

    public static String subStringWithByte(String str, int length) {
        if (str.length() == 0 || length == 0)
            return "";
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < str.length() && length > 0; i++) {
            char tmp = str.charAt(i);
            buf.append(tmp);
            if (tmp > 0 && tmp < 127) {
                length -= 1;
            } else {
                length -= 2;
            }
        }

        return buf.toString();
    }

    //向前截取字符串
    public static String subStr(String str, int subSLength) throws UnsupportedEncodingException {
        if (str == null)
            return "";
        else {
            int tempSubLength = subSLength;//截取字节数
            String subStr = str.substring(0, str.length() < subSLength ? str.length() : subSLength);//截取的子串
            int subStrByetsL = subStr.getBytes("GBK").length;//截取子串的字节长度
            //int subStrByetsL = subStr.getBytes().length;//截取子串的字节长度
            // 说明截取的字符串中包含有汉字
            while (subStrByetsL > tempSubLength) {
                int subSLengthTemp = --subSLength;
                subStr = str.substring(0, subSLengthTemp > str.length() ? str.length() : subSLengthTemp);
                subStrByetsL = subStr.getBytes("GBK").length;
                //subStrByetsL = subStr.getBytes().length;
            }
            return subStr;
        }
    }

    public static String byteSubstring(String s, int length) throws Exception {

        byte[] bytes = s.getBytes("Unicode");
        int n = 0; // 表示当前的字节数
        int i = 2; // 要截取的字节数，从第3个字节开始
        for (; i < bytes.length && n < length; i++) {
            // 奇数位置，如3、5、7等，为UCS2编码中两个字节的第二个字节
            if (i % 2 == 1) {
                n++; // 在UCS2第二个字节时n加1
            } else {
                // 当UCS2编码的第一个字节不等于0时，该UCS2字符为汉字，一个汉字算两个字节
                if (bytes[i] != 0) {
                    n++;
                }
            }
        }
        // 如果i为奇数时，处理成偶数
        if (i % 2 == 1)

        {
            // 该UCS2字符是汉字时，去掉这个截一半的汉字
            if (bytes[i - 1] != 0)
                i = i - 1;
                // 该UCS2字符是字母或数字，则保留该字符
            else
                i = i + 1;
        }

        return new String(bytes, 0, i, "Unicode");
    }

    public static boolean isEmail(String str) {
        Pattern pattern = Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
        Matcher matcher = pattern.matcher(str);
        boolean b = matcher.matches();
        return b;
    }
}
