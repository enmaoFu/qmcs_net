package com.biaoyuan.transfernet.ui.send.SendSealFgt;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.and.yzy.frame.adapter.recyclerview.BaseQuickAdapter;
import com.and.yzy.frame.adapter.recyclerview.listener.OnItemClickListener;
import com.and.yzy.frame.util.PtrInitHelper;
import com.and.yzy.frame.util.RetrofitUtils;
import com.biaoyuan.transfernet.R;
import com.biaoyuan.transfernet.adapter.SendSealWaitOrdersAdapter;
import com.biaoyuan.transfernet.base.BaseFgt;
import com.biaoyuan.transfernet.config.UserManger;
import com.biaoyuan.transfernet.domain.SendSealWaitOrdersHeadInfo;
import com.biaoyuan.transfernet.http.Seal;
import com.biaoyuan.transfernet.ui.MainAty;
import com.biaoyuan.transfernet.util.AppJsonUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

/**
 * @author :enmaoFu
 * @title :封包-》待接单页面
 * @create :2017/5/15
 */
public class SendSealIsWaitOrdersFgt extends BaseFgt {

    //下拉刷新
    @Bind(R.id.ptr_frame)
    PtrFrameLayout mPtrFrame;
    @Bind(R.id.rv_data)
    RecyclerView mRecyclerView;

    private RecyclerView.LayoutManager mLayoutManager;
    private SendSealWaitOrdersAdapter mSendSendOrdersAdapter;
    private List<SendSealWaitOrdersHeadInfo> mSendSealOrdersHeadInfos;

    //页码
    private int targetPage = 1;
    //获取每次请求的页数
    private int pageSize = Integer.parseInt(UserManger.pageSize);

    //用来标记是否在加载
    private boolean isLoading = false;

    @Override
    public int getLayoutId() {
        return R.layout.ptr_recyclerview;
    }

    @Override
    public void initData() {


        PtrInitHelper.initPtr(getActivity(), mPtrFrame);

        mPtrFrame.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                //刷新页码归一，重新开启下拉加载
                targetPage = 1;
                doHttp(RetrofitUtils.createApi(Seal.class).getIndexPacket(UserManger.getBaseId(), pageSize, targetPage, 1), 1);
            }
        });

        //实例化布局管理器
        mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        //实例化适配器
        mSendSendOrdersAdapter = new SendSealWaitOrdersAdapter(R.layout.item_viewpager_send_seal_wait_orders_listview, new ArrayList<SendSealWaitOrdersHeadInfo>());
        //设置布局管理器
        mRecyclerView.setLayoutManager(mLayoutManager);
        //设置间隔样式
        /*mTekeRecyclerview.addItemDecoration(
                new HorizontalDividerItemDecoration.Builder(getActivity())
                        .color(Color.parseColor(getResources().getString(R.string.parseColor)))
                        .sizeResId(R.dimen.size_0_5p)
                        .build());*/
        //大小不受适配器影响
        mRecyclerView.setHasFixedSize(true);
        //设置加载动画类型
        //mSendSendOrdersAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        //设置删除动画类型
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        //设置item点击事件
        mRecyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                SendSealWaitOrdersHeadInfo sendSealWaitOrdersHeadInfo = mSendSendOrdersAdapter.getItem(position);
                int packageId = sendSealWaitOrdersHeadInfo.getPackage_id();
                double basePrice = sendSealWaitOrdersHeadInfo.getPackage_carrier_reward();
                Bundle bundle = new Bundle();
                bundle.putString("value", "wait");
                bundle.putString("packageId", String.valueOf(packageId));
                bundle.putDouble("basePrice", basePrice);
                startActivity(SendSealReceivingOrdersActivity.class, bundle);
            }
        });
        //设置没有数据的页面
        setEmptyView(mSendSendOrdersAdapter, null);
        //设置adapter
        mRecyclerView.setAdapter(mSendSendOrdersAdapter);

        //上拉加载更多
        mSendSendOrdersAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                if (mRecyclerView != null)
                    mRecyclerView.post(new Runnable() {
                        @Override
                        public void run() {
                            if (mRecyclerView == null) {
                                return;
                            }
                            if (targetPage == 1) {
                                mSendSendOrdersAdapter.loadMoreEnd();
                                return;
                            }
                            doHttp(RetrofitUtils.createApi(Seal.class).getIndexPacket(UserManger.getBaseId(), pageSize, targetPage, 1), 2);
                        }
                    });

            }
        }, mRecyclerView);

    }

    @Override
    public boolean setIsInitRequestData() {
        return true;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        showLoadingContentDialog();
    }

    @Override
    public void requestData() {
        doHttp(RetrofitUtils.createApi(Seal.class).getIndexPacket(UserManger.getBaseId(), pageSize, targetPage, 1), 1);
    }

    @Override
    public void onUserInvisible() {
        super.onUserInvisible();
        isLoading = false;
    }

    @Override
    public void onUserVisible() {
        super.onUserVisible();
        if (MainAty.radioButtons.get(0).isChecked() && !isLoading) {
            //刷新界面
            isLoading = true;
            targetPage = 1;

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    doHttp(RetrofitUtils.createApi(Seal.class).getIndexPacket(UserManger.getBaseId(), pageSize, targetPage, 1), 1);
                }
            }, 500);
        }
    }

    @Override
    public void onFailure(String result, Call<ResponseBody> call, Response<ResponseBody> response, int what) {
        super.onFailure(result, call, response, what);

        if (mPtrFrame != null) {
            mPtrFrame.refreshComplete();
            mSendSendOrdersAdapter.loadMoreComplete();
        }
        isLoading = false;
    }

    @Override
    public void onError(Call<ResponseBody> call, Throwable t, int what) {
        super.onError(call, t, what);
        if (mPtrFrame != null) {
            mPtrFrame.refreshComplete();
            mSendSendOrdersAdapter.loadMoreComplete();
        }
        isLoading = false;
    }

    @Override
    public void onSuccess(String result, Call<ResponseBody> call, Response<ResponseBody> response, int what) {
        super.onSuccess(result, call, response, what);
        isLoading = false;
        switch (what) {
            case 1:

                mPtrFrame.refreshComplete();
                mSendSendOrdersAdapter.removeAll();
                List<SendSealWaitOrdersHeadInfo> sendSealWaitOrdersHeadInfos = AppJsonUtil.getArrayList(result, SendSealWaitOrdersHeadInfo.class);
                if (sendSealWaitOrdersHeadInfos != null) {
                    mSendSendOrdersAdapter.setNewData(sendSealWaitOrdersHeadInfos);
                    if (sendSealWaitOrdersHeadInfos.size() < pageSize) {
                        mSendSendOrdersAdapter.loadMoreEnd();
                    }
                } else {
                    mSendSendOrdersAdapter.loadMoreEnd();
                }
                targetPage++;

                break;
            case 2:

                //加载更多
                List<SendSealWaitOrdersHeadInfo> sendSealWaitOrdersHeadInfosTwo = AppJsonUtil.getArrayList(result, SendSealWaitOrdersHeadInfo.class);
                if (sendSealWaitOrdersHeadInfosTwo != null && sendSealWaitOrdersHeadInfosTwo.size() > 0) {
                    mSendSendOrdersAdapter.addDatas(sendSealWaitOrdersHeadInfosTwo);
                    mSendSendOrdersAdapter.loadMoreComplete();
                } else {
                    mSendSendOrdersAdapter.loadMoreEnd();
                }
                //增加页码
                targetPage++;

                break;
        }
    }
}
