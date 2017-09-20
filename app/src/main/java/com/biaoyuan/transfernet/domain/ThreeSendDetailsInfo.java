package com.biaoyuan.transfernet.domain;

/**
 * Title  : 第三方代发已代发详情 时光轴实体类
 * Create : 2017/6/5
 * Author ：enmaoFu
 */
public class ThreeSendDetailsInfo {

    private String context;

    private String time;

    private long ftime;

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public long getFtime() {
        return ftime;
    }

    public void setFtime(long ftime) {
        this.ftime = ftime;
    }
}
