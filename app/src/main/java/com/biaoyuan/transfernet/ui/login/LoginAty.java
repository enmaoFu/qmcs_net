package com.biaoyuan.transfernet.ui.login;

import android.Manifest;
import android.content.Context;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.amap.api.location.AMapLocation;
import com.and.yzy.frame.util.AppManger;
import com.and.yzy.frame.util.RetrofitUtils;
import com.biaoyuan.transfernet.R;
import com.biaoyuan.transfernet.base.BaseAty;
import com.biaoyuan.transfernet.config.UserManger;
import com.biaoyuan.transfernet.http.Login;
import com.biaoyuan.transfernet.ui.MainAty;
import com.biaoyuan.transfernet.ui.mine.MineFgt;
import com.biaoyuan.transfernet.util.AppJsonUtil;
import com.biaoyuan.transfernet.util.JPushSetAliasUtil;
import com.biaoyuan.transfernet.util.MyLocationUtil;
import com.orhanobut.logger.Logger;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RationaleListener;

import java.util.List;
import java.util.UUID;

import butterknife.Bind;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Title  : 登录页面
 * Create : 2017/5/22
 * Author ：enmaoFu
 */
public class LoginAty extends BaseAty {

    @Bind(R.id.et_name)
    EditText mEtName;
    @Bind(R.id.et_pwd)
    EditText mEtPwd;

    private String mName;
    private String mPwd;

    @Override
    public int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    public void initData() {
        if (!TextUtils.isEmpty(UserManger.getAcount())) {

            mEtName.setText(UserManger.getAcount());
            mEtPwd.setText(UserManger.getPwd());

        }
        MineFgt.isChangeImage = true;
    }

    @Override
    public boolean setIsInitRequestData() {
        return false;
    }

    @Override
    public void requestData() {

    }

    @OnClick({R.id.login, R.id.forget_pwd, R.id.delete})
    @Override
    public void btnClick(View view) {
        switch (view.getId()) {
            case R.id.login:
                mName = mEtName.getText().toString().trim();
                mPwd = mEtPwd.getText().toString().trim();
                if (mName.length() == 0) {
                    showErrorToast("请输入手机号或用户名");
                    mEtName.requestFocus();
                    return;
                }
                if (mPwd.length() < 8) {
                    showErrorToast("密码长度不对");
                    mEtPwd.requestFocus();
                    return;
                }

                opCheckPermission();

                break;
            case R.id.forget_pwd:
                startActivity(FindPasswordActvity.class, null);
                break;
            case R.id.delete:
                setHasAnimiation(false);
                AppManger.getInstance().AppExit(this);
                break;

        }
    }

    @Override
    public void onSuccess(String result, Call<ResponseBody> call, Response<ResponseBody> response, int what) {
        super.onSuccess(result, call, response, what);
        switch (what) {
            case 1:
                //登录成功
                showToast("登录成功");
                UserManger.setIsLogin(true);

                UserManger.setAcount(mEtName.getText().toString().trim());
                UserManger.setPwd(mEtPwd.getText().toString().trim());

                //保存token uuid
                UserManger.setToken(AppJsonUtil.getString(AppJsonUtil.getString(AppJsonUtil.getString(result, "data"), "appReturnData"), "token"));
                UserManger.setUUid(AppJsonUtil.getString(AppJsonUtil.getString(AppJsonUtil.getString(result, "data"), "appReturnData"), "uuid"));
                UserManger.setAddress(AppJsonUtil.getString(AppJsonUtil.getString(AppJsonUtil.getString(result, "data"), "AffiliateBasicBean"), "basicName"));
                UserManger.setPhone(AppJsonUtil.getString(AppJsonUtil.getString(AppJsonUtil.getString(result, "data"), "AffiliateBasicBean"), "outBasicTelphone"));
                UserManger.setBaseId(AppJsonUtil.getString(AppJsonUtil.getString(AppJsonUtil.getString(result, "data"), "AffiliateBasicBean"), "basicId2"));
                UserManger.setStaffIsContractors(AppJsonUtil.getInt(AppJsonUtil.getString(result, "data"), "staffIsContractor"));

                if (!TextUtils.isEmpty(AppJsonUtil.getString(AppJsonUtil.getString(result, "data"), "staffIdentityNo")))
                    UserManger.setStaffIdentityNo(AppJsonUtil.getString(AppJsonUtil.getString(result, "data"), "staffIdentityNo"));

                //Logger.v(AppJsonUtil.getString(AppJsonUtil.getString(AppJsonUtil.getString(result,"data"),"AffiliateBasicBean"),"basicName"));
                //Logger.v(AppJsonUtil.getString(AppJsonUtil.getString(result,"data"),"staffIsContractor"));
                Logger.v(UserManger.getToken() + "--------------" + UserManger.getUUid());

          //      RetrofitUtils.init(UserManger.getURL());


                //TODO 此处设置别名应该在Application里
                JPushSetAliasUtil jPushSetAliasUtil = new JPushSetAliasUtil(this);
                jPushSetAliasUtil.setAlias();


                if (AppManger.getInstance().isAddActivity(MainAty.class)) {
                    finish();
                } else {
                    setHasAnimiation(false);
                    startActivity(MainAty.class, null);
                    overridePendingTransition(R.anim.aty_in, R.anim.activity_alpha_out);
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            finish();
                        }
                    }, 2000);
                }
                break;

        }
    }


    // 权限
    public void opCheckPermission() {


        // 申请多个权限。
        AndPermission.with(this)
                .requestCode(200)
                .permission(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_PHONE_STATE
                )
                // rationale作用是：用户拒绝一次权限，再次申请时先征求用户同意，再打开授权对话框，避免用户勾选不再提示。
                .rationale(new RationaleListener() {
                               @Override
                               public void showRequestPermissionRationale(int requestCode, Rationale rationale) {
                                   // 这里的对话框可以自定义，只要调用rationale.resume()就可以继续申请。
                                   AndPermission.rationaleDialog(LoginAty.this, rationale).show();
                               }
                           }
                )
                .send();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        // 只需要调用这一句，其它的交给AndPermission吧，最后一个参数是PermissionListener。
        AndPermission.onRequestPermissionsResult(requestCode, permissions, grantResults, listener);
    }

    private PermissionListener listener = new PermissionListener() {
        @Override
        public void onSucceed(int requestCode, List<String> grantedPermissions) {
            // 权限申请成功回调。

            if (requestCode == 200) {


                TelephonyManager tm = (TelephonyManager) getBaseContext()
                        .getSystemService(Context.TELEPHONY_SERVICE);

                String tmDevice = "" + tm.getDeviceId();
                String tmSerial = "" + tm.getSimSerialNumber();
                String androidId = ""
                        + android.provider.Settings.Secure.getString(
                        getContentResolver(),
                        android.provider.Settings.Secure.ANDROID_ID);
                UUID deviceUuid = new UUID(androidId.hashCode(),
                        ((long) tmDevice.hashCode() << 32)
                                | tmSerial.hashCode());
                System.out.println(deviceUuid.toString().length());
                final String uniqueId = deviceUuid.toString();


                showLoadingDialog(null);

                new MyLocationUtil(LoginAty.this, new MyLocationUtil.MyLocationListener() {
                    @Override
                    public void sussessLocation(double lat, double lon, AMapLocation location) {

                        UserManger.setLat(lat + "");
                        UserManger.setLng(lon + "");
                        doHttp(RetrofitUtils.createApi(Login.class).login(mName, mPwd,
                                lon, lat, location.getCity() + location.getCountry() + location.getStreet(), uniqueId), 1);

                    }

                    @Override
                    public void error() {
                        doHttp(RetrofitUtils.createApi(Login.class).login(mName, mPwd,
                                0, 0, "", uniqueId), 1);
                    }
                });


            }


        }

        @Override
        public void onFailed(int requestCode, List<String> deniedPermissions) {
            showLoadingDialog(null);
            doHttp(RetrofitUtils.createApi(Login.class).login(mName, mPwd,
                    0, 0, "", ""), 1);

        }
    };
}
