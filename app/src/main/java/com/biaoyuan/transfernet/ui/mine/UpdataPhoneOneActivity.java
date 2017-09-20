package com.biaoyuan.transfernet.ui.mine;

import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.and.yzy.frame.util.RetrofitUtils;
import com.biaoyuan.transfernet.R;
import com.biaoyuan.transfernet.base.BaseAty;
import com.biaoyuan.transfernet.http.Mine;

import butterknife.Bind;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Title  : 修改手机号第一步
 * Create : 2017/6/19
 * Author ：enmaoFu
 */
public class UpdataPhoneOneActivity extends BaseAty{

    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.input_pwd)
    EditText inputPwd;
    @Bind(R.id.tv_commit)
    TextView commit;

    @Override
    public int getLayoutId() {
        return R.layout.activity_mine_updata_phone_one;
    }

    @Override
    public void initData() {
        initToolbar(mToolbar,"修改手机号");
        inputPwd.addTextChangedListener(textWatch);
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
        switch (view.getId()){
            case R.id.tv_commit:
                if (inputPwd.getText().toString().length() >=8 && inputPwd.getText().toString().length() <= 18) {
                    String getInputPwd = inputPwd.getText().toString().trim();
                    showLoadingDialog(null);
                    doHttp(RetrofitUtils.createApi(Mine.class).staffInPasswordVerification(getInputPwd),1);
                }
                break;
        }
    }

    @Override
    public void onSuccess(String result, Call<ResponseBody> call, Response<ResponseBody> response, int what) {
        super.onSuccess(result, call, response, what);
        switch (what){
            case 1:
                startActivity(UpdataPhoneTwoActivity.class,null);
                finish();
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
            if (inputPwd.getText().toString().length() >= 8 && inputPwd.getText().toString().length() <= 18) {
                commit.setBackgroundResource(R.drawable.shape_commit_btn_bg);
            } else {
                commit.setBackgroundResource(R.drawable.shape_commit_gray_btn_bg);
            }
        }
    };

}
