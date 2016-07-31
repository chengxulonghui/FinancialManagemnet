package com.graduationproject.personnalfinancialmanagement.customerservice.gasStation.javabean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by longhui on 2016/5/24.
 */
public class GasStationResultListData implements Serializable {
    private List<GasStationData> data;

    public List<GasStationData> getData() {
        return data;
    }

    public void setData(List<GasStationData> data) {
        this.data = data;
    }
}
