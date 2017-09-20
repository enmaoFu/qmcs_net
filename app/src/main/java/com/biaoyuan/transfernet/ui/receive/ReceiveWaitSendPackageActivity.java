package com.biaoyuan.transfernet.ui.receive;

import android.graphics.drawable.BitmapDrawable;
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
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.and.yzy.frame.util.DateTool;
import com.and.yzy.frame.util.RetrofitUtils;
import com.biaoyuan.transfernet.R;
import com.biaoyuan.transfernet.base.BaseAty;
import com.biaoyuan.transfernet.config.UserManger;
import com.biaoyuan.transfernet.http.Receive;
import com.biaoyuan.transfernet.ui.send.SendTakeFgt.SendMapAty;
import com.biaoyuan.transfernet.util.AppJsonUtil;
import com.biaoyuan.transfernet.view.RequestResultDialogHelper;
import com.biaoyuan.transfernet.view.VerificationCodeInput;
import com.orhanobut.logger.Logger;

import butterknife.Bind;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Title  : 代送快件详情
 * Create : 2017/6/3
 * Author ：enmaoFu
 */
public class ReceiveWaitSendPackageActivity extends BaseAty {

    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.code)
    VerificationCodeInput codeInput;
    @Bind(R.id.btn)
    TextView btn;
    @Bind(R.id.order_code)
    TextView orderCode;
    @Bind(R.id.order_id)
    TextView orderId;
    @Bind(R.id.order_date)
    TextView orderDate;
    @Bind(R.id.order_type)
    TextView orderType;
    @Bind(R.id.order_weight_size)
    TextView orderWeightSize;
    @Bind(R.id.send_name)
    TextView sendName;
    @Bind(R.id.send_phone)
    TextView sendPhone;
    @Bind(R.id.send_address)
    TextView sendAddress;
    @Bind(R.id.receivi_name)
    TextView receiviName;
    @Bind(R.id.receive_phone)
    TextView receiviPhone;
    @Bind(R.id.receive_address)
    TextView receiviAddress;

    private String orderIdStr;
    private String receiverLat;
    private String receiverLng;

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
        return R.layout.activity_receive_wait_send_package;
    }

    @Override
    public void initData() {
        Bundle bundle = getIntent().getExtras();
        orderIdStr = bundle.getString("orderIdStr");
        initToolbar(mToolbar, "待送快件");
        /*codeInput.setOnCompleteListener(new VerificationCodeInput.Listener() {
            @Override
            public void onComplete(String content) {
                showToast(content);
            }
        });*/
    }

    @Override
    public boolean setIsInitRequestData() {
        return true;
    }

    @Override
    public void requestData() {
        showLoadingContentDialog();
        doHttp(RetrofitUtils.createApi(Receive.class).sendOrder(orderIdStr), 1);
    }

    @OnClick({R.id.btn, R.id.address})
    @Override
    public void btnClick(View view) {
        switch (view.getId()) {
            case R.id.btn:
                String code = codeInput.getText().toString().trim();
                if (code.length() == 0) {
                    showErrorToast("签收码不能为空");
                } else if (code.length() < 6) {
                    showErrorToast("请输入正确的签收码");
                } else {
                    showLoadingDialog(null);
                    doHttp(RetrofitUtils.createApi(Receive.class).userSignOrder(orderIdStr, code, UserManger.getLng(), UserManger.getLat()), 2);
                }
                break;
            case R.id.address:
                Bundle bundleNew = new Bundle();
                bundleNew.putDouble("startLat", Double.parseDouble(UserManger.getLat()));
                bundleNew.putDouble("startLng", Double.parseDouble(UserManger.getLng()));
                bundleNew.putDouble("endLat", Double.parseDouble(receiverLat));
                bundleNew.putDouble("endLng", Double.parseDouble(receiverLng));
                startActivity(SendMapAty.class, bundleNew);
                break;
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

        TextView refuse = (TextView) contentView.findViewById(R.id.refuse);
        refuse.setText("拒绝签收");
        refuse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("orderIdStr", orderIdStr);
                bundle.putString("key","ds");
                startActivity(ReceiveWaitSendRefuseActivity.class, bundle);
                popupWindow.dismiss();
            }
        });

        TextView delivery = (TextView) contentView.findViewById(R.id.delivery);
        delivery.setText("投递点签收");
        delivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("orderIdStr", orderIdStr);
                startActivity(ReceiveWaitSendDeliveryActivity.class, bundle);
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
    public void onSuccess(String result, Call<ResponseBody> call, Response<ResponseBody> response, int what) {
        super.onSuccess(result, call, response, what);
        switch (what) {
            case 1:
                //快件码

                String getCode = new StringBuilder().append(AppJsonUtil.getString(AppJsonUtil.getString(AppJsonUtil.getString(result, "data"), "sendOrder"), "orderTrackingCode"))
                        .toString();
                //订单编号
                String getOrderId = new StringBuilder().append(AppJsonUtil.getString(AppJsonUtil.getString(AppJsonUtil.getString(result, "data"), "sendOrder"), "orderNo"))
                        .toString();
                //下单时间

                String getOrderDate = new StringBuilder().append(DateTool.timesToStrTime(AppJsonUtil.getString(AppJsonUtil.getString(AppJsonUtil.getString(result, "data"), "sendOrder"), "orderCreatTime")
                        + "", "yyyy.MM.dd-HH:mm:ss")).toString();
                //订单类型
                String getOrderType = new StringBuilder().append(AppJsonUtil.getString(AppJsonUtil.getString(AppJsonUtil.getString(result, "data"), "sendOrder"), "orderGoodsType"))
                        .toString();
                int getOrderTypeInt = Integer.parseInt(getOrderType);
                //订单重量
                String getOrderWeight = new StringBuilder().append(AppJsonUtil.getString(AppJsonUtil.getString(AppJsonUtil.getString(result, "data"), "sendOrder"), "orderGoodsWeight"))
                        .toString();
                //订单尺寸
                String getOrderSize = new StringBuilder().append(AppJsonUtil.getString(AppJsonUtil.getString(AppJsonUtil.getString(result, "data"), "sendOrder"), "orderGoodsSize"))
                        .toString();
                //发件人姓名

                String getSendName = new StringBuilder().append(AppJsonUtil.getString(AppJsonUtil.getString(AppJsonUtil.getString(result, "data"), "sendOrder"), "orderSenderName"))
                        .toString();
                //发件人电话

                String getSendPhone = new StringBuilder().append(AppJsonUtil.getString(AppJsonUtil.getString(AppJsonUtil.getString(result, "data"), "sendOrder"), "orderSenderTel"))
                        .toString();
                //发件人地址

                String getSendAddress = new StringBuilder().append(AppJsonUtil.getString(AppJsonUtil.getString(AppJsonUtil.getString(result, "data"), "sendOrder"), "orderSendAddr"))
                        .toString().replace("|", "");
                //收件人姓名
                String getReceiveName = new StringBuilder().append(AppJsonUtil.getString(AppJsonUtil.getString(AppJsonUtil.getString(result, "data"), "sendOrder"), "orderReceiverName"))
                        .toString();
                //收件人电话

                String getReceivePhone = new StringBuilder().append(AppJsonUtil.getString(AppJsonUtil.getString(AppJsonUtil.getString(result, "data"), "sendOrder"), "orderReceiverTel"))
                        .toString();
                //收件人地址

                String getReceiveAddress = new StringBuilder().append(AppJsonUtil.getString(AppJsonUtil.getString(AppJsonUtil.getString(result, "data"), "sendOrder"), "orderReceiveAddr"))
                        .toString().replace("|", "");
                //收件经度
                receiverLng = new StringBuilder().append(AppJsonUtil.getString(AppJsonUtil.getString(AppJsonUtil.getString(result, "data"), "sendOrder"), "orderReceiveLng")).toString();
                //收件维度
                receiverLat = new StringBuilder().append(AppJsonUtil.getString(AppJsonUtil.getString(AppJsonUtil.getString(result, "data"), "sendOrder"), "orderReceiveLat")).toString();
                orderCode.setText(getCode);
                orderId.setText("订单编号：" + getOrderId);
                orderDate.setText("下单时间：" + getOrderDate);
                orderWeightSize.setText("最长边≤" + getOrderSize + "cm   " + getOrderWeight + "kg");
                sendName.setText(getSendName);
                sendPhone.setText(getSendPhone);
                sendAddress.setText(getSendAddress);
                receiviName.setText(getReceiveName);
                receiviPhone.setText(getReceivePhone);
                receiviAddress.setText(getReceiveAddress);
                /**
                 * 物品类型 1: 文件； 2: 办公／居家； 3: 鲜花； 4: 包裹； 5: 蛋糕
                 */
                switch (getOrderTypeInt) {
                    case 1:
                        orderType.setText(GOODS_FILE);
                        break;
                    case 2:
                        orderType.setText(GOODS_OFFICE);
                        break;
                    case 3:
                        orderType.setText(GOODS_FLOWER);
                        break;
                    case 4:
                        orderType.setText(GOODS_PACKAGE);
                        break;
                    case 5:
                        orderType.setText(GOODS_CAKE);
                        break;
                }
                break;
            case 2:
                final RequestResultDialogHelper dialogHelper = new RequestResultDialogHelper(this);

                dialogHelper.setType(RequestResultDialogHelper.SEND, true).setDismissClick(new View.OnClickListener() {
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
        }
    }

    @Override
    public void onFailure(String result, Call<ResponseBody> call, Response<ResponseBody> response, int what) {
        super.onFailure(result, call, response, what);
        switch (what) {
            case 2:
                final RequestResultDialogHelper dialogHelper = new RequestResultDialogHelper(this);

                dialogHelper.setType(RequestResultDialogHelper.SEND, false).setDismissClick(new View.OnClickListener() {
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

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        Logger.v("onCreateOptionsMenu");
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
