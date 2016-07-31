package com.graduationproject.personnalfinancialmanagement.customerservice.trainTicket.javabean;

import java.io.Serializable;

/**
 * Created by longhui on 2016/5/25.
 */
public class TrainDetailSum implements Serializable{
    private TrainDetail queryLeftNewDTO;
    private String secretStr;
    private String buttonTextInfo;

    public TrainDetail getQueryLeftNewDTO() {
        return queryLeftNewDTO;
    }

    public void setQueryLeftNewDTO(TrainDetail queryLeftNewDTO) {
        this.queryLeftNewDTO = queryLeftNewDTO;
    }

    public String getSecretStr() {
        return secretStr;
    }

    public void setSecretStr(String secretStr) {
        this.secretStr = secretStr;
    }

    public String getButtonTextInfo() {
        return buttonTextInfo;
    }

    public void setButtonTextInfo(String buttonTextInfo) {
        this.buttonTextInfo = buttonTextInfo;
    }
}
