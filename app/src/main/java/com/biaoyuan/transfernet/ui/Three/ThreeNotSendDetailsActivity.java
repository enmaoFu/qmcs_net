package com.biaoyuan.transfernet.ui.Three;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.and.yzy.frame.util.AppManger;
import com.and.yzy.frame.util.DateTool;
import com.and.yzy.frame.util.RetrofitUtils;
import com.biaoyuan.transfernet.R;
import com.biaoyuan.transfernet.base.BaseAty;
import com.biaoyuan.transfernet.http.Three;
import com.biaoyuan.transfernet.util.AppJsonUtil;
import com.biaoyuan.transfernet.view.RequestResultDialogHelper;
import com.bigkoo.pickerview.OptionsPickerView;
import com.maploc.v;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Title  : 第三方代发需代发详情
 * Create : 2017/6/5
 * Author ：enmaoFu
 */
public class ThreeNotSendDetailsActivity extends BaseAty{

    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    private String orderIdStr;

    @Bind(R.id.set_code)
    TextView setCode;
    @Bind(R.id.set_dete)
    TextView setDate;
    @Bind(R.id.set_order_number)
    TextView setOrderNumber;
    @Bind(R.id.set_lower_date)
    TextView setLowerDate;
    @Bind(R.id.set_type)
    TextView setType;
    @Bind(R.id.set_weight_size)
    TextView setWeightSize;
    @Bind(R.id.send_name)
    TextView setSendName;
    @Bind(R.id.send_phone)
    TextView setSendPhone;
    @Bind(R.id.send_address)
    TextView setSendAddress;
    @Bind(R.id.receiver_name)
    TextView setReceiverName;
    @Bind(R.id.receiver_phone)
    TextView setReceiverPhone;
    @Bind(R.id.receiver_address)
    TextView setReceiverAddress;
    @Bind(R.id.select_logistics_company)
    TextView setLogisticsCompany;
    @Bind(R.id.set_express_number)
    EditText setExpressNumber;

    /**
     * 物品类型
     */
    private static final String GOODS_FILE = "文件";
    private static final String GOODS_OFFICE = "办公居家";
    private static final String GOODS_FLOWER = "鲜花";
    private static final String GOODS_CAKE = "蛋糕";
    private static final String GOODS_PACKAGE = "包裹";

    private OptionsPickerView mDialogBuilderLogisticsCompany;

    @Override
    public int getLayoutId() {
        return R.layout.activity_three_not_send_details;
    }

    @Override
    public void initData() {
        Bundle bundle = getIntent().getExtras();
        orderIdStr = bundle.getString("orderId");
        initToolbar(mToolbar,"代发详情");
        initLogisticsCompanyDialog();
    }

    @OnClick({R.id.select_logistics_company,R.id.textView})
    @Override
    public void btnClick(View view) {
       switch (view.getId()){
           case R.id.select_logistics_company:
               mDialogBuilderLogisticsCompany.show();
               break;
           case R.id.textView:
               String getLC = setLogisticsCompany.getText().toString().trim();
               String getExpressNumber = setExpressNumber.getText().toString().trim();
               if(getLC.equals("请选择物流公司")){
                   showErrorToast("请选择物流公司");
               }else if(getExpressNumber.length() == 0){
                   showErrorToast("请输入快递单号");
               }else{
                   String value = "";
                   switch (getLC){
                       case "圆通快递":
                           value = "yuantong";
                           break;
                       case "韵达快递":
                           value = "yunda";
                           break;
                       case "申通快递":
                           value = "shentong";
                           break;
                       case "中通快递":
                           value = "zhongtong";
                           break;
                       case "顺丰快递":
                           value = "shunfeng";
                           break;
                       case "EMS":
                           value = "ems";
                           break;
                       case "天天快递":
                           value = "tiantian";
                           break;
                       case "邮政":
                           value = "youzhengguonei";
                           break;
                       case "宅急送":
                           value = "zhaijisong";
                           break;
                       case "德邦":
                           value = "debangwuliu";
                           break;
                       case "百世快递":
                           value = "baishiwuliu";
                           break;

                       case "汇通快递":
                           value = "huitongkuaidi";
                           break;
                       case "国通快递":
                           value = "guotongkuaidi";
                           break;
                       case "增益快递":
                           value = "zengyisudi";
                           break;
                       case "速尔快递":
                           value = "suer";
                           break;
                       case "中铁快运":
                           value = "zhongtiewuliu";
                           break;
                       case "能达快递":
                           value = "ganzhongnengda";
                           break;
                       case "优速快递":
                           value = "youshuwuliu";
                           break;
                       case "全峰快递":
                           value = "quanfengkuaidi";
                           break;
                       case "京东快递":
                           value = "jd";
                           break;
                   }
                   showLoadingDialog(null);
                   doHttp(RetrofitUtils.createApi(Three.class).expressInfo(Long.parseLong(orderIdStr),getLC,value,getExpressNumber),2);
               }
               break;
       }
    }

    @Override
    public boolean setIsInitRequestData() {
        return true;
    }

    @Override
    public void requestData() {
        showLoadingContentDialog();
        doHttp(RetrofitUtils.createApi(Three.class).thirdOrderInfo(Long.parseLong(orderIdStr)),1);
    }

    @Override
    public void onSuccess(String result, Call<ResponseBody> call, Response<ResponseBody> response, int what) {
        super.onSuccess(result, call, response, what);
        switch (what){
            case 1:

                //快件码
                String getCode = new StringBuilder().append(AppJsonUtil.getString(AppJsonUtil.getString(result,"data"),"orderTrackingCode")).toString();
                //取件时间
                String getDate = new StringBuilder().append("取件时间：")
                        .append(DateTool.timesToStrTime(AppJsonUtil.getString(AppJsonUtil.getString(result,"data"),"orderRequiredTime") + "", "yyyy.MM.dd HH:mm:ss"))
                        .toString();
                //订单编号
                String getOrderNumber  = new StringBuilder().append("订单编号：").append(AppJsonUtil.getString(AppJsonUtil.getString(result,"data"),"orderNo")).toString();
                //下单时间
                String getLowerDate = new StringBuilder().append("下单时间：")
                        .append(DateTool.timesToStrTime(AppJsonUtil.getString(AppJsonUtil.getString(result,"data"),"orderTime") + "", "yyyy.MM.dd HH:mm:ss"))
                        .toString();
                //类型
                String getType = new StringBuilder().append(AppJsonUtil.getString(AppJsonUtil.getString(result,"data"),"orderGoodsType")).toString();
                int getTypeInt = Integer.parseInt(getType);
                //大小
                String getWeight = new StringBuilder().append(AppJsonUtil.getString(AppJsonUtil.getString(result,"data"),"orderGoodsSize")).toString();
                String getSize = new StringBuilder().append(AppJsonUtil.getString(AppJsonUtil.getString(result,"data"),"orderGoodsWeight")).toString();
                //发件人姓名
                String getSendName = new StringBuilder().append(AppJsonUtil.getString(AppJsonUtil.getString(result,"data"),"orderSenderName")).toString();
                //发件人电话
                String getSendPhone = new StringBuilder().append(AppJsonUtil.getString(AppJsonUtil.getString(result,"data"),"orderSenderTel")).toString();
                //发件人地址
                String getSendAddress = new StringBuilder().append(AppJsonUtil.getString(AppJsonUtil.getString(result,"data"),"orderSendAddr"))
                        .toString().replace("|","");
                //收件人姓名Receiver
                String getReceiverName = new StringBuilder().append(AppJsonUtil.getString(AppJsonUtil.getString(result,"data"),"orderReceiverName")).toString();
                //收件人电话
                String getReceiverPhone = new StringBuilder().append(AppJsonUtil.getString(AppJsonUtil.getString(result,"data"),"orderReceiverTel")).toString();
                //收件人地址
                String getReceiverAddress = new StringBuilder().append(AppJsonUtil.getString(AppJsonUtil.getString(result,"data"),"orderReceiveAddr"))
                        .toString().replace("|","");
                setCode.setText(getCode);
                setDate.setText(getDate);
                setOrderNumber.setText(getOrderNumber);
                setLowerDate.setText(getLowerDate);
                setWeightSize.setText("最长边≤"+getSize+ "cm   " +getWeight + "kg" );
                setSendName.setText(getSendName);
                setSendPhone.setText(getSendPhone);
                setSendAddress.setText(getSendAddress);
                setReceiverName.setText(getReceiverName);
                setReceiverPhone.setText(getReceiverPhone);
                setReceiverAddress.setText(getReceiverAddress);
                /**
                 * 物品类型 1: 文件； 2: 办公／居家； 3: 鲜花； 4: 包裹； 5: 蛋糕
                 */
                switch (getTypeInt){
                    case 1:
                        setType.setText(GOODS_FILE);
                        break;
                    case 2:
                        setType.setText(GOODS_OFFICE);
                        break;
                    case 3:
                        setType.setText(GOODS_FLOWER);
                        break;
                    case 4:
                        setType.setText(GOODS_PACKAGE);
                        break;
                    case 5:
                        setType.setText(GOODS_CAKE);
                        break;
                }

                break;
            case 2:
                final RequestResultDialogHelper dialogHelper = new RequestResultDialogHelper(this);

                dialogHelper.setType(RequestResultDialogHelper.OTHER_SEND,true).setDismissClick(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogHelper.dismiss();
                        AppManger.getInstance().killActivity(ThreeNotSendFgt.class);
                        finish();

                    }
                }).setCommitClick(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogHelper.dismiss();
                        AppManger.getInstance().killActivity(ThreeNotSendFgt.class);
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

                dialogHelper.setType(RequestResultDialogHelper.OTHER_SEND,false).setDismissClick(new View.OnClickListener() {
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

    /**
     * 选择物流公司
     */
    public void initLogisticsCompanyDialog() {

        final ArrayList<String> list = new ArrayList<>();
        list.add("圆通快递");
        list.add("韵达快递");
        list.add("申通快递");
        list.add("中通快递");
        list.add("顺丰快递");
        list.add("EMS");
        list.add("天天快递");
        list.add("邮政");
        list.add("宅急送");
        list.add("德邦快递");
        list.add("百世快递");
        list.add("汇通快递");
        list.add("国通快递");
        list.add("增益快递");
        list.add("速尔快递");
        list.add("中铁快运");
        list.add("能达快递");
        list.add("优速快递");
        list.add("全峰快递");
        list.add("京东快递");

        mDialogBuilderLogisticsCompany = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                String tx = list.get(options1);
                setLogisticsCompany.setText(tx);
            }
        })
                .setTitleText("选择物流公司")
                .setContentTextSize(14)
                .setOutSideCancelable(true)// default is true
                .build();

        mDialogBuilderLogisticsCompany.setPicker(list);
    }

}
