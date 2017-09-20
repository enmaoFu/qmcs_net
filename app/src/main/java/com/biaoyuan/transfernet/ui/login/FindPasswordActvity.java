package com.biaoyuan.transfernet.ui.login;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.and.yzy.frame.util.MatchStingUtil;
import com.and.yzy.frame.util.RetrofitUtils;
import com.biaoyuan.transfernet.R;
import com.biaoyuan.transfernet.base.BaseCodeAty;
import com.biaoyuan.transfernet.http.Login;
import com.biaoyuan.transfernet.util.AppJsonUtil;

import butterknife.Bind;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Title  : 找回密码
 * Create : 2017/5/22
 * Author ：enmaoFu
 */
public class FindPasswordActvity extends BaseCodeAty {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.tv_code)
    TextView mTvCode;

    //按钮
    @Bind(R.id.tv_commit)
    TextView commitBtn;
    //手机号
    @Bind(R.id.phone)
    EditText inputPhoe;
    //验证码
    @Bind(R.id.code)
    EditText inputCode;

    MyCount mMyCount;

    @Override
    public int getLayoutId() {
        return R.layout.activity_find_pwd;
    }

    @Override
    public void initData() {
        initToolbar(toolbar, "找回密码");
        inputPhoe.addTextChangedListener(textWatch);
        inputCode.addTextChangedListener(textWatch);
    }

    @Override
    public boolean setIsInitRequestData() {
        return false;
    }

    @Override
    public void requestData() {

    }

    @OnClick({R.id.tv_commit, R.id.tv_code})
    @Override
    public void btnClick(View view) {
        switch (view.getId()) {
            case R.id.tv_commit:
                if (inputCode.getText().toString().length() > 3 && inputPhoe.getText().toString().length() == 11) {
                    String getInputPhone = inputPhoe.getText().toString().trim();
                    String getInputCode = inputCode.getText().toString().trim();
                    if (!MatchStingUtil.isMobile(getInputPhone)) {
                        showErrorToast("请输入正确的手机号");
                        inputPhoe.requestFocus();
                        return;
                    }
                    if (getInputCode.length() < 4) {
                        showErrorToast("请输入正确的验证码");
                        inputCode.requestFocus();
                        return;
                    }

                    //此处由于验证码暂时不能发送成功，所以直接跳转  成功后取消注释
                    showLoadingDialog(null);
                    doHttp(RetrofitUtils.createApi(Login.class).findPasswordVerification(getInputPhone, getInputCode), 2);

                }

                break;
            case R.id.tv_code:

                String getPhone = inputPhoe.getText().toString().trim();
                if (!MatchStingUtil.isMobile(getPhone)) {
                    showErrorToast("请输入正确的手机号");
                    inputPhoe.requestFocus();
                    return;
                }
                showLoadingDialog("正在发送");
                doHttp(RetrofitUtils.createApi(Login.class).sendSms(getPhone, "1"), 1);

                break;
        }
    }

    @Override
    public void onSuccess(String result, Call<ResponseBody> call, Response<ResponseBody> response, int what) {
        super.onSuccess(result, call, response, what);
        switch (what) {
            case 1:
                if (mMyCount == null) {
                    mMyCount = new MyCount(60000, 1000);
                }
                mMyCount.start();
                showToast(AppJsonUtil.getString(result, "msg"));
                break;
            case 2:
                String getPhone = inputPhoe.getText().toString().trim();
                Bundle bundle = new Bundle();
                bundle.putString("phone", getPhone);
                startActivity(ResetPasswordActvity.class, bundle);
                break;
        }
    }

    /**
     * 用于判断下一步是否可点击
     */
    private TextWatcher textWatch = new TextWatcher() {

        /**
         * 变化前
         * @param s
         * @param start
         * @param count
         * @param after
         */
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        /**
         * 变化中
         * @param s
         * @param start
         * @param before
         * @param count
         */
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        /**
         * 变化后
         * @param s
         */
        @Override
        public void afterTextChanged(Editable s) {
            //s:变化后的所有字符
            if (inputCode.getText().toString().length() > 3 && inputPhoe.getText().toString().length() == 11) {
                commitBtn.setBackgroundResource(R.drawable.shape_commit_btn_bg);
            } else {
                commitBtn.setBackgroundResource(R.drawable.shape_commit_gray_btn_bg);
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mMyCount != null) {
            mMyCount.cancel();
        }
    }

    @Override
    public EditText getEditTextView() {
        return inputCode;
    }

    private class MyCount extends CountDownTimer {

        /**
         * @param millisInFuture    The number of millis in the future from the call
         *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
         *                          is called.
         * @param countDownInterval The interval along the way to receive
         *                          {@link #onTick(long)} callbacks.
         */
        public MyCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            if (mTvCode != null) {
                mTvCode.setEnabled(false);
                mTvCode.setTextColor(getResources().getColor(R.color.font_gray));
                mTvCode.setText(millisUntilFinished / 1000 + " s" + "后重发");
            }

        }

        @Override
        public void onFinish() {
            if (mTvCode != null) {
                mTvCode.setEnabled(true);
                mTvCode.setTextColor(getResources().getColor(R.color.colorAccent));
                mTvCode.setText("获取验证码");
            }

        }
    }
}
