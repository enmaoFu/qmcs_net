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
import com.biaoyuan.transfernet.adapter.WuNiuNewAdapter;
import com.biaoyuan.transfernet.base.BaseAty;
import com.biaoyuan.transfernet.config.UserManger;
import com.biaoyuan.transfernet.domain.MinePackageAlreadyInfo;
import com.biaoyuan.transfernet.domain.WuNiuItem;
import com.biaoyuan.transfernet.domain.WuNiuItemNew;
import com.biaoyuan.transfernet.domain.WuNiuItemNewOne;
import com.biaoyuan.transfernet.http.Mine;
import com.biaoyuan.transfernet.util.AppJsonUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Title  :签收快递详情
 * Create : 2017/6/14
 * Author ：enmaoFu
 */

public class MineHaveSendFileDetailsActivity extends BaseAty {
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.lv_data)
    ListViewForScrollView mLvData;
    @Bind(R.id.code)
    TextView code;
    @Bind(R.id.left_text)
    TextView leftText;
    @Bind(R.id.right_text)
    TextView rightText;
    WuNiuNewAdapter mAdapter;
    private String orderId;


    @Override
    public int getLayoutId() {
        return R.layout.activity_have_send_file_details;
    }

    @Override
    public void initData() {

        Bundle bundle = getIntent().getExtras();
        orderId = bundle.getString("orderId");

        initToolbar(mToolbar,"已派送");
        mAdapter=new WuNiuNewAdapter(this,new ArrayList<WuNiuItemNewOne>(),R.layout.item_wuniu);
        mLvData.setAdapter(mAdapter);
    }

    @Override
    public boolean setIsInitRequestData() {
        return true;
    }

    @Override
    public void requestData() {
        showLoadingContentDialog();
        doHttp(RetrofitUtils.createApi(Mine.class).mySignForExpressDetail(orderId),1);
    }

    @OnClick({R.id.details_re})
    @Override
    public void btnClick(View view) {
       switch (view.getId()){
           case R.id.details_re:
               Bundle bundle = new Bundle();
               bundle.putString("orderId",orderId);
               startActivity(MineHaveSendFileDetailsTwoActivity.class,bundle);
               break;
       }
    }

    @Override
    public void onSuccess(String result, Call<ResponseBody> call, Response<ResponseBody> response, int what) {
        super.onSuccess(result, call, response, what);
        switch (what){
            case 1:
                //快件码
                String getCode = new StringBuilder().append(AppJsonUtil.getString(AppJsonUtil.getString(result,"data"),"order_tracking_code")).toString();
                //发出地址
                String getLeftText = new StringBuilder().append(AppJsonUtil.getString(AppJsonUtil.getString(result,"data"),"order_send_addr")).toString().replace("|","");
                //到达地址
                String getRightText = new StringBuilder().append(AppJsonUtil.getString(AppJsonUtil.getString(result,"data"),"order_receive_addr")).toString().replace("|","");
                //传送信息
                List<WuNiuItemNewOne> wuNiuItemNewOneList = AppJsonUtil.getArrayList(result, "parcel_delivery_order_time", WuNiuItemNewOne.class);

                code.setText(getCode);
                leftText.setText(getLeftText);
                rightText.setText(getRightText);
                mAdapter.removeAll();
                if (wuNiuItemNewOneList != null) {
                    if(wuNiuItemNewOneList.size() != 0){
                        mAdapter.addAll(wuNiuItemNewOneList);
                    }
                }

                break;
        }
    }
}
