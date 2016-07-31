package com.graduationproject.personnalfinancialmanagement.utils.http.volley;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.baidu.location.BDLocation;
import com.graduationproject.personnalfinancialmanagement.config.StringConfig;
import com.graduationproject.personnalfinancialmanagement.customerservice.gasStation.javabean.GasStationResultSum;
import com.graduationproject.personnalfinancialmanagement.customerservice.news.config.NewsListDataHolder;
import com.graduationproject.personnalfinancialmanagement.customerservice.trainTicket.javabean.TrainDetailListDataSum;
import com.graduationproject.personnalfinancialmanagement.customerservice.trainTicket.javabean.TrainPriceSum;
import com.graduationproject.personnalfinancialmanagement.customerservice.trainTicket.javabean.TrainStationResult;
import com.graduationproject.personnalfinancialmanagement.customerservice.weatherapp.config.city.CityResult;
import com.graduationproject.personnalfinancialmanagement.customerservice.weatherapp.config.weather.WeatherDataSum;
import com.graduationproject.personnalfinancialmanagement.utils.http.volley.request.GsonRequest;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by Administrator on 2015/11/9.
 */
public class VolleyHttpUtils {
    private RequestQueue mQueue;
    private String MY_WEATHER_KEY = "9a127396ea0762ab5a6d38dc54520b24";
    private String MY_GAS_STATION_KEY = "853769d70eed5bc497f0481a7bb1a961";
    private String MY_BASE_TRAIN_KEY = "c0aa5082d5dfcda1d306fa1b2658689a";
    private String baseCityUrl = "http://v.juhe.cn/weather/citys";
    private String baseWeatherUrl = "http://v.juhe.cn/weather/index";
    private String baseGasStationByCityUrl = "http://apis.juhe.cn/oil/region";
    private String baseGasStationByLonLatUrl = "http://apis.juhe.cn/oil/local";
    private String baseTrainStationUrl = "http://apis.juhe.cn/train/station.list.php";
    private String baseTrainDetailUrl = "http://apis.juhe.cn/train/ticket.cc.php";
    private String baseTrainPriceUrl = "http://apis.juhe.cn/train/ticket.price.php";

    public VolleyHttpUtils(Context context) {
        mQueue = Volley.newRequestQueue(context);
    }

    public void getCityListHttpRequest(Response.Listener<CityResult> myCityResponse, Response.ErrorListener myCityErrorListener) throws Exception {
        String httpCityUrl = baseCityUrl + "?" + "key=" + MY_WEATHER_KEY;
        GsonRequest gsonRequest = new GsonRequest(httpCityUrl, CityResult.class, myCityResponse, myCityErrorListener);
        mQueue.add(gsonRequest);
    }

    public void getWeatherInfoHttpRequest(String cityName, Response.Listener<WeatherDataSum> mWeatherResponse, Response.ErrorListener myWeatherErrorListener) throws Exception {
        try {
            cityName = URLEncoder.encode(cityName, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String httpWeatherUrl = baseWeatherUrl + "?" + "cityname=" + cityName + "&" + "key=" + MY_WEATHER_KEY;
        Log.e("TAG", httpWeatherUrl);
        GsonRequest gsonRequest = new GsonRequest(httpWeatherUrl, WeatherDataSum.class, mWeatherResponse, myWeatherErrorListener);
        mQueue.add(gsonRequest);
    }

    public void getNewsList(Response.Listener<NewsListDataHolder> mNewsListResponse, Response.ErrorListener mNewsListErrorListener) {
        String newsListUrl = StringConfig.NEWS_LIST_URL + "0" + "-" + "20" + ".html";
        Log.e("TAG", newsListUrl);
        GsonRequest gsonRequest = new GsonRequest(newsListUrl, NewsListDataHolder.class, mNewsListResponse, mNewsListErrorListener);
        mQueue.add(gsonRequest);
    }

    public void getMoreNewsList(int newsNum, Response.Listener<NewsListDataHolder> mNewsListResponse, Response.ErrorListener mNewsListErrorListener) {
        String newsListUrl = StringConfig.NEWS_LIST_URL + newsNum + "-" + "20" + ".html";
        Log.e("TAG", newsListUrl);
        GsonRequest gsonRequest = new GsonRequest(newsListUrl, NewsListDataHolder.class, mNewsListResponse, mNewsListErrorListener);
        mQueue.add(gsonRequest);
    }

    public void getGasStationByCityName(String cityName,
                                        Response.Listener<GasStationResultSum> gasStationResultSumListener,
                                        Response.ErrorListener gasStationErrorListener) {
        getGasStationByCityNameAndKeyword(cityName, "", gasStationResultSumListener, gasStationErrorListener);
    }

    public void getGasStationByCityNameAndKeyword(String cityName, String keyword,
                                                  Response.Listener<GasStationResultSum> gasStationResultSumListener,
                                                  Response.ErrorListener gasStationErrorListener) {
        String cityNameStr = null;
        String keywordStr = null;
        try {
            cityNameStr = URLEncoder.encode(cityName, "utf-8");
            keywordStr = URLEncoder.encode(keyword, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String url = baseGasStationByCityUrl + "?" + "key=" + MY_GAS_STATION_KEY + "&" + "format=2" +
                "&" + "city=" + cityNameStr;
        if (!TextUtils.isEmpty(keyword)) {
            StringBuilder sb = new StringBuilder(url);
            sb.append("&" + "keywords=" + keywordStr);
            url = sb.toString();
        }
        Log.e("VolleyHttpUtils", url);
        GsonRequest gsonRequest = new GsonRequest(url, GasStationResultSum.class, gasStationResultSumListener, gasStationErrorListener);
        mQueue.add(gsonRequest);
    }

    public void getGasStationByLonLat(double lon, double lat,
                                      Response.Listener<GasStationResultSum> gasStationResultSumListener,
                                      Response.ErrorListener gasStationErrorListener) {
        String url = baseGasStationByLonLatUrl + "?" + "key=" + MY_GAS_STATION_KEY + "&" + "lon=" + lon
                + "&" + "lat=" + lat
                + "&" + "format=2";
        Log.e("VolleyHttpUtils", url);
        GsonRequest gsonRequest = new GsonRequest(url, GasStationResultSum.class, gasStationResultSumListener, gasStationErrorListener);
        mQueue.add(gsonRequest);
    }

    public void getTrainStationList(Response.Listener<TrainStationResult> trainStationResultListener,
                                    Response.ErrorListener errorListener) {
        String url = baseTrainStationUrl + "?" + "key=" + MY_BASE_TRAIN_KEY;
        GsonRequest gsonRequest = new GsonRequest(url, TrainStationResult.class, trainStationResultListener, errorListener);
        mQueue.add(gsonRequest);
    }

    public void getTrainDetailList(String fromStation, String toStation, String dateStr, Response.Listener<TrainDetailListDataSum> trainDetailListDataSumListener,
                                   Response.ErrorListener errorListener) {
        String fromStationStr = "";
        String toStationStr = "";
        try {
            fromStationStr = URLEncoder.encode(fromStation, "utf-8");
            toStationStr = URLEncoder.encode(toStation, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String url = baseTrainDetailUrl + "?" + "key=" + MY_BASE_TRAIN_KEY + "&" + "from=" + fromStationStr + "&" + "to=" + toStationStr + "&" + "date=" + dateStr;
        GsonRequest gsonRequest = new GsonRequest(url, TrainDetailListDataSum.class, trainDetailListDataSumListener, errorListener);
        mQueue.add(gsonRequest);
    }

    public void getTrainPrice(String train_no, String from_station_no, String to_station_no, String date,
                              Response.Listener<TrainPriceSum> trainPriceSumListener, Response.ErrorListener errorListener) {
        String url = baseTrainPriceUrl + "?" + "key=" + MY_BASE_TRAIN_KEY + "&" +
                "train_no=" + train_no + "&" + "from_station_no=" + from_station_no + "&" +
                "to_station_no=" + to_station_no + "&" + "date=" + date;
        GsonRequest gsonRequest = new GsonRequest(url, TrainPriceSum.class, trainPriceSumListener, errorListener);
        mQueue.add(gsonRequest);
    }
}
