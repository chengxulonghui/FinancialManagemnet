package com.graduationproject.personnalfinancialmanagement.customerservice.gasStation.javabean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by longhui on 2016/5/24.
 */
public class GasStationData implements Serializable {
    private String id;
    private String name;
    private String area;
    private String areaname;
    private String address;
    private String brandname;
    private String type;
    private String discount;
    private String exhaust;
    private String position;
    private String lon;
    private String lat;
    private List<TypeAndPrice> price;
    private List<GasNameAndPrice> gastprice;
    private String fwlsmc;
    private String distance;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getAreaname() {
        return areaname;
    }

    public void setAreaname(String areaname) {
        this.areaname = areaname;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBrandname() {
        return brandname;
    }

    public void setBrandname(String brandname) {
        this.brandname = brandname;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getExhaust() {
        return exhaust;
    }

    public void setExhaust(String exhaust) {
        this.exhaust = exhaust;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public List<TypeAndPrice> getPrice() {
        return price;
    }

    public void setPrice(List<TypeAndPrice> price) {
        this.price = price;
    }

    public List<GasNameAndPrice> getGastprice() {
        return gastprice;
    }

    public void setGastprice(List<GasNameAndPrice> gastprice) {
        this.gastprice = gastprice;
    }

    public String getFwlsmc() {
        return fwlsmc;
    }

    public void setFwlsmc(String fwlsmc) {
        this.fwlsmc = fwlsmc;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }
}
