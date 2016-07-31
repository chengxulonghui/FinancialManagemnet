package com.graduationproject.personnalfinancialmanagement.accounting.typeconfig;

/**
 * Created by longhui on 2016/5/13.
 */
public class IncomeCategory {
    private String categoryNum;
    private String categoryName;
    private int categoryIconId;

    public String getCategoryNum() {
        return categoryNum;
    }

    public void setCategoryNum(String categoryNum) {
        this.categoryNum = categoryNum;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public int getCategoryIconId() {
        return categoryIconId;
    }

    public void setCategoryIconId(int categoryIconId) {
        this.categoryIconId = categoryIconId;
    }
}
