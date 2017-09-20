package com.biaoyuan.transfernet.domain;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @title :封包-》已接单-》封装扫描页面 listview 实体类
 * @author :enmaoFu
 * @create :2017/5/17
 */
public class SendSealPackingScanningInfo implements Parcelable {


    /**
     * p_o_id : 40
     * order_tracking_code : QMCS10001
     */

    private long p_o_id;
    private long p_o_order_id;
    private String order_tracking_code;
    private boolean isDelete;
    private boolean isScan;

    public long getP_o_order_id() {
        return p_o_order_id;
    }

    public void setP_o_order_id(long p_o_order_id) {
        this.p_o_order_id = p_o_order_id;
    }

    public boolean isScan() {
        return isScan;
    }

    public void setScan(boolean scan) {
        isScan = scan;
    }

    public boolean isDelete() {
        return isDelete;
    }

    public void setDelete(boolean delete) {
        isDelete = delete;
    }

    //区别手动添加还是自动添加自动1 手动0
    private int type=0;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getP_o_id() {
        return p_o_id;
    }

    public void setP_o_id(long p_o_id) {
        this.p_o_id = p_o_id;
    }

    public String getOrder_tracking_code() {
        return order_tracking_code;
    }

    public void setOrder_tracking_code(String order_tracking_code) {
        this.order_tracking_code = order_tracking_code;
    }

    public SendSealPackingScanningInfo() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.p_o_id);
        dest.writeLong(this.p_o_order_id);
        dest.writeString(this.order_tracking_code);
        dest.writeByte(this.isDelete ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isScan ? (byte) 1 : (byte) 0);
        dest.writeInt(this.type);
    }

    protected SendSealPackingScanningInfo(Parcel in) {
        this.p_o_id = in.readLong();
        this.p_o_order_id = in.readLong();
        this.order_tracking_code = in.readString();
        this.isDelete = in.readByte() != 0;
        this.isScan = in.readByte() != 0;
        this.type = in.readInt();
    }

    public static final Parcelable.Creator<SendSealPackingScanningInfo> CREATOR = new Parcelable.Creator<SendSealPackingScanningInfo>() {
        @Override
        public SendSealPackingScanningInfo createFromParcel(Parcel source) {
            return new SendSealPackingScanningInfo(source);
        }

        @Override
        public SendSealPackingScanningInfo[] newArray(int size) {
            return new SendSealPackingScanningInfo[size];
        }
    };
}
