package com.graduationproject.personnalfinancialmanagement.customerservice.weatherapp.config.weather;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by Administrator on 2015/10/15.
 */
public class WeatherResult implements Serializable {
    private WeatherCurrentDetail sk;
    private WeatherTodayDetail today;
    private Map<String,WeatherFutureDetail> future;

    public WeatherCurrentDetail getSk() {
        return sk;
    }

    public void setSk(WeatherCurrentDetail sk) {
        this.sk = sk;
    }

    public WeatherTodayDetail getToday() {
        return today;
    }

    public void setToday(WeatherTodayDetail today) {
        this.today = today;
    }

    public Map<String, WeatherFutureDetail> getFuture() {
        return future;
    }

    public void setFuture(Map<String, WeatherFutureDetail> future) {
        this.future = future;
    }
}
