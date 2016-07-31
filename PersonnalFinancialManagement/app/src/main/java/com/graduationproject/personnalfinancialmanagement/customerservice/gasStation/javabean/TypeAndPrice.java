package com.graduationproject.personnalfinancialmanagement.customerservice.gasStation.javabean;

import java.io.Serializable;

/**
 * Created by longhui on 2016/5/24.
 */
public class TypeAndPrice implements Serializable {
    private String type;
    private String price;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
