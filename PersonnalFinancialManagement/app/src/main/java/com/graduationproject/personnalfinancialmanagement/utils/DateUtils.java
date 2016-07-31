package com.graduationproject.personnalfinancialmanagement.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by longhui on 2016/5/18.
 */
public class DateUtils {
    public static String getCurrentDateString() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date());
    }

    public static String getDateString(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        return sdf.format(date);
    }

    public static String getDateStringYMD(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        return sdf.format(date);
    }

    public static Date getDateYYYYMMDD(String yyyyMMdd) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Date date = null;
        try {
            date = sdf.parse(yyyyMMdd);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static String getTimeStringHM(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        return sdf.format(date);
    }

    public static String getDateStringFull(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        return sdf.format(date);
    }

    public static String getDateString(int year, int month, int day, int hour, int minute, int second) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String dateString = null;
        try {
            dateString = sdf.format(sdf.parse(year + "-" + month + "-" + day + " " + hour + ":" + minute));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateString;
    }

    public static Date getDateByDateStr(String dateStr) {
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date normalDate = null;
        try {
            normalDate = sdf1.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return normalDate;
    }

    public static Date getDate(int year, int month, int day, int hour, int minute, int second) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        try {
            date = sdf.parse(year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + second);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static String getCurrentDateString_simple() {
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm");
        return sdf.format(new Date());
    }

    public static String getDateString_simple(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm");
        return sdf.format(date);
    }

    public static String getDateString_simple(String dateStr) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat sdf1 = new SimpleDateFormat("MM-dd HH:mm");
        Date date = null;
        try {
            date = sdf.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return sdf1.format(date);
    }

    public static String getDateString_simple(int year, int month, int day) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm");
        String dateString = null;
        try {
            dateString = sdf.format(sdf.parse(month + "-" + day + " " + "00:00"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateString;
    }

    public static String getDateString_simple(int year, int month, int day, int hour, int minute, int second) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm");
        String dateString = null;
        try {
            dateString = sdf.format(sdf.parse(month + "-" + day + " " + hour + ":" + minute));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateString;
    }

    public static String getDateString_month_day(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd");
        return sdf.format(date);
    }

    public static int[] getCurrentDateDataInInt() {
        Calendar calendar = Calendar.getInstance();
        int[] dateData = new int[3];
        dateData[0] = calendar.get(Calendar.YEAR);
        dateData[1] = calendar.get(Calendar.MONTH) + 1;
        dateData[2] = calendar.get(Calendar.DAY_OF_MONTH);
        return dateData;
    }

    public static int getCurrentMonthDaysNum() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        int year = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH) + 1;
        if (month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12) {
            return 31;
        } else {
            if (month == 2) {
                if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0) {
                    return 29;
                } else {
                    return 28;
                }
            } else {
                return 30;
            }
        }
    }

    public static int getMonthDaysNum(int year, int month) {
        if (month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12) {
            return 31;
        } else {
            if (month == 2) {
                if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0) {
                    return 29;
                } else {
                    return 28;
                }
            } else {
                return 30;
            }
        }
    }

    public static int[] getCurrentTimeDataInInt() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        int[] timeData = new int[3];
        timeData[0] = calendar.get(Calendar.HOUR_OF_DAY);
        timeData[1] = calendar.get(Calendar.MINUTE);
        timeData[2] = calendar.get(Calendar.SECOND);
        return timeData;
    }

    public static String transNormalDate2MillisecondStr(String normalDateStr) {
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = sdf1.parse(normalDateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSS");
        return sdf.format(date);
    }

    public static String transCurrentDate2MillisecondStr() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSS");
        return sdf.format(new Date());
    }

    //Ms(ms):Millisecond,毫秒
    public static String transMsString2NormalDateStr(String dateInMs) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSS");
        String normalDateStr = null;
        try {
            normalDateStr = getDateString(sdf.parse(dateInMs));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return normalDateStr;
    }

    public static Date transMsString2NormalDate(String dateInMs) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSS");
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date normalDate = null;
        try {
            normalDate = sdf.parse(dateInMs);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return normalDate;
    }

    //Ms(ms):Millisecond,毫秒
    public static String transMsString2Month_Day_DateStr(String dateInMs) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSS");
        String normalDateStr = null;
        try {
            normalDateStr = getDateString_month_day(sdf.parse(dateInMs));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return normalDateStr;
    }

    /**
     * Calculate the days from beginDate to endDate.
     *
     * @param beginDate
     * @return int
     * @Param endDate
     */
    public static int calculateDay(String beginDate, String endDate) {
        if (beginDate == null || endDate == null)
            return 0;
        int difference = 0;
        try {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            Date end = df.parse(endDate);
            Date begin = df.parse(beginDate);
            long diff = (end.getTime() - begin.getTime()) / (1000 * 60 * 60 * 24);
            difference = (int) diff;

        } catch (ParseException e) {
            e.printStackTrace();
//			System.out.println("CalculateDate ERROR!!");
        }
        return difference;
    }

    /**
     * Calculate the days from beginDate to endDate.
     *
     * @param beginDate
     * @return int
     * @Param endDate
     */
    public static int calculateDayMsDate(String beginDate, String endDate) {
        if (beginDate == null || endDate == null)
            return 0;
        int difference = 0;
        try {
            SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmssSS");
            Date end = df.parse(endDate);
            Date begin = df.parse(beginDate);
            long diff = (end.getTime() - begin.getTime()) / (1000 * 60 * 60 * 24);
            difference = (int) diff;

        } catch (ParseException e) {
            e.printStackTrace();
//			System.out.println("CalculateDate ERROR!!");
        }
        return difference;
    }
}
