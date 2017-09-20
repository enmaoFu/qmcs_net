package com.biaoyuan.transfernet.ui.receive;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.and.yzy.frame.adapter.recyclerview.BaseQuickAdapter;
import com.and.yzy.frame.adapter.recyclerview.listener.OnItemClickListener;
import com.and.yzy.frame.util.PtrInitHelper;
import com.and.yzy.frame.util.RetrofitUtils;
import com.biaoyuan.transfernet.R;
import com.biaoyuan.transfernet.adapter.ReceiveInfoAdapter;
import com.biaoyuan.transfernet.base.BaseFgt;
import com.biaoyuan.transfernet.config.UserManger;
import com.biaoyuan.transfernet.domain.ReceiveInfo;
import com.biaoyuan.transfernet.http.Receive;
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
 * Title  :到达网点待送件
 * Create : 2017/6/3
 * Author ：enmaoFu
 */
public class NotSendFgt extends BaseFgt {
    @Bind(R.id.rv_data)
    RecyclerView mRvData;
    @Bind(R.id.ptr_frame)
    PtrFrameLayout mPtrFrame;

    private ReceiveInfoAdapter mAdapter;

    private int targetPage = 1;

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
                doHttp(RetrofitUtils.createApi(Receive.class).sendOrderView(Integer.parseInt(UserManger.pageSize), targetPage, Integer.parseInt(UserManger.getBaseId())), 1);
            }
        });

        LinearLayoutManager mRecyclerViewlayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

        mAdapter = new ReceiveInfoAdapter(R.layout.item_not_send, new ArrayList<ReceiveInfo>());

        //设置布局管理器
        mRvData.setLayoutManager(mRecyclerViewlayoutManager);
        mRvData.setHasFixedSize(true);
        //mAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        mRvData.setItemAnimator(new DefaultItemAnimator());
        //设置item点击事件
        mRvData.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                int orderId = mAdapter.getItem(position).getOrderID();
                String orderIdStr = String.valueOf(orderId);
                Bundle bundle = new Bundle();
                bundle.putString("orderIdStr", orderIdStr);
                startActivity(ReceiveWaitSendPackageActivity.class, bundle);
            }
        });

        setEmptyView(mAdapter, "");

        //设置adapter
        mRvData.setAdapter(mAdapter);

        //上拉加载更多
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                if (mRvData == null) {
                    return;
                }
                mRvData.post(new Runnable() {
                    @Override
                    public void run() {
                        if (targetPage == 1) {
                            mAdapter.loadMoreEnd();
                            return;
                        }
                        doHttp(RetrofitUtils.createApi(Receive.class).sendOrderView(Integer.parseInt(UserManger.pageSize), targetPage,
                                Integer.parseInt(UserManger.getBaseId())), 2);
                    }
                });

            }
        }, mRvData);

    }

    @Override
    public void onUserVisible() {
        super.onUserVisible();
        //如果选中了第一个
        isLoading = true;
        //刷新界面
        targetPage = 1;
        doHttp(RetrofitUtils.createApi(Receive.class).sendOrderView(Integer.parseInt(UserManger.pageSize), targetPage, Integer.parseInt(UserManger.getBaseId())), 1);
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
        doHttp(RetrofitUtils.createApi(Receive.class).sendOrderView(Integer.parseInt(UserManger.pageSize), targetPage, Integer.parseInt(UserManger.getBaseId())), 1);
    }

    @Override
    public void onSuccess(String result, Call<ResponseBody> call, Response<ResponseBody> response, int what) {
        super.onSuccess(result, call, response, what);
        switch (what) {
            case 1:

                mPtrFrame.refreshComplete();
                mAdapter.removeAll();
                List<ReceiveInfo> receiveInfoList = AppJsonUtil.getArrayList(result, "sendOrderList", ReceiveInfo.class);
                if (receiveInfoList != null) {
                    mAdapter.setNewData(receiveInfoList);
                    if (receiveInfoList.size() < Integer.parseInt(UserManger.pageSize)) {
                        mAdapter.loadMoreEnd();
                    }
                } else {
                    mAdapter.loadMoreEnd();
                }
                //增加页码
                targetPage++;

                break;
            case 2:

                //加载更多
                List<ReceiveInfo> receiveInfoListTwo = AppJsonUtil.getArrayList(result, "sendOrderList", ReceiveInfo.class);
                if (receiveInfoListTwo != null && receiveInfoListTwo.size() > 0) {
                    mAdapter.addDatas(receiveInfoListTwo);
                    mAdapter.loadMoreComplete();
                } else {
                    mAdapter.loadMoreEnd();
                }
                //增加页码
                targetPage++;

                break;
        }
    }

    @Override
    public void onFailure(String result, Call<ResponseBody> call, Response<ResponseBody> response, int what) {
        super.onFailure(result, call, response, what);
        isLoading = false;
        if (mPtrFrame != null) {
            mPtrFrame.refreshComplete();
            mAdapter.loadMoreComplete();
        }
    }

    @Override
    public void onError(Call<ResponseBody> call, Throwable t, int what) {
        super.onError(call, t, what);
        isLoading = false;
        if (mPtrFrame != null) {
            mPtrFrame.refreshComplete();
            mAdapter.loadMoreComplete();
        }
    }

}
