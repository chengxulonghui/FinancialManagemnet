package com.graduationproject.personnalfinancialmanagement.customerservice.weatherapp.config.city;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2015/10/15.
 */
public class CityResult implements Serializable{
    private String resultcode;
    private String reason;
    private List<CityDetail> result;
    private String error_code;

    public String getResultcode() {
        return resultcode;
    }

    public void setResultcode(String resultcode) {
        this.resultcode = resultcode;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public List<CityDetail> getResult() {
        return result;
    }

    public void setResult(List<CityDetail> result) {
        this.result = result;
    }

    public String getError_code() {
        return error_code;
    }

    public void setError_code(String error_code) {
        this.error_code = error_code;
    }
}
