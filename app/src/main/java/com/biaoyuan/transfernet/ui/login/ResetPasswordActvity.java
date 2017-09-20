package com.biaoyuan.transfernet.ui.login;

import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import com.and.yzy.frame.util.AppManger;
import com.and.yzy.frame.util.RetrofitUtils;
import com.biaoyuan.transfernet.R;
import com.biaoyuan.transfernet.base.BaseAty;
import com.biaoyuan.transfernet.config.UserManger;
import com.biaoyuan.transfernet.http.Login;

import butterknife.Bind;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Title  : 重置密码页面
 * Create : 2017/5/22
 * Author ：enmaoFu
 */
public class ResetPasswordActvity extends BaseAty {

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    //第一次输入密码
    @Bind(R.id.one_pwd)
    EditText onePwd;
    //第二次输入密码
    @Bind(R.id.two_pwd)
    EditText twoPwd;

    @Override
    public int getLayoutId() {
        return R.layout.activity_set_pwd;
    }

    @Override
    public void initData() {
        initToolbar(toolbar, "重置密码");
        AppManger.getInstance().killActivity(FindPasswordActvity.class);
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
                String getOnePwd = onePwd.getText().toString().trim();
                String getTwoPwd = twoPwd.getText().toString().trim();
                String getPhone = getIntent().getStringExtra("phone");
                if (getOnePwd.length() < 8) {
                    showErrorToast(getResources().getString(R.string.pwd_tip));
                    onePwd.requestFocus();
                    return;
                }
                if (getTwoPwd.length() < 8) {
                    showErrorToast(getResources().getString(R.string.r_pwd_tip));
                    twoPwd.requestFocus();
                    return;
                }
                if (!getOnePwd.equals(getTwoPwd)) {
                    showErrorToast(getResources().getString(R.string.pwd_not_same));
                    twoPwd.requestFocus();
                    return;
                }
                showLoadingDialog(null);
                doHttp(RetrofitUtils.createApi(Login.class).findPassword(getPhone, getOnePwd, getTwoPwd), 1);

                break;
        }
    }

    @Override
    public void onSuccess(String result, Call<ResponseBody> call, Response<ResponseBody> response, int what) {
        super.onSuccess(result, call, response, what);
        switch (what) {
            case 1:
                //showToast(AppJsonUtil.getString(result, "msg"));
                UserManger.setPwd(onePwd.getText().toString().trim());
                setResult(RESULT_OK);
                finish();
                break;
        }
    }
}
