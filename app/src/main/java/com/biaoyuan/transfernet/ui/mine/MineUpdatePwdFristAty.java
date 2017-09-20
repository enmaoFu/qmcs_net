package com.biaoyuan.transfernet.ui.mine;

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
 * Title  :修改密码第一步
 * Create : 2017/4/27
 * Author ：chen
 */

public class MineUpdatePwdFristAty extends BaseCodeAty {
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.phone)
    EditText phone;
    @Bind(R.id.code)
    EditText code;
    @Bind(R.id.get_code_sms)
    TextView getCodeSms;
    @Bind(R.id.tv_commit)
    TextView commitBtn;

    MyCount mMyCount;

    @Override
    public int getLayoutId() {
        return R.layout.activity_mine_update_first_pwd;
    }

    @Override
    public void initData() {
        initToolbar(mToolbar, "修改密码");
        phone.addTextChangedListener(textWatch);
        code.addTextChangedListener(textWatch);
    }

    @OnClick({R.id.tv_commit,R.id.get_code_sms})
    @Override
    public void btnClick(View view) {
        super.btnClick(view);
        switch (view.getId()) {
            case R.id.tv_commit:
                if (code.getText().toString().length() > 3 && phone.getText().toString().length() == 11) {
                    Bundle bundle = new Bundle();
                    String getPhone = phone.getText().toString().trim();
                    String getCode = code.getText().toString().trim();
                    if(getPhone.length() == 0){
                        showErrorToast("手机号不能为空");
                    }else if(!MatchStingUtil.isMobile(getPhone)){
                        showErrorToast("请输入正确的手机号");
                    }else if(getCode.length() == 0){
                        showErrorToast("验证码不能为空");
                    }else if(getCode.length() < 4){
                        showErrorToast("请输入正确的验证码");
                    }else{
                        bundle.putString("getPhone",getPhone);
                        startActivity(MineUpdatePwdTwoAty.class, bundle);
                        finish();
                    }
                }
                break;
            case R.id.get_code_sms:
                String getPhoneNew = phone.getText().toString().trim();
                if(getPhoneNew.length() == 0){
                    showErrorToast("手机号不能为空");
                }else if(!MatchStingUtil.isMobile(getPhoneNew)){
                    showErrorToast("请输入正确的手机号");
                }else{
                    showLoadingDialog("正在发送");
                    doHttp(RetrofitUtils.createApi(Login.class).sendSms(getPhoneNew,"1"),1);
                }
                break;
        }
    }

    @Override
    public boolean setIsInitRequestData() {
        return false;
    }

    @Override
    public void requestData() {

    }

    @Override
    public void onSuccess(String result, Call<ResponseBody> call, Response<ResponseBody> response, int what) {
        super.onSuccess(result, call, response, what);
        switch (what){
            case 1:
                if (mMyCount == null) {
                    mMyCount = new MineUpdatePwdFristAty.MyCount(60000, 1000);
                }
                mMyCount.start();
                showToast(AppJsonUtil.getString(result, "msg"));
                break;
        }
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
            if (getCodeSms != null) {
                getCodeSms.setEnabled(false);
                getCodeSms.setTextColor(getResources().getColor(R.color.font_gray));
                getCodeSms.setText(millisUntilFinished / 1000 + " s" + "后重发");
            }

        }

        @Override
        public void onFinish() {
            if (getCodeSms != null) {
                getCodeSms.setEnabled(true);
                getCodeSms.setTextColor(getResources().getColor(R.color.colorAccent));
                getCodeSms.setText("获取验证码");
            }

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
            if (code.getText().toString().length() > 3 && phone.getText().toString().length() == 11) {
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
        return code;
    }

}
