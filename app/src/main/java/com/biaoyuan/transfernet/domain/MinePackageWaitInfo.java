package com.biaoyuan.transfernet.domain;

/**
 * Title  :
 * Create : 2017/5/15
 * Author ：chen
 */

public class MinePackageWaitInfo {


    /**
     * user_telphone : 17723163877
     * package_code : QMWD201704030085
     * basic_name : 巷口镇网点
     * package_status : 4
     * package_id : 43
     */

    private String user_telphone;
    private String package_code;
    private String basic_name;
    private int package_status;
    private int package_id;

    public String getUser_telphone() {
        return user_telphone;
    }

    public void setUser_telphone(String user_telphone) {
        this.user_telphone = user_telphone;
    }

    public String getPackage_code() {
        return package_code;
    }

    public void setPackage_code(String package_code) {
        this.package_code = package_code;
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

    public int getPackage_id() {
        return package_id;
    }

    public void setPackage_id(int package_id) {
        this.package_id = package_id;
    }
}
