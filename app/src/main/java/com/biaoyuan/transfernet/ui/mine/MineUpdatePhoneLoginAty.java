package com.biaoyuan.transfernet.ui.mine;

import android.support.v7.widget.Toolbar;

import com.biaoyuan.transfernet.R;
import com.biaoyuan.transfernet.base.BaseAty;

import butterknife.Bind;

/**
 * Title  :
 * Create : 2017/5/10
 * Author ：chen
 */

public class MineUpdatePhoneLoginAty extends BaseAty {
    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    @Override
    public int getLayoutId() {
        return R.layout.activity_mine_set_phone_login;
    }

    @Override
    public boolean setIsInitRequestData() {
        return false;
    }

    @Override
    public void initData() {
        initToolbar(mToolbar,"修改手机号");


    }

    @Override
    public void requestData() {

    }
}
