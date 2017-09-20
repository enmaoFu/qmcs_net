package com.biaoyuan.transfernet.domain;

/**
 * @title : 发件网点-》发布fragment ListView实体类
 * @author :enmaoFu
 * @create :2017/5/9
 */
public class SendSendItemInfo {
    /**
     * deliveryUpdateTime : 1496625452024
     * orderTrackingCode : QMCS10001
     */

    private String deliveryUpdateTime;
    private String orderTrackingCode;

    public boolean isLast() {
        return isLast;
    }

    public void setLast(boolean last) {
        isLast = last;
    }

    private boolean isLast;

    public SendSendItemInfo(boolean isLast) {
        this.isLast = isLast;
    }

    public SendSendItemInfo() {
    }


    public String getDeliveryUpdateTime() {
        return deliveryUpdateTime;
    }

    public void setDeliveryUpdateTime(String deliveryUpdateTime) {
        this.deliveryUpdateTime = deliveryUpdateTime;
    }

    public String getOrderTrackingCode() {
        return orderTrackingCode;
    }

    public void setOrderTrackingCode(String orderTrackingCode) {
        this.orderTrackingCode = orderTrackingCode;
    }
}
