package com.biaoyuan.transfernet.domain;


/**
 * Title  :我的取件-已取消
 * Create : 2017/6/14
 * Author ：enmaoFu
 */

public class MineTakeAlreadyInfo {


    /**
     * order_no : 5002321001497844490444
     * order_status : 10
     * order_goods_size : 20
     * order_goods_weight : 3
     * order_send_addr : 重庆市|重庆|武隆县|巷口镇|武隆县行政审批服务中心
     * order_goods_distance : 144.23
     * order_required_time : 1497952800000
     * order_time : 1497844490444
     * order_id : 180
     * staff_tephone : 15213478863
     */

    private String order_no;
    private int order_status;
    private int order_goods_size;
    private int order_goods_weight;
    private String order_send_addr;
    private double order_goods_distance;
    private long order_required_time;
    private long order_time;
    private int order_id;
    private long staff_tephone;

    public String getOrder_no() {
        return order_no;
    }

    public void setOrder_no(String order_no) {
        this.order_no = order_no;
    }

    public int getOrder_status() {
        return order_status;
    }

    public void setOrder_status(int order_status) {
        this.order_status = order_status;
    }

    public int getOrder_goods_size() {
        return order_goods_size;
    }

    public void setOrder_goods_size(int order_goods_size) {
        this.order_goods_size = order_goods_size;
    }

    public int getOrder_goods_weight() {
        return order_goods_weight;
    }

    public void setOrder_goods_weight(int order_goods_weight) {
        this.order_goods_weight = order_goods_weight;
    }

    public String getOrder_send_addr() {
        return order_send_addr;
    }

    public void setOrder_send_addr(String order_send_addr) {
        this.order_send_addr = order_send_addr;
    }

    public double getOrder_goods_distance() {
        return order_goods_distance;
    }

    public void setOrder_goods_distance(double order_goods_distance) {
        this.order_goods_distance = order_goods_distance;
    }

    public long getOrder_required_time() {
        return order_required_time;
    }

    public void setOrder_required_time(long order_required_time) {
        this.order_required_time = order_required_time;
    }

    public long getOrder_time() {
        return order_time;
    }

    public void setOrder_time(long order_time) {
        this.order_time = order_time;
    }

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public long getStaff_tephone() {
        return staff_tephone;
    }

    public void setStaff_tephone(long staff_tephone) {
        this.staff_tephone = staff_tephone;
    }
}
