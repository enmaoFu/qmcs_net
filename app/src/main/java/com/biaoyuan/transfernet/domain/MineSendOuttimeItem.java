package com.biaoyuan.transfernet.domain;

/**
 * Title  :
 * Create : 2017/5/17
 * Author ：chen
 */

public class MineSendOuttimeItem {


    /**
     * order_no : 5002321001497843426270
     * order_goods_size : 20
     * order_goods_weight : 2
     * order_send_addr : 重庆市|重庆|武隆县|巷口镇|武隆县行政审批服务中心
     * order_required_time : 1497847021954
     * excep_id : 29
     * excep_record_time : 1497948001303
     * excep_handle_status : 1
     * excep_type : 4
     * order_id : 179
     * excep_rejection_reason : 5
     * delivery_update_time : 1499844199374
     * order_tracking_code : QMCS121212
     */

    private String order_no;
    private int order_goods_size;
    private int order_goods_weight;
    private String order_send_addr;
    private long order_required_time;
    private int excep_id;
    private long excep_record_time;
    private int excep_handle_status;
    private int excep_type;
    private int order_id;
    private int excep_rejection_reason;
    private long delivery_update_time;
    private String order_tracking_code;

    public String getOrder_no() {
        return order_no;
    }

    public void setOrder_no(String order_no) {
        this.order_no = order_no;
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

    public long getOrder_required_time() {
        return order_required_time;
    }

    public void setOrder_required_time(long order_required_time) {
        this.order_required_time = order_required_time;
    }

    public int getExcep_id() {
        return excep_id;
    }

    public void setExcep_id(int excep_id) {
        this.excep_id = excep_id;
    }

    public long getExcep_record_time() {
        return excep_record_time;
    }

    public void setExcep_record_time(long excep_record_time) {
        this.excep_record_time = excep_record_time;
    }

    public int getExcep_handle_status() {
        return excep_handle_status;
    }

    public void setExcep_handle_status(int excep_handle_status) {
        this.excep_handle_status = excep_handle_status;
    }

    public int getExcep_type() {
        return excep_type;
    }

    public void setExcep_type(int excep_type) {
        this.excep_type = excep_type;
    }

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public int getExcep_rejection_reason() {
        return excep_rejection_reason;
    }

    public void setExcep_rejection_reason(int excep_rejection_reason) {
        this.excep_rejection_reason = excep_rejection_reason;
    }

    public long getDelivery_update_time() {
        return delivery_update_time;
    }

    public void setDelivery_update_time(long delivery_update_time) {
        this.delivery_update_time = delivery_update_time;
    }

    public String getOrder_tracking_code() {
        return order_tracking_code;
    }

    public void setOrder_tracking_code(String order_tracking_code) {
        this.order_tracking_code = order_tracking_code;
    }
}
