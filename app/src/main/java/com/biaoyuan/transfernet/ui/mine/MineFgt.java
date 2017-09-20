package com.biaoyuan.transfernet.ui.mine;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.and.yzy.frame.util.DateTool;
import com.and.yzy.frame.util.RetrofitUtils;
import com.biaoyuan.transfernet.R;
import com.biaoyuan.transfernet.base.BaseFgt;
import com.biaoyuan.transfernet.config.UserManger;
import com.biaoyuan.transfernet.http.Mine;
import com.biaoyuan.transfernet.util.AppJsonUtil;
import com.biaoyuan.transfernet.util.MyNumberFormat;
import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.Bind;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Title  :我的
 * Create :2017/5/9
 * Author :chen
 */

public class MineFgt extends BaseFgt {

    @Bind(R.id.mine_phone)
    TextView minePhone;
    @Bind(R.id.mine_spot)
    TextView mineSpot;
    @Bind(R.id.mine_login_date)
    TextView minLoginDate;
    @Bind(R.id.mine_img)
    SimpleDraweeView mineImg;
    @Bind(R.id.mine_ye)
    TextView mineYe;

    private String getMinePhone;
    private String getMineImge;
    private String rawStaffPortraitUrl;
    private String mineYeNew;

    public static boolean isChangeImage = true;
    public boolean onLoadingData = false;


    @Override
    public int getLayoutId() {
        return R.layout.fgt_mine_main;
    }

    @Override
    public void initData() {

    }

    @Override
    public void onUserVisible() {
        super.onUserVisible();
        if (onLoadingData) {
            return;
        }
        onLoadingData = true;

        doHttp(RetrofitUtils.createApi(Mine.class).toPersonalCenter(Long.parseLong(UserManger.getBaseId())), 1);
    }

    @Override
    public boolean setIsInitRequestData() {
        return true;
    }

    @Override
    public void requestData() {
        showLoadingContentDialog();
        doHttp(RetrofitUtils.createApi(Mine.class).toPersonalCenter(Long.parseLong(UserManger.getBaseId())), 1);
    }

    @OnClick({R.id.mine_setting, R.id.ll_data, R.id.tv_three, R.id.tv_take, R.id.tv_not_receive_package,
            R.id.tv_send_package, R.id.tv_have_send_file, R.id.tv_exception_file, R.id.tv_customer_evaluate,
            R.id.feedback})
    @Override
    public void btnClick(View view) {
        switch (view.getId()) {
            case R.id.mine_setting:
                //设置
                startActivity(MineSettingActivity.class, null);
                break;
            case R.id.ll_data:
                //我的资料
                Bundle bundle = new Bundle();
                bundle.putString("getMinePhone", getMinePhone);
                bundle.putString("getMineImge", getMineImge);
                bundle.putString("rawStaffPortraitUrl", rawStaffPortraitUrl);
                startActivity(MineDataActivity.class, bundle);
                break;
            case R.id.tv_three:
                //第三方代发
                startActivity(MineThreeSendActivity.class, null);
                break;
            case R.id.tv_take:
                //我的取件
                startActivity(MineTakeAty.class, null);
                break;
            case R.id.tv_not_receive_package:
                //待收包裹
                startActivity(MineNotReceivePackageAty.class, null);
                break;
            case R.id.tv_send_package:
                //发出包裹
                startActivity(MineSendPackageActivity.class, null);
                break;
            case R.id.tv_have_send_file:
                //签收快件
                startActivity(MineHaveSendFileAty.class, null);
                break;
            case R.id.tv_exception_file:
                //异常快件
                startActivity(MineExceptionActivity.class, null);
                break;
            case R.id.tv_customer_evaluate:
                //客户评价
                startActivity(MineCustomerEvaluateActivity.class, null);
                break;
            case R.id.feedback:
                //意见反馈
                startActivity(MineFeedbackActivity.class, null);
                break;
        }
    }

    @Override
    public void onSuccess(String result, Call<ResponseBody> call, Response<ResponseBody> response, int what) {
        super.onSuccess(result, call, response, what);
        onLoadingData = false;
        switch (what) {
            case 1:
                //手机号码
                getMinePhone = new StringBuilder().append(AppJsonUtil.getString(AppJsonUtil.getString(result, "data"), "staffTephone")).toString();
                long getMinePhoneLong = Long.valueOf(getMinePhone);
                UserManger.setUserPhone(getMinePhone);
                //所属网点
                String getMineSpot = new StringBuilder().append(AppJsonUtil.getString(AppJsonUtil.getString(result, "data"), "basicName")).toString();

                //上次登录时间
                String getMineLoginDate = null;
                if (AppJsonUtil.getLong(AppJsonUtil.getString(result, "data"), "recordTime") == 0) {
                    //上次登录时间
                    getMineLoginDate = new StringBuilder().append(DateTool.timesToStrTime(System.currentTimeMillis()
                            + "", "yyyy.MM.dd HH:mm")).toString();
                } else {
                    //上次登录时间
                    getMineLoginDate = new StringBuilder().append(DateTool.timesToStrTime(AppJsonUtil.getLong(AppJsonUtil.getString(result, "data"), "recordTime")
                            + "", "yyyy.MM.dd HH:mm")).toString();
                }


                //图片路径
                getMineImge = new StringBuilder().append(AppJsonUtil.getString(AppJsonUtil.getString(result, "data"), "staffPortrait")).toString();
                //网点责任人账户余额
                mineYeNew = new StringBuilder().append(AppJsonUtil.getString(AppJsonUtil.getString(result, "data"), "staffAccount")).toString();
                rawStaffPortraitUrl = AppJsonUtil.getString(AppJsonUtil.getString(result, "data"), "rawStaffPortraitUrl");
                minePhone.setText(MyNumberFormat.toPwdPhone(getMinePhoneLong));
                mineSpot.setText(getMineSpot);
                minLoginDate.setText("上次登录时间：" + getMineLoginDate);
                mineYe.setText(mineYeNew+"元");
                if (isChangeImage) {
                    Uri uri = Uri.parse(getMineImge);
                    mineImg.setImageURI(uri);
                    isChangeImage = false;
                }

                break;
        }
    }

    @Override
    public void onError(Call<ResponseBody> call, Throwable t, int what) {
        super.onError(call, t, what);
        onLoadingData = false;

    }

    @Override
    public void onFailure(String result, Call<ResponseBody> call, Response<ResponseBody> response, int what) {
        super.onFailure(result, call, response, what);
        onLoadingData = false;
    }
}
