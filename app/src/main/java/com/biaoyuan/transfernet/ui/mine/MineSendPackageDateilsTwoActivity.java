package com.biaoyuan.transfernet.ui.mine;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.RelativeLayout;
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
 * Title  :我发出的包裹详情,货物详情
 * Create : 2017/6/14
 * Author ：enmaoFu
 */
public class MineSendPackageDateilsTwoActivity extends BaseAty{

    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.top)
    RelativeLayout top;
    @Bind(R.id.top1)
    RelativeLayout top1;
    @Bind(R.id.title2)
    TextView title2;
    @Bind(R.id.datetop)
    TextView datetop;
    @Bind(R.id.datedow)
    TextView datedow;
    @Bind(R.id.weight_size)
    TextView weightSize;
    @Bind(R.id.take_spot)
    TextView takeSpot;
    @Bind(R.id.arrive_spot)
    TextView arriveSpot;
    @Bind(R.id.take_date)
    TextView takeDate;
    @Bind(R.id.receive_date)
    TextView receiveDate;
    @Bind(R.id.price)
    TextView price;
    @Bind(R.id.date)
    TextView date;
    @Bind(R.id.dow_hr)
    View dowHr;
    @Bind(R.id.price_re)
    RelativeLayout priceRe;
    private String key;
    private String packageId;

    @Override
    public int getLayoutId() {
        return R.layout.activity_mine_send_package_details_two;
    }

    @Override
    public void initData() {
        Bundle bundle = getIntent().getExtras();
        key = bundle.getString("key");
        packageId = bundle.getString("packageId");
        if(key.equals("fcbg")){
            top1.setVisibility(View.VISIBLE);
        }else if(key.equals("dsbg")){
            top.setVisibility(View.VISIBLE);
            dowHr.setVisibility(View.GONE);
            priceRe.setVisibility(View.GONE);
        }
        initToolbar(mToolbar,"货物详情");
    }

    @Override
    public boolean setIsInitRequestData() {
        return true;
    }

    @Override
    public void requestData() {
        showLoadingContentDialog();
        if(key.equals("fcbg")){
            doHttp(RetrofitUtils.createApi(Mine.class).cargoDetails(packageId),1);
        }else if(key.equals("dsbg")){
            doHttp(RetrofitUtils.createApi(Mine.class).cargoDetails(packageId),2);
        }
    }

    @Override
    public void onSuccess(String result, Call<ResponseBody> call, Response<ResponseBody> response, int what) {
        super.onSuccess(result, call, response, what);
        switch (what){
            case 1:
                //快件码
                String getCode = new StringBuilder().append(AppJsonUtil.getString(AppJsonUtil.getString(result,"data"),"package_code")).toString();
                //封包完成时间
                String getDatetop;
                if(AppJsonUtil.getString(AppJsonUtil.getString(result,"data"),"publish_actu_pack_time").equals("null") ||
                        AppJsonUtil.getString(AppJsonUtil.getString(result,"data"),"publish_actu_pack_time").equals("")||
                        AppJsonUtil.getString(AppJsonUtil.getString(result,"data"),"publish_actu_pack_time") == null){
                    getDatetop = new StringBuilder().append("").toString();
                }else{
                    getDatetop = new StringBuilder().append(DateTool.timesToStrTime(AppJsonUtil.getString(AppJsonUtil.getString(result,"data"),"publish_actu_pack_time")
                            ,"yyyy.MM.dd HH:mm")).toString();
                }
                //传送开始时间
                String getDatedow;
                if(AppJsonUtil.getString(AppJsonUtil.getString(result,"data"),"carry_pickup_time").equals("null") ||
                        AppJsonUtil.getString(AppJsonUtil.getString(result,"data"),"carry_pickup_time").equals("") ||
                        AppJsonUtil.getString(AppJsonUtil.getString(result,"data"),"carry_pickup_time") == null){
                    getDatedow = new StringBuilder().append("").toString();
                }else{
                    getDatedow = new StringBuilder().append(DateTool.timesToStrTime(AppJsonUtil.getString(AppJsonUtil.getString(result,"data"),"carry_pickup_time")
                            ,"yyyy.MM.dd HH:mm")).toString();
                }
                //大小
                String getSize = new StringBuilder().append(AppJsonUtil.getString(AppJsonUtil.getString(result,"data"),"package_size")).toString();
                String getWeight = new StringBuilder().append(AppJsonUtil.getString(AppJsonUtil.getString(result,"data"),"package_weight")).toString();
                //取件网点
                String getTakeSpot = new StringBuilder().append(AppJsonUtil.getString(AppJsonUtil.getString(result,"data"),"package_send_affil_name")).toString();
                //送达网点
                String getArriveSpot = new StringBuilder().append(AppJsonUtil.getString(AppJsonUtil.getString(result,"data"),"package_receive_affil_name")).toString();
                //取件时间
                String getTakeDate = new StringBuilder().append(DateTool.timesToStrTime(AppJsonUtil.getString(AppJsonUtil.getString(result,"data"),"publish_req_pickup_time")
                        ,"yyyy.MM.dd HH:mm")).toString();
                //送达时间
                String getarriveDate = new StringBuilder().append(DateTool.timesToStrTime(AppJsonUtil.getString(AppJsonUtil.getString(result,"data"),"publish_req_deliv_time")
                        ,"yyyy.MM.dd HH:mm")).toString();
                //收益
                String getPrice = new StringBuilder().append(AppJsonUtil.getString(AppJsonUtil.getString(result,"data"),"package_carrier_reward")).toString();

                title2.setText(getCode);
                datetop.setText("封包开始：" + getDatetop);
                datedow.setText("传送开始：" + getDatedow);
                weightSize.setText(getSize + "cm   " + getWeight + "kg");
                takeSpot.setText(getTakeSpot);
                arriveSpot.setText(getArriveSpot);
                takeDate.setText(getTakeDate);
                receiveDate.setText(getarriveDate);
                price.setText("¥" + getPrice);
                break;
            case 2:
                //传送开始时间
                String getDate;
                if(AppJsonUtil.getString(AppJsonUtil.getString(result,"data"),"publish_actu_pack_time").equals("null")){
                    getDate = new StringBuilder().append("").toString();
                }else{
                    getDate = new StringBuilder().append(DateTool.timesToStrTime(AppJsonUtil.getString(AppJsonUtil.getString(result,"data"),"publish_actu_pack_time")
                            ,"yyyy.MM.dd HH:mm")).toString();
                }
                //大小
                String getSizeTwo = new StringBuilder().append(AppJsonUtil.getString(AppJsonUtil.getString(result,"data"),"package_size")).toString();
                String getWeightTwo = new StringBuilder().append(AppJsonUtil.getString(AppJsonUtil.getString(result,"data"),"package_weight")).toString();
                //取件网点
                String getTakeSpotTwo = new StringBuilder().append(AppJsonUtil.getString(AppJsonUtil.getString(result,"data"),"package_send_affil_name")).toString();
                //送达网点
                String getArriveSpotTwo = new StringBuilder().append(AppJsonUtil.getString(AppJsonUtil.getString(result,"data"),"package_receive_affil_name")).toString();
                //取件时间
                String getTakeDateTwo = new StringBuilder().append(DateTool.timesToStrTime(AppJsonUtil.getString(AppJsonUtil.getString(result,"data"),"publish_req_pickup_time")
                        ,"yyyy.MM.dd HH:mm")).toString();
                //送达时间
                String getarriveDateTwo = new StringBuilder().append(DateTool.timesToStrTime(AppJsonUtil.getString(AppJsonUtil.getString(result,"data"),"publish_req_deliv_time")
                        ,"yyyy.MM.dd HH:mm")).toString();
                date.setText("传送开始：" + getDate);
                weightSize.setText(getSizeTwo + "cm   " + getWeightTwo + "kg");
                takeSpot.setText(getTakeSpotTwo);
                arriveSpot.setText(getArriveSpotTwo);
                takeDate.setText(getTakeDateTwo);
                receiveDate.setText(getarriveDateTwo);
                break;
        }
    }

}
