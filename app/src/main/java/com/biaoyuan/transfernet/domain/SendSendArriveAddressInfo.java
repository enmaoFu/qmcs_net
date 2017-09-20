package com.biaoyuan.transfernet.domain;

/**
 * @author :enmaoFu
 * @title : 发布-》发布到到达地址recyclerview实体类
 * @create :2017/5/12
 */
public class SendSendArriveAddressInfo {
    /**
     * order_receive_addr : 花园小区
     * basic_code : ac21f1d0-a9ca-46d7-9658-2600e827f426
     * delivery_update_time : 1496625452024
     * order_id : 1
     * order_tracking_code : QMCS10001
     * basic_id : 3
     */

    private String order_receive_addr;
    private String basic_code;
    private String delivery_update_time;
    private long order_id;
    private String order_tracking_code;
    private long basic_id;
    private boolean isSelect;

    public String getOrder_receive_addr() {
        return order_receive_addr;
    }

    public void setOrder_receive_addr(String order_receive_addr) {
        this.order_receive_addr = order_receive_addr;
    }

    public String getBasic_code() {
        return basic_code;
    }

    public void setBasic_code(String basic_code) {
        this.basic_code = basic_code;
    }

    public String getDelivery_update_time() {
        return delivery_update_time;
    }

    public void setDelivery_update_time(String delivery_update_time) {
        this.delivery_update_time = delivery_update_time;
    }

    public long getOrder_id() {
        return order_id;
    }

    public void setOrder_id(long order_id) {
        this.order_id = order_id;
    }

    public String getOrder_tracking_code() {
        return order_tracking_code;
    }

    public void setOrder_tracking_code(String order_tracking_code) {
        this.order_tracking_code = order_tracking_code;
    }

    public long getBasic_id() {
        return basic_id;
    }

    public void setBasic_id(long basic_id) {
        this.basic_id = basic_id;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }
}
