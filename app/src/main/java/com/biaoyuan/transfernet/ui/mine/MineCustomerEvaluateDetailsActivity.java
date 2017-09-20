package com.biaoyuan.transfernet.ui.mine;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.widget.TextView;

import com.and.yzy.frame.util.DateTool;
import com.and.yzy.frame.util.RetrofitUtils;
import com.biaoyuan.transfernet.R;
import com.biaoyuan.transfernet.base.BaseAty;
import com.biaoyuan.transfernet.config.UserManger;
import com.biaoyuan.transfernet.http.Mine;
import com.biaoyuan.transfernet.util.AppJsonUtil;
import com.biaoyuan.transfernet.util.MyNumberFormat;

import am.widget.drawableratingbar.DrawableRatingBar;
import butterknife.Bind;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

/**
 * @title  :我的客户评价详情页面
 * @create :2017/5/22
 * @author :enmaoFu
 */
public class MineCustomerEvaluateDetailsActivity extends BaseAty{

    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.ratingbar)
    DrawableRatingBar ratingBar;
    @Bind(R.id.phone)
    TextView phone;
    @Bind(R.id.date)
    TextView date;
    @Bind(R.id.message)
    TextView message;
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
    @Bind(R.id.receive_name)
    TextView receiveName;
    @Bind(R.id.receive_phone)
    TextView receivePhone;
    @Bind(R.id.receive_address)
    TextView receiveAddress;
    @Bind(R.id.take_date)
    TextView takeDate;
    @Bind(R.id.order_price)
    TextView order_price;

    private String ocommentId;
    private long tel;
    private long time;
    private String context;
    private int score;

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
        return R.layout.activity_mine_customer_evaluate_details;
    }

    @Override
    public void initData() {

        Bundle bundle = getIntent().getExtras();
        ocommentId = bundle.getString("ocommentId");
        tel = bundle.getLong("tel");
        time = bundle.getLong("time");
        context = bundle.getString("context");
        score = bundle.getInt("score");

        phone.setText(MyNumberFormat.toPwdPhone(tel));
        date.setText(DateTool.timesToStrTime(time + "", "yyyy.MM.dd"));
        message.setText(context);

        initToolbar(mToolbar,"评价详情");
        ratingBar.setRatingDrawable(this.getResources().getDrawable(R.drawable.star_allmark),this.getResources().getDrawable(R.drawable.star_nomark));
        ratingBar.setDrawablePadding(5);
        ratingBar.setGravity(Gravity.LEFT);
        ratingBar.setMax(5);
        ratingBar.setMin(1);
        ratingBar.setRating(score);
        ratingBar.setManually(false);
        ratingBar.setOnlyItemTouchable(false);
    }

    @Override
    public boolean setIsInitRequestData() {
        return true;
    }

    @Override
    public void requestData() {
        showLoadingContentDialog();
        doHttp(RetrofitUtils.createApi(Mine.class).commentObject(UserManger.getBaseId(),ocommentId),1);
    }

    @Override
    public void onSuccess(String result, Call<ResponseBody> call, Response<ResponseBody> response, int what) {
        super.onSuccess(result, call, response, what);
        switch (what){
            case 1:
                //订单编号
                String getOrderId = new StringBuilder().append(AppJsonUtil.getString(AppJsonUtil.getString(AppJsonUtil.getString(result,"data"),"order"),"orderNo"))
                        .toString();
                //下单时间
                String getOrderDate =  new StringBuilder().append(DateTool.timesToStrTime(
                        AppJsonUtil.getString(AppJsonUtil.getString(AppJsonUtil.getString(result,"data"),"order"),"orderCreatTime") + "", "yyyy.MM.dd-HH:mm:ss")).toString();
                //订单类型
                String getOrderType = new StringBuilder().append(AppJsonUtil.getString(AppJsonUtil.getString(AppJsonUtil.getString(result,"data"),"order"),"orderGoodsType"))
                        .toString();
                int getOrderTypeInt = Integer.parseInt(getOrderType);
                //订单重量
                String getOrderWeight = new StringBuilder().append(AppJsonUtil.getString(AppJsonUtil.getString(AppJsonUtil.getString(result,"data"),"order"),"orderGoodsWeight"))
                        .toString();
                //订单大小
                String getOrderSize = new StringBuilder().append(AppJsonUtil.getString(AppJsonUtil.getString(AppJsonUtil.getString(result,"data"),"order"),"orderGoodsSize"))
                        .toString();
                //发件人姓名
                String getSendName = new StringBuilder().append(AppJsonUtil.getString(AppJsonUtil.getString(AppJsonUtil.getString(result,"data"),"order"),"orderSenderName"))
                        .toString();
                //发件人电话
                String getSendPhone = new StringBuilder().append(AppJsonUtil.getString(AppJsonUtil.getString(AppJsonUtil.getString(result,"data"),"order"),"orderSenderTel"))
                        .toString();
                //发件人地址
                String getSendAddress = new StringBuilder().append(AppJsonUtil.getString(AppJsonUtil.getString(AppJsonUtil.getString(result,"data"),"order"),"orderSendAddr"))
                        .toString().replace("|","");
                //收件人姓名
                String getReceiveName = new StringBuilder().append(AppJsonUtil.getString(AppJsonUtil.getString(AppJsonUtil.getString(result,"data"),"order"),"orderReceiverName"))
                        .toString();
                //收件人电话
                String getReceivePhone = new StringBuilder().append(AppJsonUtil.getString(AppJsonUtil.getString(AppJsonUtil.getString(result,"data"),"order"),"orderReceiverTel"))
                        .toString();
                //收件人地址
                String getReceiveAddress = new StringBuilder().append(AppJsonUtil.getString(AppJsonUtil.getString(AppJsonUtil.getString(result,"data"),"order"),"orderReceiveAddr"))
                        .toString().replace("|","");
                //取件时间
                String getTakeDate = new StringBuilder().append(DateTool.timesToStrTime(
                        AppJsonUtil.getString(AppJsonUtil.getString(result,"data"),"pieceTime") + "", "yyyy.MM.dd-HH:mm:ss")).toString();
                //订单金额
                String getOrderPrice = new StringBuilder().append(AppJsonUtil.getString(AppJsonUtil.getString(AppJsonUtil.getString(result,"data"),"order"),"orderTotalPrice"))
                    .toString();
                orderId.setText("订单编号：" + getOrderId);
                orderDate.setText("下单时间：" + getOrderDate);
                orderWeightSize.setText(getOrderSize + "≤10cm   " +  getOrderWeight + "kg");
                sendName.setText(getSendName);
                sendPhone.setText(getSendPhone);
                sendAddress.setText(getSendAddress);
                receiveName.setText(getReceiveName);
                receivePhone.setText(getReceivePhone);
                receiveAddress.setText(getReceiveAddress);
                takeDate.setText(getTakeDate);
                order_price.setText("¥" + MyNumberFormat.m2(Double.parseDouble(getOrderPrice)));
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
