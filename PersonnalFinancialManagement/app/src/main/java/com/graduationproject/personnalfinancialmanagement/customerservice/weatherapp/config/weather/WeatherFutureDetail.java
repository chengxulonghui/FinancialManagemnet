package com.graduationproject.personnalfinancialmanagement.customerservice.weatherapp.config.weather;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by Administrator on 2015/10/15.
 */
public class WeatherFutureDetail implements Serializable{
    private String temperature;
    private String weather;
    private Map<String,String> weather_id;
    private String wind;
    private String week;
    private String date;

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public Map<String, String> getWeather_id() {
        return weather_id;
    }

    public void setWeather_id(Map<String, String> weather_id) {
        this.weather_id = weather_id;
    }

    public String getWind() {
        return wind;
    }

    public void setWind(String wind) {
        this.wind = wind;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
