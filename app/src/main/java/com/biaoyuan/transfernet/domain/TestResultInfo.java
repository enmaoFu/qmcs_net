package com.biaoyuan.transfernet.domain;

import java.util.List;

/**
 * Title  :
 * Create : 2017/6/25
 * Author ：chen
 */

public class TestResultInfo {

    /**
     * listOrder : [{"orderID":176,"orderNo":"5002321001497840049792","orderTrackingCode":"QMCS1003"},{"orderID":175,"orderNo":"5002321001497837882022","orderTrackingCode":"QMCS1004"},{"orderID":173,"orderNo":"5002321001497837716773","orderTrackingCode":"QMCS20202020"},{"orderID":174,"orderNo":"5002321001497837851089","orderTrackingCode":"QMCS1005"},{"orderID":172,"orderNo":"5002321001497837690072","orderTrackingCode":"QMCS20170102"},{"orderID":171,"orderNo":"5002321001497837659966","orderTrackingCode":"QMCS20181012"}]
     * listOrderSize : 6
     * packagePublish : {"entBasicAreaName":"重庆市,重庆,武隆县,港口镇,芙蓉西路","entBasicId":4,"entBasicName":"巷口镇网点","outBasicAreaName":"重庆市,重庆,武隆县,港口镇,芙蓉西路","outBasicName":"巷口镇网点","packageCode":"QMWD201704030085","packageId":43,"packageSize":45,"packageWeight":10,"publishReqDelivTime":1497931200000}
     */

    private int listOrderSize;
    private PackagePublishBean packagePublish;
    private List<TestPackageListInfo> listOrder;

    public int getListOrderSize() {
        return listOrderSize;
    }

    public void setListOrderSize(int listOrderSize) {
        this.listOrderSize = listOrderSize;
    }

    public PackagePublishBean getPackagePublish() {
        return packagePublish;
    }

    public void setPackagePublish(PackagePublishBean packagePublish) {
        this.packagePublish = packagePublish;
    }

    public List<TestPackageListInfo> getListOrder() {
        return listOrder;
    }

    public void setListOrder(List<TestPackageListInfo> listOrder) {
        this.listOrder = listOrder;
    }

    public static class PackagePublishBean {
        /**
         * entBasicAreaName : 重庆市,重庆,武隆县,港口镇,芙蓉西路
         * entBasicId : 4
         * entBasicName : 巷口镇网点
         * outBasicAreaName : 重庆市,重庆,武隆县,港口镇,芙蓉西路
         * outBasicName : 巷口镇网点
         * packageCode : QMWD201704030085
         * packageId : 43
         * packageSize : 45
         * packageWeight : 10
         * publishReqDelivTime : 1497931200000
         */

        private String entBasicAreaName;
        private int entBasicId;
        private String entBasicName;
        private String outBasicAreaName;
        private String outBasicName;
        private String packageCode;
        private long packageId;
        private int packageSize;
        private int packageWeight;
        private long publishReqDelivTime;

        private String publishPicsUrl;

        public String getPublishPicsUrl() {
            return publishPicsUrl;
        }

        public void setPublishPicsUrl(String publishPicsUrl) {
            this.publishPicsUrl = publishPicsUrl;
        }

        public String getEntBasicAreaName() {
            return entBasicAreaName;
        }

        public void setEntBasicAreaName(String entBasicAreaName) {
            this.entBasicAreaName = entBasicAreaName;
        }

        public int getEntBasicId() {
            return entBasicId;
        }

        public void setEntBasicId(int entBasicId) {
            this.entBasicId = entBasicId;
        }

        public String getEntBasicName() {
            return entBasicName;
        }

        public void setEntBasicName(String entBasicName) {
            this.entBasicName = entBasicName;
        }

        public String getOutBasicAreaName() {
            return outBasicAreaName;
        }

        public void setOutBasicAreaName(String outBasicAreaName) {
            this.outBasicAreaName = outBasicAreaName;
        }

        public String getOutBasicName() {
            return outBasicName;
        }

        public void setOutBasicName(String outBasicName) {
            this.outBasicName = outBasicName;
        }

        public String getPackageCode() {
            return packageCode;
        }

        public void setPackageCode(String packageCode) {
            this.packageCode = packageCode;
        }

        public long getPackageId() {
            return packageId;
        }

        public void setPackageId(long packageId) {
            this.packageId = packageId;
        }

        public int getPackageSize() {
            return packageSize;
        }

        public void setPackageSize(int packageSize) {
            this.packageSize = packageSize;
        }

        public int getPackageWeight() {
            return packageWeight;
        }

        public void setPackageWeight(int packageWeight) {
            this.packageWeight = packageWeight;
        }

        public long getPublishReqDelivTime() {
            return publishReqDelivTime;
        }

        public void setPublishReqDelivTime(long publishReqDelivTime) {
            this.publishReqDelivTime = publishReqDelivTime;
        }
    }


}
