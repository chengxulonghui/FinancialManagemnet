package com.graduationproject.personnalfinancialmanagement.accounting.typeconfig;

import com.graduationproject.personnalfinancialmanagement.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by longhui on 2016/5/13.
 */
public class CategoryManager {
    private volatile static CategoryManager singleton;
    private String[] paymentCategories = {"餐饮", "交通", "购物", "娱乐", "医教", "居家",
            "投资", "人情", "生意"};
    private int[] incomeCategoryIconIds = {R.drawable.ico_inc_1, R.drawable.ico_inc_7, R.drawable.ico_inc_2, R.drawable.ico_inc_8, R.drawable.ico_inc_11
            , R.drawable.ico_inc_11, R.drawable.ico_inc_11, R.drawable.ico_inc_9, R.drawable.ico_inc_11, R.drawable.ico_inc_11, R.drawable.ico_inc_15, R.drawable.ico_inc_11, R.drawable.ico_inc_6
            , R.drawable.ico_inc_11, R.drawable.ico_inc_4, R.drawable.ico_inc_5, R.drawable.ico_inc_10, R.drawable.ico_inc_3, R.drawable.ico_inc_12, R.drawable.ico_inc_11, R.drawable.ico_inc_11
            , R.drawable.ico_inc_11, R.drawable.ico_inc_13};
    private String[] incomeCategories = {"工资薪水", "奖金", "兼职外快", "福利补贴", "生活费",
            "公积金", "退款返款", "礼金", "红包", "赔付款", "漏记款", "报销款", "利息",
            "余额宝", "基金", "分红", "租金", "股票", "销售款", "应收款", "营业收入",
            "工程款", "其他"};
    private String[] incomeCategoryNum = {"10", "11", "12", "13", "14", "15", "16", "17",
            "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30",
            "31", "32"};
    private int[] categoryIconIds = {R.drawable.ico_exp_10, R.drawable.ico_exp_11,
            R.drawable.ico_exp_12, R.drawable.ico_exp_13,
            R.drawable.ico_exp_14, R.drawable.ico_exp_15,
            R.drawable.ico_exp_16, R.drawable.ico_exp_17,
            R.drawable.ico_exp_18};
    private String[] categoriesNums = {"0", "1", "2", "3", "4", "5", "6", "7", "8"};
    private String[][] subcategories = {
            {"早餐", "午餐", "晚餐", "饮料水果", "零食", "买菜原料", "夜宵", "油盐酱醋", "餐饮其他"},
            {"打车", "公交", "加油", "停车费", "地铁", "火车", "长途汽车", "过路过桥", "保养维修", "飞机", "车款车贷", "罚款赔偿", "车险", "自行车", "船舶", "驾照费用", "交通其他"},
            {"电子数码", "宝宝用品", "家具家纺", "报刊书籍", "电器", "珠宝首饰", "文具玩具", "保健用品", "摄影文印", "购物其他"},
            {"旅游度假", "网游电玩", "电影", "洗浴足浴", "运动健身", "卡拉OK", "茶酒咖啡", "歌舞演出", "电视", "花鸟宠物", "麻将棋牌", "聚会玩乐", "娱乐其他"},
            {"医疗保健", "挂号门诊", "养生保健", "住院费", "养老院", "学杂教材", "培训考试", "家教补习", "学费", "幼儿教育", "出国留学", "助学贷款", "医教其他"},
            {"手机电话", "房款房贷", "水电燃气", "美容美发", "住宿房租", "材料建材", "电脑宽带", "快速邮政", "物业", "家政服务", "生活费", "婚庆摄影", "漏记款", "保险费", "消费贷款", "税费手续费", "生活其他"},
            {"股票", "基金", "理财产品", "余额宝", "银行存款", "保险", "P2P", "证劵期货", "出资", "贵金属", "投资贷款", "外汇", "收藏品", "利息支出", "投资其他"},
            {"礼金红包", "物品", "请客", "代付款", "孝敬", "给予", "慈善捐款", "人情其他"},
            {"进货采购", "人工支出", "材料辅料", "工程付款", "交通运输", "运营费", "会务费", "办公费用", "营销广告", "店面租金", "注册登记", "生意其他"},
    };
    private List<PaymentCategory> paymentCategoryList;
    private List<IncomeCategory> incomeCategoryList;

    private CategoryManager() {
        initData();
    }

    public static CategoryManager getSingleton() {
        if (singleton == null) {
            synchronized (CategoryManager.class) {
                if (singleton == null) {
                    singleton = new CategoryManager();
                }
            }
        }
        return singleton;
    }

    public void initData() {
        paymentCategoryList = new ArrayList<PaymentCategory>();
        for (int i = 0; i < paymentCategories.length; i++) {
            PaymentCategory paymentCategory = new PaymentCategory();
            paymentCategory.setCategoryNum(categoriesNums[i]);
            paymentCategory.setCategoryName(paymentCategories[i]);
            paymentCategory.setIconId(categoryIconIds[i]);
            List<PaymentSubcategory> subcategoryList = new ArrayList<PaymentSubcategory>();
            for (int j = 0; j < subcategories[i].length; j++) {
                PaymentSubcategory paymentSubcategory = new PaymentSubcategory();
                paymentSubcategory.setSubcategoryNum(j + "");
                paymentSubcategory.setSubcategoryName(subcategories[i][j]);
                subcategoryList.add(paymentSubcategory);
            }
            paymentCategory.setSubcategories(subcategoryList);
            paymentCategoryList.add(paymentCategory);
        }
        incomeCategoryList = new ArrayList<IncomeCategory>();
        for (int i = 0; i < incomeCategoryNum.length; i++) {
            IncomeCategory incomeCategory = new IncomeCategory();
            incomeCategory.setCategoryIconId(incomeCategoryIconIds[i]);
            incomeCategory.setCategoryNum(incomeCategoryNum[i]);
            incomeCategory.setCategoryName(incomeCategories[i]);
            incomeCategoryList.add(incomeCategory);
        }
    }

    public List<PaymentCategory> getPaymentCategoryList() {
        return paymentCategoryList;
    }

    public List<IncomeCategory> getIncomeCategoryList() {
        return incomeCategoryList;
    }

    public int getIncomeCategoryIconIdByCategoryNum(String categoryNum) {
        for (IncomeCategory incomeCategory : incomeCategoryList) {
            if (incomeCategory.getCategoryNum().equals(categoryNum)) {
                return incomeCategory.getCategoryIconId();
            }
        }
        return -1;
    }

    public int getPaymentCategoryIconIdByCategoryNum(String categoryNum) {
        for (PaymentCategory paymentCategory : paymentCategoryList) {
            if (paymentCategory.getCategoryNum().equals(categoryNum)) {
                return paymentCategory.getIconId();
            }
        }
        return -1;
    }

    public IncomeCategory getIncomeCategoryByCategoryNum(String categoryNum) {
        for (IncomeCategory incomeCategory : incomeCategoryList) {
            if (incomeCategory.getCategoryNum().equals(categoryNum)) {
                return incomeCategory;
            }
        }
        return null;
    }

    public PaymentCategory getPaymentCategoryByCategoryNum(String categoryNum) {
        for (PaymentCategory paymentCategory : paymentCategoryList) {
            if (paymentCategory.getCategoryNum().equals(categoryNum)) {
                return paymentCategory;
            }
        }
        return null;
    }

    public PaymentSubcategory getPaymentSubCategoryByCategoryNum(PaymentCategory paymentCategory, String subcategoryNum) {
        for (PaymentSubcategory paymentSubcategory : paymentCategory.getSubcategories()) {
            if (paymentSubcategory.getSubcategoryNum().equals(subcategoryNum)) {
                return paymentSubcategory;
            }
        }
        return null;
    }
}
