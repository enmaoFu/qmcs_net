package com.biaoyuan.transfernet.ui.login;

import android.Manifest;
import android.os.Handler;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.and.yzy.frame.util.RetrofitUtils;
import com.biaoyuan.transfernet.R;
import com.biaoyuan.transfernet.base.BaseAty;
import com.biaoyuan.transfernet.config.UserManger;
import com.biaoyuan.transfernet.ui.MainAty;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RationaleListener;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Title  :
 * Create : 2017/7/3
 * Author ：chen
 */

public class TestAty extends BaseAty {
    @Bind(R.id.toolbar)
    Toolbar mToolbar;


    @Override
    public int getLayoutId() {
        return R.layout.test_layout;
    }

    @Override
    public void initData() {

        initToolbar(mToolbar, "选择服务器");
        opCheckPermission();

    }

    // 权限
    public void opCheckPermission() {


        // 申请多个权限。
        AndPermission.with(this)
                .requestCode(200)
                .permission(
                        Manifest.permission.CAMERA,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.CALL_PHONE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.READ_SMS
                )
                // rationale作用是：用户拒绝一次权限，再次申请时先征求用户同意，再打开授权对话框，避免用户勾选不再提示。
                .rationale(new RationaleListener() {
                               @Override
                               public void showRequestPermissionRationale(int requestCode, Rationale rationale) {
                                   // 这里的对话框可以自定义，只要调用rationale.resume()就可以继续申请。
                                   AndPermission.rationaleDialog(TestAty.this, rationale).show();
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

        }

        @Override
        public void onFailed(int requestCode, List<String> deniedPermissions) {


        }
    };

    @OnClick({R.id.tv_zd, R.id.tv_syn, R.id.tv_cc, R.id.tv_tz, R.id.tv_200, R.id.tv_online,R.id.tv_online_test})
    @Override
    public void btnClick(View view) {
        super.btnClick(view);
        String url = null;
        switch (view.getId()) {
            case R.id.tv_zd:
                url = "http://192.168.2.4:8080/nw/";
                break;
            case R.id.tv_syn:
                url = "http://192.168.2.17:8081/nw/";
                break;
            case R.id.tv_cc:
                url = "http://192.168.2.33:8080/qmcs-nw/";

                break;
            case R.id.tv_tz:
                url = "http://192.168.2.21/";
                break;
            case R.id.tv_200:
                url = "http://192.168.2.200:8080/qmcs2-nw/";
                break;
            case R.id.tv_online_test:
                url = "http://www2.qmcs-china.com/qmcs2-nw/";
                break;
            case R.id.tv_online:
                url = "http://m2.qmcs-china.com/";
                break;
        }
        UserManger.setURL(url);
       RetrofitUtils.init(url);
        setHasAnimiation(false);
        if (UserManger.isLogin()) {
            startActivity(MainAty.class, null);
        } else {
            startActivity(LoginAty.class, null);
        }
        overridePendingTransition(R.anim.aty_in, R.anim.activity_alpha_out);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, 2000);
    }

    @Override
    public void requestData() {

    }
}
