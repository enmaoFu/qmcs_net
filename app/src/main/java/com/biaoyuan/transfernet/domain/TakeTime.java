package com.biaoyuan.transfernet.domain;

import com.bigkoo.pickerview.model.IPickerViewData;

import java.util.ArrayList;

/**
 * Title  :
 * Create : 2017/6/7
 * Author ：chen
 */

public class TakeTime implements IPickerViewData {

    private String type;

    private ArrayList<Long> mLongTime;

    private ArrayList<String> times;


    public ArrayList<Long> getLongTime() {
        return mLongTime;
    }

    public void setLongTime(ArrayList<Long> longTime) {
        mLongTime = longTime;
    }

    public ArrayList<String> getTimes() {
        return times;
    }

    public void setTimes(ArrayList<String> times) {
        this.times = times;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    @Override
    public String getPickerViewText() {
        return type;
    }
}
