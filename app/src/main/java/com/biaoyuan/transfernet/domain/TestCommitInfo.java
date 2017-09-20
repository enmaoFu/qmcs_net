package com.biaoyuan.transfernet.domain;

/**
 * Title  :
 * Create : 2017/6/25
 * Author ：chen
 */

public class TestCommitInfo {

    /**
     * orderId : 177
     * orderStauts : 3
     * orderText : 正常
     * fileUrl :
     */

    private String orderId;
    private String orderStauts;
    private String orderText;
    private String fileUrl;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderStauts() {
        return orderStauts;
    }

    public void setOrderStauts(String orderStauts) {
        this.orderStauts = orderStauts;
    }

    public String getOrderText() {
        return orderText;
    }

    public void setOrderText(String orderText) {
        this.orderText = orderText;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }
}
