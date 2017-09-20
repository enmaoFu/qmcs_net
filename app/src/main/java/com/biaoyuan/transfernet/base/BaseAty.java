package com.biaoyuan.transfernet.base;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.and.yzy.frame.adapter.recyclerview.BaseQuickAdapter;
import com.and.yzy.frame.base.BaseFrameAty;
import com.biaoyuan.transfernet.R;
import com.biaoyuan.transfernet.config.UserManger;
import com.biaoyuan.transfernet.ui.login.LoginAty;
import com.biaoyuan.transfernet.util.AppJsonUtil;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by yzy on 2017/3/10.
 */
public abstract class BaseAty extends BaseFrameAty {

    public boolean isShowOnFailureToast = true;

    @Override
    public boolean setIsInitRequestData() {
        return false;
    }

    @Override
    public void btnClick(View view) {

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
                View view = getWindow().peekDecorView();
                if (view != null) {
                    InputMethodManager inputmanger = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                finish();
            }
        });
    }

    @Override
    public void onFailure(String result, Call<ResponseBody> call, Response<ResponseBody> response, int what) {
        super.onFailure(result, call, response, what);

        if (isShowOnFailureToast) {
            //把错误的msg弹出来 如果不需要弹出设置为false
            String msg = AppJsonUtil.getString(result, "msg");
            if (msg != null) {
                showErrorToast(msg);
            }
        }
        int code = AppJsonUtil.getInt(result, "code");

        if (code == 401) {
            showErrorToast("登录已失效");
            UserManger.setIsLogin(false);
            setHasAnimiation(false);
            Intent intent = new Intent(BaseAty.this, LoginAty.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }


    }

    /**
     * 设置空数据
     *
     * @param quickAdapter
     * @param noDate
     */
    public void setEmptyView(BaseQuickAdapter quickAdapter, String noDate) {
        View view = getLayoutInflater().inflate(R.layout.public_defect_main, null, false);

        if (noDate != null && noDate.length() > 0) {
            TextView tv_nodate = (TextView) view.findViewById(R.id.tv_no_data);
            tv_nodate.setText(noDate);
        }
        quickAdapter.setEmptyView(view);

    }
}
