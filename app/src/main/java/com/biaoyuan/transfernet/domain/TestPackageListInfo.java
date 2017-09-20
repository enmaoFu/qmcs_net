package com.biaoyuan.transfernet.domain;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Title  :包裹详情里面的快件
 * Create : 2017/6/5
 * Author ：chen
 */

public class TestPackageListInfo implements Parcelable {

    /**
     * orderID : 176
     * orderNo : 5002321001497840049792
     * orderTrackingCode : QMCS1003
     */

    private long orderID;
    private String orderNo;
    private String orderTrackingCode;

    //是否破损
    private boolean isPS;
    //是否丢失
    private boolean isDS;


    private String msg;

    private List<PicInfo> mPicInfos;

    private String imagePath;



    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<PicInfo> getPicInfos() {
        return mPicInfos;
    }

    public void setPicInfos(List<PicInfo> picInfos) {
        mPicInfos = picInfos;
    }

    public boolean isPS() {
        return isPS;
    }

    public void setPS(boolean PS) {
        isPS = PS;
    }

    public boolean isDS() {
        return isDS;
    }

    public void setDS(boolean DS) {
        isDS = DS;
    }

    public long getOrderID() {
        return orderID;
    }

    public void setOrderID(long orderID) {
        this.orderID = orderID;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getOrderTrackingCode() {
        return orderTrackingCode;
    }

    public void setOrderTrackingCode(String orderTrackingCode) {
        this.orderTrackingCode = orderTrackingCode;
    }

    public TestPackageListInfo() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.orderID);
        dest.writeString(this.orderNo);
        dest.writeString(this.orderTrackingCode);
        dest.writeByte(this.isPS ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isDS ? (byte) 1 : (byte) 0);
        dest.writeString(this.msg);
        dest.writeTypedList(this.mPicInfos);
        dest.writeString(this.imagePath);
    }

    protected TestPackageListInfo(Parcel in) {
        this.orderID = in.readLong();
        this.orderNo = in.readString();
        this.orderTrackingCode = in.readString();
        this.isPS = in.readByte() != 0;
        this.isDS = in.readByte() != 0;
        this.msg = in.readString();
        this.mPicInfos = in.createTypedArrayList(PicInfo.CREATOR);
        this.imagePath = in.readString();
    }

    public static final Parcelable.Creator<TestPackageListInfo> CREATOR = new Parcelable.Creator<TestPackageListInfo>() {
        @Override
        public TestPackageListInfo createFromParcel(Parcel source) {
            return new TestPackageListInfo(source);
        }

        @Override
        public TestPackageListInfo[] newArray(int size) {
            return new TestPackageListInfo[size];
        }
    };
}
