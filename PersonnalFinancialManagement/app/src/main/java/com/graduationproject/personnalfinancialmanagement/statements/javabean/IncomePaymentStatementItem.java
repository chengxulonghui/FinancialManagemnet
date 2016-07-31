package com.graduationproject.personnalfinancialmanagement.statements.javabean;

import java.io.Serializable;

/**
 * Created by longhui on 2016/5/26.
 */
public class IncomePaymentStatementItem implements Serializable {
    private Integer dataType;
    private Integer CategoryNum;
    private String CategoryName;
    private Float moneySum;
    private Float percent;

    public Integer getDataType() {
        return dataType;
    }

    public void setDataType(Integer dataType) {
        this.dataType = dataType;
    }

    public Integer getCategoryNum() {
        return CategoryNum;
    }

    public void setCategoryNum(Integer categoryNum) {
        CategoryNum = categoryNum;
    }

    public String getCategoryName() {
        return CategoryName;
    }

    public void setCategoryName(String categoryName) {
        CategoryName = categoryName;
    }

    public Float getMoneySum() {
        return moneySum;
    }

    public void setMoneySum(Float moneySum) {
        this.moneySum = moneySum;
    }

    public Float getPercent() {
        return percent;
    }

    public void setPercent(Float percent) {
        this.percent = percent;
    }
}
