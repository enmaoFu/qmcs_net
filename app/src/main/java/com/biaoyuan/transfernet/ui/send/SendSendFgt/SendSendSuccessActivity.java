package com.biaoyuan.transfernet.ui.send.SendSendFgt;

import android.support.v7.widget.Toolbar;

import com.biaoyuan.transfernet.R;
import com.biaoyuan.transfernet.base.BaseAty;

import butterknife.Bind;

/**
 * @title :发布-》发布到到达地址-》确认发布-》发布成功页面
 * @author :enmaoFu
 * @create :2017/5/15
 */
public class SendSendSuccessActivity extends BaseAty {

    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    @Override
    public int getLayoutId() {
        return R.layout.activity_send_send_success;
    }

    @Override
    public void initData() {
        initToolbar(mToolbar,"发布成功");
    }

    @Override
    public boolean setIsInitRequestData() {
        return false;
    }

    @Override
    public void requestData() {

    }
}
