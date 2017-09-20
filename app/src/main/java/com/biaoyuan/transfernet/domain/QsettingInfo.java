package com.biaoyuan.transfernet.domain;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Title  :计价规则
 * Create : 2017/6/8
 * Author ：chen
 */

public class QsettingInfo implements Parcelable {


    /**
     * qsettingCreatTime : 1
     * qsettingId : 9
     * qsettingIsDeleted : 0
     * qsettingParamDesc : 类型1距离最大值
     * qsettingParamFathernode : 6
     * qsettingParamValue : 0
     * qsettingParameter : distanceLimit
     * qsettingUpdateTime : 1
     * qsettingVersion : 0
     */

    private int qsettingId;
    private String qsettingParamDesc;
    private int qsettingParamFathernode;
    private int qsettingParamValue;
    private String qsettingParameter;


    private ArrayList<Integer> sizeList;
    private ArrayList<String> sizeStringList;
    private ArrayList<Integer> weightList;
    private ArrayList<String> weightStringList;

    public ArrayList<String> getSizeStringList() {
        return sizeStringList;
    }

    public void setSizeStringList(ArrayList<String> sizeStringList) {
        this.sizeStringList = sizeStringList;
    }

    public ArrayList<String> getWeightStringList() {
        return weightStringList;
    }

    public void setWeightStringList(ArrayList<String> weightStringList) {
        this.weightStringList = weightStringList;
    }

    public ArrayList<Integer> getSizeList() {
        return sizeList;
    }

    public void setSizeList(ArrayList<Integer> sizeList) {
        this.sizeList = sizeList;
    }

    public ArrayList<Integer> getWeightList() {
        return weightList;
    }

    public void setWeightList(ArrayList<Integer> weightList) {
        this.weightList = weightList;
    }

    private int sizeMax;
    private int sizeMin;
    private int sizeStep;


    private int weightMax;
    private int weightMin;
    private int weightStep;


    public int getSizeMax() {
        return sizeMax;
    }

    public void setSizeMax(int sizeMax) {
        this.sizeMax = sizeMax;
    }

    public int getSizeMin() {
        return sizeMin;
    }

    public void setSizeMin(int sizeMin) {
        this.sizeMin = sizeMin;
    }

    public int getSizeStep() {
        return sizeStep;
    }

    public void setSizeStep(int sizeStep) {
        this.sizeStep = sizeStep;
    }

    public int getWeightMax() {
        return weightMax;
    }

    public void setWeightMax(int weightMax) {
        this.weightMax = weightMax;
    }

    public int getWeightMin() {
        return weightMin;
    }

    public void setWeightMin(int weightMin) {
        this.weightMin = weightMin;
    }

    public int getWeightStep() {
        return weightStep;
    }

    public void setWeightStep(int weightStep) {
        this.weightStep = weightStep;
    }

    public int getQsettingId() {
        return qsettingId;
    }

    public void setQsettingId(int qsettingId) {
        this.qsettingId = qsettingId;
    }

    public String getQsettingParamDesc() {
        return qsettingParamDesc;
    }

    public void setQsettingParamDesc(String qsettingParamDesc) {
        this.qsettingParamDesc = qsettingParamDesc;
    }

    public int getQsettingParamFathernode() {
        return qsettingParamFathernode;
    }

    public void setQsettingParamFathernode(int qsettingParamFathernode) {
        this.qsettingParamFathernode = qsettingParamFathernode;
    }

    public int getQsettingParamValue() {
        return qsettingParamValue;
    }

    public void setQsettingParamValue(int qsettingParamValue) {
        this.qsettingParamValue = qsettingParamValue;
    }

    public String getQsettingParameter() {
        return qsettingParameter;
    }

    public void setQsettingParameter(String qsettingParameter) {
        this.qsettingParameter = qsettingParameter;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.qsettingId);
        dest.writeString(this.qsettingParamDesc);
        dest.writeInt(this.qsettingParamFathernode);
        dest.writeInt(this.qsettingParamValue);
        dest.writeString(this.qsettingParameter);
        dest.writeList(this.sizeList);
        dest.writeStringList(this.sizeStringList);
        dest.writeList(this.weightList);
        dest.writeStringList(this.weightStringList);
        dest.writeInt(this.sizeMax);
        dest.writeInt(this.sizeMin);
        dest.writeInt(this.sizeStep);
        dest.writeInt(this.weightMax);
        dest.writeInt(this.weightMin);
        dest.writeInt(this.weightStep);
    }

    public QsettingInfo() {
    }

    protected QsettingInfo(Parcel in) {
        this.qsettingId = in.readInt();
        this.qsettingParamDesc = in.readString();
        this.qsettingParamFathernode = in.readInt();
        this.qsettingParamValue = in.readInt();
        this.qsettingParameter = in.readString();
        this.sizeList = new ArrayList<Integer>();
        in.readList(this.sizeList, Integer.class.getClassLoader());
        this.sizeStringList = in.createStringArrayList();
        this.weightList = new ArrayList<Integer>();
        in.readList(this.weightList, Integer.class.getClassLoader());
        this.weightStringList = in.createStringArrayList();
        this.sizeMax = in.readInt();
        this.sizeMin = in.readInt();
        this.sizeStep = in.readInt();
        this.weightMax = in.readInt();
        this.weightMin = in.readInt();
        this.weightStep = in.readInt();
    }

    public static final Parcelable.Creator<QsettingInfo> CREATOR = new Parcelable.Creator<QsettingInfo>() {
        @Override
        public QsettingInfo createFromParcel(Parcel source) {
            return new QsettingInfo(source);
        }

        @Override
        public QsettingInfo[] newArray(int size) {
            return new QsettingInfo[size];
        }
    };
}
