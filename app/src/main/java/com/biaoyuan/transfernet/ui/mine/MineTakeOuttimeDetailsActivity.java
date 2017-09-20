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
import com.biaoyuan.transfernet.util.MyNumberFormat;

import java.text.SimpleDateFormat;

import butterknife.Bind;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

/**
 * @title  :取件超时详情
 * @create :2017/5/18
 * @author :enmaoFu
 */
public class MineTakeOuttimeDetailsActivity extends BaseAty{

    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.yq_date)
    TextView yqDate;
    @Bind(R.id.cc_date)
    TextView ccDate;
    @Bind(R.id.code)
    TextView code;
    @Bind(R.id.order_id)
    TextView orderId;
    @Bind(R.id.order_date)
    TextView orderDate;
    @Bind(R.id.order_type)
    TextView orderType;
    @Bind(R.id.order_weight_size)
    TextView orderWeightSize;
    @Bind(R.id.odrer_send_name)
    TextView orderSendName;
    @Bind(R.id.order_send_phone)
    TextView orderSendPhone;
    @Bind(R.id.order_send_address)
    TextView orderSendAddress;
    @Bind(R.id.order_receive_name)
    TextView orderReceiveName;
    @Bind(R.id.order_receive_phone)
    TextView orderReceivePhone;
    @Bind(R.id.order_receive_address)
    TextView orderReceiveAddress;
    @Bind(R.id.order_take_date)
    TextView orderTakeDate;
    @Bind(R.id.order_price)
    TextView orderPrice;

    private String excepId;
    private int excepType;
    private String yqDateStr;
    private String csDateStr;
    private String orderNo;

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
        return R.layout.activity_mine_take_outtime_details;
    }

    @Override
    public void initData() {
        Bundle bundle = getIntent().getExtras();
        excepId = bundle.getString("excepId");
        excepType = bundle.getInt("excepType");
        /*yqDateStr = bundle.getString("yqDateStr");
        csDateStr = bundle.getString("csDateStr");*/
        orderNo = bundle.getString("orderNo");

        /*yqDate.setText(yqDateStr);
        ccDate.setText(csDateStr);*/
        initToolbar(mToolbar,"取件超时");
    }

    @Override
    public boolean setIsInitRequestData() {
        return true;
    }

    @Override
    public void requestData() {
        showLoadingContentDialog();
        //doHttp(RetrofitUtils.createApi(Mine.class).myAbnormalExpressDetail(excepId,excepType),1);
        doHttp(RetrofitUtils.createApi(Mine.class).myAbnormalExpressDetail(orderNo,excepType),1);
    }

    @Override
    public void onSuccess(String result, Call<ResponseBody> call, Response<ResponseBody> response, int what) {
        super.onSuccess(result, call, response, what);
        switch (what){
            case 1:
                //快件码
                String getcode = new StringBuilder().append(AppJsonUtil.getString(AppJsonUtil.getString(result,"data"),"order_tracking_code")).toString();
                //订单编号
                String getOrderId = new StringBuilder().append(AppJsonUtil.getString(AppJsonUtil.getString(result,"data"),"order_no")).toString();
                //下单时间
                String getOrderDate = new StringBuilder().append(DateTool.timesToStrTime(AppJsonUtil.getString(AppJsonUtil.getString(result,"data"),"order_time")
                        ,"yyyy.MM.dd HH:mm")).toString();
                //类型
                String getOrderType = new StringBuilder().append(AppJsonUtil.getString(AppJsonUtil.getString(result,"data"),"order_goods_type")).toString();
                int getOrderTypeInt = Integer.parseInt(getOrderType);
                //大小
                String getWeight = new StringBuilder().append(AppJsonUtil.getString(AppJsonUtil.getString(result,"data"),"order_goods_weight")).toString();
                String getSize = new StringBuilder().append(AppJsonUtil.getString(AppJsonUtil.getString(result,"data"),"order_goods_size")).toString();
                //发件人名字
                String getSendName = new StringBuilder().append(AppJsonUtil.getString(AppJsonUtil.getString(result,"data"),"order_sender_name")).toString();
                //发件人电话
                String getSendPhoen = new StringBuilder().append(AppJsonUtil.getString(AppJsonUtil.getString(result,"data"),"order_sender_tel")).toString();
                //发件人地址
                String getSendAddress = new StringBuilder().append(AppJsonUtil.getString(AppJsonUtil.getString(result,"data"),"order_send_addr"))
                        .toString().replace("|","");
                //收件人名字
                String getReceiveName = new StringBuilder().append(AppJsonUtil.getString(AppJsonUtil.getString(result,"data"),"order_receiver_name")).toString();
                //收件人电话
                String getReceivePhone = new StringBuilder().append(AppJsonUtil.getString(AppJsonUtil.getString(result,"data"),"order_receiver_tel")).toString();
                //收件人地址
                String getReceiveAddress = new StringBuilder().append(AppJsonUtil.getString(AppJsonUtil.getString(result,"data"),"order_receive_addr"))
                        .toString().replace("|","");
                //取件时间
                String getTakeDate = new StringBuilder().append( DateTool.timesToStrTime(AppJsonUtil.getString(AppJsonUtil.getString(result,"data"),"order_required_time")
                        ,"yyyy.MM.dd HH:mm")).toString();
                //取件金额
                String getTakePrice = new StringBuilder().append(MyNumberFormat.m2(Double.parseDouble(AppJsonUtil.getString(AppJsonUtil.getString(result,"data")
                        ,"order_total_price")))).toString();
                code.setText("快件码：" + getcode);
                orderId.setText("订单编号：" + getOrderId);
                orderDate.setText("下单时间：" + getOrderDate);
                orderWeightSize.setText("最长边≤"+getSize + "cm   " + getWeight + "kg");
                orderSendName.setText(getSendName);
                orderSendPhone.setText(getSendPhoen);
                orderSendAddress.setText(getSendAddress);
                orderReceiveName.setText(getReceiveName);
                orderReceivePhone.setText(getReceivePhone);
                orderReceiveAddress.setText(getReceiveAddress);
                orderTakeDate.setText(getTakeDate);
                orderPrice.setText("¥" + getTakePrice);
                yqDate.setText("要求时间：" + getTakeDate);
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
                long getDeliveryUpdateTime = Long.parseLong(AppJsonUtil.getString(AppJsonUtil.getString(result,"data"),"delivery_update_time"));
                long getOrderRequiredTime = Long.parseLong(AppJsonUtil.getString(AppJsonUtil.getString(result,"data"),"order_required_time"));
                if(getDeliveryUpdateTime > getOrderRequiredTime){
                    try{
                        SimpleDateFormat simpleFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");
                        String fromDate = simpleFormat.format(getDeliveryUpdateTime);
                        String toDate = simpleFormat.format(getOrderRequiredTime);
                        long from = simpleFormat.parse(fromDate).getTime();
                        long to = simpleFormat.parse(toDate).getTime();
                        int hours = (int) ((from - to)/(1000 * 60 * 60));
                        ccDate.setText("(超时" + hours + "小时)");
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }else{
                    ccDate.setText("(超时0.00小时)");
                }

                break;
        }
    }
}
