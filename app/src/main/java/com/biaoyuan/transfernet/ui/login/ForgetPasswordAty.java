package com.biaoyuan.transfernet.ui.login;

import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import com.biaoyuan.transfernet.R;
import com.biaoyuan.transfernet.base.BaseAty;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Title  : 忘记密码页面
 * Create : 2017/5/22
 * Author ：enmaoFu
 */
public class ForgetPasswordAty extends BaseAty {

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    //手机号
    @Bind(R.id.phone)
    EditText inputPhoe;
    //验证码
    @Bind(R.id.code)
    EditText inputCode;

    @Override
    public int getLayoutId() {
        return R.layout.activity_forget_password;
    }

    @Override
    public void initData() {
        initToolbar(toolbar, "找回密码");
    }

    @Override
    public boolean setIsInitRequestData() {
        return false;
    }

    @Override
    public void requestData() {

    }

    @OnClick({R.id.tv_commit})
    @Override
    public void btnClick(View view) {
        switch (view.getId()) {
            case R.id.tv_commit:
                startActivity(ResetPasswordActvity.class, null);
                break;
        }
    }
}
