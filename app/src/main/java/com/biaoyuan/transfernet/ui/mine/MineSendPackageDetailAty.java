package com.biaoyuan.transfernet.ui.mine;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.and.yzy.frame.util.DateTool;
import com.and.yzy.frame.util.RetrofitUtils;
import com.and.yzy.frame.view.dialog.MaterialDialog;
import com.and.yzy.frame.view.listview.ListViewForScrollView;
import com.biaoyuan.transfernet.R;
import com.biaoyuan.transfernet.adapter.WuNiuAdapter;
import com.biaoyuan.transfernet.base.BaseAty;
import com.biaoyuan.transfernet.domain.SendTakeInfo;
import com.biaoyuan.transfernet.domain.WuNiuItem;
import com.biaoyuan.transfernet.domain.WuNiuItemNew;
import com.biaoyuan.transfernet.http.Mine;
import com.biaoyuan.transfernet.ui.send.SendTakeFgt.SendTakeItemDetailsActivity;
import com.biaoyuan.transfernet.util.AppJsonUtil;
import com.orhanobut.logger.Logger;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RationaleListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

import static com.biaoyuan.transfernet.config.UserManger.pageSize;

/**
 * Title  :我发出的包裹,待收包裹详情
 * Create : 2017/5/18
 * Author ：chen
 */

public class MineSendPackageDetailAty extends BaseAty {

    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.lv_data)
    ListViewForScrollView mLvData;
    @Bind(R.id.angel_msg)
    TextView angelMsg;
    @Bind(R.id.fd_msg)
    TextView fdMsg;
    @Bind(R.id.fd_phone)
    TextView fdPhone;
    @Bind(R.id.db_code)
    TextView dbCode;
    @Bind(R.id.left_text)
    TextView leftText;
    @Bind(R.id.right_text)
    TextView rightText;

    private String key;
    private String key1;
    private String packageId;
    WuNiuAdapter mAdapter;
    private int type;
    private String getAngleMsg;
    private String getFdPhone;

    @Override
    public int getLayoutId() {
        return R.layout.activity_mine_send_package_detail;
    }

    @Override
    public void initData() {

        Bundle bundle = getIntent().getExtras();
        key = bundle.getString("key");
        key1 = bundle.getString("key1");
        packageId = bundle.getString("packageId");

        if(key1.equals("wait")){
            initToolbar(mToolbar,"待送达");
        }else if(key1.equals("already")){
            initToolbar(mToolbar,"已送达");
        }else if(key1.equals("dsbg")){
            initToolbar(mToolbar,"待接收");
        }
        mAdapter=new WuNiuAdapter(this,new ArrayList<WuNiuItemNew>(),R.layout.item_wuniu);
        mLvData.setAdapter(mAdapter);
    }

    @Override
    public boolean setIsInitRequestData() {
        return true;
    }

    @Override
    public void requestData() {
        showLoadingContentDialog();
        doHttp(RetrofitUtils.createApi(Mine.class).myParcelIsSentDetail(packageId),1);
    }

    @OnClick({R.id.query_details,R.id.angel_msg,R.id.fd_phone})
    @Override
    public void btnClick(View view) {
        switch (view.getId()){
            case R.id.query_details:
                Bundle bundle = new Bundle();
                bundle.putString("key",key);
                bundle.putString("packageId",packageId);
                startActivity(MineSendPackageDateilsTwoActivity.class,bundle);
                break;
            case R.id.angel_msg:
                type = 0;
                opCheckPermission();
                break;
            case R.id.fd_phone:
                type = 1;
                opCheckPermission();
                break;
        }
    }

    @Override
    public void onSuccess(String result, Call<ResponseBody> call, Response<ResponseBody> response, int what) {
        super.onSuccess(result, call, response, what);
        switch (what){
            case 1:
                //传送天使信息
                getAngleMsg = new StringBuilder().append(AppJsonUtil.getString(AppJsonUtil.getString(result,"data"),"user_telphone")).toString();
                //网点信息
                String getFdMsg = new StringBuilder().append(AppJsonUtil.getString(AppJsonUtil.getString(result,"data"),"send_affil_name")).toString();
                //网点电话
                getFdPhone = new StringBuilder().append(AppJsonUtil.getString(AppJsonUtil.getString(result,"data"),"basic_telphone")).toString();
                //打包码
                String getDbCode = new StringBuilder().append(AppJsonUtil.getString(AppJsonUtil.getString(result,"data"),"package_code")).toString();
                //发出地址
                String getLeftText = new StringBuilder().append(AppJsonUtil.getString(AppJsonUtil.getString(result,"data"),"send_affil_name")).toString().replace("|","");
                //到达地址
                String getRightText = new StringBuilder().append(AppJsonUtil.getString(AppJsonUtil.getString(result,"data"),"receive_affil_name")).toString().replace("|","");
                //传送信息
                List<WuNiuItem> wuNiuItemList = AppJsonUtil.getArrayList(result, "parcel_delivery_record", WuNiuItem.class);
                List<WuNiuItemNew> wuNiuItemListNew = new ArrayList<>();
                for(WuNiuItem wni:wuNiuItemList){
                    WuNiuItemNew wnin = new WuNiuItemNew();
                    if(wni.getRecordValue().equals("网点封包已完成")){
                        String date = DateTool.timesToStrTime(wni.getRecordTime(),"yyyy.MM.dd HH:mm");
                        String year = date.substring(0,10);
                        String hm = date.substring(date.length() - 6,date.length());
                        wnin.setYear(year);
                        wnin.setDate(hm);
                        wnin.setContent(wni.getRecordValue());
                        wuNiuItemListNew.add(0,wnin);
                    }else if(wni.getRecordValue().equals("传送")){
                        String date = DateTool.timesToStrTime(wni.getRecordTime(),"yyyy.MM.dd HH:mm");
                        String year = date.substring(0,10);
                        String hm = date.substring(date.length() - 6,date.length());
                        wnin.setYear(year);
                        wnin.setDate(hm);
                        wnin.setContent("传送员" + "【" + wni.getUserLoginName() + "】" + wni.getRecordValue() + "中" + ",联系电话" + "【" + wni.getUserTelphone() + "】");
                        wuNiuItemListNew.add(0,wnin);
                    }else if(wni.getRecordValue().equals("验收")){
                        String date = DateTool.timesToStrTime(wni.getRecordTime(),"yyyy.MM.dd HH:mm");
                        String year = date.substring(0,10);
                        String hm = date.substring(date.length() - 6,date.length());
                        wnin.setYear(year);
                        wnin.setDate(hm);
                        wnin.setContent("网点" + "【" + wni.getUserLoginName() + "】" + "已" + wni.getRecordValue() + ",联系电话" + "【" + wni.getUserTelphone() + "】");
                        wuNiuItemListNew.add(0,wnin);
                    }
                }
                angelMsg.setText(getAngleMsg);
                fdMsg.setText(getFdMsg);
                fdPhone.setText(getFdPhone);
                dbCode.setText(getDbCode);
                leftText.setText(getLeftText);
                rightText.setText(getRightText);
                mAdapter.removeAll();
                if (wuNiuItemList != null) {
                    if(wuNiuItemList.size() != 0){
                        mAdapter.addAll(wuNiuItemListNew);
                    }
                }
                break;
        }
    }

    // 权限
    public void opCheckPermission() {


        // 申请多个权限。
        AndPermission.with(this)
                .requestCode(200)
                .permission(Manifest.permission.CALL_PHONE
                )
                // rationale作用是：用户拒绝一次权限，再次申请时先征求用户同意，再打开授权对话框，避免用户勾选不再提示。
                .rationale(new RationaleListener() {
                               @Override
                               public void showRequestPermissionRationale(int requestCode, Rationale rationale) {
                                   // 这里的对话框可以自定义，只要调用rationale.resume()就可以继续申请。
                                   AndPermission.rationaleDialog(MineSendPackageDetailAty.this, rationale).show();
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


                if (type == 0) {
                    new MaterialDialog(MineSendPackageDetailAty.this)
                            .setMDMessage("是否立即拨打传送天使电话?")
                            .setMDConfirmBtnClick(new MaterialDialog.DialogBtnCallBack() {
                                @Override
                                public void dialogBtnOnClick() {
                                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + getAngleMsg));
                                    startActivity(intent);
                                }
                            }).show();
                }else if(type == 1){
                    new MaterialDialog(MineSendPackageDetailAty.this)
                    .setMDMessage("是否立即拨打网点电话?")
                            .setMDConfirmBtnClick(new MaterialDialog.DialogBtnCallBack() {
                                @Override
                                public void dialogBtnOnClick() {
                                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + getAngleMsg));
                                    startActivity(intent);
                                }
                            }).show();
                }

            }

        }

        @Override
        public void onFailed(int requestCode, List<String> deniedPermissions) {
            // initMap();
            showErrorToast("未授权");

        }
    };

}
