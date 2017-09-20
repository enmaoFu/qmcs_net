package com.biaoyuan.transfernet.ui.Three;

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

import com.and.yzy.frame.util.AppManger;
import com.and.yzy.frame.util.DateTool;
import com.and.yzy.frame.util.RetrofitUtils;
import com.biaoyuan.transfernet.R;
import com.biaoyuan.transfernet.base.BaseAty;
import com.biaoyuan.transfernet.http.Three;
import com.biaoyuan.transfernet.ui.receive.ReceiveWaitSendDeliveryActivity;
import com.biaoyuan.transfernet.ui.receive.ReceiveWaitSendRefuseActivity;
import com.biaoyuan.transfernet.util.AppJsonUtil;
import com.biaoyuan.transfernet.view.RequestResultDialogHelper;
import com.biaoyuan.transfernet.view.VerificationCodeInput;

import butterknife.Bind;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Title  : 三方快递代发-直送服务详情
 * Create : 2017/5/9
 * Author ：chen
 */
public class ThreeSendDetailsActivity extends BaseAty {

    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.code)
    VerificationCodeInput codeInput;
    private long orderId;

    @Bind(R.id.kjm)
    TextView kjm;
    @Bind(R.id.number)
    TextView number;
    @Bind(R.id.dow_date)
    TextView dowDate;
    @Bind(R.id.type)
    TextView type;
    @Bind(R.id.weight_size)
    TextView weightSize;
    @Bind(R.id.send_name)
    TextView sendName;
    @Bind(R.id.send_phone)
    TextView sendPhone;
    @Bind(R.id.send_address)
    TextView sendAddress;
    @Bind(R.id.collect_name)
    TextView collectName;
    @Bind(R.id.collect_phone)
    TextView collectPhone;
    @Bind(R.id.collect_address)
    TextView collectAddress;

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
        return R.layout.activity_three_send_detailss;
    }

    @Override
    public void initData() {
        Bundle bundle = getIntent().getExtras();
        orderId = Long.parseLong(bundle.getString("orderId"));
        initToolbar(mToolbar,"直送服务");
    }

    @Override
    public boolean setIsInitRequestData() {
        return true;
    }

    @Override
    public void requestData() {
        doHttp(RetrofitUtils.createApi(Three.class).thirdOrderInfo(orderId),1);
    }

    @OnClick({R.id.btnss})
    @Override
    public void btnClick(View view) {
        switch (view.getId()){
            case R.id.btnss:
                String code = codeInput.getText().toString().trim();
                if(code.length() == 0){
                    showErrorToast("签收码不能为空");
                }else if (code.length() < 6){
                    showErrorToast("请输入正确的签收码");
                }else{
                    showLoadingDialog(null);
                    doHttp(RetrofitUtils.createApi(Three.class).updateAccept(orderId,code,1,1),2);
                }
                break;
        }
    }

    @Override
    public void onSuccess(String result, Call<ResponseBody> call, Response<ResponseBody> response, int what) {
        super.onSuccess(result, call, response, what);
        switch (what){
            case 1:

                //快件码
                String getCode = new StringBuilder().append(AppJsonUtil.getString(AppJsonUtil.getString(result, "data"),"orderTrackingCode")).toString();
                //订单编号
                String getNumber = new StringBuilder().append("订单编号：").append(AppJsonUtil.getString(AppJsonUtil.getString(result,"data"),"orderNo")).toString();
                //下单时间
                String getDowDate = new StringBuilder().append("下单时间：")
                        .append(DateTool.timesToStrTime(AppJsonUtil.getString(AppJsonUtil.getString(result,"data"),"orderTime") + "", "yyyy.MM.dd HH:mm:ss"))
                        .toString();
                //类型
                int getType = Integer.parseInt(AppJsonUtil.getString(AppJsonUtil.getString(result,"data"),"orderGoodsType"));
                //大小
                String getWeight = new StringBuilder().append(AppJsonUtil.getString(AppJsonUtil.getString(result,"data"),"orderGoodsWeight")).append("kg   ").toString();
                String getSize = new StringBuilder().append("最长边≤").append(AppJsonUtil.getString(AppJsonUtil.getString(result,"data"),"orderGoodsSize")).append("cm").toString();
                //发件人
                String getSendName = new StringBuilder().append(AppJsonUtil.getString(AppJsonUtil.getString(result,"data"),"orderSenderName")).toString();
                //发件人电话
                String getSendPhone = new StringBuilder().append(AppJsonUtil.getString(AppJsonUtil.getString(result,"data"),"orderSenderTel")).toString();
                //发件人地址
                String getSendAddress = new StringBuilder().append(AppJsonUtil.getString(AppJsonUtil.getString(result,"data"),"orderSendAddr")).toString();
                //收件人
                String getCollectName = new StringBuilder().append(AppJsonUtil.getString(AppJsonUtil.getString(result,"data"),"orderReceiverName")).toString();
                //收件人电话
                String getCollectPhone = new StringBuilder().append(AppJsonUtil.getString(AppJsonUtil.getString(result,"data"),"orderReceiverTel")).toString();
                //收件人地址
                String getCollectAddress = new StringBuilder().append(AppJsonUtil.getString(AppJsonUtil.getString(result,"data"),"orderReceiveAddr")).toString();
                kjm.setText(getCode);
                number.setText(getNumber);
                dowDate.setText(getDowDate);
                weightSize.setText(getWeight + getSize);
                sendName.setText(getSendName);
                sendPhone.setText(getSendPhone);
                sendAddress.setText(getSendAddress);
                collectName.setText(getCollectName);
                collectPhone.setText(getCollectPhone);
                collectAddress.setText(getCollectAddress);
                /**
                 * 物品类型 1: 文件； 2: 办公／居家； 3: 鲜花； 4: 包裹； 5: 蛋糕
                 */
                switch (getType){
                    case 1:
                        type.setText(GOODS_FILE);
                        break;
                    case 2:
                        type.setText(GOODS_OFFICE);
                        break;
                    case 3:
                        type.setText(GOODS_FLOWER);
                        break;
                    case 4:
                        type.setText(GOODS_PACKAGE);
                        break;
                    case 5:
                        type.setText(GOODS_CAKE);
                        break;
                }
                break;
            case 2:
                final RequestResultDialogHelper dialogHelper = new RequestResultDialogHelper(this);

                dialogHelper.setType(RequestResultDialogHelper.SIGN,true).setDismissClick(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogHelper.dismiss();
                        AppManger.getInstance().killActivity(ThreeSendFgt.class);
                        finish();

                    }
                }).setCommitClick(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogHelper.dismiss();
                        AppManger.getInstance().killActivity(ThreeSendFgt.class);
                        finish();
                    }
                }).show();
                break;
        }
    }

    @Override
    public void onFailure(String result, Call<ResponseBody> call, Response<ResponseBody> response, int what) {
        super.onFailure(result, call, response, what);
        switch (what){
            case 2:
                final RequestResultDialogHelper dialogHelper = new RequestResultDialogHelper(this);

                dialogHelper.setType(RequestResultDialogHelper.SIGN,false).setDismissClick(new View.OnClickListener() {
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
                bundle.putString("orderIdStr",String.valueOf(orderId));
                bundle.putString("key","zs");
                startActivity(ReceiveWaitSendRefuseActivity.class, bundle);
                popupWindow.dismiss();
            }
        });

        TextView delivery = (TextView) contentView.findViewById(R.id.delivery);
        delivery.setVisibility(View.GONE);

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
