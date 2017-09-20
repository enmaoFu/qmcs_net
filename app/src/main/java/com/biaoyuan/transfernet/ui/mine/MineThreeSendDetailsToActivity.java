package com.biaoyuan.transfernet.ui.mine;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.and.yzy.frame.util.DateTool;
import com.and.yzy.frame.util.RetrofitUtils;
import com.biaoyuan.transfernet.R;
import com.biaoyuan.transfernet.base.BaseAty;
import com.biaoyuan.transfernet.http.Mine;
import com.biaoyuan.transfernet.util.AppJsonUtil;

import butterknife.Bind;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Title  : 第三方代发->查看货物详情
 * Create : 2017/6/5
 * Author ：enmaoFu
 */
public class MineThreeSendDetailsToActivity extends BaseAty{

    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.code)
    TextView code;
    @Bind(R.id.take_date)
    TextView takeDate;
    @Bind(R.id.order_number)
    TextView orderNumber;
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
    @Bind(R.id.receive_name)
    TextView receiveName;
    @Bind(R.id.receive_phone)
    TextView receivePhone;
    @Bind(R.id.receive_address)
    TextView receiveAddress;

    private String orderId;

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
        return R.layout.activity_three_send_details_to;
    }

    @Override
    public void initData() {

        Bundle bundle = getIntent().getExtras();
        orderId = bundle.getString("orderId");

        initToolbar(mToolbar,"货物详情");

    }

    @Override
    public boolean setIsInitRequestData() {
        return true;
    }

    @Override
    public void requestData() {
        showLoadingContentDialog();
        doHttp(RetrofitUtils.createApi(Mine.class).mySignForExpressGoodsDetail(orderId),1);
    }

    @Override
    public void onSuccess(String result, Call<ResponseBody> call, Response<ResponseBody> response, int what) {
        super.onSuccess(result, call, response, what);
        switch (what){
            case 1:
                //快件码
                String getCode = new StringBuilder().append(AppJsonUtil.getString(AppJsonUtil.getString(result,"data"),"order_tracking_code")).toString();
                //取件时间
                String getTakeDate = new StringBuilder().append(DateTool.timesToStrTime(AppJsonUtil.getString(AppJsonUtil.getString(result,"data"),"delivery_update_time")
                        ,"yyyy.MM.dd HH:mm")).toString();
                //订单编号
                String getOrderNumber = new StringBuilder().append(AppJsonUtil.getString(AppJsonUtil.getString(result,"data"),"order_no")).toString();
                //下单时间
                String getOrderDate = new StringBuilder().append(DateTool.timesToStrTime(AppJsonUtil.getString(AppJsonUtil.getString(result,"data"),"order_time")
                    ,"yyyy.MM.dd HH:mm")).toString();
                //订单类型
                //快件码
                String getOrderType = new StringBuilder().append(AppJsonUtil.getString(AppJsonUtil.getString(result,"data"),"order_goods_type")).toString();
                int getOrderTypeInt = Integer.parseInt(getOrderType);
                //订单体积
                String getOrderWeight = new StringBuilder().append(AppJsonUtil.getString(AppJsonUtil.getString(result,"data"),"order_goods_weight")).toString();
                //订单大小
                String getOrderSize = new StringBuilder().append(AppJsonUtil.getString(AppJsonUtil.getString(result,"data"),"order_goods_size")).toString();
                //发件人名字
                String getSendName = new StringBuilder().append(AppJsonUtil.getString(AppJsonUtil.getString(result,"data"),"order_sender_name")).toString();
                //发件人电话
                String getSendPhone = new StringBuilder().append(AppJsonUtil.getString(AppJsonUtil.getString(result,"data"),"order_sender_tel")).toString();
                //发件人地址
                String getSendAddress = new StringBuilder().append(AppJsonUtil.getString(AppJsonUtil.getString(result,"data"),"order_send_addr"))
                        .toString().replace("|","");
                //接收人名字
                String getReceiveName = new StringBuilder().append(AppJsonUtil.getString(AppJsonUtil.getString(result,"data"),"order_receiver_name")).toString();
                //接收人电话
                String getReceivePhone = new StringBuilder().append(AppJsonUtil.getString(AppJsonUtil.getString(result,"data"),"order_receiver_tel")).toString();
                //接收人地址
                String getReceiveAddress = new StringBuilder().append(AppJsonUtil.getString(AppJsonUtil.getString(result,"data"),"order_receive_addr"))
                        .toString().replace("|","");
                code.setText(getCode);
                takeDate.setText("取件时间：" + getTakeDate);
                orderNumber.setText(getOrderNumber);
                orderDate.setText(getOrderDate);
                orderWeightSize.setText(getOrderSize + "cm   " + getOrderWeight + "kg");
                sendName.setText(getSendName);
                sendPhone.setText(getSendPhone);
                sendAddress.setText(getSendAddress);
                receiveName.setText(getReceiveName);
                receivePhone.setText(getReceivePhone);
                receiveAddress.setText(getReceiveAddress);
                /**
                 * 物品类型 1: 文件； 2: 办公／居家； 3: 鲜花； 4: 包裹； 5: 蛋糕
                 */
                switch (getOrderTypeInt){
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
        }
    }
}
