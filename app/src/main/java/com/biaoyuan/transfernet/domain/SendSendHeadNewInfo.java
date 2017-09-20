package com.biaoyuan.transfernet.domain;

import java.util.List;

/**
 * Created by Administrator on 2017/6/7.
 */

public class SendSendHeadNewInfo {


    /**
     * basicCode : ac21f1d0-a9ca-46d7-9658-2600e827f426
     * childList : [{"deliveryUpdateTime":"1496625452024","orderTrackingCode":"QMCS10001"},{"deliveryUpdateTime":"1496729590472","orderTrackingCode":"QMCS1001"},{"deliveryUpdateTime":"1496733414731","orderTrackingCode":"QMCS1002"}]
     * basicName : 兴隆镇网点
     */

    private String basicCode;
    private String basicName;
    private List<ChildListBean> childList;

    public String getBasicCode() {
        return basicCode;
    }

    public void setBasicCode(String basicCode) {
        this.basicCode = basicCode;
    }

    public String getBasicName() {
        return basicName;
    }

    public void setBasicName(String basicName) {
        this.basicName = basicName;
    }

    public List<ChildListBean> getChildList() {
        return childList;
    }

    public void setChildList(List<ChildListBean> childList) {
        this.childList = childList;
    }

    public static class ChildListBean {
        /**
         * deliveryUpdateTime : 1496625452024
         * orderTrackingCode : QMCS10001
         */

        private String deliveryUpdateTime;
        private String orderTrackingCode;

        public String getDeliveryUpdateTime() {
            return deliveryUpdateTime;
        }

        public void setDeliveryUpdateTime(String deliveryUpdateTime) {
            this.deliveryUpdateTime = deliveryUpdateTime;
        }

        public String getOrderTrackingCode() {
            return orderTrackingCode;
        }

        public void setOrderTrackingCode(String orderTrackingCode) {
            this.orderTrackingCode = orderTrackingCode;
        }
    }
}
