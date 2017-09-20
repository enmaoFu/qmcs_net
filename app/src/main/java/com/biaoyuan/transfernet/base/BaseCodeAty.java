package com.biaoyuan.transfernet.base;

import android.Manifest;
import android.os.Bundle;
import android.widget.EditText;

import com.biaoyuan.transfernet.util.sms.SmsObserver;
import com.biaoyuan.transfernet.util.sms.SmsResponseCallback;
import com.biaoyuan.transfernet.util.sms.VerificationCodeSmsFilter;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RationaleListener;

import java.util.List;

/**
 * Title  :
 * Create : 2017/7/6
 * Author ：chen
 */

public abstract class BaseCodeAty extends BaseAty {


    private SmsObserver mSmsObserver;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //检查权限
        opCheckPermission();

    }


    // 权限
    public void opCheckPermission() {


        // 申请多个权限。
        AndPermission.with(this)
                .requestCode(200)
                .permission(
                        Manifest.permission.READ_SMS
                )
                // rationale作用是：用户拒绝一次权限，再次申请时先征求用户同意，再打开授权对话框，避免用户勾选不再提示。
                .rationale(new RationaleListener() {
                               @Override
                               public void showRequestPermissionRationale(int requestCode, Rationale rationale) {
                                   // 这里的对话框可以自定义，只要调用rationale.resume()就可以继续申请。
                                   AndPermission.rationaleDialog(BaseCodeAty.this, rationale).show();
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
                /***
                 * 构造器
                 * @param context
                 * @param callback 短信接收器
                 * @param smsFilter 短信过滤器
                 */
                //这里接收短信
                mSmsObserver = new SmsObserver(BaseCodeAty.this, new SmsResponseCallback() {
                    @Override
                    public void onCallbackSmsContent(String smsContent) {
                        //这里接收短信
                        if (getEditTextView()!=null)
                        getEditTextView().setText(smsContent);
                    }
                }, new VerificationCodeSmsFilter("全民传送"));

                mSmsObserver.registerSMSObserver();
            }
        }

        @Override
        public void onFailed(int requestCode, List<String> deniedPermissions) {


        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mSmsObserver != null) {
            mSmsObserver.unregisterSMSObserver();
        }
    }

    public abstract EditText getEditTextView();
}
