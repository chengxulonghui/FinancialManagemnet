package com.graduationproject.personnalfinancialmanagement.customerservice.trainTicket.javabean;

import java.util.List;

/**
 * Created by longhui on 2016/5/25.
 */
public class TrainStationResult {
    private String reason;
    private List<TrainStation> result;

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public List<TrainStation> getResult() {
        return result;
    }

    public void setResult(List<TrainStation> result) {
        this.result = result;
    }
}
