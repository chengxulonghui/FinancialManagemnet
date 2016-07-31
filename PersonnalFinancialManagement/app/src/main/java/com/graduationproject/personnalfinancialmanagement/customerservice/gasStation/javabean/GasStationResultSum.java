package com.graduationproject.personnalfinancialmanagement.customerservice.gasStation.javabean;

/**
 * Created by longhui on 2016/5/24.
 */
public class GasStationResultSum {
    private String resultcode;
    private String reason;
    private GasStationResultListData result;
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

    public GasStationResultListData getResult() {
        return result;
    }

    public void setResult(GasStationResultListData result) {
        this.result = result;
    }

    public String getError_code() {
        return error_code;
    }

    public void setError_code(String error_code) {
        this.error_code = error_code;
    }
}
