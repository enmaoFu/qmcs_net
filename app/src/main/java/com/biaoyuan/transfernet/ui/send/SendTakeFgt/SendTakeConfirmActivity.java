package com.biaoyuan.transfernet.ui.send.SendTakeFgt;

import android.graphics.Color;
import android.support.v7.widget.Toolbar;

import com.biaoyuan.transfernet.R;
import com.biaoyuan.transfernet.base.BaseAty;

import butterknife.Bind;

/**
 * @title :确认接件页面
 * @author :enmaoFu
 * @create :2017/5/12
 */
public class SendTakeConfirmActivity extends BaseAty{

    @Bind(R.id.toolbar)
    Toolbar mToolbal;

    @Override
    public int getLayoutId() {
        return R.layout.activity_send_take_confirm;
    }

    @Override
    public void initData() {
        initToolbar(mToolbal,"成功");
    }

    @Override
    public boolean setIsInitRequestData() {
        return false;
    }

    @Override
    public void requestData() {

    }
}
