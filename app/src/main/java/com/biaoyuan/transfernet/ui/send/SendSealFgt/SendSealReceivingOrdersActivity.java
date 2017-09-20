package com.biaoyuan.transfernet.ui.send.SendSealFgt;

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
import com.biaoyuan.transfernet.R;
import com.biaoyuan.transfernet.base.BaseAty;
import com.biaoyuan.transfernet.http.Seal;
import com.biaoyuan.transfernet.util.AppJsonUtil;
import com.biaoyuan.transfernet.util.MyNumberFormat;
import com.biaoyuan.transfernet.view.RequestResultDialogHelper;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RationaleListener;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

/**
 * @author :enmaoFu
 * @title :封包-》待接单详情页面
 * @create :2017/5/15
 */
public class SendSealReceivingOrdersActivity extends BaseAty {

    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.oders_details)
    TextView odersDetails;
    @Bind(R.id.oders_details1)
    TextView odersDetails1;
    @Bind(R.id.but)
    TextView but;
    @Bind(R.id.date)
    TextView date;
    @Bind(R.id.weight_size)
    TextView theWeightSize;
    @Bind(R.id.take_spot)
    TextView takeSpot;
    @Bind(R.id.send_spot)
    TextView sendSpot;
    @Bind(R.id.take_date)
    TextView takeDate;
    @Bind(R.id.receive_date)
    TextView receiveDate;
    @Bind(R.id.price)
    TextView price;
    private String packageId;
    private String value;
    private String basicCode;
    private String basicName;
    private String mPhone;

    private long requiredTimeNew;
    private long requiredTimeSendNew;
    private double getPriceDoubleNew;
    private double basePrice;

    @Override
    public int getLayoutId() {
        return R.layout.activity_send_seal_orders;
    }

    @Override
    public void initData() {
        Bundle bundle = getIntent().getExtras();
        value = bundle.getString("value");
        packageId = bundle.getString("packageId");
        basicCode = bundle.getString("basicCode");
        basicName = bundle.getString("basicName");
        basePrice = bundle.getDouble("basePrice");
        if (value.equals("wait")) {
            initToolbar(mToolbar, "待接单详情");
            odersDetails.setText("暂时无人接单");
            odersDetails.setVisibility(View.VISIBLE);
            but.setText("我要加价");
        } else if (value.equals("already")) {
            initToolbar(mToolbar, "已接单详情");
            odersDetails1.setVisibility(View.VISIBLE);
            but.setText("封包扫描");
        } else {
            initToolbar(mToolbar, "再次发布");
            requiredTimeNew = Long.parseLong(bundle.getString("requiredTime"));
            requiredTimeSendNew = Long.parseLong(bundle.getString("requiredTimeSend"));
            getPriceDoubleNew = Double.parseDouble(bundle.getString("getPriceDouble"));
            odersDetails.setText("暂时无人接单");
            odersDetails.setVisibility(View.VISIBLE);
            but.setText("确认发布");
        }
    }

    @Override
    public boolean setIsInitRequestData() {
        return true;
    }

    @Override
    public void requestData() {
        showLoadingContentDialog();
        if (value.equals("wait")) {
            doHttp(RetrofitUtils.createApi(Seal.class).getIndexPacketDetail(packageId, 1), 1);
        } else if (value.equals("already")) {
            doHttp(RetrofitUtils.createApi(Seal.class).getIndexPacketDetail(packageId, 2), 4);
        } else {
            doHttp(RetrofitUtils.createApi(Seal.class).getIndexPacketDetail(packageId, 1), 3);
        }
    }

    @OnClick({R.id.but})
    @Override
    public void btnClick(View view) {
        switch (view.getId()) {
            case R.id.but:
                Bundle bundle = new Bundle();
                if (value.equals("wait")) {
                    bundle.putString("packageId", packageId);
                    bundle.putDouble("basePrice",basePrice);
                    startActivity(SendSealAddPriceActivity.class, bundle);
                } else if (value.equals("already")) {
                    Bundle bundle1 = new Bundle();
                    bundle1.putString("packageId", packageId);
                    bundle1.putString("basicCode", basicCode);
                    bundle1.putString("basicName", basicName);
                    startActivity(SendSealPackingScanningActivity.class, bundle1);
                } else {
                    doHttp(RetrofitUtils.createApi(Seal.class).releaseAgain(packageId, requiredTimeNew, requiredTimeSendNew, getPriceDoubleNew), 2);
                }
                break;
        }
    }

    @Override
    public void onSuccess(String result, Call<ResponseBody> call, Response<ResponseBody> response, int what) {
        super.onSuccess(result, call, response, what);
        switch (what) {
            case 1:
                //发布时间
                String sendDate = new StringBuilder().append("发布时间：")
                        .append(DateTool.timesToStrTime(AppJsonUtil.getString(AppJsonUtil.getString(result, "data"), "publish_time") + "", "yyyy.MM.dd HH:mm"))
                        .toString();
                //体积大小
                String isWeightSize = new StringBuilder().append(AppJsonUtil.getString(AppJsonUtil.getString(result, "data"), "package_weight"))
                        .append("kg   最长边≤")
                        .append(AppJsonUtil.getString(AppJsonUtil.getString(result, "data"), "package_size"))
                        .append("cm")
                        .toString();
                //取件网点
                String getTakeSpot = new StringBuilder().append(AppJsonUtil.getString(AppJsonUtil.getString(result, "data"), "package_send_affil_name")).toString();
                //送达网点
                String getReceiveSpot = new StringBuilder().append(AppJsonUtil.getString(AppJsonUtil.getString(result, "data"), "package_receive_affil_name")).toString();


                String startTime = DateTool.getTimeType(Long.parseLong(AppJsonUtil.getString(AppJsonUtil.getString(result, "data"), "publish_req_pickup_time")));
                String endTime = DateTool.getTimeType(Long.parseLong(AppJsonUtil.getString(AppJsonUtil.getString(result, "data"), "publish_req_deliv_time")));

                if (startTime == null) {
                    takeDate.setText(DateTool.timesToStrTime(AppJsonUtil.getString(AppJsonUtil.getString(result, "data"), "publish_req_pickup_time"), "yyyy.MM.dd HH:mm"));
                } else {
                    takeDate.setText(startTime + DateTool.timesToStrTime(AppJsonUtil.getString(AppJsonUtil.getString(result, "data"), "publish_req_pickup_time"), "HH:mm") + "之前");
                }

                if (endTime == null) {
                    receiveDate.setText(DateTool.timesToStrTime(AppJsonUtil.getString(AppJsonUtil.getString(result, "data"), "publish_req_deliv_time"), "yyyy.MM.dd HH:mm"));
                } else {
                    receiveDate.setText(endTime + DateTool.timesToStrTime(AppJsonUtil.getString(AppJsonUtil.getString(result, "data"), "publish_req_deliv_time"), "HH:mm") + "之前");
                }


                //传送员收益
                double getPriceDouble = Double.parseDouble(AppJsonUtil.getString(AppJsonUtil.getString(result, "data"), "package_carrier_reward"));
                String getPrice = new StringBuilder().append(String.valueOf(getPriceDouble)).toString();
                date.setText(sendDate);
                theWeightSize.setText(isWeightSize);
                takeSpot.setText(getTakeSpot);
                sendSpot.setText(getReceiveSpot);
                price.setText(new StringBuilder().append("¥").append(MyNumberFormat.m2(Double.parseDouble(getPrice))).toString());
                break;
            case 2:
                final RequestResultDialogHelper dialogHelper = new RequestResultDialogHelper(this);

                dialogHelper.setType(RequestResultDialogHelper.PUBLISH, true).setDismissClick(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogHelper.dismiss();
                        finish();
                    }
                }).setCommitClick(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogHelper.dismiss();
                        finish();
                    }
                }).show();

                break;
            case 3:
                //发布时间
                String sendDateThree = new StringBuilder().append("发布时间：")
                        .append(DateTool.timesToStrTime(AppJsonUtil.getString(AppJsonUtil.getString(result, "data"), "publish_time") + "", "yyyy.MM.dd HH:mm"))
                        .toString();
                //体积大小
                String isWeightSizeThree = new StringBuilder().append(AppJsonUtil.getString(AppJsonUtil.getString(result, "data"), "package_weight"))
                        .append("kg   最长边≤")
                        .append(AppJsonUtil.getString(AppJsonUtil.getString(result, "data"), "package_size"))
                        .append("cm")
                        .toString();
                //取件网点
                String getTakeSpotThree = new StringBuilder().append(AppJsonUtil.getString(AppJsonUtil.getString(result, "data"), "package_send_affil_name")).toString();
                //送达网点
                String getReceiveSpotThree = new StringBuilder().append(AppJsonUtil.getString(AppJsonUtil.getString(result, "data"), "package_receive_affil_name")).toString();


                String startTimeThree = DateTool.getTimeType(Long.parseLong(AppJsonUtil.getString(AppJsonUtil.getString(result, "data"), "publish_req_pickup_time")));
                String endTimeThree = DateTool.getTimeType(Long.parseLong(AppJsonUtil.getString(AppJsonUtil.getString(result, "data"), "publish_req_deliv_time")));

                if (startTimeThree == null) {
                    takeDate.setText(DateTool.timesToStrTime(AppJsonUtil.getString(AppJsonUtil.getString(result, "data"), "publish_req_pickup_time"), "yyyy.MM.dd HH:mm"));
                } else {
                    takeDate.setText(startTimeThree + DateTool.timesToStrTime(AppJsonUtil.getString(AppJsonUtil.getString(result, "data"), "publish_req_pickup_time"), "HH:mm") + "之前");
                }

                if (endTimeThree == null) {
                    receiveDate.setText(DateTool.timesToStrTime(AppJsonUtil.getString(AppJsonUtil.getString(result, "data"), "publish_req_deliv_time"), "yyyy.MM.dd HH:mm"));
                } else {
                    receiveDate.setText(endTimeThree + DateTool.timesToStrTime(AppJsonUtil.getString(AppJsonUtil.getString(result, "data"), "publish_req_deliv_time"), "HH:mm") + "之前");
                }

                //传送员收益
                double getPriceDoubleThree = Double.parseDouble(AppJsonUtil.getString(AppJsonUtil.getString(result, "data"), "package_carrier_reward"));

                date.setText(sendDateThree);
                theWeightSize.setText(isWeightSizeThree);
                takeSpot.setText(getTakeSpotThree);
                sendSpot.setText(getReceiveSpotThree);
                price.setText(new StringBuilder().append("¥").append(MyNumberFormat.m2(getPriceDoubleNew).toString()));
                takeDate.setText(DateTool.timesToStrTime(String.valueOf(requiredTimeNew), "yyyy.MM.dd HH:mm") + "之前");
                receiveDate.setText(DateTool.timesToStrTime(String.valueOf(requiredTimeSendNew), "yyyy.MM.dd HH:mm") + "之前");
                break;
            case 4:
                //发布时间
                String sendDateFour = new StringBuilder().append("发布时间：")
                        .append(DateTool.timesToStrTime(AppJsonUtil.getString(AppJsonUtil.getString(result, "data"), "publish_time") + "", "yyyy.MM.dd HH:mm"))
                        .toString();
                //接单电话
                mPhone = new StringBuilder().append(AppJsonUtil.getString(AppJsonUtil.getString(result, "data"), "user_telphone")).toString();

                odersDetails1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        opCheckPermission();
                    }
                });

                //体积大小
                String isWeightSizeFour = new StringBuilder().append(AppJsonUtil.getString(AppJsonUtil.getString(result, "data"), "package_weight"))
                        .append("kg   最长边≤")
                        .append(AppJsonUtil.getString(AppJsonUtil.getString(result, "data"), "package_size"))
                        .append("cm")
                        .toString();
                //取件网点
                String getTakeSpotFour = new StringBuilder().append(AppJsonUtil.getString(AppJsonUtil.getString(result, "data"), "package_send_affil_name")).toString();
                //送达网点
                String getReceiveSpotFour = new StringBuilder().append(AppJsonUtil.getString(AppJsonUtil.getString(result, "data"), "package_receive_affil_name")).toString();

                String startTimeFour = DateTool.getTimeType(Long.parseLong(AppJsonUtil.getString(AppJsonUtil.getString(result, "data"), "publish_req_pickup_time")));
                String endTimeFour = DateTool.getTimeType(Long.parseLong(AppJsonUtil.getString(AppJsonUtil.getString(result, "data"), "publish_req_deliv_time")));

                if (startTimeFour == null) {
                    takeDate.setText(DateTool.timesToStrTime(AppJsonUtil.getString(AppJsonUtil.getString(result, "data"), "publish_req_pickup_time"), "yyyy.MM.dd HH:mm"));
                } else {
                    takeDate.setText(startTimeFour + DateTool.timesToStrTime(AppJsonUtil.getString(AppJsonUtil.getString(result, "data"), "publish_req_pickup_time"), "HH:mm") + "之前");
                }

                if (endTimeFour == null) {
                    receiveDate.setText(DateTool.timesToStrTime(AppJsonUtil.getString(AppJsonUtil.getString(result, "data"), "publish_req_deliv_time"), "yyyy.MM.dd HH:mm"));
                } else {
                    receiveDate.setText(endTimeFour + DateTool.timesToStrTime(AppJsonUtil.getString(AppJsonUtil.getString(result, "data"), "publish_req_deliv_time"), "HH:mm") + "之前");
                }


                //传送员收益
                //String getPrice = new StringBuilder().append("¥ ").append(AppJsonUtil.getString(AppJsonUtil.getString(result,"data"),"package_carrier_reward")).toString();
                double getPriceDoubleFour = Double.parseDouble(AppJsonUtil.getString(AppJsonUtil.getString(result, "data"), "package_carrier_reward"));
                String getPriceFour = new StringBuilder().append("¥").append(MyNumberFormat.m2(getPriceDoubleFour)).toString();
                date.setText(sendDateFour);
                odersDetails1.setText("传送天使电话：" + mPhone);
                theWeightSize.setText(isWeightSizeFour);
                takeSpot.setText(getTakeSpotFour);
                sendSpot.setText(getReceiveSpotFour);
                price.setText(getPriceFour);
                break;
        }
    }

    @Override
    public void onFailure(String result, Call<ResponseBody> call, Response<ResponseBody> response, int what) {
        super.onFailure(result, call, response, what);
        switch (what) {
            case 2:
                final RequestResultDialogHelper dialogHelper = new RequestResultDialogHelper(this);

                dialogHelper.setType(RequestResultDialogHelper.PUBLISH, false).setDismissClick(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogHelper.dismiss();

                    }
                }).setCommitClick(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogHelper.dismiss();
                    }
                }).show();
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
                                   AndPermission.rationaleDialog(SendSealReceivingOrdersActivity.this, rationale).show();
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


                new MaterialDialog(SendSealReceivingOrdersActivity.this)
                        .setMDMessage("是否立即拨打传送天使电话?")
                        .setMDConfirmBtnClick(new MaterialDialog.DialogBtnCallBack() {
                            @Override
                            public void dialogBtnOnClick() {
                                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + mPhone));
                                startActivity(intent);
                            }
                        }).show();


            }

        }

        @Override
        public void onFailed(int requestCode, List<String> deniedPermissions) {
            // initMap();
            showErrorToast("未授权");

        }
    };
}
