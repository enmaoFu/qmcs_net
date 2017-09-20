package com.biaoyuan.transfernet.domain;

/**
 * Title  :
 * Create : 2017/5/10
 * Author ：chen
 */

public class MineSendWaitInfo {


    /**
     * order_no : 5002321001499915185676
     * order_receive_addr : 云南省|昭通市|鲁甸县|文屏镇|文屏镇中学|呜呜呜
     * order_third_express_name : 申通快递
     * context : 已签收,签收人是:【俊杰超市代收】
     * order_update_time : 1499915258910
     * order_id : 684
     * order_third_express_code : 3333159578604
     */

    private String order_no;
    private String order_receive_addr;
    private String order_third_express_name;
    private String context;
    private String order_update_time;
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

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getOrder_update_time() {
        return order_update_time;
    }

    public void setOrder_update_time(String order_update_time) {
        this.order_update_time = order_update_time;
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
