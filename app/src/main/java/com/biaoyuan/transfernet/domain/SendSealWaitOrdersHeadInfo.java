package com.biaoyuan.transfernet.domain;

/**
 * @title : 发件网点-》封包fragment recyclerview实体类
 * @author :enmaoFu
 * @create :2017/5/11
 */
public class SendSealWaitOrdersHeadInfo {

    /**
     * package_carrier_reward : 0
     * basic_name : 兴隆镇网点
     * package_status : 1
     * package_size : 25
     * publish_req_pickup_time : 1497239119063
     * package_weight : 1
     * package_id : 17
     */

    private double package_carrier_reward;
    private String basic_name;
    private int package_status;
    private int package_size;
    private long publish_req_pickup_time;
    private int package_weight;
    private int package_id;

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
