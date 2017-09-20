package com.biaoyuan.transfernet.domain;

/**
 * Title  :
 * Create : 2017/5/17
 * Author ：chen
 */

public class MineHaveSendItem {

    /**
     * order_status : 8
     * order_confirm_staff : 7
     * order_receive_addr : 重庆市|重庆|沙坪坝区|虎溪街道|大学城地铁站1号口
     * order_id : 184
     * delivery_update_time : 1497855081812
     * order_tracking_code : QMCS
     */

    private int order_status;
    private int order_confirm_staff;
    private String order_receive_addr;
    private int order_id;
    private long delivery_update_time;
    private String order_tracking_code;

    public int getOrder_status() {
        return order_status;
    }

    public void setOrder_status(int order_status) {
        this.order_status = order_status;
    }

    public int getOrder_confirm_staff() {
        return order_confirm_staff;
    }

    public void setOrder_confirm_staff(int order_confirm_staff) {
        this.order_confirm_staff = order_confirm_staff;
    }

    public String getOrder_receive_addr() {
        return order_receive_addr;
    }

    public void setOrder_receive_addr(String order_receive_addr) {
        this.order_receive_addr = order_receive_addr;
    }

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
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
