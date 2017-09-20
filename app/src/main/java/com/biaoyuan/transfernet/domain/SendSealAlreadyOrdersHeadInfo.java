package com.biaoyuan.transfernet.domain;

/**
 * @title : 发件网点-》封包fragment recyclerview实体类
 * @author :enmaoFu
 * @create :2017/5/11
 */
public class SendSealAlreadyOrdersHeadInfo{


    /**
     * user_telphone : 15213478863
     * package_carrier_reward : 30
     * basic_name : 兴隆镇网点
     * package_status : 2
     * package_size : 25
     * basic_code : ac21f1d0-a9ca-46d7-9658-2600e827f426
     * publish_req_pickup_time : 1497237711049
     * package_weight : 1
     * package_id : 16
     */

    private long user_telphone;
    private double package_carrier_reward;
    private String basic_name;
    private int package_status;
    private int package_size;
    private String basic_code;
    private long publish_req_pickup_time;
    private int package_weight;
    private int package_id;

    public long getUser_telphone() {
        return user_telphone;
    }

    public void setUser_telphone(long user_telphone) {
        this.user_telphone = user_telphone;
    }

    public double getPackage_carrier_reward() {
        return package_carrier_reward;
    }

    public void setPackage_carrier_reward(double package_carrier_reward) {
        this.package_carrier_reward = package_carrier_reward;
    }

    public String getBasic_name() {
        return basic_name;
    }

    public void setBasic_name(String basic_name) {
        this.basic_name = basic_name;
    }

    public int getPackage_status() {
        return package_status;
    }

    public void setPackage_status(int package_status) {
        this.package_status = package_status;
    }

    public int getPackage_size() {
        return package_size;
    }

    public void setPackage_size(int package_size) {
        this.package_size = package_size;
    }

    public String getBasic_code() {
        return basic_code;
    }

    public void setBasic_code(String basic_code) {
        this.basic_code = basic_code;
    }

    public long getPublish_req_pickup_time() {
        return publish_req_pickup_time;
    }

    public void setPublish_req_pickup_time(long publish_req_pickup_time) {
        this.publish_req_pickup_time = publish_req_pickup_time;
    }

    public int getPackage_weight() {
        return package_weight;
    }

    public void setPackage_weight(int package_weight) {
        this.package_weight = package_weight;
    }

    public int getPackage_id() {
        return package_id;
    }

    public void setPackage_id(int package_id) {
        this.package_id = package_id;
    }
}
