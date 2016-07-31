package com.graduationproject.personnalfinancialmanagement.config.javabean;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

import cn.bmob.v3.datatype.BmobDate;

/**
 * Created by longhui on 2016/5/27.
 */
public class QuickRecordModel extends DataSupport implements Serializable {
    private int id;
    private String userId;//用户Id
    private String name;
    private int dataType;//数据类型，0：支出，1:收入
    private int categoryNum;//类型编号
    private int subcategoryNum;//子类型编号（收入没有这一项）
    private int reimbursed;//报销(收入没有这一项)0:已报销 1：未报销

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDataType() {
        return dataType;
    }

    public void setDataType(int dataType) {
        this.dataType = dataType;
    }

    public int getCategoryNum() {
        return categoryNum;
    }

    public void setCategoryNum(int categoryNum) {
        this.categoryNum = categoryNum;
    }

    public int getSubcategoryNum() {
        return subcategoryNum;
    }

    public void setSubcategoryNum(int subcategoryNum) {
        this.subcategoryNum = subcategoryNum;
    }

    public int getReimbursed() {
        return reimbursed;
    }

    public void setReimbursed(int reimbursed) {
        this.reimbursed = reimbursed;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
