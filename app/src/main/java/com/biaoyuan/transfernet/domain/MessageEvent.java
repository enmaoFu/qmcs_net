package com.biaoyuan.transfernet.domain;

/**
 * @title  :事件总线自定义类
 * @create :2017/6/3
 * @author :enmaoFu
 */
public class MessageEvent {

    private String mesage;

    private int position;



    public MessageEvent(String mesage, int position) {
        this.mesage = mesage;
        this.position = position;

    }




    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getMesage() {
        return mesage;
    }

    public void setMesage(String mesage) {
        this.mesage = mesage;
    }

}
