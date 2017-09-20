package com.biaoyuan.transfernet.domain;

/**
 * Title  :
 * Create : 2017/5/10
 * Author ：chen
 */

public class ThreeNoSendInfo {


    /**
     * orderId : 61
     * orderReceiveAddr : 北京市|北京|海淀区|上地街道|西二旗(地铁站)
     * orderRequiredTime : 1497341653132
     * orderTrackingCode :
     */

    private int orderId;
    private String orderReceiveAddr;
    private long orderRequiredTime;
    private String orderTrackingCode;

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getOrderReceiveAddr() {
        return orderReceiveAddr;
    }

    public void setOrderReceiveAddr(String orderReceiveAddr) {
        this.orderReceiveAddr = orderReceiveAddr;
    }

    public long getOrderRequiredTime() {
        return orderRequiredTime;
    }

    public void setOrderRequiredTime(long orderRequiredTime) {
        this.orderRequiredTime = orderRequiredTime;
    }

    public String getOrderTrackingCode() {
        return orderTrackingCode;
    }

    public void setOrderTrackingCode(String orderTrackingCode) {
        this.orderTrackingCode = orderTrackingCode;
    }
}
