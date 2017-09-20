package com.biaoyuan.transfernet.domain;

/**
 * Created by Administrator on 2017/6/19.
 */

public class MineCollectPackageInfo {


    /**
     * basic_address : 重庆市武隆县港口镇芙蓉西路
     * user_login_name : shenyuyu
     * package_status : 4
     * carry_pickup_time : 0
     * package_id : 44
     */

    private String basic_address;
    private String user_login_name;
    private String package_status;
    private String carry_pickup_time;
    private String package_id;

    public String getBasic_address() {
        return basic_address;
    }

    public void setBasic_address(String basic_address) {
        this.basic_address = basic_address;
    }

    public String getUser_login_name() {
        return user_login_name;
    }

    public void setUser_login_name(String user_login_name) {
        this.user_login_name = user_login_name;
    }

    public String getPackage_status() {
        return package_status;
    }

    public void setPackage_status(String package_status) {
        this.package_status = package_status;
    }

    public String getCarry_pickup_time() {
        return carry_pickup_time;
    }

    public void setCarry_pickup_time(String carry_pickup_time) {
        this.carry_pickup_time = carry_pickup_time;
    }

    public String getPackage_id() {
        return package_id;
    }

    public void setPackage_id(String package_id) {
        this.package_id = package_id;
    }
}
