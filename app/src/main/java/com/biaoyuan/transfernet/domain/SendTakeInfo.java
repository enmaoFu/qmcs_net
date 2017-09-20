package com.biaoyuan.transfernet.domain;

/**
 * @title : 发件网点-》取件fragment recyclerview实体类
 * @author :enmaoFu
 * @create :2017/5/9
 */
public class SendTakeInfo {


    /**
     * createTime : 1496476775297
     * adr : 测试小区十五栋1单元1204
     * staffId : 1
     * orderTotalPrice : 33
     * orderID : 15
     * sendAddress : 北京市北京东城区北新桥街道测试小区十五栋1单元1204
     * staffPhone : 15123059793
     * basicLat : 29.3250999
     * orderStatus : 2
     * ciry : 北京
     * sendLng : 50.66555
     * goodsSize : 5
     * orderNo : Q2222222gj5m
     * area : 东城区
     * pickupDistance : 6104061
     * basicId : 4
     * areaName : 北新桥街道
     * sendLat : 78.255
     * orderSenderName : 赵六6
     * requiredTime : 1496476775297
     * province : 北京市
     * goodsWeight : 10
     * basicLng : 107.7490005
     * goodsType : 5
     */

    private long createTime;
    private String adr;
    private int staffId=0;
    private double orderTotalPrice;
    private int orderID;
    private String sendAddress;
    private long staffPhone;
    private double basicLat;
    private int orderStatus;
    private String ciry;
    private double sendLng;
    private int goodsSize;
    private String orderNo;
    private String area;
    private double pickupDistance;
    private int basicId;
    private String areaName;
    private double sendLat;
    private String orderSenderName;
    private long requiredTime;
    private String province;
    private int goodsWeight;
    private double basicLng;
    private int goodsType;


    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getAdr() {
        return adr;
    }

    public void setAdr(String adr) {
        this.adr = adr;
    }

    public int getStaffId() {
        return staffId;
    }

    public void setStaffId(int staffId) {
        this.staffId = staffId;
    }

    public double getOrderTotalPrice() {
        return orderTotalPrice;
    }

    public void setOrderTotalPrice(double orderTotalPrice) {
        this.orderTotalPrice = orderTotalPrice;
    }

    public int getOrderID() {
        return orderID;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public String getSendAddress() {
        return sendAddress;
    }

    public void setSendAddress(String sendAddress) {
        this.sendAddress = sendAddress;
    }

    public long getStaffPhone() {
        return staffPhone;
    }

    public void setStaffPhone(long staffPhone) {
        this.staffPhone = staffPhone;
    }

    public double getBasicLat() {
        return basicLat;
    }

    public void setBasicLat(double basicLat) {
        this.basicLat = basicLat;
    }

    public int getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(int orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getCiry() {
        return ciry;
    }

    public void setCiry(String ciry) {
        this.ciry = ciry;
    }

    public double getSendLng() {
        return sendLng;
    }

    public void setSendLng(double sendLng) {
        this.sendLng = sendLng;
    }

    public int getGoodsSize() {
        return goodsSize;
    }

    public void setGoodsSize(int goodsSize) {
        this.goodsSize = goodsSize;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public double getPickupDistance() {
        return pickupDistance;
    }

    public void setPickupDistance(double pickupDistance) {
        this.pickupDistance = pickupDistance;
    }

    public int getBasicId() {
        return basicId;
    }

    public void setBasicId(int basicId) {
        this.basicId = basicId;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public double getSendLat() {
        return sendLat;
    }

    public void setSendLat(double sendLat) {
        this.sendLat = sendLat;
    }

    public String getOrderSenderName() {
        return orderSenderName;
    }

    public void setOrderSenderName(String orderSenderName) {
        this.orderSenderName = orderSenderName;
    }

    public long getRequiredTime() {
        return requiredTime;
    }

    public void setRequiredTime(long requiredTime) {
        this.requiredTime = requiredTime;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public int getGoodsWeight() {
        return goodsWeight;
    }

    public void setGoodsWeight(int goodsWeight) {
        this.goodsWeight = goodsWeight;
    }

    public double getBasicLng() {
        return basicLng;
    }

    public void setBasicLng(double basicLng) {
        this.basicLng = basicLng;
    }

    public int getGoodsType() {
        return goodsType;
    }

    public void setGoodsType(int goodsType) {
        this.goodsType = goodsType;
    }
}
