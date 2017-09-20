package com.biaoyuan.transfernet.ui.mine;

import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.and.yzy.frame.util.AppUtils;
import com.biaoyuan.transfernet.R;
import com.biaoyuan.transfernet.base.BaseAty;

import butterknife.Bind;

/**
 * Title  : 关于我们
 */

public class MineAboutWeActivity extends BaseAty {

    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.tv_code)
    TextView mTvCode;

    @Override
    public int getLayoutId() {
        return R.layout.activity_mine_about_we;
    }

    @Override
    public void initData() {
        initToolbar(mToolbar, "关于我们");

        mTvCode.setText("版本号v" + AppUtils.getVersionName(this));

    }

    @Override
    public boolean setIsInitRequestData() {
        return false;
    }

    @Override
    public void requestData() {

    }


}
