package com.biaoyuan.transfernet.domain;

/**
 * Title  :
 * Create : 2017/5/10
 * Author ：chen
 */

public class ThreeSendWaitInfo {


    /**
     * orderGoodsType : 5
     * orderId : 1
     * orderReceiveAddr : 花园小区
     * orderRequiredTime : 1496476775297
     * orderTrackingCode : QMCS10012011
     */

    private int orderGoodsType;
    private int orderId;
    private String orderReceiveAddr;
    private long orderRequiredTime;
    private String orderTrackingCode;

    public int getOrderGoodsType() {
        return orderGoodsType;
    }

    public void setOrderGoodsType(int orderGoodsType) {
        this.orderGoodsType = orderGoodsType;
    }

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
