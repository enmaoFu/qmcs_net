package com.biaoyuan.transfernet.domain;

import com.and.yzy.frame.adapter.recyclerview.entity.SectionEntity;

/**
 * @title : 发件网点-》发布fragment recyclerview实体类
 * @author :enmaoFu
 * @create :2017/5/9
 */
public class SendSendHeadInfo extends SectionEntity<SendSendItemInfo> {

    private String basicCode;

    private String basicName;



    public SendSendHeadInfo(boolean isHeader, String header) {
        super(isHeader, header);
    }

    public SendSendHeadInfo(SendSendItemInfo sendSendHeadInfo) {
        super(sendSendHeadInfo);
    }

    public String getBasicCode() {
        return basicCode;
    }

    public void setBasicCode(String basicCode) {
        this.basicCode = basicCode;
    }

    public String getBasicName() {
        return basicName;
    }

    public void setBasicName(String basicName) {
        this.basicName = basicName;
    }
}
