package com.biaoyuan.transfernet.ui.send.SendSendFgt;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.and.yzy.frame.adapter.recyclerview.BaseQuickAdapter;
import com.and.yzy.frame.adapter.recyclerview.listener.OnItemClickListener;
import com.and.yzy.frame.util.RetrofitUtils;
import com.biaoyuan.transfernet.R;
import com.biaoyuan.transfernet.adapter.SendSednArriveAddressAdapter;
import com.biaoyuan.transfernet.base.BaseAty;
import com.biaoyuan.transfernet.domain.SendSealPackingScanningInfo;
import com.biaoyuan.transfernet.domain.SendSendArriveAddressInfo;
import com.biaoyuan.transfernet.http.Send;
import com.biaoyuan.transfernet.util.AppJsonUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

/**
 * @author :enmaoFu
 * @title :发布-》发布到到达地址页面
 * @create :2017/5/12
 */
public class SendSendArriveAddressActivity extends BaseAty {

    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.rv_data)
    RecyclerView mRecyclerView;
    @Bind(R.id.checkbox)
    CheckBox checkbox;
    @Bind(R.id.text)
    TextView SendNumberText;
    @Bind(R.id.release)
    TextView releaseBnt;
    @Bind(R.id.bom)
    RelativeLayout mBom;

    private String BasicName;
    private String basicCode;
    private String basicCodeNew;
    private boolean isSelect = false;
    private String key;

    //布局管理器
    private RecyclerView.LayoutManager layoutManager;
    //数据源
    private List<SendSendArriveAddressInfo> addressInfos;
    //适配器
    private SendSednArriveAddressAdapter arriveAddressAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_send_send_arrive_address;
    }

    @Override
    public void initData() {

        Bundle bundle = getIntent().getExtras();
        key = bundle.getString("key");
        if (key.equals("SendSendFgt")) {
            basicCode = bundle.getString("basicCode");
            BasicName = bundle.getString("BasicName");
            releaseBnt.setText("去发布");
        } else {
            BasicName = bundle.getString("basicName");
            basicCodeNew = bundle.getString("basicCode");
            releaseBnt.setText("添加");
        }

        initToolbar(mToolbar, "发往" + BasicName);

        //实例化布局管理器
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        //实例化适配器
        arriveAddressAdapter = new SendSednArriveAddressAdapter(R.layout.item_send_send_arrive_address, new ArrayList<SendSendArriveAddressInfo>());
        //设置布局管理器
        mRecyclerView.setLayoutManager(layoutManager);
        //设置间隔样式
        /*mRecyclerView.addItemDecoration(
                new HorizontalDividerItemDecoration.Builder(this)
                        .color(Color.parseColor(getResources().getString(R.string.parseColor)))
                        .sizeResId(R.dimen.size_0_5p)
                        .build());*/
        //大小不受适配器影响
        mRecyclerView.setHasFixedSize(true);
        //设置加载动画类型
        //arriveAddressAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        //设置删除动画类型
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        //设置没有数据的页面
        setEmptyView(arriveAddressAdapter, "暂无数据");
        //设置adapter
        mRecyclerView.setAdapter(arriveAddressAdapter);

        mRecyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                SendSendArriveAddressInfo addressInfo = arriveAddressAdapter.getItem(position);
                addressInfo.setSelect(!addressInfo.isSelect());
                adapter.notifyItemChanged(position);
                /**
                 * 判断多选的时候
                 * 如果已经全选，取消其中一个，则全选的按钮变灰
                 * 如果没有全选，一直选到全选，则全选的按钮变亮
                 */
                if (checkbox.isChecked() == true) {
                    checkbox.setChecked(false);
                } else {
                    //获取选择了那些checkbox的Item
                    List<SendSendArriveAddressInfo> sendSendArriveAddressInfos = arriveAddressAdapter.getData();
                    List<String> orderIds = new ArrayList<>();
                    for (int i = 0; i < sendSendArriveAddressInfos.size(); i++) {
                        if (sendSendArriveAddressInfos.get(i).isSelect() == true) {
                            orderIds.add(sendSendArriveAddressInfos.get(i).getOrder_id() + "");
                        }
                    }
                    if (sendSendArriveAddressInfos.size() != orderIds.size()) {
                        checkbox.setChecked(false);
                    } else {
                        checkbox.setChecked(true);
                    }
                }
            }
        });
    }

    @Override
    public boolean setIsInitRequestData() {
        return true;
    }

    @Override
    public void requestData() {
        if (key.equals("SendSendFgt")) {
            showLoadingContentDialog();
            doHttp(RetrofitUtils.createApi(Send.class).getIndexReleaseDetailList(basicCode), 1);
        } else {
            showLoadingContentDialog();
            doHttp(RetrofitUtils.createApi(Send.class).getIndexReleaseDetailList(basicCodeNew), 1);
        }

    }

    @OnClick({R.id.release, R.id.check})
    @Override
    public void btnClick(View view) {
        switch (view.getId()) {
            case R.id.release:
                if (key.equals("SendSendFgt")) {
                    //获取选择了那些checkbox的Item
                    List<SendSendArriveAddressInfo> sendSendArriveAddressInfos = arriveAddressAdapter.getData();
                    List<String> orderIds = new ArrayList<>();
                    for (int i = 0; i < sendSendArriveAddressInfos.size(); i++) {
                        if (sendSendArriveAddressInfos.get(i).isSelect() == true) {
                            orderIds.add(sendSendArriveAddressInfos.get(i).getOrder_id() + "");
                        }
                    }
                    if (orderIds.size() == 0) {
                        showErrorToast("请选择快件");
                    } else {
                        //Log.v("print",orderIds.size()+"-------选择的id的个数");
                        //因为所有item的 头部+item 整个的id 都是一样的，所以只获取第一个的就可以
                        long basicId = sendSendArriveAddressInfos.get(0).getBasic_id();
                        //Log.v("print",basicId+"-------获取的收件网点id");
                        Bundle bundle = new Bundle();
                        //已经选择的item id
                        bundle.putStringArrayList("orderIds", (ArrayList<String>) orderIds);
                        //收件网点 头部+item 整个的id
                        bundle.putString("basicId", String.valueOf(basicId));
                        //收件网点名字
                        bundle.putString("BasicName", BasicName);
                        startActivity(SendSendConfirmActivity.class, bundle);
                    }
                } else {
                    //获取选择了那些checkbox的Item
                    List<SendSendArriveAddressInfo> sendSendArriveAddressInfos = arriveAddressAdapter.getData();
                    ArrayList<SendSealPackingScanningInfo> orderIds = new ArrayList<>();
                    for (int i = 0; i < sendSendArriveAddressInfos.size(); i++) {
                        if (sendSendArriveAddressInfos.get(i).isSelect()) {

                            SendSealPackingScanningInfo scanningInfo = new SendSealPackingScanningInfo();
                            scanningInfo.setOrder_tracking_code(sendSendArriveAddressInfos.get(i).getOrder_tracking_code());
                            scanningInfo.setP_o_order_id(sendSendArriveAddressInfos.get(i).getOrder_id());
                            scanningInfo.setType(0);
                            orderIds.add(scanningInfo);
                        }
                    }
                    if (orderIds.size() == 0) {
                        showErrorToast("请选择快件");
                    } else {
                        Intent intent = new Intent();
                        intent.putParcelableArrayListExtra("data", orderIds);
                        setResult(1, intent);
                        finish();
                    }
                }
                break;
            case R.id.check:
                //点击全选与全取消
                List<SendSendArriveAddressInfo> sendSendArriveAddressInfosCheck = arriveAddressAdapter.getData();
                if (!isSelect) {
                    for (SendSendArriveAddressInfo ssaaiCheck : sendSendArriveAddressInfosCheck) {
                        ssaaiCheck.setSelect(true);
                        arriveAddressAdapter.notifyDataSetChanged();
                    }
                    checkbox.setChecked(true);
                } else {
                    checkbox.setChecked(false);
                    for (SendSendArriveAddressInfo ssaaiCheck : sendSendArriveAddressInfosCheck) {
                        ssaaiCheck.setSelect(false);
                        arriveAddressAdapter.notifyDataSetChanged();
                    }
                }
                isSelect = !isSelect;
                break;
        }
    }

    @Override
    public void onError(Call<ResponseBody> call, Throwable t, int what) {
        super.onError(call, t, what);
        arriveAddressAdapter.removeAll();
    }

    @Override
    public void onFailure(String result, Call<ResponseBody> call, Response<ResponseBody> response, int what) {
        super.onFailure(result, call, response, what);
        arriveAddressAdapter.removeAll();
    }

    @Override
    public void onSuccess(String result, Call<ResponseBody> call, Response<ResponseBody> response, int what) {
        super.onSuccess(result, call, response, what);
        switch (what) {
            case 1:
                arriveAddressAdapter.removeAll();
                List<SendSendArriveAddressInfo> sendSendArriveAddressInfos = AppJsonUtil.getArrayList(result, SendSendArriveAddressInfo.class);
                SendNumberText.setText("共有" + sendSendArriveAddressInfos.size() + "个快件需要发往该网点");
                if (sendSendArriveAddressInfos != null) {
                    arriveAddressAdapter.addDatas(sendSendArriveAddressInfos);
                    if (sendSendArriveAddressInfos.size() == 0) {
                        mBom.setVisibility(View.GONE);
                    }

                } else {
                    mBom.setVisibility(View.GONE);
                }
                break;
        }
    }
}
