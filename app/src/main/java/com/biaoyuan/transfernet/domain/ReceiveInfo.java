package com.biaoyuan.transfernet.domain;

/**
 * Title  :
 * Create : 2017/5/9
 * Author ：chen
 */

public class ReceiveInfo {


    /**
     * adr : 重庆市|重庆|南岸区|天文街道|长生轻轨站(公交站)
     * area : 南岸区
     * areaName : 天文街道
     * ciry : 重庆
     * orderID : 206
     * orderReceiverName : 张小姐
     * orderTrackingCode : QMCS8989
     * orderUpdateTime : 1497951943658
     * province : 重庆市
     */

    private String adr;
    private String area;
    private String areaName;
    private String ciry;
    private int orderID;
    private String orderReceiverName;
    private String orderTrackingCode;
    private long orderUpdateTime;
    private String province;

    public String getAdr() {
        return adr;
    }

    public void setAdr(String adr) {
        this.adr = adr;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getCiry() {
        return ciry;
    }

    public void setCiry(String ciry) {
        this.ciry = ciry;
    }

    public int getOrderID() {
        return orderID;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public String getOrderReceiverName() {
        return orderReceiverName;
    }

    public void setOrderReceiverName(String orderReceiverName) {
        this.orderReceiverName = orderReceiverName;
    }

    public String getOrderTrackingCode() {
        return orderTrackingCode;
    }

    public void setOrderTrackingCode(String orderTrackingCode) {
        this.orderTrackingCode = orderTrackingCode;
    }

    public long getOrderUpdateTime() {
        return orderUpdateTime;
    }

    public void setOrderUpdateTime(long orderUpdateTime) {
        this.orderUpdateTime = orderUpdateTime;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }
}
