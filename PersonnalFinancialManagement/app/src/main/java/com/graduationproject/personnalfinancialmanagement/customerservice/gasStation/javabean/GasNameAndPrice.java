package com.graduationproject.personnalfinancialmanagement.customerservice.gasStation.javabean;

import java.io.Serializable;

/**
 * Created by longhui on 2016/5/24.
 */
public class GasNameAndPrice implements Serializable {
    private String name;
    private String price;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
