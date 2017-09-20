package com.biaoyuan.transfernet.ui.Three;

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
import com.biaoyuan.transfernet.adapter.ThreeSendAdapter;
import com.biaoyuan.transfernet.base.BaseFgt;
import com.biaoyuan.transfernet.config.UserManger;
import com.biaoyuan.transfernet.domain.ThreeSendWaitInfo;
import com.biaoyuan.transfernet.http.Three;
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
 * Title  : 第三方代发直属服务
 * Create : 2017/6/5
 * Author ：enmaoFu
 */
public class ThreeSendFgt extends BaseFgt {
    @Bind(R.id.rv_data)
    RecyclerView mRvData;
    @Bind(R.id.ptr_frame)
    PtrFrameLayout mPtrFrame;

    private ThreeSendAdapter mAdapter;

    //页码
    private int targetPage = 1;
    //获取每次请求的条数
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
                targetPage = 1;
                doHttp(RetrofitUtils.createApi(Three.class).getTypeOrders(Long.parseLong(UserManger.getBaseId()), targetPage, pageSize), 1);
            }
        });

        LinearLayoutManager mRecyclerViewlayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

        mAdapter = new ThreeSendAdapter(R.layout.item_three_send, new ArrayList<ThreeSendWaitInfo>());

        //设置布局管理器
        mRvData.setLayoutManager(mRecyclerViewlayoutManager);
        mRvData.setHasFixedSize(true);
        //mAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        mRvData.setItemAnimator(new DefaultItemAnimator());
        //设置没有数据的页面
        setEmptyView(mAdapter, null);
        //设置adapter
        mRvData.setAdapter(mAdapter);
        mRvData.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                long orderId = mAdapter.getItem(position).getOrderId();
                Bundle bundle = new Bundle();
                bundle.putString("orderId", String.valueOf(orderId));
                startActivity(ThreeSendDetailsActivity.class, bundle);
            }
        });

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
                        doHttp(RetrofitUtils.createApi(Three.class).getTypeOrders(Long.parseLong(UserManger.getBaseId()), targetPage, pageSize), 2);
                    }
                });

            }
        }, mRvData);

    }

    @Override
    public boolean setIsInitRequestData() {
        return true;
    }

    @Override
    public void onUserVisible() {
        super.onUserVisible();
        //刷新界面
        if (MainAty.radioButtons.get(2).isChecked() && !isLoading) {
            isLoading = true;
            targetPage = 1;
            doHttp(RetrofitUtils.createApi(Three.class).getTypeOrders(Long.parseLong(UserManger.getBaseId()), targetPage, pageSize), 1);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        showLoadingContentDialog();
    }

    @Override
    public void requestData() {
        doHttp(RetrofitUtils.createApi(Three.class).getTypeOrders(Long.parseLong(UserManger.getBaseId()), targetPage, pageSize), 1);
    }

    @Override
    public void onFailure(String result, Call<ResponseBody> call, Response<ResponseBody> response, int what) {
        super.onFailure(result, call, response, what);

        if (mPtrFrame != null) {
            mPtrFrame.refreshComplete();
            mAdapter.loadMoreComplete();
        }
        isLoading = false;
    }

    @Override
    public void onError(Call<ResponseBody> call, Throwable t, int what) {
        super.onError(call, t, what);
        if (mPtrFrame != null) {
            mPtrFrame.refreshComplete();
            mAdapter.loadMoreComplete();
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
                mAdapter.removeAll();
                List<ThreeSendWaitInfo> threeSendWaitInfoList = AppJsonUtil.getArrayList(result, ThreeSendWaitInfo.class);
                if (threeSendWaitInfoList != null) {
                    mAdapter.setNewData(threeSendWaitInfoList);
                    if (threeSendWaitInfoList.size() < Integer.parseInt(UserManger.pageSize)) {
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
                List<ThreeSendWaitInfo> threeSendWaitInfoListTwo = AppJsonUtil.getArrayList(result, ThreeSendWaitInfo.class);
                if (threeSendWaitInfoListTwo != null && threeSendWaitInfoListTwo.size() > 0) {
                    mAdapter.addDatas(threeSendWaitInfoListTwo);
                    mAdapter.loadMoreComplete();
                } else {
                    mAdapter.loadMoreEnd();
                }
                //增加页码
                targetPage++;

                break;
        }
    }
}
