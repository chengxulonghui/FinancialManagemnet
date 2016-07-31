package com.graduationproject.personnalfinancialmanagement.accounting.typeconfig;

import java.util.List;

/**
 * Created by longhui on 2016/5/13.
 */
public class PaymentCategory {
    private String categoryNum;
    private String categoryName;
    private List<PaymentSubcategory> subcategories;
    private int iconId;
    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryNum() {
        return categoryNum;
    }

    public void setCategoryNum(String categoryNum) {
        this.categoryNum = categoryNum;
    }

    public List<PaymentSubcategory> getSubcategories() {
        return subcategories;
    }

    public void setSubcategories(List<PaymentSubcategory> subcategories) {
        this.subcategories = subcategories;
    }

    public int getIconId() {
        return iconId;
    }

    public void setIconId(int iconId) {
        this.iconId = iconId;
    }
}
