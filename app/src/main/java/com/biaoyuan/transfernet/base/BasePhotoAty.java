package com.biaoyuan.transfernet.base;

import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.and.yzy.frame.base.BaseFrameTakePhotoAty;
import com.biaoyuan.transfernet.config.UserManger;
import com.biaoyuan.transfernet.ui.login.LoginAty;
import com.biaoyuan.transfernet.util.AppJsonUtil;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by yzy on 2017/3/29.
 */

public abstract class BasePhotoAty extends BaseFrameTakePhotoAty {

    public boolean isShowOnFailureToast = true;

    @Override
    public boolean setIsInitRequestData() {
        return false;
    }

    public void initToolbar(Toolbar toolbar, String title) {
        TextView tv_title = (TextView) toolbar.getChildAt(toolbar.getChildCount() - 1);

        tv_title.setText(title);

        ImageView iv_return = (ImageView) toolbar.getChildAt(toolbar.getChildCount() - 2);

        setSupportActionBar(toolbar);
        iv_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  setHasAnimiation(false);
                finish();
            }
        });
    }


    @Override
    public void onFailure(String result, Call<ResponseBody> call, Response<ResponseBody> response, int what) {
        super.onFailure(result, call, response, what);
        if (isShowOnFailureToast) {
            String msg = AppJsonUtil.getString(result, "msg");
            if (msg != null) {
                showErrorToast(msg);
            }
        }

        int code=AppJsonUtil.getInt(result,"code");

        if (code==401){
            showErrorToast("登录已失效");
       //     AppManger.getInstance().killAllActivity();
            setHasAnimiation(false);
            UserManger.setIsLogin(false);
            Intent intent = new Intent(BasePhotoAty.this, LoginAty.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }
    }

}
