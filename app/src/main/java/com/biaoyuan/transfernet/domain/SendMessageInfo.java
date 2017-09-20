package com.biaoyuan.transfernet.domain;

/**
 * Title  : 消息中心实体类
 * Create : 2017/5/24
 * Author ：enmaoFu
 */
public class SendMessageInfo {


    /**
     * messageContent : 您有新订单，订单号为【5002321001499494156522】
     * messageCreatTime : 1499494170624
     * messageId : 37
     * messageIsDeleted : 0
     * messageIsRead : 0
     * messageStaffId : 7
     * messageTime : 1499494170624
     * messageTitle : 订单提醒
     * messageType : 2
     * messageUpdateTime : 0
     * messageVersion : 0
     */

    private String messageContent;
    private long messageCreatTime;
    private int messageId;
    private int messageIsDeleted;
    private int messageIsRead;
    private int messageStaffId;
    private long messageTime;
    private String messageTitle;
    private int messageType;
    private long messageUpdateTime;
    private int messageVersion;

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public long getMessageCreatTime() {
        return messageCreatTime;
    }

    public void setMessageCreatTime(long messageCreatTime) {
        this.messageCreatTime = messageCreatTime;
    }

    public int getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }

    public int getMessageIsDeleted() {
        return messageIsDeleted;
    }

    public void setMessageIsDeleted(int messageIsDeleted) {
        this.messageIsDeleted = messageIsDeleted;
    }

    public int getMessageIsRead() {
        return messageIsRead;
    }

    public void setMessageIsRead(int messageIsRead) {
        this.messageIsRead = messageIsRead;
    }

    public int getMessageStaffId() {
        return messageStaffId;
    }

    public void setMessageStaffId(int messageStaffId) {
        this.messageStaffId = messageStaffId;
    }

    public long getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(long messageTime) {
        this.messageTime = messageTime;
    }

    public String getMessageTitle() {
        return messageTitle;
    }

    public void setMessageTitle(String messageTitle) {
        this.messageTitle = messageTitle;
    }

    public int getMessageType() {
        return messageType;
    }

    public void setMessageType(int messageType) {
        this.messageType = messageType;
    }

    public long getMessageUpdateTime() {
        return messageUpdateTime;
    }

    public void setMessageUpdateTime(long messageUpdateTime) {
        this.messageUpdateTime = messageUpdateTime;
    }

    public int getMessageVersion() {
        return messageVersion;
    }

    public void setMessageVersion(int messageVersion) {
        this.messageVersion = messageVersion;
    }
}
