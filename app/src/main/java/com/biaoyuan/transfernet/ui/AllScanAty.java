package com.biaoyuan.transfernet.ui;

import android.Manifest;
import android.content.Intent;
import android.os.Vibrator;
import android.view.View;

import com.biaoyuan.transfernet.R;
import com.biaoyuan.transfernet.base.BaseAty;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RationaleListener;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import cn.bingoogolapple.qrcode.core.QRCodeView;

/**
 * Title  : 二维码扫描
 * Create : 2017/5/9
 * Author ：chen
 */
public class AllScanAty extends BaseAty implements QRCodeView.Delegate {
    @Bind(R.id.zxingview)
    QRCodeView mQRCodeView;

    private int key;

    @Override
    public boolean setIsInitRequestData() {
        return false;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fgt_receive_scan;
    }

    @Override
    public void initData() {

        mQRCodeView.setDelegate(this);

        //权限控制
        opCheckPermission();


    }

    @OnClick({R.id.img_return, R.id.tv_go_input})
    @Override
    public void btnClick(View view) {
        super.btnClick(view);
        switch (view.getId()) {
            case R.id.img_return:
                finish();
                break;
            case R.id.tv_go_input:
                startActivityForResult(InputCodeAty.class, getIntent().getExtras(), 1);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && data != null) {
            setResult(RESULT_OK, data);
            finish();
        }
    }

    // 权限
    public void opCheckPermission() {


        // 申请多个权限。
        AndPermission.with(this)
                .requestCode(200)
                .permission(Manifest.permission.CAMERA
                )
                // rationale作用是：用户拒绝一次权限，再次申请时先征求用户同意，再打开授权对话框，避免用户勾选不再提示。
                .rationale(new RationaleListener() {
                               @Override
                               public void showRequestPermissionRationale(int requestCode, Rationale rationale) {
                                   // 这里的对话框可以自定义，只要调用rationale.resume()就可以继续申请。
                                   AndPermission.rationaleDialog(AllScanAty.this, rationale).show();
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
                mQRCodeView.showScanRect();
                mQRCodeView.startSpot();
            }
        }

        @Override
        public void onFailed(int requestCode, List<String> deniedPermissions) {
            showErrorToast("没有相机权限");
        }
    };


    @Override
    public void requestData() {

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mQRCodeView != null) {
            mQRCodeView.onDestroy();
        }

    }

    private void vibrate() {
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(200);
    }

    @Override
    public void onScanQRCodeSuccess(String result) {

        vibrate();
        Intent intent = getIntent();
        intent.putExtra("codeResult", result);
        setResult(RESULT_OK, intent);
        finish();


    }

    @Override
    public void onScanQRCodeOpenCameraError() {

    }
}
