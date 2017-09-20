package com.biaoyuan.transfernet.ui.mine;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.and.yzy.frame.util.DateTool;
import com.and.yzy.frame.util.RetrofitUtils;
import com.and.yzy.frame.view.dialog.MaterialDialog;
import com.biaoyuan.transfernet.R;
import com.biaoyuan.transfernet.base.BaseAty;
import com.biaoyuan.transfernet.domain.PriceRuleInfo;
import com.biaoyuan.transfernet.domain.SendTakeDetailsInfo;
import com.biaoyuan.transfernet.http.Send;
import com.biaoyuan.transfernet.ui.send.SendTakeFgt.SendMapAty;
import com.biaoyuan.transfernet.util.AppJsonUtil;
import com.biaoyuan.transfernet.util.MyNumberFormat;
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

/**
 * @author :enmaoFu
 * @title :我的取消详情页面
 * @create :2017/5/12
 */
public class MineTakeCancleDetalseActivity extends BaseAty {

    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    @Bind(R.id.sm)
    LinearLayout sm;
    @Bind(R.id.sm_hr)
    ImageView sm_hr;
    @Bind(R.id.order_id)
    TextView mOrderId;
    @Bind(R.id.order_date)
    TextView mOrderDate;
    @Bind(R.id.order_type)
    TextView mOrderType;
    @Bind(R.id.order_gg)
    TextView mOrderGg;
    @Bind(R.id.odrer_send_name)
    TextView mOdrerSendName;
    @Bind(R.id.order_send_phone)
    TextView mOrderSendPhone;
    @Bind(R.id.order_send_address)
    TextView mOrderSendAddress;
    @Bind(R.id.order_collect_name)
    TextView mOrderCollectName;
    @Bind(R.id.order_collect_phone)
    TextView mOrderCollectPhone;
    @Bind(R.id.order_collect_address)
    TextView mOrderCollectAddress;
    @Bind(R.id.order_take_date)
    TextView mOrderTakeDate;
    @Bind(R.id.order_price)
    TextView mOrderPrice;
    @Bind(R.id.send_name)
    TextView sendName;
    @Bind(R.id.send_addrese)
    TextView sendAddress;
    @Bind(R.id.send_tel)
    TextView sendTel;
    @Bind(R.id.textView)
    TextView but;

    private int orderId;
    private int staffId;
    private String orderSizeStr;
    private String orderWeightStr;
    private String orderPriceStr;
    private String orderIdStr;
    private int orderType;
    private List<PriceRuleInfo> priceRuleInfoList = new ArrayList<>();

    private int type = 0;
    private String orderSendPhoneStr;
    private double sendLng;
    private double sendLat;
    private double receiverLng;
    private double receiverLat;

    /**
     * 物品类型
     */
    private static final String GOODS_FILE = "文件";
    private static final String GOODS_OFFICE = "办公居家";
    private static final String GOODS_FLOWER = "鲜花";
    private static final String GOODS_CAKE = "蛋糕";
    private static final String GOODS_PACKAGE = "包裹";

    @Override
    public int getLayoutId() {
        return R.layout.activity_send_take_item_details;
    }

    @Override
    public void initData() {
        initToolbar(mToolbar, "取消详情");
        Bundle bundle = getIntent().getExtras();
        staffId = bundle.getInt("staffId");
        orderId = bundle.getInt("orderId");
        orderIdStr = String.valueOf(orderId);
        but.setVisibility(View.GONE);
    }

    @Override
    public boolean setIsInitRequestData() {
        return true;
    }

    @Override
    public void requestData() {
        showLoadingContentDialog();
        doHttp(RetrofitUtils.createApi(Send.class).staffConfirm(orderIdStr), 1);
    }

    @OnClick({R.id.send_tel_lin, R.id.map})
    @Override
    public void btnClick(View view) {
        switch (view.getId()) {
            case R.id.send_tel_lin:
                type = 0;
                opCheckPermission();
                break;
            case R.id.map:
                Bundle bundleNew = new Bundle();
                bundleNew.putDouble("startLat", sendLat);
                bundleNew.putDouble("startLng", sendLng);
                bundleNew.putDouble("endLat", receiverLat);
                bundleNew.putDouble("endLng", receiverLng);
                startActivity(SendMapAty.class, bundleNew);
                break;
        }
    }

    @Override
    public void onSuccess(String result, Call<ResponseBody> call, Response<ResponseBody> response, int what) {
        super.onSuccess(result, call, response, what);
        switch (what) {
            case 1:
                SendTakeDetailsInfo sendTakeDetailsInfo = AppJsonUtil.getObject(result, SendTakeDetailsInfo.class);
                //ID
                orderId = sendTakeDetailsInfo.getOrder_id();
                //订单号
                String orderId = sendTakeDetailsInfo.getOrder_no();
                //时间
                String orderDateStr = DateTool.timesToStrTime(sendTakeDetailsInfo.getOrder_time() + "", "yyyy.MM.dd-HH:mm:ss");
                //类型
                orderType = sendTakeDetailsInfo.getOrder_goods_type();
                //大小
                orderSizeStr = String.valueOf(sendTakeDetailsInfo.getOrder_goods_size());
                //重量
                orderWeightStr = String.valueOf(sendTakeDetailsInfo.getOrder_goods_weight());
                //取件时间
                long orderTakeDate = sendTakeDetailsInfo.getOrder_required_time();
                //订单金额
                orderPriceStr = String.valueOf(sendTakeDetailsInfo.getOrder_total_price());
                //发件人电话
                orderSendPhoneStr = String.valueOf(sendTakeDetailsInfo.getOrder_sender_tel());
                //收件人电话
                String orderCollectPhoneStr = String.valueOf(sendTakeDetailsInfo.getOrder_receiver_tel());

                //发件经度
                sendLng = sendTakeDetailsInfo.getOrder_send_lng();
                //发件维度
                sendLat = sendTakeDetailsInfo.getOrder_send_lat();
                //收件经度
                receiverLng = sendTakeDetailsInfo.getOrder_receive_lng();
                //收件维度
                receiverLat = sendTakeDetailsInfo.getOrder_receive_lat();

                priceRuleInfoList = AppJsonUtil.getArrayList(result, "setting", PriceRuleInfo.class);

                mOrderId.setText("订单编号：" + orderId);
                mOrderDate.setText("下单时间：" + orderDateStr);
                /**
                 * 物品类型 1: 文件； 2: 办公／居家； 3: 鲜花； 4: 包裹； 5: 蛋糕
                 */
                switch (orderType) {
                    case 1:
                        mOrderType.setText(GOODS_FILE);
                        break;
                    case 2:
                        mOrderType.setText(GOODS_OFFICE);
                        break;
                    case 3:
                        mOrderType.setText(GOODS_FLOWER);
                        break;
                    case 4:
                        mOrderType.setText(GOODS_PACKAGE);
                        break;
                    case 5:
                        mOrderType.setText(GOODS_CAKE);
                        break;
                }
                mOrderGg.setText("最长边≤" + orderSizeStr + "cm" + "     " + orderWeightStr + "kg");
                //发件人姓名
                mOdrerSendName.setText(sendTakeDetailsInfo.getOrder_sender_name());
                sendName.setText(sendTakeDetailsInfo.getOrder_sender_name());
                //发件人地址
                mOrderSendAddress.setText(sendTakeDetailsInfo.getOrder_send_addr().replace("|", ""));
                sendAddress.setText(sendTakeDetailsInfo.getOrder_send_addr().replace("|", ""));
                //发件人电话
                mOrderSendPhone.setText(orderSendPhoneStr);
                sendTel.setText("(" + orderSendPhoneStr + ")");
                //收件人姓名
                mOrderCollectName.setText(sendTakeDetailsInfo.getOrder_receiver_name());
                //收件人地址
                mOrderCollectAddress.setText(sendTakeDetailsInfo.getOrder_receive_addr().replace("|", ""));
                //收件人电话
                mOrderCollectPhone.setText(orderCollectPhoneStr);
                //取件时间
                if (DateTool.getTimeType(orderTakeDate) == null || DateTool.getTimeType(orderTakeDate).equals("") || DateTool.getTimeType(orderTakeDate).equals("null")) {
                    mOrderTakeDate.setText(DateTool.timesToStrTime(orderTakeDate + "", "yyyy.MM.dd HH:mm") + "取件");
                    //mOrderTakeDate.setVisibility(View.GONE);
                } else {
                    mOrderTakeDate.setText(DateTool.getTimeType(orderTakeDate) + DateTool.timesToStrTime(orderTakeDate + "", "HH:mm") + "取件");
                }


                //订单金额
                mOrderPrice.setText("¥" + MyNumberFormat.m2(Double.parseDouble(orderPriceStr)));
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
                                   AndPermission.rationaleDialog(MineTakeCancleDetalseActivity.this, rationale).show();
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
                    new MaterialDialog(MineTakeCancleDetalseActivity.this)
                            .setMDMessage("是否立即拨打寄件人电话?")
                            .setMDConfirmBtnClick(new MaterialDialog.DialogBtnCallBack() {
                                @Override
                                public void dialogBtnOnClick() {
                                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + orderSendPhoneStr));
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
