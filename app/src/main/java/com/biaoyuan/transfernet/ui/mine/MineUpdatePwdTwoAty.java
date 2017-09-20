package com.biaoyuan.transfernet.ui.mine;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

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
 * Title  :修改密码第二步
 * Create : 2017/4/27
 * Author ：chen
 */

public class MineUpdatePwdTwoAty extends BaseAty {
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.et_pwd)
    EditText mEtPwd;
    @Bind(R.id.et_pwd_r)
    EditText mEtPwdR;
    @Bind(R.id.one_pwd_img)
    ImageView onePwdImg;
    @Bind(R.id.two_pwd_img)
    ImageView twoPwdImg;

    private String getPhone;
    private boolean flag = false;

    @Override
    public int getLayoutId() {
        return R.layout.activity_mine_update_two_pwd;
    }

    @Override
    public void initData() {
        Bundle bundle = getIntent().getExtras();
        getPhone = bundle.getString("getPhone");
        initToolbar(mToolbar, "修改密码");
    }

    @Override
    public boolean setIsInitRequestData() {
        return false;
    }

    @Override
    public void requestData() {

    }

    @OnClick({R.id.one_pwd_img,R.id.two_pwd_img,R.id.tv_commit})
    @Override
    public void btnClick(View view) {
        switch (view.getId()){
            case R.id.one_pwd_img:

                if (!flag) {
                    mEtPwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    onePwdImg.setImageResource(R.drawable.eye_adble);
                } else {
                    mEtPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    onePwdImg.setImageResource(R.drawable.eye_unadble);
                }
                flag = !flag;
                //将光标移至文字末尾
                mEtPwd.setSelection(mEtPwd.length());

                break;
            case R.id.two_pwd_img:

                if (!flag) {
                    mEtPwdR.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    twoPwdImg.setImageResource(R.drawable.eye_adble);
                } else {
                    mEtPwdR.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    twoPwdImg.setImageResource(R.drawable.eye_unadble);
                }
                flag = !flag;
                //将光标移至文字末尾
                mEtPwdR.setSelection(mEtPwdR.length());

                break;
            case R.id.tv_commit:
                String getOnePwd = mEtPwd.getText().toString().trim();
                String getTwoPwd = mEtPwdR.getText().toString().trim();
                if (getOnePwd.length() < 8) {
                    showErrorToast(getResources().getString(R.string.pwd_tip));
                    mEtPwd.requestFocus();
                    return;
                }
                if (getTwoPwd.length() < 8) {
                    showErrorToast(getResources().getString(R.string.r_pwd_tip));
                    mEtPwdR.requestFocus();
                    return;
                }
                if (!getOnePwd.equals(getTwoPwd)) {
                    showErrorToast(getResources().getString(R.string.pwd_not_same));
                    mEtPwdR.requestFocus();
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
                UserManger.setPwd(mEtPwd.getText().toString().trim());
                showToast("修改密码成功");
                finish();
                break;
        }
    }

}
