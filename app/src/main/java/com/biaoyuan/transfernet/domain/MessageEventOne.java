package com.biaoyuan.transfernet.domain;

/**
 * @title  :事件总线自定义类
 * @create :2017/6/3
 * @author :enmaoFu
 */
public class MessageEventOne {

    private String mesage;

    private String name;

    public MessageEventOne(String mesage, String name) {
        this.mesage = mesage;
        this.name = name;
    }

    public String getMesage() {
        return mesage;
    }

    public void setMesage(String mesage) {
        this.mesage = mesage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
