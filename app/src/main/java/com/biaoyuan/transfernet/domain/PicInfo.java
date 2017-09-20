package com.biaoyuan.transfernet.domain;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Title  :
 * Create : 2017/6/2
 * Author ：chen
 */

public class PicInfo implements Parcelable {
    private String path;
    private long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public PicInfo() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.path);
        dest.writeLong(this.id);
    }

    protected PicInfo(Parcel in) {
        this.path = in.readString();
        this.id = in.readLong();
    }

    public static final Parcelable.Creator<PicInfo> CREATOR = new Parcelable.Creator<PicInfo>() {
        @Override
        public PicInfo createFromParcel(Parcel source) {
            return new PicInfo(source);
        }

        @Override
        public PicInfo[] newArray(int size) {
            return new PicInfo[size];
        }
    };
}
