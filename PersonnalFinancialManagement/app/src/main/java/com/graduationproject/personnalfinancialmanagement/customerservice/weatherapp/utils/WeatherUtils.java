package com.graduationproject.personnalfinancialmanagement.customerservice.weatherapp.utils;

import android.util.Log;


import com.graduationproject.personnalfinancialmanagement.R;
import com.graduationproject.personnalfinancialmanagement.utils.CommonUtils;

import java.util.Map;

/**
 * Created by Administrator on 2015/11/15.
 */
public class WeatherUtils {
    //获取底部天气Box的天气图标Id
    public static int getWeatherBoxIconId(Map<String, String> weatherMap) {
        int weatherIconId = -1;
        int weatherId = 0;
        if (CommonUtils.isPassNoon()) {
            weatherId = Integer.parseInt(weatherMap.get("fb"));
        } else {
            weatherId = Integer.parseInt(weatherMap.get("fa"));
        }
        switch (weatherId) {
            case 0:
                weatherIconId = R.drawable.ww0;
                break;
            case 1:
                weatherIconId = R.drawable.ww1;
                break;
            case 2:
                weatherIconId = R.drawable.ww2;
                break;
            case 3:
                weatherIconId = R.drawable.ww3;
                break;
            case 4:
                weatherIconId = R.drawable.ww4;
                break;
            case 5:
                weatherIconId = R.drawable.ww5;
                break;
            case 6:
                weatherIconId = R.drawable.ww6;
                break;
            case 7:
                weatherIconId = R.drawable.ww7;
                break;
            case 8:
                weatherIconId = R.drawable.ww8;
                break;
            case 9:
                weatherIconId = R.drawable.ww9;
                break;
            case 10:
                weatherIconId = R.drawable.ww10;
                break;
            case 11:
                weatherIconId = R.drawable.ww10;
                break;
            case 12:
                weatherIconId = R.drawable.ww10;
                break;
            case 13:
                weatherIconId = R.drawable.ww13;
                break;
            case 14:
                weatherIconId = R.drawable.ww14;
                break;
            case 15:
                weatherIconId = R.drawable.ww15;
                break;
            case 16:
                weatherIconId = R.drawable.ww16;
                break;
            case 17:
                weatherIconId = R.drawable.ww17;
                break;
            case 18:
                weatherIconId = R.drawable.ww18;
                break;
            case 19:
                weatherIconId = R.drawable.ww19;
                break;
            case 20:
                weatherIconId = R.drawable.ww20;
                break;
            case 21:
                weatherIconId = R.drawable.ww8;
                break;
            case 22:
                weatherIconId = R.drawable.ww9;
                break;
            case 23:
            case 24:
            case 25:
                weatherIconId = R.drawable.ww10;
                break;
            case 26:
                weatherIconId = R.drawable.ww15;
                break;
            case 27:
                weatherIconId = R.drawable.ww16;
                break;
            case 28:
                weatherIconId = R.drawable.ww17;
                break;
            case 29:
                weatherIconId = R.drawable.ww29;
                break;
            case 30:
                weatherIconId = R.drawable.ww30;
                break;
            case 31:
                weatherIconId = R.drawable.ww36;
                break;
            case 53:
                weatherIconId = R.drawable.ww18;
                break;
        }
        return weatherIconId;
    }

    public static int getWeatherBgId(Map<String, String> weatherMap) {
        int weatherBgId = -1;
        int weatherId = 0;
        if (CommonUtils.isNight()) {
            weatherId = Integer.parseInt(weatherMap.get("fb"));
        } else {
            weatherId = Integer.parseInt(weatherMap.get("fa"));
        }
        switch (weatherId) {
            case 0:
                if (!CommonUtils.isNight()) {
                    weatherBgId = R.drawable.day_ww_0;
                } else {
                    weatherBgId = R.drawable.night_ww_0;
                }
                break;
            case 1:
            case 2:
                if (!CommonUtils.isNight()) {
                    weatherBgId = R.drawable.day_ww_01_02;
                } else {
                    weatherBgId = R.drawable.night_ww_01_02;
                }
                break;
            case 3:
            case 4:
            case 5:
                if (!CommonUtils.isNight()) {
                    weatherBgId = R.drawable.day_ww_03_04_05;
                } else {
                    weatherBgId = R.drawable.night_ww_03_04_05;
                }
                break;
            case 6:
            case 7:
            case 8:
            case 21:
            case 22:
                if (!CommonUtils.isNight()) {
                    weatherBgId = R.drawable.day_ww_06_07_08_21_22;
                } else {
                    weatherBgId = R.drawable.night_ww_06_07_08_21_22;
                }
                break;
            case 9:
            case 10:
            case 11:
            case 12:
            case 19:
            case 23:
            case 24:
            case 25:
                if (!CommonUtils.isNight()) {
                    weatherBgId = R.drawable.day_ww_09_10_11_12_19_23_24_25;
                } else {
                    weatherBgId = R.drawable.night_ww_09_10_11_12_19_23_24_25;
                }
                break;
            case 13:
            case 14:
            case 15:
            case 16:
            case 26:
            case 27:
            case 28:
                if (!CommonUtils.isNight()) {
                    weatherBgId = R.drawable.day_ww_13_14_15_16_26_27_28;
                } else {
                    weatherBgId = R.drawable.night_ww_13_14_15_16_26_27_28;
                }
                break;
            case 18:
            case 20:
            case 29:
            case 30:
            case 31:
            case 53:
                if (!CommonUtils.isNight()) {
                    weatherBgId = R.drawable.day_ww_18_20_29_30_31_53;
                } else {
                    weatherBgId = R.drawable.night_ww_18_20_29_30_31_53;
                }
                break;
            default:
                weatherBgId = R.drawable.weather_bg_null;
        }
        return weatherBgId;
    }
}
