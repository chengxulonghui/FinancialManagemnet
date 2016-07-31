package com.graduationproject.personnalfinancialmanagement.config.javabean;

import cn.bmob.v3.BmobObject;

/**
 * Created by longhui on 2016/5/21.
 */
public class MemoBMob extends BmobObject {
    private String userId;
    private String memoId;
    private String content;
    private String createDate;
    private String updateDate;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMemoId() {
        return memoId;
    }

    public void setMemoId(String memoId) {
        this.memoId = memoId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }
}
