package com.biaoyuan.transfernet.domain;

/**
 * Title  :
 * Create : 2017/5/15
 * Author ：chen
 */

public class MinePackageAlreadyInfo {

    /**
     * package_code : null
     * carry_check_time : 0
     * basic_name : 巷口镇网点
     * package_id : 44
     */

    private String package_code;
    private String carry_check_time;
    private String basic_name;
    private int package_id;

    public String getPackage_code() {
        return package_code;
    }

    public void setPackage_code(String package_code) {
        this.package_code = package_code;
    }

    public String getCarry_check_time() {
        return carry_check_time;
    }

    public void setCarry_check_time(String carry_check_time) {
        this.carry_check_time = carry_check_time;
    }

    public String getBasic_name() {
        return basic_name;
    }

    public void setBasic_name(String basic_name) {
        this.basic_name = basic_name;
    }

    public int getPackage_id() {
        return package_id;
    }

    public void setPackage_id(int package_id) {
        this.package_id = package_id;
    }
}
