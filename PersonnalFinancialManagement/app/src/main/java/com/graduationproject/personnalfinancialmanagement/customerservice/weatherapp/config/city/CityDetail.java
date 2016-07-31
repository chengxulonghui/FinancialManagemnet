package com.graduationproject.personnalfinancialmanagement.customerservice.weatherapp.config.city;

import java.io.Serializable;

/**
 * Created by Administrator on 2015/10/15.
 */
public class CityDetail implements Serializable{
    private String id;
    private String province;
    private String city;
    private String district;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }
}
