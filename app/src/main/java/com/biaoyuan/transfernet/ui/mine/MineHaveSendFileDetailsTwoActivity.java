package com.biaoyuan.transfernet.ui.mine;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.and.yzy.frame.util.DateTool;
import com.and.yzy.frame.util.RetrofitUtils;
import com.and.yzy.frame.view.listview.ListViewForScrollView;
import com.biaoyuan.transfernet.R;
import com.biaoyuan.transfernet.adapter.WuNiuAdapter;
import com.biaoyuan.transfernet.base.BaseAty;
import com.biaoyuan.transfernet.domain.WuNiuItem;
import com.biaoyuan.transfernet.http.Mine;
import com.biaoyuan.transfernet.util.AppJsonUtil;
import com.biaoyuan.transfernet.util.MyNumberFormat;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Title  :签收快递详情,货物详情
 * Create : 2017/6/14
 * Author ：enmaoFu
 */
public class MineHaveSendFileDetailsTwoActivity extends BaseAty {
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.order_id)
    TextView orderIdT;
    @Bind(R.id.order_date)
    TextView orderDate;
    @Bind(R.id.order_type)
    TextView orderType;
    @Bind(R.id.order_gg)
    TextView orderGG;
    @Bind(R.id.odrer_send_name)
    TextView orderSendName;
    @Bind(R.id.order_send_phone)
    TextView orderSendPhone;
    @Bind(R.id.order_send_address)
    TextView orderSendAdderss;
    @Bind(R.id.order_collect_name)
    TextView orderCollectName;
    @Bind(R.id.order_collect_phone)
    TextView orderCollectPhone;
    @Bind(R.id.order_collect_address)
    TextView orderCollectAddress;
    @Bind(R.id.order_take_date)
    TextView orderTakeDate;
    @Bind(R.id.order_price)
    TextView orderPrice;
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
        return R.layout.activity_have_send_file_details_two;
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

                //订单编号
                String getOrderId = new StringBuilder().append(AppJsonUtil.getString(AppJsonUtil.getString(result,"data"),"order_no")).toString();
                //下单时间
                String getOrderDate = new StringBuilder().append(DateTool.timesToStrTime(AppJsonUtil.getString(AppJsonUtil.getString(result,"data"),"order_time")
                        ,"yyyy.MM.dd HH:mm")).toString();
                //类型
                String getOrderType = new StringBuilder().append(AppJsonUtil.getString(AppJsonUtil.getString(result,"data"),"order_goods_type")).toString();
                int getOrderTypeInt = Integer.parseInt(getOrderType);
                //重量
                String getOrderWeight = new StringBuilder().append(AppJsonUtil.getString(AppJsonUtil.getString(result,"data"),"order_goods_weight")).toString();
                //大小
                String getOrderSize = new StringBuilder().append(AppJsonUtil.getString(AppJsonUtil.getString(result,"data"),"order_goods_size")).toString();
                //发件人姓名
                String getOrderSendName = new StringBuilder().append(AppJsonUtil.getString(AppJsonUtil.getString(result,"data"),"order_sender_name")).toString();
                //发件人电话
                String getOrderSendPhone = new StringBuilder().append(AppJsonUtil.getString(AppJsonUtil.getString(result,"data"),"order_sender_tel")).toString();
                //发件人地址
                String getOrderSendAddress = new StringBuilder().append(AppJsonUtil.getString(AppJsonUtil.getString(result,"data"),"order_send_addr"))
                        .toString().replace("|","");
                //收件人姓名
                String getOrderCollectName = new StringBuilder().append(AppJsonUtil.getString(AppJsonUtil.getString(result,"data"),"order_receiver_name")).toString();
                //收件人电话
                String getOrderCollectPhone = new StringBuilder().append(AppJsonUtil.getString(AppJsonUtil.getString(result,"data"),"order_receiver_tel")).toString();
                //收件人地址
                String getOrderCollectAddress = new StringBuilder().append(AppJsonUtil.getString(AppJsonUtil.getString(result,"data"),"order_receive_addr"))
                        .toString().replace("|","");
                //取件时间
                String getOrderTakeDate = new StringBuilder().append( DateTool.timesToStrTime(AppJsonUtil.getString(AppJsonUtil.getString(result,"data"),
                        "order_required_time"),"yyyy.MM.dd HH:mm")).toString();
                //订单金额
                String getOrderPrice = new StringBuilder().append(MyNumberFormat.m2(Double.parseDouble(
                        AppJsonUtil.getString(AppJsonUtil.getString(result,"data"),"order_total_price")))).toString();
                orderIdT.setText("订单编号：" + getOrderId);
                orderDate.setText("下单时间：" + getOrderDate);
                orderGG.setText(getOrderSize + "cm   " + getOrderWeight + "kg");
                orderSendName.setText(getOrderSendName);
                orderSendPhone.setText(getOrderSendPhone);
                orderSendAdderss.setText(getOrderSendAddress);
                orderCollectName.setText(getOrderCollectName);
                orderCollectPhone.setText(getOrderCollectPhone);
                orderCollectAddress.setText(getOrderCollectAddress);
                orderTakeDate.setText(getOrderTakeDate);
                orderPrice.setText("¥" + getOrderPrice);
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
