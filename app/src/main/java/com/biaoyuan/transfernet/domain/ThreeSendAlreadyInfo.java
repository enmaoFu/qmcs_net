package com.biaoyuan.transfernet.domain;

/**
 * Title  :
 * Create : 2017/5/10
 * Author ：chen
 */

public class ThreeSendAlreadyInfo {


    /**
     * order_no : 5002321001497929844747
     * order_receive_addr : 重庆市|重庆|南岸区|天文街道|长生轻轨站(公交站)
     * order_third_express_name : null
     * order_update_time : 1497929907057
     * time : null
     * order_id : 203
     * order_third_express_code : null
     */

    private String order_no;
    private String order_receive_addr;
    private String order_third_express_name;
    private String order_update_time;
    private long time;
    private String order_id;
    private String order_third_express_code;

    public String getOrder_no() {
        return order_no;
    }

    public void setOrder_no(String order_no) {
        this.order_no = order_no;
    }

    public String getOrder_receive_addr() {
        return order_receive_addr;
    }

    public void setOrder_receive_addr(String order_receive_addr) {
        this.order_receive_addr = order_receive_addr;
    }

    public String getOrder_third_express_name() {
        return order_third_express_name;
    }

    public void setOrder_third_express_name(String order_third_express_name) {
        this.order_third_express_name = order_third_express_name;
    }

    public String getOrder_update_time() {
        return order_update_time;
    }

    public void setOrder_update_time(String order_update_time) {
        this.order_update_time = order_update_time;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getOrder_third_express_code() {
        return order_third_express_code;
    }

    public void setOrder_third_express_code(String order_third_express_code) {
        this.order_third_express_code = order_third_express_code;
    }
}
