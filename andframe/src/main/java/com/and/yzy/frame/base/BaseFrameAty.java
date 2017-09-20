package com.and.yzy.frame.base;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.and.yzy.frame.R;
import com.and.yzy.frame.http.HttpCallBack;
import com.and.yzy.frame.util.AppManger;
import com.and.yzy.frame.util.AppUtils;
import com.and.yzy.frame.util.NetWorkUtils;
import com.and.yzy.frame.util.ToastUtil;
import com.and.yzy.frame.util.ViewStateManger;
import com.orhanobut.logger.Logger;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * activity的基类
 */
public abstract class BaseFrameAty extends AppCompatActivity implements HttpCallBack {


    private ViewStateManger mStateManger;

    /**
     * 是否初始化请求网络数据
     */
    protected boolean isInitRequestData = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppManger.getInstance().addActivity(this);

        // 设置是否是初始化网络操作
        isInitRequestData = setIsInitRequestData();
        //设置为竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setInputMode();
        setContentView(R.layout.frame_base_layout);
        FrameLayout content = (FrameLayout) findViewById(R.id.content);

        View contentView = null;
        if (getLayoutId() != -1) {
            contentView = getLayoutInflater().inflate(getLayoutId(), null, false);
            if (contentView.getParent() != content) {
                content.addView(contentView);
            }
        }

        mStateManger = new ViewStateManger(this, content, contentView, R.layout.frame_error_layout, R.layout.frame_loading_content_dialog, new ViewStateManger.BtnRefreshClickListener() {
            @Override
            public void onRefreshClick() {
                //判断网络是否可用
                if (NetWorkUtils.isNetworkConnected(BaseFrameAty.this)) {
                    requestData();
                } else {
                    showErrorToast("网络连接错误");
                }
            }
        });


        ButterKnife.bind(this);

        initData();


        initRequsetMethod();


    }


    public void setInputMode() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_UNSPECIFIED | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    /**
     * 设置是否是初始化网络
     *
     * @return
     */
    public abstract boolean setIsInitRequestData();

    /**
     * 布局文件ID
     *
     * @return
     */
    public abstract int getLayoutId();

    /**
     * 初始化数据
     */
    public abstract void initData();

    /**
     * 请求网络初始化数据
     */
    public abstract void requestData();

    /**
     * 初始化请求网络
     */
    public void initRequsetMethod() {

        if (isInitRequestData) {
            //判断网络是否可用
            if (NetWorkUtils.isNetworkConnected(this)) {

                requestData();

            } else {
                showNetWorkErrorPage();
            }
        }

    }

    /**
     * view的点击事件
     */
    public abstract void btnClick(View view);


    //======================================进度条相关=========================================

    /**
     * 显示网络错误界面
     */
    public void showNetWorkErrorPage() {
        mStateManger.showNetWorkErrorPage();
    }

    /**
     * 隐藏网络错误界面
     */
    public void hideNetWorkErrorPage() {
        mStateManger.hideNetWorkErrorPage();
    }

    /**
     * 显示提示信息
     *
     * @param message
     */
    public void showToast(String message) {
        ToastUtil.showSuccessToast(message, Toast.LENGTH_SHORT);
    }

    /**
     * 显示提示信息
     *
     * @param message
     */
    public void showErrorToast(String message) {
        ToastUtil.showErrorToast(message, Toast.LENGTH_SHORT);
    }

    /**
     * 显示进度对话条
     */
    public void showLoadingDialog(String message) {
        mStateManger.showLoadingDialog(message);
    }

    /**
     * 设置全屏遮盖的进度条
     *
     * @param spacingTop    距离顶部的距离单位dp
     * @param spacingBottom 距离底部的距离单位dp
     */
    public void setLoadingContentSpace(int spacingTop, int spacingBottom) {
        mStateManger.setLoadingContentSpace(spacingTop, spacingBottom);
    }

    /**
     * 显示全屏遮盖的进度条(toolbar可以显示出来默认50dp)
     */
    public void showLoadingContentDialog() {
        mStateManger.showLoadingContentDialog();
    }

    /**
     * 隐藏进度条
     */
    public void dismissLoadingContentDialog() {
        mStateManger.dismissLoadingContentDialog();
    }

    /**
     * 隐藏进度条
     */
    public void dismissLoadingDialog() {
        mStateManger.dismissLoadingDialog();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        dismissLoadingDialog();
        ButterKnife.unbind(this);
        AppManger.getInstance().killActivity(this);
    }
    // ============================== 启动Activity ==============================

    public boolean isHasAnimiation() {
        return hasAnimiation;
    }

    public void setHasAnimiation(boolean hasAnimiation) {
        this.hasAnimiation = hasAnimiation;
    }

    private boolean hasAnimiation = true;

    /**
     * 启动一个Activity
     *
     * @param className 将要启动的Activity的类名
     * @param options   传到将要启动Activity的Bundle，不传时为null
     */
    public float startActivity(Class<?> className, Bundle options) {
        if (AppUtils.isFastDoubleClick()) {
            return 0;
        }
        Intent intent = new Intent(this, className);
        if (options != null) {
            intent.putExtras(options);
        }
        startActivity(intent);
        if (hasAnimiation) {
            overridePendingTransition(R.anim.slide_right_in,
                    R.anim.slide_left_out);

        }
        return 0;
    }

    /**
     * @param className
     * @param options
     * @param flag      是否防止两次进入
     */
    public void startActivity(Class<?> className, Bundle options, boolean flag) {
        if (flag && AppUtils.isFastDoubleClick()) {
            return;
        }
        Intent intent = new Intent(this, className);
        if (options != null) {
            intent.putExtras(options);
        }
        startActivity(intent);
        if (hasAnimiation) {
            overridePendingTransition(R.anim.slide_right_in,
                    R.anim.slide_left_out);

        }
    }

    /**
     * 启动一个有会返回值的Activity
     *
     * @param className   将要启动的Activity的类名
     * @param options     传到将要启动Activity的Bundle，不传时为null
     * @param requestCode 请求码
     */
    public void startActivityForResult(Class<?> className, Bundle options,
                                       int requestCode) {
        if (AppUtils.isFastDoubleClick()) {
            return;
        }
        Intent intent = new Intent(this, className);
        if (options != null) {
            intent.putExtras(options);
        }
        startActivityForResult(intent, requestCode);
        if (hasAnimiation) {
            overridePendingTransition(R.anim.slide_right_in,
                    R.anim.slide_left_out);

        }
    }

    public void startActivityForResult(Class<?> className, Bundle options,
                                       int requestCode, boolean flag) {
        if (flag && AppUtils.isFastDoubleClick()) {
            return;
        }
        Intent intent = new Intent(this, className);
        if (options != null) {
            intent.putExtras(options);
        }
        startActivityForResult(intent, requestCode);
        if (hasAnimiation) {
            overridePendingTransition(R.anim.slide_right_in,
                    R.anim.slide_left_out);

        }
    }

    @Override
    public void finish() {
        super.finish();
        if (hasAnimiation) {
            overridePendingTransition(R.anim.slide_left_in,
                    R.anim.slide_right_out);
        }
    }


    //==============================================网络相关===================================================
    @Override
    public void onSuccess(String result, Call<ResponseBody> call, Response<ResponseBody> response, int what) {
        isInitRequestData = false;
        dismissLoadingDialog();
        dismissLoadingContentDialog();


    }

    @Override
    public void onFailure(String result, Call<ResponseBody> call, Response<ResponseBody> response, int what) {
        isInitRequestData = false;
        dismissLoadingDialog();
        dismissLoadingContentDialog();


    }

    @Override
    public void onError(Call<ResponseBody> call, Throwable t, int what) {
        if (isFinishing()) {
            return;
        }
        showErrorToast("网络连接错误");
        dismissLoadingDialog();
        dismissLoadingContentDialog();
        if (isInitRequestData) {
            showNetWorkErrorPage();
        } else {
            hideNetWorkErrorPage();
        }
    }


    public void doHttp(Call<ResponseBody> bodyCall, final int what) {
        bodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                try {
                    String result = response.body().string();
                    Logger.json(result);
                    JSONObject object = JSONObject.parseObject(result);
                    if (object.containsKey("code")) {
                        int code = object.getInteger("code");
                        if (code == 200) {
                            BaseFrameAty.this.onSuccess(result, call, response, what);
                        } else {
                            BaseFrameAty.this.onFailure(result, call, response, what);
                        }
                    }

                } catch (Exception e) {
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    e.printStackTrace(new PrintStream(baos));
                    String exception = baos.toString();

                    Logger.w(exception);
                }
            }


            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Logger.w(t.getMessage() + call.request().url().toString());

                BaseFrameAty.this.onError(call, t, what);

            }
        });

    }

    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        if (res.getConfiguration().fontScale != 1) {//非默认值
            Configuration newConfig = new Configuration();
            newConfig.setToDefaults();//设置默认
            res.updateConfiguration(newConfig, res.getDisplayMetrics());
        }
        return res;
    }

    /**
     * 点击空白位置 隐藏软键盘
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideKeyboard(v, ev)) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    // 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘
    private boolean isShouldHideKeyboard(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0],
                    top = l[1],
                    bottom = top + v.getHeight(),
                    right = left + v.getWidth();
            return !(event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom);
        }
        return false;
    }
}
