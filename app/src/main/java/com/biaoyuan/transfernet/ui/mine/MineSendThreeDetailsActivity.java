package com.biaoyuan.transfernet.ui.mine;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.amap.api.maps.model.Text;
import com.and.yzy.frame.util.RetrofitUtils;
import com.and.yzy.frame.view.listview.ListViewForScrollView;
import com.biaoyuan.transfernet.R;
import com.biaoyuan.transfernet.adapter.ThreeSendDetailsAdapter;
import com.biaoyuan.transfernet.base.BaseAty;
import com.biaoyuan.transfernet.domain.ThreeSendDetailsInfo;
import com.biaoyuan.transfernet.domain.WuNiuItem;
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
 * Title  : 第三方代发已代发详情
 * Create : 2017/6/5
 * Author ：enmaoFu
 */
public class MineSendThreeDetailsActivity extends BaseAty{

    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    @Bind(R.id.listview)
    ListViewForScrollView mListview;
    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.name_number)
    TextView nameNumber;
    @Bind(R.id.code)
    TextView code;
    @Bind(R.id.left_text)
    TextView leftText;
    @Bind(R.id.right_text)
    TextView rightText;
    private List<ThreeSendDetailsInfo> threeSendDetailsInfos;
    private ThreeSendDetailsAdapter threeSendDetailsAdapter;

    private String orderId;

    @Override
    public int getLayoutId() {
        return R.layout.activity_three_send_details;
    }

    @Override
    public void initData() {

        Bundle bundle = getIntent().getExtras();
        orderId = bundle.getString("orderId");

        initToolbar(mToolbar,"已代发");

        //去掉listview焦点,保证进入页面是在Scrollview顶部
        mListview.setFocusable(false);
        threeSendDetailsAdapter = new ThreeSendDetailsAdapter(this,new ArrayList<ThreeSendDetailsInfo>(),R.layout.item_three_send_details);
        mListview.setAdapter(threeSendDetailsAdapter);

    }

    @Override
    public boolean setIsInitRequestData() {
        return true;
    }

    @Override
    public void requestData() {
        showLoadingContentDialog();
        doHttp(RetrofitUtils.createApi(Mine.class).myExpressMailDetail(orderId),1);
    }

    @OnClick({R.id.hw_details})
    @Override
    public void btnClick(View view) {
        switch (view.getId()){
            case R.id.hw_details:
                Bundle bundle = new Bundle();
                bundle.putString("orderId",orderId);
                startActivity(MineThreeSendDetailsToActivity.class,bundle);
                break;
        }
    }

    @Override
    public void onSuccess(String result, Call<ResponseBody> call, Response<ResponseBody> response, int what) {
        super.onSuccess(result, call, response, what);
        switch (what){
            case 1:
                //目前到达哪里
                String getTitle = new StringBuilder().append(AppJsonUtil.getString(AppJsonUtil.getString(result,"data"),"state")).toString();
                //快递公司名字
                String getName = new StringBuilder().append(AppJsonUtil.getString(AppJsonUtil.getString(result,"data"),"order_third_express_name")).toString();
                //快递单号
                String getNumber = new StringBuilder().append(AppJsonUtil.getString(AppJsonUtil.getString(result,"data"),"order_third_express_code")).toString();
                //快件码
                String getCode = new StringBuilder().append(AppJsonUtil.getString(AppJsonUtil.getString(result,"data"),"order_tracking_code")).toString();
                //发出地址
                String getLeftText = new StringBuilder().append(AppJsonUtil.getString(AppJsonUtil.getString(result,"data"),"order_send_addr")).toString().replace("|","");
                //到达地址
                String getRightText = new StringBuilder().append(AppJsonUtil.getString(AppJsonUtil.getString(result,"data"),"order_send_addr")).toString().replace("|","");
                //物流信息
                List<ThreeSendDetailsInfo> threeSendDetailsInfos = AppJsonUtil.getArrayList(result, "data", ThreeSendDetailsInfo.class);
                if(getTitle.equals("null") || getTitle == null | getTitle.equals("")){
                    title.setText("快件已到达：暂无信息");
                }else{
                    title.setText("快件已到达：" + getTitle);
                }
                nameNumber.setText(getName + getNumber);
                code.setText("快件码：" + getCode);
                leftText.setText(getLeftText);
                rightText.setText(getRightText);
                threeSendDetailsAdapter.removeAll();
                if (threeSendDetailsInfos != null) {
                    if(threeSendDetailsInfos.size() != 0){
                        threeSendDetailsAdapter.addAll(threeSendDetailsInfos);
                    }
                }
                break;
        }
    }
}
