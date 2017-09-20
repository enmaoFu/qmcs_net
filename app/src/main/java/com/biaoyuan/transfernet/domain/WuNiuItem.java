package com.biaoyuan.transfernet.domain;

/**
 * Title  :
 * Create : 2017/5/18
 * Author ：chen
 */

public class WuNiuItem {


    /**
     * userTelphone : 13657606421
     * recordTime : 0
     * userLoginName : shenyuyu
     * recordValue : 传送
     */

    private String userTelphone;
    private String recordTime;
    private String userLoginName;
    private String recordValue;
    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUserTelphone() {
        return userTelphone;
    }

    public void setUserTelphone(String userTelphone) {
        this.userTelphone = userTelphone;
    }

    public String getRecordTime() {
        return recordTime;
    }

    public void setRecordTime(String recordTime) {
        this.recordTime = recordTime;
    }

    public String getUserLoginName() {
        return userLoginName;
    }

    public void setUserLoginName(String userLoginName) {
        this.userLoginName = userLoginName;
    }

    public String getRecordValue() {
        return recordValue;
    }

    public void setRecordValue(String recordValue) {
        this.recordValue = recordValue;
    }
}
