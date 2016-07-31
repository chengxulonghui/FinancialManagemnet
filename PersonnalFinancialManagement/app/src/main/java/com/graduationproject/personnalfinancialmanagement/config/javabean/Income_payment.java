package com.graduationproject.personnalfinancialmanagement.config.javabean;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobDate;

/**
 * Created by longhui on 2016/5/18.
 */
public class Income_payment extends BmobObject {
    private String userId;//用户Id
    private Integer dataType;//数据类型，0：支出，1:收入
    private String dataTypeName;//数据类型的名字，支出or收入
    private Float money;//金额
    private Integer categoryNum;//类型编号
    private String categoryName;//类型名字
    private Integer subcategoryNum;//子类型编号（收入没有这一项）
    private String subcategoryName;//子类型名字（收入没有这一项）
    private BmobDate date;//时间
    private Boolean reimbursed;//报销(收入没有这一项)
    private String remark;//备注
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Integer getDataType() {
        return dataType;
    }

    public void setDataType(Integer dataType) {
        this.dataType = dataType;
    }

    public String getDataTypeName() {
        return dataTypeName;
    }

    public void setDataTypeName(String dataTypeName) {
        this.dataTypeName = dataTypeName;
    }

    public Float getMoney() {
        return money;
    }

    public void setMoney(Float money) {
        this.money = money;
    }

    public Integer getCategoryNum() {
        return categoryNum;
    }

    public void setCategoryNum(Integer categoryNum) {
        this.categoryNum = categoryNum;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Integer getSubcategoryNum() {
        return subcategoryNum;
    }

    public void setSubcategoryNum(Integer subcategoryNum) {
        this.subcategoryNum = subcategoryNum;
    }

    public String getSubcategoryName() {
        return subcategoryName;
    }

    public void setSubcategoryName(String subcategoryName) {
        this.subcategoryName = subcategoryName;
    }

    public BmobDate getDate() {
        return date;
    }

    public void setDate(BmobDate date) {
        this.date = date;
    }

    public Boolean getReimbursed() {
        return reimbursed;
    }

    public void setReimbursed(Boolean reimbursed) {
        this.reimbursed = reimbursed;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
