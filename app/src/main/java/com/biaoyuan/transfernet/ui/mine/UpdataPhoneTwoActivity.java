package com.biaoyuan.transfernet.ui.mine;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.and.yzy.frame.util.MatchStingUtil;
import com.and.yzy.frame.util.RetrofitUtils;
import com.biaoyuan.transfernet.R;
import com.biaoyuan.transfernet.base.BaseCodeAty;
import com.biaoyuan.transfernet.config.UserManger;
import com.biaoyuan.transfernet.domain.MessageEvent;
import com.biaoyuan.transfernet.http.Mine;
import com.biaoyuan.transfernet.util.AppJsonUtil;

import org.greenrobot.eventbus.EventBus;

import butterknife.Bind;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Title  : 修改手机号第二步
 * Create : 2017/6/19
 * Author ：enmaoFu
 */
public class UpdataPhoneTwoActivity extends BaseCodeAty{

    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.editText)
    EditText editText;
    @Bind(R.id.code)
    EditText code;
    @Bind(R.id.get_code_sms)
    TextView getCodeSms;

    MyCount mMyCount;

    private String getPhoneNew;

    @Override
    public int getLayoutId() {
        return R.layout.activity_mine_updata_phone_two;
    }

    @Override
    public void initData() {
        Bundle bundle = getIntent().getExtras();
        initToolbar(mToolbar,"修改手机号");
    }

    @OnClick({R.id.get_code_sms,R.id.tv_commit})
    @Override
    public void btnClick(View view) {
        super.btnClick(view);
        switch (view.getId()){
            case R.id.get_code_sms:
                String getPhone = editText.getText().toString().trim();
                if(getPhone.length() == 0){
                    showErrorToast("手机号不能为空");
                }else if(!MatchStingUtil.isMobile(getPhone)){
                    showErrorToast("请输入正确的手机号");
                }else{
                    showLoadingDialog("正在发送");
                    doHttp(RetrofitUtils.createApi(Mine.class).sendSmsNotSignin(getPhone),1);
                }
                break;
            case R.id.tv_commit:
                getPhoneNew = editText.getText().toString().trim();
                String getCode = code.getText().toString().trim();
                if(getPhoneNew.length() == 0){
                    showErrorToast("手机号不能为空");
                }else if(!MatchStingUtil.isMobile(getPhoneNew)){
                    showErrorToast("请输入正确的手机号");
                }else if(getCode.length() == 0){
                    showErrorToast("验证码不能为空");
                }else if(getCode.length() < 4){
                    showErrorToast("请输入正确的验证码");
                }else{
                    showLoadingDialog(null);
                    doHttp(RetrofitUtils.createApi(Mine.class).setStaffInformation("","","","", Long.valueOf(getPhoneNew),getCode),2);
                }
                break;
        }
    }

    @Override
    public void onSuccess(String result, Call<ResponseBody> call, Response<ResponseBody> response, int what) {
        super.onSuccess(result, call, response, what);
        switch (what){
            case 1:
                if (mMyCount == null) {
                    mMyCount = new UpdataPhoneTwoActivity.MyCount(60000, 1000);
                }
                mMyCount.start();
                showToast(AppJsonUtil.getString(result, "msg"));
                break;
            case 2:
                showToast("修改手机号成功");
                UserManger.setAcount(getPhoneNew);
                EventBus.getDefault().post(new MessageEvent(getPhoneNew,0));
                UserManger.setUserPhone(getPhoneNew);
                finish();
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
    public EditText getEditTextView() {
        return code;
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

}
