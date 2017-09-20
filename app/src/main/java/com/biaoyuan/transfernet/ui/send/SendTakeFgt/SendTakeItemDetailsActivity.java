package com.biaoyuan.transfernet.ui.send.SendTakeFgt;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.and.yzy.frame.util.DateTool;
import com.and.yzy.frame.util.RetrofitUtils;
import com.and.yzy.frame.view.dialog.MaterialDialog;
import com.biaoyuan.transfernet.R;
import com.biaoyuan.transfernet.base.BaseAty;
import com.biaoyuan.transfernet.config.UserManger;
import com.biaoyuan.transfernet.domain.PriceRuleInfo;
import com.biaoyuan.transfernet.domain.SendTakeDetailsInfo;
import com.biaoyuan.transfernet.http.Send;
import com.biaoyuan.transfernet.ui.AllScanAty;
import com.biaoyuan.transfernet.ui.InputCodeAty;
import com.biaoyuan.transfernet.util.AppJsonUtil;
import com.biaoyuan.transfernet.util.MyNumberFormat;
import com.biaoyuan.transfernet.view.MyAutoCompleteTextView;
import com.biaoyuan.transfernet.view.RequestResultDialogHelper;
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

import static com.biaoyuan.transfernet.R.id.map;

/**
 * @author :enmaoFu
 * @title :待确认详情页面
 * @create :2017/5/12
 */
public class SendTakeItemDetailsActivity extends BaseAty {

    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    @Bind(R.id.sm)
    LinearLayout sm;
    @Bind(R.id.sm_hr)
    ImageView sm_hr;
    @Bind(R.id.textView)
    TextView btn;
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

    @Bind(R.id.input_code)
    MyAutoCompleteTextView inputCode;

    private int orderId;
    private int orderType;
    private String orderNo;
    private List<PriceRuleInfo> priceRuleInfoList = new ArrayList<>();
    private String orderSizeStr;
    private String orderWeightStr;
    private String orderPriceStr;
    private String orderIdStr;
    private String orderSendPhoneStr;
    private long orderConfirmStaff;
    /**
     * 物品类型
     */
    private static final String GOODS_FILE = "文件";
    private static final String GOODS_OFFICE = "办公居家";
    private static final String GOODS_FLOWER = "鲜花";
    private static final String GOODS_CAKE = "蛋糕";
    private static final String GOODS_PACKAGE = "包裹";
    /*private static final String GOODS_CLOTHING = "衣物";
    private static final String GOODS_ELECTRONIC = "电子产品";
    private static final String GOODS_OTHER = "其他";*/

    private int orderStatus;
    private int type = 0;

    private double sendLng;
    private double sendLat;
    private double receiverLng;
    private double receiverLat;

    private int order_pay_status;

    @Override
    public int getLayoutId() {
        return R.layout.activity_send_take_item_details;
    }

    @Override
    public void initData() {

        ArrayAdapter arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, new String[]{"QMCS"});
        inputCode.setAdapter(arrayAdapter);
        inputCode.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(inputCode, InputMethodManager.SHOW_FORCED);
            }
        });


        Bundle bundle = getIntent().getExtras();
        orderStatus = bundle.getInt("orderStatus");
        orderId = bundle.getInt("orderId");
        orderIdStr = String.valueOf(orderId);
        orderNo = bundle.getString("orderNo");

        Log.v("print", "--------" + orderStatus);

        if (orderStatus == 1) {
            initToolbar(mToolbar, "待确认详情");
        } else {
            initToolbar(mToolbar, "扫描确认详情");
            sm.setVisibility(View.VISIBLE);
            sm_hr.setVisibility(View.VISIBLE);
            btn.setText("确认取件");
        }

    }

    boolean isFirst = true;

    @Override
    protected void onResume() {
        super.onResume();
        if (!isFirst) {
            doHttp(RetrofitUtils.createApi(Send.class).staffConfirm(orderNo), 1);
        }
        isFirst = false;

    }

    @Override
    public boolean setIsInitRequestData() {
        return true;
    }

    @Override
    public void requestData() {
        showLoadingContentDialog();
        // doHttp(RetrofitUtils.createApi(Send.class).staffConfirm(orderIdStr), 1);
        doHttp(RetrofitUtils.createApi(Send.class).staffConfirm(orderNo), 1);
    }


    @OnClick({R.id.textView, R.id.query_code, R.id.send_tel_lin, map})
    @Override
    public void btnClick(View view) {
        switch (view.getId()) {
            case R.id.textView:
                String orderIdStr = String.valueOf(orderId);
                String btnMessage = btn.getText().toString().trim();
                String code = inputCode.getText().toString().trim();
                if (btnMessage.equals("确认接件")) {
                    showLoadingDialog(null);
                    doHttp(RetrofitUtils.createApi(Send.class).confirmConnector(orderIdStr), 2);
                } else {
                    if(!String.valueOf(orderConfirmStaff).equals(UserManger.getUUid())){
                        showErrorToast(getString(R.string.no_order));
                        return;
                    }
                    if (!code.contains("QMCS") || code.length() < 16) {
                        showErrorToast("快件码不匹配");
                    } else {
                        showLoadingDialog(null);
                        doHttp(RetrofitUtils.createApi(Send.class).confirmTake(orderIdStr, Double.parseDouble(UserManger.getLng()),
                                Double.parseDouble(UserManger.getLat()), code), 3);
                    }
                }
                break;
            case R.id.query_code:
                if(!String.valueOf(orderConfirmStaff).equals(UserManger.getUUid())){
                    showErrorToast(getString(R.string.no_order));
                    return;
                }
                Bundle bunble = new Bundle();
                bunble.putInt("type", InputCodeAty.TYPE_TAKE);
                startActivityForResult(AllScanAty.class, bunble, 1);
                break;
            case R.id.send_tel_lin:
                type = 0;
                opCheckPermission();
                break;
            case map:
                Bundle bundleNew = new Bundle();
                bundleNew.putDouble("startLat", Double.parseDouble(UserManger.getLat()));
                bundleNew.putDouble("startLng", Double.parseDouble(UserManger.getLng()));
                bundleNew.putDouble("endLat", sendLat);
                bundleNew.putDouble("endLng", sendLng);
                startActivity(SendMapAty.class, bundleNew);
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
                                   AndPermission.rationaleDialog(SendTakeItemDetailsActivity.this, rationale).show();
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
                    new MaterialDialog(SendTakeItemDetailsActivity.this)
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

    String data;
    @Override
    public void onSuccess(String result, Call<ResponseBody> call, Response<ResponseBody> response, int what) {
        super.onSuccess(result, call, response, what);
        switch (what) {
            case 1:
                this.data=AppJsonUtil.getString(AppJsonUtil.getString(result,"data"),"setting");
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
                //支付状态
                order_pay_status = sendTakeDetailsInfo.getOrder_pay_status();
                //当前操作ID
                orderConfirmStaff = sendTakeDetailsInfo.getOrder_confirm_staff();

                priceRuleInfoList = AppJsonUtil.getArrayList(result, "setting", PriceRuleInfo.class);

                //发件经度
                sendLng = sendTakeDetailsInfo.getOrder_send_lng();
                //发件维度
                sendLat = sendTakeDetailsInfo.getOrder_send_lat();
                //收件经度
                receiverLng = sendTakeDetailsInfo.getOrder_receive_lng();
                //收件维度
                receiverLat = sendTakeDetailsInfo.getOrder_receive_lat();

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
                if (DateTool.getTimeType(orderTakeDate) == null) {
                    mOrderTakeDate.setText(DateTool.timesToStrTime(orderTakeDate + "", "yyyy.MM.dd HH:mm") + "取件");
                } else {
                    mOrderTakeDate.setText(DateTool.getTimeType(orderTakeDate) + DateTool.timesToStrTime(orderTakeDate + "", "HH:mm") + "取件");
                }


                //订单金额
                mOrderPrice.setText("¥" + MyNumberFormat.m2(Double.parseDouble(orderPriceStr)));
                break;
            case 2:
                final RequestResultDialogHelper dialogHelper = new RequestResultDialogHelper(this);

                dialogHelper.setType(RequestResultDialogHelper.CONFIRM, true).setDismissClick(new View.OnClickListener() {
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
                btn.setVisibility(View.GONE);
                break;
            case 3:
                final RequestResultDialogHelper dialogHelperThree = new RequestResultDialogHelper(this);

                dialogHelperThree.setType(RequestResultDialogHelper.TAKE, true).setDismissClick(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogHelperThree.dismiss();
                        finish();

                    }
                }).setCommitClick(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogHelperThree.dismiss();
                        finish();
                    }
                }).show();
                break;
        }
    }

    @Override
    public void onFailure(String result, Call<ResponseBody> call, Response<ResponseBody> response, int what) {
        super.onFailure(result, call, response, what);
        switch (what) {
            case 2:
                final RequestResultDialogHelper dialogHelper = new RequestResultDialogHelper(this);

                dialogHelper.setType(RequestResultDialogHelper.CONFIRM, false).setDismissClick(new View.OnClickListener() {
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
            case 3:
                final RequestResultDialogHelper dialogHelperThree = new RequestResultDialogHelper(this);

                dialogHelperThree.setType(RequestResultDialogHelper.TAKE, false).setDismissClick(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogHelperThree.dismiss();

                    }
                }).setCommitClick(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogHelperThree.dismiss();
                    }
                }).show();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            switch (requestCode) {
                case 1:
                    Bundle budle = data.getExtras();
                    String codeResult = budle.getString("codeResult");
                    inputCode.setText(codeResult);
                    break;
            }
        }
    }

    private void showPopupWindow(View view) {


        // 一个自定义的布局，作为显示的内容
        View contentView = LayoutInflater.from(this).inflate(
                R.layout.activity_receive_wait_send_package_popupwindow, null, false);

        final PopupWindow popupWindow = new PopupWindow(contentView,
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);

        LinearLayout layout = (LinearLayout) contentView.findViewById(R.id.ll_data);

        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });

        final TextView refuse = (TextView) contentView.findViewById(R.id.refuse);
        refuse.setText("追加金额");
        refuse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!String.valueOf(orderConfirmStaff).equals(UserManger.getUUid())){
                    showErrorToast(getString(R.string.no_order));
                    return;
                }
                if (order_pay_status == 1) {
                    showErrorToast("已对该快件进行追加，不能再次追加");
                    popupWindow.dismiss();
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putString("orderId", String.valueOf(orderId));
                    bundle.putInt("orderType", orderType);
                    bundle.putString("orderPriceStr", orderPriceStr);
                    bundle.putString("orderSizeStr", orderSizeStr);
                    bundle.putString("orderWeightStr", orderWeightStr);
                    bundle.putString("data", data);

                    startActivityForResult(SendTakeAddPriceActivity.class, bundle, 2);
                    popupWindow.dismiss();
                }
            }
        });

        TextView delivery = (TextView) contentView.findViewById(R.id.delivery);
        delivery.setText("拒绝接收");
        delivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!String.valueOf(orderConfirmStaff).equals(UserManger.getUUid())){
                    showErrorToast(getString(R.string.no_order));
                    return;
                }
                Bundle bundle = new Bundle();
                bundle.putString("orderId", String.valueOf(orderId));
                startActivity(SendTakeRefuseReceiveActivity.class, bundle);
                popupWindow.dismiss();
            }
        });

        popupWindow.setTouchable(true);

        popupWindow.setTouchInterceptor(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                Log.i("mengdd", "onTouch : ");

                return false;
                // 这里如果返回true的话，touch事件将被拦截
                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
            }
        });

        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
        popupWindow.setBackgroundDrawable(new BitmapDrawable());

        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        // 设置好参数之后再show
        if (Build.VERSION.SDK_INT >= 24) {
            int[] a = new int[2];
            view.getLocationInWindow(a);
            popupWindow.showAtLocation(this.getWindow().getDecorView(), Gravity.NO_GRAVITY, 0, a[1] + view.getHeight());
        } else {
            popupWindow.showAsDropDown(view);
        }

    }


    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        if (orderStatus == 1) {
            return super.onCreateOptionsMenu(menu);
        } else {
            getMenuInflater().inflate(R.menu.menu_edit, menu);
            menu.getItem(0).setIcon(R.drawable.anv_omit);
            menu.getItem(0).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    showPopupWindow(mToolbar);
                    return false;
                }
            });
            return super.onCreateOptionsMenu(menu);
        }

    }


}
