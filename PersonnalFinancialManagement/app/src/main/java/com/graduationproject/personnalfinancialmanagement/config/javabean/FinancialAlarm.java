package com.graduationproject.personnalfinancialmanagement.config.javabean;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

import cn.bmob.v3.datatype.BmobDate;

/**
 * Created by longhui on 2016/5/23.
 */
public class FinancialAlarm extends DataSupport implements Serializable {
    private int id;
    private String userId;//用户Id
    private int dataType;//数据类型，0：支出，1:收入
    private String dataTypeName;//数据类型的名字，支出or收入
    private float money;//金额
    private int categoryNum;//类型编号
    private String categoryName;//类型名字
    private int subcategoryNum;//子类型编号（收入没有这一项）
    private String subcategoryName;//子类型名字（收入没有这一项）
    private String date;//时间
    private boolean reimbursed;//报销(收入没有这一项)
    private String remark;//备注
    private int isAlarm;//是否已经提醒 0:false 1:true
    private int isHandle;//是否已经处理 0:false 1:true
    private int isObsolete;//是否已经过时 0::false 1:true
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getDataType() {
        return dataType;
    }

    public void setDataType(int dataType) {
        this.dataType = dataType;
    }

    public String getDataTypeName() {
        return dataTypeName;
    }

    public void setDataTypeName(String dataTypeName) {
        this.dataTypeName = dataTypeName;
    }

    public float getMoney() {
        return money;
    }

    public void setMoney(float money) {
        this.money = money;
    }

    public int getCategoryNum() {
        return categoryNum;
    }

    public void setCategoryNum(int categoryNum) {
        this.categoryNum = categoryNum;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public int getSubcategoryNum() {
        return subcategoryNum;
    }

    public void setSubcategoryNum(int subcategoryNum) {
        this.subcategoryNum = subcategoryNum;
    }

    public String getSubcategoryName() {
        return subcategoryName;
    }

    public void setSubcategoryName(String subcategoryName) {
        this.subcategoryName = subcategoryName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isReimbursed() {
        return reimbursed;
    }

    public void setReimbursed(boolean reimbursed) {
        this.reimbursed = reimbursed;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getIsAlarm() {
        return isAlarm;
    }

    public void setIsAlarm(int isAlarm) {
        this.isAlarm = isAlarm;
    }

    public int getIsHandle() {
        return isHandle;
    }

    public void setIsHandle(int isHandle) {
        this.isHandle = isHandle;
    }

    public int getIsObsolete() {
        return isObsolete;
    }

    public void setIsObsolete(int isObsolete) {
        this.isObsolete = isObsolete;
    }
}
