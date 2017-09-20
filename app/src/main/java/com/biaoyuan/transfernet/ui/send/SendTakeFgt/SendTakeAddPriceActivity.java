package com.biaoyuan.transfernet.ui.send.SendTakeFgt;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.and.yzy.frame.util.RetrofitUtils;
import com.and.yzy.frame.view.dialog.Effectstype;
import com.and.yzy.frame.view.dialog.MaterialDialog;
import com.biaoyuan.transfernet.R;
import com.biaoyuan.transfernet.base.BaseAty;
import com.biaoyuan.transfernet.domain.QsettingInfo;
import com.biaoyuan.transfernet.http.Send;
import com.biaoyuan.transfernet.util.AppJsonUtil;
import com.biaoyuan.transfernet.view.MyNumberButton;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

/**
 * @author :enmaoFu
 * @title :追加金额页面
 * @create :2017/6/7
 */

public class SendTakeAddPriceActivity extends BaseAty {

    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.is_img_agree)
    ImageView isImgAgree;
    @Bind(R.id.add_weight)
    MyNumberButton addWeight;
    @Bind(R.id.add_size)
    MyNumberButton addSize;
    @Bind(R.id.btn)
    TextView btn;
    @Bind(R.id.toast)
    TextView toast;

    //默认为没有同意
    private boolean isAgreeFlag = false;

    //订单ID
    private String orderId;
    //订单类型
    private int orderType;
    //订单大小
    private int orderSizeStr;
    //订单重量
    private int orderWeightStr;


    @Override
    public int getLayoutId() {
        return R.layout.activity_send_take_add_price;
    }

    @Override
    public void initData() {
        Bundle bundle = getIntent().getExtras();
        orderId = bundle.getString("orderId");
        orderType = bundle.getInt("orderType");
        orderSizeStr = Integer.parseInt(bundle.getString("orderSizeStr"));
        orderWeightStr = Integer.parseInt(bundle.getString("orderWeightStr"));

        initToolbar(mToolbar, "追加金额");
        //   isBeyond(orderWeightStr, orderSizeStr);


        initSize();


    }

    List<QsettingInfo> mOrderTypes;

    ArrayList<Integer> mSizeList = new ArrayList<>();
    ArrayList<Integer> mWeightList = new ArrayList<>();

    private void initSize() {

        String data = getIntent().getStringExtra("data");

        List<QsettingInfo> qsettingInfos = AppJsonUtil.getMyArrayList(data, QsettingInfo.class);


        //1.先得到order_type的数据
        mOrderTypes = new ArrayList<>();

        for (QsettingInfo qsettingInfo : qsettingInfos) {

            if (qsettingInfo.getQsettingParameter().equals("orderType")) {

                mOrderTypes.add(qsettingInfo);
            }
        }

        //1.得到对应的重量和尺寸限制

        for (QsettingInfo mOrderType : mOrderTypes) {

            for (QsettingInfo qsettingInfo : qsettingInfos) {

                if (qsettingInfo.getQsettingParamFathernode() == mOrderType.getQsettingParamFathernode()) {

                    //得到尺寸size
                    if (qsettingInfo.getQsettingParameter().equals("sizeLimit")) {
                        mOrderType.setSizeMax(qsettingInfo.getQsettingParamValue());
                    }
                    if (qsettingInfo.getQsettingParameter().equals("standardSize")) {
                        mOrderType.setSizeMin(qsettingInfo.getQsettingParamValue());
                    }

                    if (qsettingInfo.getQsettingParameter().equals("overSize")) {
                        mOrderType.setSizeStep(qsettingInfo.getQsettingParamValue());
                    }

                    //得到weight
                    if (qsettingInfo.getQsettingParameter().equals("weightLimit")) {
                        mOrderType.setWeightMax(qsettingInfo.getQsettingParamValue());
                    }
                    if (qsettingInfo.getQsettingParameter().equals("standardWeight")) {
                        mOrderType.setWeightMin(qsettingInfo.getQsettingParamValue());
                    }

                    if (qsettingInfo.getQsettingParameter().equals("overWeight")) {
                        mOrderType.setWeightStep(qsettingInfo.getQsettingParamValue());
                    }

                }

            }

        }
        //设置他们相应的范围

        for (QsettingInfo mOrderType : mOrderTypes) {

            if (mOrderType.getQsettingParamValue() == orderType) {
                ArrayList<Integer> sizeList = new ArrayList<>();
                ArrayList<String> sizeStrinList = new ArrayList<>();

                int size = orderSizeStr;

                int tempSize = 0;
                while (size <= mOrderType.getSizeMax()) {
                    sizeList.add(tempSize);
                    sizeStrinList.add(tempSize + "cm之内");
                    tempSize += mOrderType.getSizeStep();
                    size += mOrderType.getSizeStep();
                }


                ArrayList<Integer> weightList = new ArrayList<>();
                ArrayList<String> weightStrinList = new ArrayList<>();
                int weight = orderWeightStr;
                int tempWeight = 0;
                while (weight <= mOrderType.getWeightMax()) {
                    weightList.add(tempWeight);
                    weightStrinList.add(tempWeight + "kg");
                    tempWeight += mOrderType.getWeightStep();
                    weight += mOrderType.getWeightStep();
                }


                mOrderType.setWeightList(weightList);
                mOrderType.setSizeList(sizeList);
                mOrderType.setSizeStringList(sizeStrinList);
                mOrderType.setWeightStringList(weightStrinList);

                addWeight.setData(weightStrinList);
                addSize.setData(sizeStrinList);
                mSizeList = sizeList;
                mWeightList = weightList;
                break;
            }
        }

    }

    /**
     * 根据现有订单的数据计算可以追加的重量和尺寸
     * <p>
     * 计算方法：
     * 用类型1的最大重量和最大尺寸减去，包裹实际的重量和实际的尺寸
     * 显示的时候，就从0到最大数据减去实际数据的值
     *
     * @param orderWeightStr
     * @param orderSizeStr
     * @return
     */
    public void isBeyond(int orderWeightStr, int orderSizeStr) {

        List<String> weightList = new ArrayList<>();
        List<String> sizeList = new ArrayList<>();
        int weightResult;
        int sizeResult;

       /* //判断是 文件、办公/居家、包裹类型
        if(orderType == 1 || orderType == 2 || orderType == 4){
            weightResult = oneMaxWeight - orderWeightStr;
            sizeResult = oneMaxSize - orderSizeStr;
            //把数据加入集合
            for(int i = 0; i <= weightResult; i++){
                weightList.add(i + "kg");
            }
            *//**
         * 判断最大的尺寸减去订单实际尺寸是否等于0，等于就表示订单尺寸已经是最大尺寸
         * 所以，只能显示0cm之内
         *//*
            if(sizeResult == 0){
                volume = new String[0];
                sizeList.add(sizeResult + "cm之内");
                volume = sizeList.toArray(new String[sizeList.size()]);
            }if(orderSizeStr == 20){
                for(int j = 0; j <= 20; j+=20){
                    sizeList.add(j + "cm之内");
                }
                sizeList.add(0,"0cm之内");
                volume = new String[2];
                volume = sizeList.toArray(new String[sizeList.size()]);
                *//**
         * 如果不等0，说明还不是最大值，那么可以选择0，20和45cm两个界别的尺寸
         *//*
            }else{
                for(int j = 20; j <= oneMaxSize; j+=twoQsettingParamValue){
                    sizeList.add(j + "cm之内");
                }
                sizeList.add(0,"0cm之内");
                volume = new String[3];
                volume = sizeList.toArray(new String[sizeList.size()]);
            }
            weight = new String[weightResult];
            weight = weightList.toArray(new String[weightList.size()]);

            //判断是 鲜花、蛋糕类型
        }else if(orderType == 3 || orderType == 5){
            weightResult = twoMaxWeight - orderWeightStr;
            sizeResult = twoMaxSize - orderSizeStr;
            //把数据加入集合
            for(int i = 0; i <= weightResult; i++){
                weightList.add(i + "kg");
            }
            if(sizeResult == 0){
                volume = new String[0];
                sizeList.add(sizeResult + "cm之内");
                volume = sizeList.toArray(new String[sizeList.size()]);
            }if(orderSizeStr == 20){
                for(int j = 0; j <= 20; j+=20){
                    sizeList.add(j + "cm之内");
                }
                sizeList.add(0,"0cm之内");
                volume = new String[2];
                volume = sizeList.toArray(new String[sizeList.size()]);
                *//**
         * 如果不等0，说明还不是最大值，那么可以选择0，20和45cm三个界别的尺寸
         *//*
            }else{
                for(int j = 20; j <= oneMaxSize; j+=twoQsettingParamValue){
                    sizeList.add(j + "cm之内");
                }
                sizeList.add(0,"0cm之内");
                volume = new String[3];
                volume = sizeList.toArray(new String[sizeList.size()]);
            }
            weight = new String[weightResult];
            weight = weightList.toArray(new String[weightList.size()]);
        }*/


    }

    @Override
    public boolean setIsInitRequestData() {
        return false;
    }

    @Override
    public void requestData() {

    }

    @OnClick({R.id.is_select, R.id.btn})
    @Override
    public void btnClick(View view) {
        switch (view.getId()) {
            case R.id.is_select:
                //判断是否点击同意了追加费用
                if (!isAgreeFlag) {
                    isImgAgree.setImageResource(R.drawable.btn_select_active);
                } else {
                    isImgAgree.setImageResource(R.drawable.btn_select_normal);
                }
                isAgreeFlag = !isAgreeFlag;
                break;
            case R.id.btn:
                final int getWeight = mWeightList.get(addWeight.getPosition());
                final int getVolume = mSizeList.get(addSize.getPosition());

                if (getVolume == 0 && getWeight == 0) {
                    showErrorToast("未执行加价操作");
                    return;
                }
                if (isAgreeFlag) {

                    MaterialDialog dialog = new MaterialDialog(this);

                    dialog.setMDMessage("是否确认加价").setMDConfirmBtnClick(new MaterialDialog.DialogBtnCallBack() {
                        @Override
                        public void dialogBtnOnClick() {

                            doHttp(RetrofitUtils.createApi(Send.class).additionalAmount(orderId, getWeight, getVolume), 1);

                        }
                    }).setMDEffect(Effectstype.Shake).show();

                } else {
                    showErrorToast("请选择是否与用户协商一致");
                }

                break;
        }
    }

    @Override
    public void onSuccess(String result, Call<ResponseBody> call, Response<ResponseBody> response, int what) {
        super.onSuccess(result, call, response, what);
        switch (what) {
            case 1:

                finish();

                break;
        }
    }

    @Override
    public void onFailure(String result, Call<ResponseBody> call, Response<ResponseBody> response, int what) {
        super.onFailure(result, call, response, what);
        switch (what) {
            case 1:

                finish();

                break;
        }
    }

}
