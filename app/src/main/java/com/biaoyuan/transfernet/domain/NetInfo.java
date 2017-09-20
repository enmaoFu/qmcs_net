package com.biaoyuan.transfernet.domain;

/**
 * Title  :网点切换
 * Create : 2017/5/26
 * Author ：chen
 */

public class NetInfo {


    /**
     * area : 武隆县
     * areaName : 巷口镇
     * basicId2 : 4
     * basicName : 巷口镇网点
     * city : 重庆
     * outBasicTelphone : 13996856866
     * whether : 0
     */

    private String area;
    private String areaName;
    private int basicId2;
    private String basicName;
    private String city;
    private long outBasicTelphone;
    private int whether;

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public int getBasicId2() {
        return basicId2;
    }

    public void setBasicId2(int basicId2) {
        this.basicId2 = basicId2;
    }

    public String getBasicName() {
        return basicName;
    }

    public void setBasicName(String basicName) {
        this.basicName = basicName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public long getOutBasicTelphone() {
        return outBasicTelphone;
    }

    public void setOutBasicTelphone(long outBasicTelphone) {
        this.outBasicTelphone = outBasicTelphone;
    }

    public int getWhether() {
        return whether;
    }

    public void setWhether(int whether) {
        this.whether = whether;
    }
}
